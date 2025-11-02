package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class VirementDto implements Serializable {

    private Integer idCompteDebit;   
    private Integer idCompteCredit;  
    private LocalDate dateVirement;
    private String devise = "MG";
    private Double montant;
}
