package com.example.mappers;

import com.example.models.*;
import com.example.server_dtos.*;

public class ValidationDepotMapper {

    public static ValidationDepotDto toDto(ValidationDepot v) {
        ValidationDepotDto dto = new ValidationDepotDto();
        dto.setIdValidation(v.getIdValidation());
        dto.setIdDepot(v.getDepot().getIdDepot());
        dto.setIdEtat(v.getEtat().getIdEtat());
        dto.setDateValidation(v.getDateValidation());
        if (v.getEtat() != null)dto.setEtat(EtatMapper.toDto(v.getEtat()));

        return dto;
    }

    public static ValidationDepot toEntity(ValidationDepotDto dto) {
        ValidationDepot v = new ValidationDepot();
        v.setIdValidation(dto.getIdValidation());
        v.setDateValidation(dto.getDateValidation());

        if (dto.getIdDepot() != null) {
            Depot depot = new Depot();
            depot.setIdDepot(dto.getIdDepot());
            v.setDepot(depot);
        }

        if (dto.getIdEtat() != null) {
            Etat etat = new Etat();
            etat.setIdEtat(dto.getIdEtat());
            v.setEtat(etat);
        }

        return v;
    }
}
