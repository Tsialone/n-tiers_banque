using System.Collections.Generic;
using System.Threading.Tasks;
using Epargne.Data;
using Epargne.DTO;
using Epargne.Models;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using System;
using Epargne.Utils;

namespace Epargne.Services
{
    public class TransactionEpargneService
    {
        private readonly AppDbContext _context;
        private readonly CompteEpargneService _compteEpargneService;

        public TransactionEpargneService(
            AppDbContext context,
            CompteEpargneService compteEpargneService
            )
        {
            _context = context;
            _compteEpargneService = compteEpargneService;
        }
        public AppDbContext Context => _context;

        public async Task<double> getSoldeByClientAndEpargneAndDate(int idClient, int idCompteEpargne, DateOnly date)
        {
            try
            {
                var compteEpargne = await _compteEpargneService.GetByIdAsync(idCompteEpargne);

                decimal tauxMensuel = compteEpargne.TauxInteret / 12 / 100;

                decimal solde = compteEpargne.CapitalEpargne;
                DateOnly currentDate = compteEpargne.DateOuverture;
                while (currentDate <= date)
                {
                    // Transactions du mois courant
                    var transactionsMois = await _context.TransactionsEpargne
                        .Where(t => t.IdCompte == idCompteEpargne
                                 && t.Compte.IdClient == idClient
                                 && t.DateTransaction.Year == currentDate.Year
                                 && t.DateTransaction.Month == currentDate.Month
                                )
                        .ToListAsync();
                    Console.WriteLine("ouverture " + date);
                    decimal credit = transactionsMois.Where(t => t.Sens == "credit").Sum(t => t.Montant);
                    decimal debit = transactionsMois.Where(t => t.Sens == "debit").Sum(t => t.Montant);


                    // Ajouter intérêts du mois
                    solde += solde * tauxMensuel;

                    // Ajouter crédits et retirer débits
                    Console.WriteLine("nemanyyyyyyyyyyyyyyyy " + transactionsMois);
                    Console.WriteLine("==== Transactions du mois ==== pour idCompteEpargne= " + idCompteEpargne + " et idClient= " + idClient);
                    foreach (var t in transactionsMois)
                    {
                        Console.WriteLine($"ID: {t.IdTransaction}, Montant: {t.Montant},  idClient: {t.Compte.IdClient} Sens: {t.Sens}, Date: {t.DateTransaction}");
                    }
                    solde += credit - debit;
                    Console.WriteLine("soldeeeeeeeeee" + solde);

                    // Passer au mois suivant
                    currentDate = currentDate.AddMonths(1);
                }
                    Console.WriteLine("soldeeeeeeeeee" + solde);


                return (double)Math.Round(solde, 2); ;
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Erreur : " + innerMessage, ex);
            }
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
        public async Task<List<TransactionEpargneDto>> GetByIdClient(int idClient)
        {
            var transactions = await _context.TransactionsEpargne
                .Include(t => t.Compte)
                .Where(t => t.Compte.IdClient == idClient)
                .ToListAsync();
            return transactions.Select(t => new TransactionEpargneDto
            {
                IdCompte = t.IdCompte,
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
                // .ThenInclude(c => c.Client)
                .ToListAsync();

        public async Task<TransactionEpargne> GetByIdAsync(int idTransaction) =>
            await _context.TransactionsEpargne
                .Include(t => t.Compte)
                // .ThenInclude(c => c.Client)
                .FirstOrDefaultAsync(t => t.IdTransaction == idTransaction);

        public async Task<List<TransactionEpargne>> GetByCompteIdAsync(int idCompte) =>
            await _context.TransactionsEpargne
                .Where(t => t.IdCompte == idCompte)
                .ToListAsync();

        public async Task<TransactionEpargne> AddAsync(TransactionEpargne transaction)
        {
            try
            {
                // on verifie si la transaction debit est conforme au % de retrait
                var compte_epargne = await _compteEpargneService.GetByIdAsync(transaction.IdCompte);
                double pourcentage_retrait = (double)compte_epargne.Retrait;
                double compte_solde = await getSoldeByClientAndEpargneAndDate(compte_epargne.IdClient, transaction.IdCompte,  transaction.DateTransaction );
                double max_retrait = compte_solde * pourcentage_retrait / 100;
                if (max_retrait < (double)transaction.Montant && transaction.Sens == "debit")
                {
                    throw new Exception("Retrait maximum atteint: " + max_retrait + ", or que vous avez essaieé de retiré: " + transaction.Montant);
                }

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
