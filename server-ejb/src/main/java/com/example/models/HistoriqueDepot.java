package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historiques_depot")
public class HistoriqueDepot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique")
    private Integer idHistorique;

    @ManyToOne
    @JoinColumn(name = "id_depot", nullable = false)
    private Depot depot;

    @Column(name = "id_compte_cred", nullable = false)
    private Integer idCompteCred;

    @Column(name = "date_depot", nullable = false)
    private LocalDateTime dateDepot;

    @Column(nullable = false)
    private Double montant;

    @Column(length = 50, nullable = false)
    private String devise = "ar";

    @Column(nullable = false)
    private Double taux ;

    @ManyToOne
    @JoinColumn(name = "id_etat")
    private Etat etat;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private ClientCourant utilisateur;

    @Column(name = "date_historique", nullable = false)
    private LocalDateTime dateHistorique = LocalDateTime.now();

    @Column(length = 50, nullable = false)
    private String action;
}
