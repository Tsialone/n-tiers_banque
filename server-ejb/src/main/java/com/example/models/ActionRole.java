package com.example.models;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "actions_roles")
public class ActionRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_action_role")
    private Integer idActionRole;

    @Column(name = "nom_table")
    private String nomTable;

    @ManyToOne
    @JoinColumn(name = "id_action", nullable = false)
    @JsonbTransient
    private Action action;

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    @JsonbTransient
    private Role role;

    // @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    // @ToString.Exclude
    // @JsonbTransient
    // private List<CompteCourant> comptes;

}
