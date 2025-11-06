package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Entity
@Table(name = "depots")
public class Depot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depot")
    private Integer idDepot;

    @Column(name = "id_object", nullable = false, unique = true)
    private String idObject;

    @ManyToOne
    @JoinColumn(name = "id_compte_cred", nullable = false)
    private CompteCourant compteCredit;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false, name = "taux")
    private Double taux;

    @OneToMany(mappedBy = "depot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationDepot> validations;

    @Column(name = "devise", nullable = false)
    private String devise = "ar";

    @OneToMany(mappedBy = "depot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueDepot> historiques = new ArrayList<>();

    @Transient
    public HistoriqueDepot getLastHistorique() {
        if (historiques == null || historiques.isEmpty()) {
            return null;
        }
        return historiques.stream()
                .max(Comparator.comparing(HistoriqueDepot::getDateHistorique))
                .orElse(null);
    }

    public HistoriqueDepot generateHistorique(Etat etat, ClientCourant utilisateur, String action) {
        HistoriqueDepot hist = new HistoriqueDepot();

        hist.setDepot(this); // référence au dépôt
        hist.setIdCompteCred(this.getCompteCredit().getIdCompte());
        hist.setMontant(this.getMontant());
        hist.setDevise(this.getDevise());
        hist.setTaux(this.getTaux());
        hist.setAction(action);
        hist.setEtat(etat);
        hist.setDateDepot(LocalDateTime.now()); // ou this.getDateDepot() si tu as un champ dateDepot
        hist.setDateHistorique(LocalDateTime.now());

        // Si l'utilisateur n'est pas fourni, on prend le client du compte crédité
        if (utilisateur == null && this.getCompteCredit() != null) {
            hist.setUtilisateur(this.getCompteCredit().getClient());
        } else {
            hist.setUtilisateur(utilisateur);
        }

        return hist;
    }

    // Dans la classe Depot
    public ValidationDepot changeEtat(Etat etat, ClientCourant utilisateur, boolean doubleChangeControl)
            throws Exception {
        if (etat == null)
            throw new Exception("L'état ne doit pas être null");
        // if (utilisateur == null)
        // throw new Exception("Le validateur ne doit pas être null");

        ValidationDepot last = getLastValidation();

        Etat currentEtat = null;
        if (last != null)
            currentEtat = last.getEtat();

        if (doubleChangeControl && currentEtat != null
                && !validations.isEmpty()
                && !currentEtat.getLibelle().equals("en_attente")) {
            throw new Exception("État déjà " + currentEtat.getLibelle());
        }

        ValidationDepot validation = new ValidationDepot();
        validation.setDepot(this);
        validation.setEtat(etat);
        validation.setDateValidation(LocalDateTime.now());

        if (validations != null) {
            validations.add(validation);
        }

        return validation;
    }

    @Transient
    public ValidationDepot getLastValidation() {
        if (validations == null || validations.isEmpty())
            return null;
        return validations.stream()
                .max(Comparator.comparing(ValidationDepot::getDateValidation))
                .orElse(null);
    }

    public boolean isValide() {
        if (getLastValidation() == null || getLastValidation().getEtat().getLibelle().equals("en_attente")) {
            return false;
        }
        return true;
    }
}
