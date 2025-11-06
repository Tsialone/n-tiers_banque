package com.example.mappers;

import com.example.models.ValidationTransaction;
import com.example.server_dtos.ValidationTransactionDto;

public class ValidationTransactionMapper {

    public static ValidationTransactionDto toDto(ValidationTransaction entity) {
        if (entity == null) return null;
        ValidationTransactionDto dto = new ValidationTransactionDto();
        dto.setIdValidation(entity.getIdValidation());
        dto.setIdTransaction(entity.getTransaction().getIdTransaction());
        dto.setIdEtat(entity.getEtat().getIdEtat());
        dto.setDateValidation(entity.getDateValidation());
        dto.setEtat(EtatMapper.toDto(entity.getEtat()));
        return dto;
    }

    public static ValidationTransaction toEntity(ValidationTransactionDto dto) {
        if (dto == null) return null;
        ValidationTransaction entity = new ValidationTransaction();
        entity.setIdValidation(dto.getIdValidation());
        entity.setDateValidation(dto.getDateValidation());
        return entity;
    }
}
