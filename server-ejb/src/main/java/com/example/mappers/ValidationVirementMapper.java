package com.example.mappers;

import com.example.models.*;
import com.example.server_dtos.*;

public class ValidationVirementMapper {

    public static ValidationVirement toEntity(ValidationVirementDto dto, Virement virement, Etat etat , ClientCourant utilisateur) {
        ValidationVirement validation = new ValidationVirement();
        validation.setIdValidation(dto.getIdValidation());
        validation.setVirement(virement);
        validation.setEtat(etat);
        validation.setUtilisateur(utilisateur);
        validation.setDateValidation(dto.getDateValidation());
        return validation;
    }

    public static ValidationVirementDto toDto(ValidationVirement validation) {
        ValidationVirementDto dto = new ValidationVirementDto();
        dto.setIdValidation(validation.getIdValidation());
        dto.setIdVirement(validation.getVirement().getIdVirement());
        dto.setIdEtat(validation.getEtat().getIdEtat());
        
        if (validation.getUtilisateur() != null)  dto.setIdUtilisateur(validation.getUtilisateur().getIdClient());
        if (validation.getEtat() != null)dto.setEtat(EtatMapper.toDto(validation.getEtat()));
        
        dto.setDateValidation(validation.getDateValidation());
        return dto;
    }
}
