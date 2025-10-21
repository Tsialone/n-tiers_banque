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
@Table(name = "comptes_courant")
public class CompteCourant  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte")
    private Integer idCompte;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    @JsonbTransient
    private ClientCourant client;

    @Column(name = "date_ouverture")
    private LocalDate dateOuverture   = LocalDate.now();

    @Column(name = "capital")
    private Double capital;

    @Column(name = "decouvert_autorise")
    private Double decouvertAutorise = 0.0;

    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonbTransient
    private List<TransactionCourant> transactions;

    // getters & setters
}
