package com.example.server_dtos;

import lombok.Data;
import java.io.Serializable;

@Data
public class ClientRoleDto implements Serializable {
    private Integer idClientRole;
    private Integer idRole;
    private Integer idClient;
}
