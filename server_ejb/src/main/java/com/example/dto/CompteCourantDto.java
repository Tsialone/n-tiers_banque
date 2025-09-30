package com.example.dto;

import lombok.Data;

@Data
public class CompteCourantDto {
    private Integer idCompte;
    private String nom;
    private Double solde;
    private Double decouvertAutorise;

}
