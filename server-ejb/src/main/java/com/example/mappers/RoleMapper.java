package com.example.mappers;

import com.example.models.ActionRole;
import com.example.models.Role;
import com.example.server_dtos.ActionRoleDto;
import com.example.server_dtos.RoleDto;

import java.util.stream.Collectors;

public class RoleMapper {

    // Entity -> DTO
    public static RoleDto toDto(Role entity) {
        if (entity == null) return null;

        RoleDto dto = new RoleDto();
        dto.setIdRole(entity.getIdRole());
        dto.setLibelle(entity.getLibelle());
        dto.setActionRoles(
            entity.getActionRoles()
                  .stream()
                  .map(RoleMapper::actionRoleToDto)
                  .collect(Collectors.toList())
        );
        return dto;
    }

    private static ActionRoleDto actionRoleToDto(ActionRole ar) {
        ActionRoleDto dto = new ActionRoleDto();
        dto.setIdActionRole(ar.getIdActionRole());
        dto.setNomTable(ar.getNomTable());
        dto.setIdAction(ar.getAction().getIdAction());
        dto.setIdRole(ar.getRole().getIdRole());
        return dto;
    }

    // DTO -> Entity
    public static Role toEntity(RoleDto dto) {
        if (dto == null) return null;

        Role entity = new Role();
        entity.setIdRole(dto.getIdRole());
        entity.setLibelle(dto.getLibelle());
        // On ne mappe pas les actionRoles ici, c’est géré séparément pour éviter les cycles
        return entity;
    }
}
