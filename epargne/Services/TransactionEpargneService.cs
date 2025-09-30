using System.Collections.Generic;
using System.Threading.Tasks;
using Epargne.Data;
using Epargne.DTO;
using Epargne.Models;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using System;

namespace Epargne.Services
{
    public class TransactionEpargneService
    {
        private readonly AppDbContext _context;

        public TransactionEpargneService(AppDbContext context)
        {
            _context = context;
        }
        public AppDbContext Context => _context;

        public async Task<double> getSoldeByClientAndEpargneAndDate(int idClient, int idCompteEpargne, DateOnly date)
        {
            
            var solde = await _context.TransactionsEpargne
                .Where(t => t.IdCompte == idCompteEpargne
                         && t.Compte.IdClient == idClient
                         && t.DateTransaction <= date) 
                .SumAsync(t => t.Sens == "credit" ? t.Montant : -t.Montant);

            return (double)solde;
        }
        public async Task<List<TransactionEpargneDto>> GetByIdClientAndIdCompteAsync(int idClient, int idCompte)
        {
            var transactions = await _context.TransactionsEpargne
                .Include(t => t.Compte)
                .Where(t => t.IdCompte == idCompte && t.Compte.IdClient == idClient)
                .ToListAsync();

            return transactions.Select(t => new TransactionEpargneDto
            {
                IdTransaction = t.IdTransaction,
                DateTransaction = t.DateTransaction,
                Libelle = t.Libelle,
                Montant = t.Montant,
                Sens = t.Sens
            }).ToList();
        }


        // Retourne toutes les transactions en DTO
        public async Task<List<TransactionEpargneDto>> GetAllDtoAsync()
        {
            var transactions = await _context.TransactionsEpargne
                .Include(t => t.Compte) // si tu veux info compte, sinon ignore
                .ToListAsync();

            return transactions.Select(t => new TransactionEpargneDto
            {
                IdTransaction = t.IdTransaction,
                DateTransaction = t.DateTransaction,
                Libelle = t.Libelle,
                Montant = t.Montant,
                Sens = t.Sens
            }).ToList();
        }

        public async Task<List<TransactionEpargne>> GetAllAsync() =>
            await _context.TransactionsEpargne
                .Include(t => t.Compte)
                .ThenInclude(c => c.Client)
                .ToListAsync();

        public async Task<TransactionEpargne> GetByIdAsync(int idTransaction) =>
            await _context.TransactionsEpargne
                .Include(t => t.Compte)
                .ThenInclude(c => c.Client)
                .FirstOrDefaultAsync(t => t.IdTransaction == idTransaction);

        public async Task<List<TransactionEpargne>> GetByCompteIdAsync(int idCompte) =>
            await _context.TransactionsEpargne
                .Where(t => t.IdCompte == idCompte)
                .ToListAsync();

        public async Task<TransactionEpargne> AddAsync(TransactionEpargne transaction)
        {
            try
            {
                await _context.TransactionsEpargne.AddAsync(transaction);
                await _context.SaveChangesAsync();
                return transaction;
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Impossible d'ajouter la transaction : " + innerMessage, ex);
            }
        }

        public async Task UpdateAsync(TransactionEpargne transaction)
        {
            _context.TransactionsEpargne.Update(transaction);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int idTransaction)
        {
            var transaction = await _context.TransactionsEpargne.FindAsync(idTransaction);
            if (transaction != null)
            {
                _context.TransactionsEpargne.Remove(transaction);
                await _context.SaveChangesAsync();
            }
        }
    }
}
