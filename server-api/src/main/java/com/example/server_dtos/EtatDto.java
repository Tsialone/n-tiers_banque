package com.example.server_dtos;

import java.io.Serializable;
import lombok.Data;

@Data
public class EtatDto implements Serializable {
    private Integer idEtat;
    private String libelle;
}
