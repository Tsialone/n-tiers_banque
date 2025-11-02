package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CompteEpargneDto implements Serializable {

    private Integer idClient;
    private Double capitalEpargne;
    private String libelle;
    private LocalDate dateOuverture = LocalDate.now();
    private Double retrait = 50.0;
    private Double tauxInteret = 2.1;

}
