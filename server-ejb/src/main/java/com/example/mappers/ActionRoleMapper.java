package com.example.mappers;

import com.example.models.ActionRole;
import com.example.server_dtos.ActionRoleDto;

public class ActionRoleMapper {

    public static ActionRoleDto toDto(ActionRole entity) {
        if (entity == null) return null;
        ActionRoleDto dto = new ActionRoleDto();
        dto.setIdActionRole(entity.getIdActionRole());
        dto.setNomTable(entity.getNomTable());
        dto.setIdAction(entity.getAction() != null ? entity.getAction().getIdAction() : null);
        dto.setIdRole(entity.getRole() != null ? entity.getRole().getIdRole() : null);
        return dto;
    }

    public static ActionRole toEntity(ActionRoleDto dto) {
        if (dto == null) return null;
        ActionRole entity = new ActionRole();
        entity.setIdActionRole(dto.getIdActionRole());
        entity.setNomTable(dto.getNomTable());

        // Relations Action et Role doivent être chargées via repository
        return entity;
    }
}
