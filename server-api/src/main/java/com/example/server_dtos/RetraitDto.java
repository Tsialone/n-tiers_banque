package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class RetraitDto implements Serializable {
    private Integer idRetrait;
    private String idObject;
    private String devise;
    private Integer idCompteDebit;
    private Double montant;
    private Double taux;


    private List<ValidationRetraitDto> validations;
    private ValidationRetraitDto lastValidation;
}
