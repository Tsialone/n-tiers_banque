package com.example.mappers;

import com.example.models.ClientRole;
import com.example.server_dtos.ClientRoleDto;

public class ClientRoleMapper {

    public static ClientRoleDto toDto(ClientRole entity) {
        if (entity == null) return null;
        ClientRoleDto dto = new ClientRoleDto();
        dto.setIdClientRole(entity.getIdClientRole());
        dto.setIdRole(entity.getRole() != null ? entity.getRole().getIdRole() : null);
        dto.setIdClient(entity.getClient() != null ? entity.getClient().getIdClient() : null);
        return dto;
    }

    public static ClientRole toEntity(ClientRoleDto dto) {
        if (dto == null) return null;
        ClientRole entity = new ClientRole();
        entity.setIdClientRole(dto.getIdClientRole());
        // Les relations ManyToOne (Role/Client) doivent être chargées via repository
        return entity;
    }
}
