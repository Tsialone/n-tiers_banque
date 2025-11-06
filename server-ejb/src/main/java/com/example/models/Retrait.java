package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Entity
@Table(name = "retraits")
public class Retrait implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_retrait")
    private Integer idRetrait;

    @Column(name = "id_object", nullable = false, unique = true)
    private String idObject;

    @Column(name = "devise", nullable = false)
    private String devise = "ar";

    @ManyToOne
    @JoinColumn(name = "id_compte_deb", nullable = false)
    private CompteCourant compteDebit;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false, name = "taux")
    private Double taux;

    @OneToMany(mappedBy = "retrait", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationRetrait> validations;

    @OneToMany(mappedBy = "retrait", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueRetrait> historiques = new ArrayList<>();

    @Transient
    public HistoriqueRetrait getLastHistorique() {
        if (historiques == null || historiques.isEmpty()) {
            return null;
        }
        return historiques.stream()
                .max(Comparator.comparing(HistoriqueRetrait::getDateHistorique))
                .orElse(null);
    }

    @Transient
    public ValidationRetrait getLastValidation() {
        if (validations == null || validations.isEmpty())
            return null;
        return validations.stream()
                .max(Comparator.comparing(ValidationRetrait::getDateValidation))
                .orElse(null);
    }

    public boolean isValide() {
        if (getLastValidation() == null || getLastValidation().getEtat().getLibelle().equals("en_attente")) {
            return false;
        }
        return true;
    }

    public HistoriqueRetrait generateHistorique(Etat etat, ClientCourant utilisateur, String action) {
        HistoriqueRetrait hist = new HistoriqueRetrait();

        hist.setRetrait(this); // référence au retrait
        hist.setIdCompteDeb(this.getCompteDebit().getIdCompte());
        hist.setMontant(this.getMontant());
        hist.setDevise(this.getDevise());
        hist.setTaux(this.getTaux());
        hist.setAction(action);
        hist.setEtat(etat);
        hist.setDateRetrait(LocalDateTime.now()); // ou this.getDateRetrait() si tu as un champ dateRetrait
        hist.setDateHistorique(LocalDateTime.now());

        // Si l'utilisateur n'est pas fourni, on prend le client du compte débité
        if (utilisateur == null && this.getCompteDebit() != null) {
            hist.setUtilisateur(this.getCompteDebit().getClient());
        } else {
            hist.setUtilisateur(utilisateur);
        }

        return hist;
    }

    // Equivalent de changeEtat pour Retrait
    public ValidationRetrait changeEtat(Etat etat, ClientCourant utilisateur, boolean doubleChangeControl)
            throws Exception {
        if (etat == null)
            throw new Exception("L'état ne doit pas être null");
        // if (utilisateur == null)
        // throw new Exception("Le validateur ne doit pas être null");

        ValidationRetrait last = getLastValidation();
        Etat currentEtat = null;
        if (last != null)
            currentEtat = last.getEtat();

        if (doubleChangeControl && currentEtat != null
                && !validations.isEmpty()
                && !currentEtat.getLibelle().equals("en_attente")) {
            throw new Exception("État déjà " + currentEtat.getLibelle());
        }

        ValidationRetrait validation = new ValidationRetrait();
        validation.setRetrait(this);
        validation.setEtat(etat);
        validation.setDateValidation(LocalDateTime.now());

        if (validations != null) {
            validations.add(validation);
        }

        return validation;
    }
}
