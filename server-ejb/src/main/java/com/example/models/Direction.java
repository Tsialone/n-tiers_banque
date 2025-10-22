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
@Table(name = "directions")
public class Direction  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_direction")
    private Integer idDirection;

    private String libelle;
    
    private Integer niveau;

    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // @ToString.Exclude
    // @JsonbTransient
    // private List<CompteCourant> comptes;

}
