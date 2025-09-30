using Pret.DTO;
using Pret.Models;
using System.Collections.Generic;
using System.Linq;

namespace Pret.Mappers
{
    public static class ComptePretMapper
    {
        // Entity -> DTO
        public static ComptePretDto ToDto(this ComptePret entity)
        {
            if (entity == null) return null;

            return new ComptePretDto
            {
                IdCompte = entity.IdCompte,
                IdClient = entity.IdClient,
                ClientNom = entity.Client?.Nom,
                ClientPrenom = entity.Client?.Prenom,
                DateOuverture = entity.DateOuverture,
                Montant = entity.Montant,
                TauxInteret = entity.TauxInteret,
                DureeMois = entity.DureeMois,
                DateEcheance = entity.DateEcheance,
                Statut = entity.Statut
            };
        }

        // DTO -> Entity
        public static ComptePret ToEntity(this ComptePretCreateDto dto)
        {
            if (dto == null) return null;

            return new ComptePret
            {
                IdClient = dto.IdClient,
                DateOuverture = dto.DateOuverture,
                Montant = dto.Montant,
                TauxInteret = dto.TauxInteret,
                DureeMois = dto.DureeMois,
                DateEcheance = dto.DateEcheance,
                Statut = dto.Statut
            };
        }

        // Optional: liste de entities -> DTOs
        public static List<ComptePretDto> ToDtoList(this IEnumerable<ComptePret> entities)
        {
            return entities?.Select(e => e.ToDto()).ToList();
        }
    }
}
