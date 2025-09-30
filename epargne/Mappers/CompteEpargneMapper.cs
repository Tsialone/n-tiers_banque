using Epargne.DTO;
using Epargne.Models;
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
                ClientNom = entity.Client?.Nom,
                ClientPrenom = entity.Client?.Prenom,
                DateOuverture = entity.DateOuverture,
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

        // DTO -> Entity
        public static CompteEpargne ToEntity(this CompteEpargneDto dto)
        {
            if (dto == null) return null;

            return new CompteEpargne
            {
                IdCompte = dto.IdCompte,
                IdClient = dto.IdClient,
                DateOuverture = dto.DateOuverture,
                TauxInteret = dto.TauxInteret,
                Transactions = dto.Transactions?
                    .Select(t => new TransactionEpargne
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
    }

}
