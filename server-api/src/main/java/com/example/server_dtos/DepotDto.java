package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class DepotDto implements Serializable {
    private Integer idDepot;
    private String idObject;
    private Integer idCompteCredit;
    private Double montant;
    private Double taux;

    private String devise;


    private List<ValidationDepotDto> validations;
    private ValidationDepotDto lastValidation;
}
