package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "validations_depot")
public class ValidationDepot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validation")
    private Integer idValidation;

    @ManyToOne
    @JoinColumn(name = "id_depot", nullable = false)
    private Depot depot;

    @ManyToOne
    @JoinColumn(name = "id_etat", nullable = false)
    private Etat etat;

    @Column(name = "date_validation", nullable = false)
    private LocalDateTime dateValidation = LocalDateTime.now();
}
