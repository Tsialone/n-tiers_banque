package com.example.views;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CompteCourantView {
    private Integer idCompte;
    private String nom;
    private Integer idClient;
    private Double capital;
    private LocalDate dateOuverture;
    private Double decouvertAutorise;
    private Double solde = 0.0;
}



