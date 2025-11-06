package com.example.mappers;

import java.util.stream.Collectors;
import com.example.models.TransactionCourant;
import com.example.server_dtos.TransactionCourantDto;

public class TransactionCourantMapper {

    public static TransactionCourantDto toDto(TransactionCourant entity) {
        if (entity == null) return null;
        TransactionCourantDto dto = new TransactionCourantDto();
        dto.setIdTransaction(entity.getIdTransaction());
        dto.setSource(entity.getSource());
        dto.setDateTransaction(entity.getDateTransaction());
        dto.setLibelle(entity.getLibelle());
        dto.setMontant(entity.getMontant());
        dto.setSens(entity.getSens());
        dto.setDevise(entity.getDevise());

        if (entity.getValidations() != null) {
            dto.setValidations(entity.getValidations().stream()
                    .map(ValidationTransactionMapper::toDto)
                    .collect(Collectors.toList()));
        }

        if (entity.getLastValidation() != null) {
            dto.setLastValidation(ValidationTransactionMapper.toDto(entity.getLastValidation()));
        }

        return dto;
    }

    public static TransactionCourant toEntity(TransactionCourantDto dto) {
        if (dto == null) return null;
        TransactionCourant entity = new TransactionCourant();
        entity.setIdTransaction(dto.getIdTransaction());
        entity.setSource(dto.getSource());
        entity.setDateTransaction(dto.getDateTransaction());
        entity.setLibelle(dto.getLibelle());
        entity.setMontant(dto.getMontant());
        entity.setSens(dto.getSens());
        return entity;
    }
}
