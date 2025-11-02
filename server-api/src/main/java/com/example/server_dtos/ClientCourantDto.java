package com.example.server_dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ClientCourantDto implements Serializable {
    private Integer idClient;
    private String nom;
    private String prenoms;
    private String email;
    private String mdp;
    private LocalDate dateNaissance;
    private Integer idDirection;
    private List<ClientRoleDto> clientRoles;
}
