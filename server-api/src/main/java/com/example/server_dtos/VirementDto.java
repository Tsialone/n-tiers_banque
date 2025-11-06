package com.example.server_dtos;



import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VirementDto implements Serializable {
    private Integer idVirement;
    private String idObject;
    private Integer idCompteDebit;
    private Integer idCompteCredit;
    private LocalDateTime dateVirement;
    private Double montant;
    private Double taux;
    private String devise;


    private Double frais; 
    private List<ValidationVirementDto> validations;
    private ValidationVirementDto lastValidation;
}
