package com.example.mappers;

import com.example.models.*;
import com.example.server_dtos.*;
import java.util.stream.Collectors;

public class VirementMapper {

    public static Virement toEntity(VirementDto dto, CompteCourant debit, CompteCourant credit) throws Exception {
        Virement v = new Virement();
        v.setIdVirement(dto.getIdVirement());
        v.setIdObject(dto.getIdObject());
        v.setCompteDebit(debit);
        v.setCompteCredit(credit);
        v.setDateVirement(dto.getDateVirement());
        v.setMontant(dto.getMontant());
        v.setDevise(dto.getDevise());
        v.setTaux(dto.getTaux());

        
        return v;
    }

    public static VirementDto toDto(Virement v) throws Exception {
        VirementDto dto = new VirementDto();
        dto.setIdVirement(v.getIdVirement());
        dto.setIdObject(v.getIdObject());
        dto.setIdCompteDebit(v.getCompteDebit().getIdCompte());
        dto.setIdCompteCredit(v.getCompteCredit().getIdCompte());
        dto.setDateVirement(v.getDateVirement());
        dto.setMontant(v.getMontant());
        dto.setDevise(v.getDevise());
        dto.setTaux(v.getTaux());
        dto.setFrais(v.getFrais().getFraisValue(v.getMontant()));

        // dto.setFrais(v.getFraisValue());

        if (v.getValidations() != null) {
            dto.setValidations(v.getValidations().stream()
                    .map(ValidationVirementMapper::toDto)
                    .collect(Collectors.toList()));
        }

        if (v.getLastValidation() != null) {
            dto.setLastValidation(ValidationVirementMapper.toDto(v.getLastValidation()));
        }

        return dto;
    }
}
