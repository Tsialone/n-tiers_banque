using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Epargne.Data;
using Epargne.DTO;
using System.Linq;
using Epargne.Models;
using System;
using Epargne.Mappers;

namespace Epargne.Services
{
    public class CompteEpargneService
    {
        private readonly AppDbContext _context;

        public CompteEpargneService(AppDbContext context)
        {
            _context = context;
        }


        public AppDbContext Context => _context;


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

        public async Task<CompteEpargne> GetByIdAsync(int idCompte)
        {
            try
            {
                var compte = await _context.ComptesEpargne
                    .Include(c => c.Client)
                    .Include(c => c.Transactions)
                    .FirstOrDefaultAsync(c => c.IdCompte == idCompte);

                if (compte == null)
                    Console.WriteLine($"Compte épargne avec Id {idCompte} introuvable.");

                return compte;
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                Console.WriteLine($"Erreur lors de la récupération du compte : {innerMessage}");
                throw new Exception($"Erreur lors de la récupération du compte épargne {idCompte} : {innerMessage}", ex);
            }
        }


        public async Task<List<CompteEpargne>> GetByClientIdAsync(int idClient, DateOnly? date)
        {
            var query = _context.ComptesEpargne
                .Where(c => c.IdClient == idClient)
                .Include(c => c.Transactions)
                .AsQueryable();

            if (date.HasValue)
            {
                query = query.Where(c => c.DateOuverture <= date.Value);
            }
            Console.WriteLine("daty " + date);

            // Retourne la liste finale
            return await query.ToListAsync();
        }
        public async Task<List<CompteEpargne>> GetByClientIdAndCompteIdAsync(int idClient, int idCompte) =>
            await _context.ComptesEpargne
                .Where(c => c.IdClient == idClient && c.IdCompte == idCompte)
                .Include(c => c.Transactions)
                .ToListAsync();

        public async Task<CompteEpargne> AddAsync(CompteEpargneCreateDto dto)
        {

            try
            {
                var entity = CompteEpargneMapper.ToEntity(dto);
                await _context.ComptesEpargne.AddAsync(entity);
                await _context.SaveChangesAsync();
                return entity;
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Erreur : " + innerMessage, ex);
            }
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
