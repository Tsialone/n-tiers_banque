package com.example.mappers;

import com.example.models.ClientCourant;
import com.example.models.ClientRole;
import com.example.models.Direction;
import com.example.server_dtos.ClientCourantDto;
import com.example.server_dtos.ClientRoleDto;

import java.util.stream.Collectors;

public class ClientCourantMapper {

    public static ClientCourantDto toDto(ClientCourant entity) {
        if (entity == null) return null;

        ClientCourantDto dto = new ClientCourantDto();
        dto.setIdClient(entity.getIdClient());
        dto.setNom(entity.getNom());
        dto.setPrenoms(entity.getPrenoms());
        dto.setEmail(entity.getEmail());
        dto.setMdp(entity.getMdp());
        dto.setIdDirection(entity.getDirection() != null ? entity.getDirection().getIdDirection() : null);
        dto.setClientRoles(
            entity.getClientRoles()
                  .stream()
                  .map(ClientCourantMapper::clientRoleToDto)
                  .collect(Collectors.toList())
        );
        return dto;
    }

    private static ClientRoleDto clientRoleToDto(ClientRole cr) {
        ClientRoleDto dto = new ClientRoleDto();
        dto.setIdClientRole(cr.getIdClientRole());
        dto.setIdClient(cr.getClient().getIdClient());
        dto.setIdRole(cr.getRole().getIdRole());
        return dto;
    }

    public static ClientCourant toEntity(ClientCourantDto dto, Direction direction) {
        if (dto == null) return null;

        ClientCourant entity = new ClientCourant();
        entity.setIdClient(dto.getIdClient());
        entity.setNom(dto.getNom());
        entity.setPrenoms(dto.getPrenoms());
        entity.setEmail(dto.getEmail());
        entity.setMdp(dto.getMdp());
        entity.setDirection(direction);
        // Les ClientRoles peuvent être ajoutés séparément
        return entity;
    }
}
