package com.example.models;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "clients_courant")
public class ClientCourant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;

    private String nom;

    private String prenoms;

    private String email;
    
    private String mdp;


    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @ManyToOne
    @JoinColumn(name = "id_direction")
    private Direction direction;

    // @ManyToOne
    // @JoinColumn(name = "id_role")
    // private Role role;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonbTransient
    private List<ClientRole> clientRoles;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonbTransient
    private List<CompteCourant> comptes;

}
