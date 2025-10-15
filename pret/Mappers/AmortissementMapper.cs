using Pret.DTO;
using Pret.Models;
using System.Linq;
using System.Collections.Generic;

namespace Pret.Mappers
{
    public static class AmortissementMapper
    {
        // Entity -> DTO
        public static AmortissementDto ToDto(this Amortissement entity)
        {
            if (entity == null) return null;

            return new AmortissementDto
            {
                IdAmortissement = entity.IdAmortissement,
                IdCompte = entity.IdCompte,
                Mois = entity.Mois,
                Mensualite = entity.Mensualite,
                Interet = entity.Interet,
                Capital = entity.Capital,
                ResteDu = entity.ResteDu,
                CreatedAt = entity.CreatedAt,
                Statut = entity.Statut,
                CompteIdClient = entity.ComptePret?.IdClient ?? 0,
                // CompteClientNom = entity.ComptePret?.Client?.Nom,
                // CompteClientPrenom = entity.ComptePret?.Client?.Prenom
            };
        }

        // DTO -> Entity
        public static Amortissement ToEntity(this AmortissementCreateDto dto)
        {
            if (dto == null) return null;

            return new Amortissement
            {
                IdCompte = dto.IdCompte,
                Mois = dto.Mois,
                Mensualite = dto.Mensualite,
                Interet = dto.Interet,
                Capital = dto.Capital,
                ResteDu = dto.ResteDu,
                CreatedAt = dto.CreatedAt,
                Statut = dto.Statut
            };
        }

        // Optional: liste de entities -> DTOs
        public static List<AmortissementDto> ToDtoList(this IEnumerable<Amortissement> entities)
        {
            return entities?.Select(e => e.ToDto()).ToList();
        }
    }
}
