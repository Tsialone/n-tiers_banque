package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ValidationRetraitDto implements Serializable {
    private Integer idValidation;
    private Integer idRetrait;
    private Integer idEtat;
    private LocalDateTime dateValidation;

    private EtatDto etat;

}
