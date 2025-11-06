package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name = "types_compte")
public class TypeCompte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_compte")
    private Integer idTypeCompte;

    @Column(nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "typeCompte", cascade = CascadeType.ALL)
    private List<CompteCourant> comptes;
}
