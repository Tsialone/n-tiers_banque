package com.example.mappers;

import com.example.models.Direction;
import com.example.server_dtos.DirectionDto;

public class DirectionMapper {

    public static DirectionDto toDto(Direction entity) {
        if (entity == null) return null;
        DirectionDto dto = new DirectionDto();
        dto.setIdDirection(entity.getIdDirection());
        dto.setLibelle(entity.getLibelle());
        dto.setNiveau(entity.getNiveau());
        return dto;
    }

    public static Direction toEntity(DirectionDto dto) {
        if (dto == null) return null;
        Direction entity = new Direction();
        entity.setIdDirection(dto.getIdDirection());
        entity.setLibelle(dto.getLibelle());
        entity.setNiveau(dto.getNiveau());
        return entity;
    }
}
