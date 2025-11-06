package com.example.mappers;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.TypeCompte;
import com.example.server_dtos.CompteCourantDto;

public class CompteCourantMapper {

    // Entité → DTO
    public static CompteCourantDto toDto(CompteCourant compte)  {
        CompteCourantDto dto = new CompteCourantDto();
        dto.setIdCompte(compte.getIdCompte());
        dto.setNom(compte.getNom());
        dto.setIdClient(compte.getClient() != null ? compte.getClient().getIdClient() : null);
        dto.setTypeCompte(
                compte.getTypeCompte() != null ? TypeCompteMapper.toDto(compte.getTypeCompte(), false) : null);
        dto.setDateOuverture(compte.getDateOuverture());
        dto.setCapital(compte.getCapital());
        try {
            dto.setSolde(compte.getSolde());
        } catch (Exception e) {
            // TODO: handle exception
        }
        dto.setDecouvertAutorise(compte.getDecouvertAutorise());
        return dto;
    }

    // DTO → Entité
    public static CompteCourant toEntity(CompteCourantDto dto, ClientCourant client, TypeCompte type) {
        CompteCourant compte = new CompteCourant();
        compte.setIdCompte(dto.getIdCompte());
        compte.setNom(dto.getNom());
        compte.setClient(client);
        compte.setTypeCompte(type);
        compte.setDateOuverture(dto.getDateOuverture());
        compte.setCapital(dto.getCapital());
        compte.setDecouvertAutorise(dto.getDecouvertAutorise());
        return compte;
    }
}
