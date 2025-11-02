package com.example.mappers;

import java.time.LocalDate;

import com.example.models.TransactionCourant;
import com.example.models.Validation;
import com.example.server_dtos.ValidationDto;

public class ValidationMapper {

    // DTO → Entité
    public static Validation toEntity(ValidationDto dto, TransactionCourant transaction) {
        Validation validation = new Validation();

        if (dto.getIdValidation() != null) {
            validation.setIdValidation(dto.getIdValidation());
        }

        validation.setTransaction(transaction);
        validation.setEtat(dto.getEtat());
        validation.setDateValidation(dto.getDateValidation() != null
                ? dto.getDateValidation()
                : LocalDate.now());

        return validation;
    }

    // Entité → DTO
    public static ValidationDto toDto(Validation validation) {
        ValidationDto dto = new ValidationDto();
        dto.setIdValidation(validation.getIdValidation());
        dto.setIdTransaction(validation.getTransaction() != null
                ? validation.getTransaction().getIdTransaction()
                : null);
        dto.setEtat(validation.getEtat());
        dto.setDateValidation(validation.getDateValidation());
        return dto;
    }
}
