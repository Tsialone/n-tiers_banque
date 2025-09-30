package com.example.models;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "clients_courant")
public class ClientCourant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_client")
    private Integer idClient;

    private String nom;
    
    private String prenoms;
    @Column(name="date_naissance")
    private LocalDate dateNaissance;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonbTransient
    private List<CompteCourant> comptes;

}
