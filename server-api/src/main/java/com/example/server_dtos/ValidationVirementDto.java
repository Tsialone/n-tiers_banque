package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ValidationVirementDto implements Serializable {
    private Integer idValidation;
    private Integer idEtat;
    private Integer idVirement;
    private LocalDateTime dateValidation;
    private Integer idUtilisateur;

    private EtatDto etat;

}
