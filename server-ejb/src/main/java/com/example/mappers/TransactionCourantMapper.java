package com.example.mappers;

import java.util.stream.Collectors;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;
import com.example.server_dtos.CompteCourantDto;
import com.example.server_dtos.TransactionCourantDto;

public class TransactionCourantMapper {

    // DTO → Entité
    public static TransactionCourant toEntity(TransactionCourantDto dto, CompteCourant compteCourant , CompteCourant compteDest) {
        TransactionCourant transaction = new TransactionCourant();

        if (dto.getIdTransaction() != null) {
            transaction.setIdTransaction(dto.getIdTransaction());
        }
        // System.out.println(dto);
        // Récupérer le compte depuis l'ID
        transaction.setCompte(compteCourant);
        transaction.setCompteDest(compteDest);
        transaction.setLibelle(dto.getLibelle());
        transaction.setMontant(dto.getMontant());
        transaction.setSens(dto.getSens());
        transaction.setDevise(dto.getDevise());
        transaction.setValidate(dto.isValidate());
        transaction.setDateTransaction(dto.getDateTransaction() != null
                ? dto.getDateTransaction()
                : java.time.LocalDate.now());

        return transaction;
    }

    // Entité → DTO
    public static TransactionCourantDto toDto(TransactionCourant transaction) {
        TransactionCourantDto dto = new TransactionCourantDto();
        dto.setIdTransaction(transaction.getIdTransaction());
        dto.setIdCompte(transaction.getCompte().getIdCompte());
        dto.setLibelle(transaction.getLibelle());
        dto.setMontant(transaction.getMontant());
        dto.setSens(transaction.getSens());
        dto.setValidate(transaction.isValidate());
        dto.setDateTransaction(transaction.getDateTransaction());
        dto.setDevise(transaction.getDevise());
        if (transaction.getCompteDest() != null) dto.setIdCompteDest(transaction.getCompteDest().getIdCompte());
        
        

        if (transaction.getValidations() != null) {
            dto.setValidations(transaction.getValidations().stream()
                    .map(ValidationMapper::toDto)
                    .collect(Collectors.toList()));
        }

        if (transaction.getLastValidation() != null) {
            dto.setLastValidation(ValidationMapper.toDto(transaction.getLastValidation()));
        }
        return dto;
    }
}
