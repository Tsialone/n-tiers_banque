package com.example.mappers;

import com.example.models.TypeCompte;
import com.example.server_dtos.TypeCompteDto;

public class TypeCompteMapper {

    // Entité → DTO
        public static TypeCompteDto toDto(TypeCompte typeCompte, boolean includeComptes) {
            TypeCompteDto dto = new TypeCompteDto();
            dto.setIdTypeCompte(typeCompte.getIdTypeCompte());
            dto.setLibelle(typeCompte.getLibelle());

            if (includeComptes && typeCompte.getComptes() != null) {
                dto.setComptes(null);
            }

            return dto;
        }

    // DTO → Entité
    public static TypeCompte toEntity(TypeCompteDto dto) {
        TypeCompte typeCompte = new TypeCompte();
        typeCompte.setIdTypeCompte(dto.getIdTypeCompte());
        typeCompte.setLibelle(dto.getLibelle());
        // La liste des comptes peut être ignorée ou gérée via service pour éviter des problèmes de persistance
        return typeCompte;
    }
}
