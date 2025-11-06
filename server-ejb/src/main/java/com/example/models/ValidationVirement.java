package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "validations_virement")
public class ValidationVirement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validation")
    private Integer idValidation;

    @ManyToOne
    @JoinColumn(name = "id_virement", nullable = false)
    private Virement virement;

    @ManyToOne
    @JoinColumn(name = "id_etat", nullable = false)
    private Etat etat;

    @Column(name = "date_validation", nullable = false)
    private LocalDateTime dateValidation = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private ClientCourant utilisateur;
}
