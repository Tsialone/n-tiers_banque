package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ValidationDepotDto implements Serializable {
    private Integer idValidation;
    private Integer idDepot;
    private Integer idEtat;
    private LocalDateTime dateValidation;

    private EtatDto etat;
}
