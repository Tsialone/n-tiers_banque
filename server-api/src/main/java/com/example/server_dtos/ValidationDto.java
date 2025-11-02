package com.example.server_dtos;


import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ValidationDto  implements Serializable {
    private Integer idValidation;
    private Integer idTransaction;
    private String etat;
    private LocalDate dateValidation;
}
