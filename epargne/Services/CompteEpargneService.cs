using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Epargne.Data;
using Epargne.DTO;
using System.Linq;
using Epargne.Models;

namespace Epargne.Services
{
    public class CompteEpargneService
    {
        private readonly AppDbContext _context;

        public CompteEpargneService(AppDbContext context)
        {
            _context = context;
        }



        // Récupère tous les comptes avec DTO
        public async Task<List<CompteEpargneDto>> GetAllDtoAsync()
        {
            var comptes = await _context.ComptesEpargne
                .Include(c => c.Client)
                .Include(c => c.Transactions)
                .ToListAsync();

            return comptes.Select(c => new CompteEpargneDto
            {
                IdCompte = c.IdCompte,
                IdClient = c.IdClient,
                ClientNom = c.Client.Nom,
                ClientPrenom = c.Client.Prenom,
                DateOuverture = c.DateOuverture,
                TauxInteret = c.TauxInteret,
                Transactions = c.Transactions?.Select(t => new TransactionEpargneDto
                {
                    IdTransaction = t.IdTransaction,
                    DateTransaction = t.DateTransaction,
                    Libelle = t.Libelle,
                    Montant = t.Montant,
                    Sens = t.Sens
                }).ToList()
            }).ToList();
        }

        public async Task<List<CompteEpargne>> GetAllAsync() =>
            await _context.ComptesEpargne
                .Include(c => c.Client)
                .ToListAsync();

        public async Task<CompteEpargne> GetByIdAsync(int idCompte) =>
            await _context.ComptesEpargne
                .Include(c => c.Client)
                .Include(c => c.Transactions)
                .FirstOrDefaultAsync(c => c.IdCompte == idCompte);

        public async Task<List<CompteEpargne>> GetByClientIdAsync(int idClient) =>
            await _context.ComptesEpargne
                .Where(c => c.IdClient == idClient)
                .Include(c => c.Transactions)
                .ToListAsync();

        public async Task AddAsync(CompteEpargne compte)
        {
            await _context.ComptesEpargne.AddAsync(compte);
            await _context.SaveChangesAsync();
        }

        public async Task UpdateAsync(CompteEpargne compte)
        {
            _context.ComptesEpargne.Update(compte);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int idCompte)
        {
            var compte = await _context.ComptesEpargne.FindAsync(idCompte);
            if (compte != null)
            {
                _context.ComptesEpargne.Remove(compte);
                await _context.SaveChangesAsync();
            }
        }
    }
}
