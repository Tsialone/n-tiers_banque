package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historiques_retrait")
public class HistoriqueRetrait implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_historique")
    private Integer idHistorique;

    @ManyToOne
    @JoinColumn(name = "id_retrait", nullable = false)
    private Retrait retrait;

    @Column(name = "id_compte_deb", nullable = false)
    private Integer idCompteDeb;

    @Column(name = "date_retrait", nullable = false)
    private LocalDateTime dateRetrait;

    @Column(nullable = false)
    private Double montant;

    @Column(length = 50, nullable = false)
    private String devise = "ar";

    @Column(nullable = false)
    private Double taux;

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
