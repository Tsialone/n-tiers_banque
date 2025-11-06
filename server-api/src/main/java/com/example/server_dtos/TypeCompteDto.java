package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class TypeCompteDto implements Serializable {

    private Integer idTypeCompte;
    private String libelle;

    // Optionnel : liste des comptes associés
    private List<CompteCourantDto> comptes;
}
