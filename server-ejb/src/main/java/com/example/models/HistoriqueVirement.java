package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historiques_virement")
public class HistoriqueVirement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique")
    private Integer idHistorique;

    @ManyToOne
    @JoinColumn(name = "id_virement", nullable = false)
    private Virement virement;

    @Column(name = "id_compte_deb")
    private Integer idCompteDebit;

    @Column(name = "id_compte_cred")
    private Integer idCompteCredit;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "taux")
    private Double taux;

    @Column(name = "devise")
    private String devise;

    @Column(name = "date_virement")
    private LocalDateTime dateVirement;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private ClientCourant utilisateur;

    @Column(name = "action") // ex: "VALIDATION", "ANNULATION"
    private String action;

    @Column(name = "date_action")
    private LocalDateTime dateAction = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_etat")
    private Etat etat;

   

}
