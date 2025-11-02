package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class ActionRoleDto implements Serializable {
    private Integer idActionRole;
    private String nomTable;
    private Integer idAction;
    private Integer idRole;

    // Champs pour affichage uniquement
    private String libelleAction;
    private String libelleRole;
}
