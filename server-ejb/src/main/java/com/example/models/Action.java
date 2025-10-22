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
@Table(name = "actions")
public class  Action  implements  Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_action")
    private Integer idAction;

    private String libelle;
    

    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // @ToString.Exclude
    // @JsonbTransient
    // private List<CompteCourant> comptes;

}
