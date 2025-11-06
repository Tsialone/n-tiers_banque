package com.example.mappers;

import com.example.models.Etat;
import com.example.server_dtos.EtatDto;

public class EtatMapper {

    public static EtatDto toDto(Etat etat) {
        if (etat == null) return null;
        EtatDto dto = new EtatDto();
        dto.setIdEtat(etat.getIdEtat());
        dto.setLibelle(etat.getLibelle());
        return dto;
    }

    public static Etat toEntity(EtatDto dto) {
        if (dto == null) return null;
        Etat etat = new Etat();
        etat.setIdEtat(dto.getIdEtat());
        etat.setLibelle(dto.getLibelle());
        return etat;
    }
}
