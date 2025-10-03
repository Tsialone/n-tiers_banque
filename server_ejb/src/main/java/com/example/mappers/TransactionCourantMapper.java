package com.example.mappers;

import com.example.dto.CompteCourantDto;
import com.example.dto.TransactionCourantDto;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;

public class TransactionCourantMapper {

    // DTO → Entité
    public static TransactionCourant toEntity(TransactionCourantDto dto, CompteCourant compteCourant) {
        TransactionCourant transaction = new TransactionCourant();

        if (dto.getIdTransaction() != null) {
            transaction.setIdTransaction(dto.getIdTransaction());
        }
        // System.out.println(dto);
        // Récupérer le compte depuis l'ID
        transaction.setCompte(compteCourant);

        transaction.setLibelle(dto.getLibelle());
        transaction.setMontant(dto.getMontant());
        transaction.setSens(dto.getSens());
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
        dto.setDateTransaction(transaction.getDateTransaction());
        return dto;
    }
}
