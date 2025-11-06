package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CompteCourantDto implements Serializable {
    private Integer idCompte;
    private String nom;
    private Integer idClient;
    private TypeCompteDto typeCompte;
    private LocalDate dateOuverture;
    private Double capital;
    private Double decouvertAutorise;

    private Double solde = 0.0;
}
