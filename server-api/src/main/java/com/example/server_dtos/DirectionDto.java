package com.example.server_dtos;


import lombok.Data;
import java.io.Serializable;

@Data
public class DirectionDto implements Serializable {
    private Integer idDirection;
    private String libelle;
    private Integer niveau;
}
