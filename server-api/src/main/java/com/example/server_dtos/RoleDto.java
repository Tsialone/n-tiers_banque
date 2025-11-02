package com.example.server_dtos;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RoleDto implements Serializable {
    private Integer idRole;
    private String libelle;
    private List<ActionRoleDto> actionRoles; 
}
