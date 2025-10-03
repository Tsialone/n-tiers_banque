package com.example.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CompteCourantDto {
    
    private Integer idCompte;
    private String nom;
    private Integer idClient;
    private Double capital;
    private LocalDate dateOuverture  = LocalDate.now();
    private Double decouvertAutorise = 0.0;
}
