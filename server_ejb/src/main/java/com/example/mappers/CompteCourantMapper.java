package com.example.mappers;

import com.example.dto.CompteCourantDto;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;

public class CompteCourantMapper {

    // DTO -> Entity
    public static CompteCourant toEntity(CompteCourantDto dto, ClientCourant client) {
        try {
            if (dto == null) return null;

            CompteCourant entity = new CompteCourant();
            entity.setIdCompte(dto.getIdCompte());
            entity.setNom(dto.getNom());
            entity.setCapital(dto.getCapital());
            entity.setDateOuverture(dto.getDateOuverture());
            entity.setDecouvertAutorise(dto.getDecouvertAutorise());

            // Associer le client (si fourni)
            if (client != null) {
                entity.setClient(client);
            }

            return entity;
        } catch (Exception e) {
            System.err.println("Erreur lors du mapping DTO -> Entity : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Entity -> DTO
    public static CompteCourantDto toDto(CompteCourant entity) {
        try {
            if (entity == null) return null;

            CompteCourantDto dto = new CompteCourantDto();
            dto.setIdCompte(entity.getIdCompte());
            dto.setNom(entity.getNom());
            dto.setCapital(entity.getCapital());
            dto.setDateOuverture(entity.getDateOuverture());
            dto.setDecouvertAutorise(entity.getDecouvertAutorise());

            if (entity.getClient() != null) {
                dto.setIdClient(entity.getClient().getIdClient());
            }

            return dto;
        } catch (Exception e) {
            System.err.println("Erreur lors du mapping Entity -> DTO : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
