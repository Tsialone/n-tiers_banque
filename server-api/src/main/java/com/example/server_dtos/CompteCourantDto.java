package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CompteCourantDto implements Serializable {
    
    private Integer idCompte;
    private String nom;
    private Integer idClient;
    private Double capital;
    private LocalDate dateOuverture  = LocalDate.now();
    private Double decouvertAutorise = 0.0;
}
