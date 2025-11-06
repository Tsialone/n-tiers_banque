package com.example.mappers;

import com.example.models.*;
import com.example.server_dtos.*;
import java.util.stream.Collectors;

public class DepotMapper {

    public static DepotDto toDto(Depot depot) {
        DepotDto dto = new DepotDto();
        dto.setIdDepot(depot.getIdDepot());
        dto.setIdObject(depot.getIdObject());
        dto.setIdCompteCredit(depot.getCompteCredit().getIdCompte());
        dto.setMontant(depot.getMontant());
        dto.setDevise(depot.getDevise());
        dto.setTaux(depot.getTaux());
        if (depot.getValidations() != null) {
            dto.setValidations(depot.getValidations().stream()
                    .map(ValidationDepotMapper::toDto)
                    .collect(Collectors.toList()));
        }
        if (depot.getLastValidation() != null) {
            dto.setLastValidation(ValidationDepotMapper.toDto(depot.getLastValidation()));
        }
        return dto;
    }

    public static Depot toEntity(DepotDto dto) {
        Depot depot = new Depot();
        depot.setIdDepot(dto.getIdDepot());
        depot.setIdObject(dto.getIdObject());
        depot.setMontant(dto.getMontant());
        depot.setDevise(dto.getDevise());
        depot.setTaux(dto.getTaux());

        if (dto.getIdCompteCredit() != null) {
            CompteCourant compte = new CompteCourant();
            compte.setIdCompte(dto.getIdCompteCredit());
            depot.setCompteCredit(compte);
        }

        if (dto.getValidations() != null) {
            depot.setValidations(dto.getValidations().stream()
                    .map(ValidationDepotMapper::toEntity)
                    .collect(java.util.stream.Collectors.toList()));
        }

        return depot;
    }
}
