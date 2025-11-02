package com.example.mappers;

import com.example.classes.Virement;
import com.example.models.CompteCourant;
import com.example.server_dtos.VirementDto;

public class VirementMapper {

    public static Virement toEntity(VirementDto dto, CompteCourant compteDebit, CompteCourant compteCredit) throws Exception {
        if (dto == null) {
            return null;
        }

        return new Virement(
                compteDebit,         
                compteCredit,          
                dto.getDateVirement(),
                dto.getMontant(),
                dto.getDevise()
        );
    }

    public static VirementDto toDto(Virement virement) {
        if (virement == null) {
            return null;
        }

        VirementDto dto = new VirementDto();
        dto.setIdCompteDebit(virement.getCompteDebit() != null ? virement.getCompteDebit().getIdCompte() : null);
        dto.setIdCompteCredit(virement.getCompteCredit() != null ? virement.getCompteCredit().getIdCompte() : null);
        dto.setDateVirement(virement.getDateVirement());
        dto.setMontant(virement.getMontant());
        dto.setDevise(virement.getDevise());

        return dto;
    }
}
