using Pret.DTO;
using Pret.Models;
using System.Collections.Generic;
using System.Linq;

namespace Pret.Mappers
{
    public static class TransactionPretMapper
    {
        // Entity -> DTO
        public static TransactionPretDto ToDto(this TransactionPret entity)
        {
            if (entity == null) return null;

            return new TransactionPretDto
            {
                IdTransaction = entity.IdTransaction,
                IdCompte = entity.IdCompte,
                IdAmortissement = entity.IdAmortissement,
                DateTransaction = entity.DateTransaction,
                Libelle = entity.Libelle,
                Montant = entity.Montant,
                TypeTransaction = entity.TypeTransaction,
                CompteIdClient = entity.ComptePret?.IdClient ?? 0,
                // CompteClientNom = entity.ComptePret?.Client?.Nom,
                // CompteClientPrenom = entity.ComptePret?.Client?.Prenom
            };
        }

        // DTO -> Entity
        public static TransactionPret ToEntity(this TransactionPretCreateDto dto)
        {
            if (dto == null) return null;

            return new TransactionPret
            {
                IdCompte = dto.IdCompte,
                IdAmortissement = dto.IdAmortissement,
                Libelle = dto.Libelle,
                Montant = dto.Montant,
                TypeTransaction = dto.TypeTransaction
            };
        }

        // Optional: liste de entities -> DTOs
        public static List<TransactionPretDto> ToDtoList(this IEnumerable<TransactionPret> entities)
        {
            return entities?.Select(e => e.ToDto()).ToList();
        }
    }
}
