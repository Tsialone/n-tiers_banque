using Epargne.DTO;
using Epargne.Models;
using System.Collections.Generic;
using System.Linq;

namespace Epargne.Mappers
{
    public static class CompteEpargneMapper
    {
        // Entity -> DTO
        public static CompteEpargneDto ToDto(this CompteEpargne entity)
        {
            if (entity == null) return null;

            return new CompteEpargneDto
            {
                IdCompte = entity.IdCompte,
                IdClient = entity.IdClient,
                CapitalEpargne = entity.CapitalEpargne,
                Libelle = entity.Libelle,
                DateOuverture = entity.DateOuverture,
                Retrait = entity.Retrait ,
                TauxInteret = entity.TauxInteret,
                Transactions = entity.Transactions?
                    .Select(t => new TransactionEpargneDto
                    {
                        IdTransaction = t.IdTransaction,
                        IdCompte = t.IdCompte,
                        DateTransaction = t.DateTransaction,
                        Libelle = t.Libelle,
                        Montant = t.Montant,
                        Sens = t.Sens
                    }).ToList()
            };
        }

        // CreateDTO -> Entity
        public static CompteEpargne ToEntity(this CompteEpargneCreateDto dto)
        {
            if (dto == null) return null;

            return new CompteEpargne
            {
                IdClient = dto.IdClient,
                CapitalEpargne = dto.CapitalEpargne,
                Libelle = dto.Libelle,
                DateOuverture = dto.DateOuverture,
                Retrait = dto.Retrait ,
                TauxInteret = dto.TauxInteret
            };
        }

        // Liste d’entities -> Liste DTOs
        public static List<CompteEpargneDto> ToDtoList(this IEnumerable<CompteEpargne> entities)
        {
            return entities?.Select(e => e.ToDto()).ToList();
        }
    }
}
