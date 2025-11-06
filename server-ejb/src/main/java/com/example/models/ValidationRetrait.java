package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "validations_retrait")
public class ValidationRetrait implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validation")
    private Integer idValidation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etat", nullable = false)
    private Etat etat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_retrait", nullable = false)
    private Retrait retrait;

    @Column(name = "date_validation", nullable = false)
    private LocalDateTime dateValidation = LocalDateTime.now();
}
