package com.example.mappers;

import com.example.models.*;
import com.example.server_dtos.RetraitDto;
import java.util.stream.Collectors;

public class RetraitMapper {

    public static RetraitDto toDto(Retrait retrait) {
        RetraitDto dto = new RetraitDto();
        dto.setIdRetrait(retrait.getIdRetrait());
        dto.setIdObject(retrait.getIdObject());
        dto.setIdCompteDebit(retrait.getCompteDebit().getIdCompte());
        dto.setMontant(retrait.getMontant());
        dto.setDevise(retrait.getDevise());
        dto.setTaux(retrait.getTaux());
        if (retrait.getValidations() != null) {
            dto.setValidations(retrait.getValidations().stream()
                    .map(ValidationRetraitMapper::toDto)
                    .collect(Collectors.toList()));
        }
        if (retrait.getLastValidation() != null) {
            dto.setLastValidation(ValidationRetraitMapper.toDto(retrait.getLastValidation()));
        }
        return dto;
    }

    public static Retrait toEntity(RetraitDto dto, CompteCourant compteDebit) {
        if (dto == null) return null;

        Retrait retrait = new Retrait();
        retrait.setIdRetrait(dto.getIdRetrait());
        retrait.setIdObject(dto.getIdObject());
        retrait.setCompteDebit(compteDebit);
        retrait.setMontant(dto.getMontant());
        retrait.setDevise(dto.getDevise());
        retrait.setTaux(dto.getTaux());

        if (dto.getValidations() != null) {
            retrait.setValidations(dto.getValidations().stream()
                    .map(vdto -> ValidationRetraitMapper.toEntity(vdto, retrait, null)) // inject Retrait + Etat à gérer
                    .collect(Collectors.toList()));
        }

        return retrait;
    }
}
