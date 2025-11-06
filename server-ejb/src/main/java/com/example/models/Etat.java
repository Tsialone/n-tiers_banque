package com.example.models;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "etats")
public class Etat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etat")
    private Integer idEtat;

    @Column(nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "etat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationTransaction> validations;
    
    @OneToMany(mappedBy = "etat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationDepot> validationsDepot;

    @OneToMany(mappedBy = "etat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationRetrait> validationsRetrait;
}
