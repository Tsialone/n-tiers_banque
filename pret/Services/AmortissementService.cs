using Microsoft.EntityFrameworkCore;
using Pret.Data;
using Pret.DTO;
using Pret.Models;
using Pret.Mappers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Net.Quic;
using Pret.ExternalApi.Services;
using Pret.ExternalApi.DTO;
using Pret.Utils;

namespace Pret.Services
{
    public class AmortissementService
    {
        private readonly AppDbContext _context;
        private readonly TransactionPretService _transactionPretService;
        private readonly TransactionCourantApiClient _transactionCourantApiClient;


        public AmortissementService(
            AppDbContext context,
            TransactionPretService transactionPretService,
            TransactionCourantApiClient transactionCourantApiClient
            )
        {
            _context = context;
            _transactionPretService = transactionPretService;
            _transactionCourantApiClient = transactionCourantApiClient;
        }

        public AppDbContext Context => _context;



        // rembourssement du pret sur le dernier mois de l'amortissement
        public async Task<List<AmortissementDto>> rembourssementPret(int idComptePret, int idCompteCourant, DateOnly? date, double montant)
        {

            await using var dbTransaction = await _context.Database.BeginTransactionAsync();
            try
            {
                var temp_date = date ?? DateUtils.Today();
                var amortissementsNonPaye = await GetByPretAndDateAndStatus(idComptePret, date, "en_attente");
                var amortissementsPaye = await GetByPretAndDateAndStatus(idComptePret, date, "paye");

                if (amortissementsNonPaye.Count == 0 && amortissementsPaye.Count != 0) throw new Exception("Tout les mois de ce compte pret son ok");
                if (amortissementsNonPaye.Count == 0 && amortissementsPaye.Count == 0) throw new Exception("Pas d'amortissement sur cette depuis cette date: " + date);

                // verifie si le montant est ok, au cas ou
                // ici
                var lastMonth = amortissementsNonPaye.First();
                // update du statut
                var amortissement = await GetByIdAsync(lastMonth.IdAmortissement);
                amortissement.Statut = "paye";
                // Console.WriteLine("ito ny id any " + amortissement.IdAmortissement);

                var transaction_courant_created = await _transactionCourantApiClient.CreateAsync(
                     new TransactionCourantDto
                     {
                         IdCompte = idCompteCourant,
                         DateTransaction = temp_date,
                         Libelle = "remboursement du mois: " + lastMonth.CreatedAt,
                         Montant = lastMonth.Mensualite, // ca peut varie si payement partiel
                         Sens = "debit"
                     }
                );
                await UpdateAsync(amortissement);

                await dbTransaction.CommitAsync();
                return await GetByCompteIdAsync(idComptePret);
            }
            catch (Exception ex)
            {
                await dbTransaction.RollbackAsync();
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Erreur : " + innerMessage, ex);
            }
        }

        // getSoldeByPretAndDate
        public async Task<List<AmortissementDto>> GetByPretAndDateAndStatus(int idComptePret, DateOnly? date, string statut)
        {

            try
            {
                var query = _context.Amortissements
                .Include(a => a.ComptePret)
                // .ThenInclude(c => c.Client)
                .Where(a => a.ComptePret.IdCompte == idComptePret);
                Console.WriteLine("tsy null ny date " + date);
                if (date.HasValue)
                {
                    query = query.Where(a => a.CreatedAt <= date.Value);
                    Console.WriteLine("tsy null ny date " + date.Value);
                }
                if (!string.IsNullOrEmpty(statut))
                {
                    query = query.Where(a => a.Statut == statut);
                    Console.WriteLine("tsy null ny satut " + statut);


                }
                var amortissements = await query
                .OrderBy(a => a.CreatedAt)
                .ToListAsync();
                return amortissements.Select(a => a.ToDto()).ToList();
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Erreur : " + innerMessage, ex);
            }

        }



        // Récupérer tous les amortissements
        public async Task<List<AmortissementDto>> GetAllDtoAsync()
        {
            var amortissements = await _context.Amortissements
                .Include(a => a.ComptePret)
                // .ThenInclude(c => c.Client)
                .ToListAsync();

            return amortissements.Select(a => a.ToDto()).ToList();
        }

        // Récupérer par ID
        public async Task<AmortissementDto> GetByIdDtoAsync(int idAmortissement)
        {
            var amortissement = await _context.Amortissements
                .Include(a => a.ComptePret)
                // .ThenInclude(c => c.Client)
                .FirstOrDefaultAsync(a => a.IdAmortissement == idAmortissement);

            return amortissement?.ToDto();
        }
        public async Task<Amortissement> GetByIdAsync(int idAmortissement)
        {
            var amortissement = await _context.Amortissements
                .Include(a => a.ComptePret)
                // .ThenInclude(c => c.Client)
                .FirstOrDefaultAsync(a => a.IdAmortissement == idAmortissement);

            return amortissement;
        }
        // Récupérer tous les amortissements d'un compte
        public async Task<List<AmortissementDto>> GetByCompteIdAsync(int idCompte)
        {
            var amortissements = await _context.Amortissements
                .Where(a => a.IdCompte == idCompte)
                .Include(a => a.ComptePret)
                // .ThenInclude(c => c.Client)
                .ToListAsync();

            return amortissements.Select(a => a.ToDto()).ToList();
        }

        // Ajouter un amortissement
        public async Task<Amortissement> AddAsync(Amortissement amortissement)
        {
            await _context.Amortissements.AddAsync(amortissement);
            await _context.SaveChangesAsync();
            return amortissement;
        }

        // Ajouter depuis DTO de création
        public async Task<Amortissement> AddAsync(AmortissementCreateDto dto)
        {
            var entity = dto.ToEntity();
            await _context.Amortissements.AddAsync(entity);
            await _context.SaveChangesAsync();
            return entity;
        }

        // Mettre à jour
        public async Task UpdateAsync(Amortissement amortissement)
        {
            try
            {
                // var entity = dto.ToEntity();
                _context.Amortissements.Update(amortissement);
                await _context.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Erreur : " + innerMessage, ex);
            }

        }

        // Supprimer
        public async Task DeleteAsync(int idAmortissement)
        {
            var amortissement = await _context.Amortissements.FindAsync(idAmortissement);
            if (amortissement != null)
            {
                _context.Amortissements.Remove(amortissement);
                await _context.SaveChangesAsync();
            }
        }
    }
}
