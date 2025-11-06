package com.example.mappers;

import com.example.models.*;
import com.example.server_dtos.ValidationRetraitDto;

public class ValidationRetraitMapper {

    // DTO -> Entity
    public static ValidationRetrait toEntity(ValidationRetraitDto dto, Retrait retrait, Etat etat) {
        if (dto == null)
            return null;

        ValidationRetrait entity = new ValidationRetrait();
        entity.setIdValidation(dto.getIdValidation());
        entity.setRetrait(retrait); // inject Retrait existant
        entity.setEtat(etat); // inject Etat existant
        entity.setDateValidation(
                dto.getDateValidation() != null ? dto.getDateValidation() : java.time.LocalDateTime.now());

        return entity;
    }

    public static ValidationRetraitDto toDto(ValidationRetrait entity) {
        if (entity == null)
            return null;

        ValidationRetraitDto dto = new ValidationRetraitDto();
        dto.setIdValidation(entity.getIdValidation());
        dto.setIdRetrait(entity.getRetrait() != null ? entity.getRetrait().getIdRetrait() : null);
        dto.setIdEtat(entity.getEtat() != null ? entity.getEtat().getIdEtat() : null);
        dto.setDateValidation(entity.getDateValidation());
        if (entity.getEtat() != null)
            dto.setEtat(EtatMapper.toDto(entity.getEtat()));

        return dto;
    }
}
