using Microsoft.EntityFrameworkCore;
using Pret.Data;
using Pret.DTO;
using Pret.Models;
using Pret.Mappers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Pret.Services
{
    public class AmortissementService
    {
        private readonly AppDbContext _context;

        public AmortissementService(AppDbContext context)
        {
            _context = context;
        }

        public AppDbContext Context => _context;

        // Récupérer tous les amortissements
        public async Task<List<AmortissementDto>> GetAllDtoAsync()
        {
            var amortissements = await _context.Amortissements
                .Include(a => a.ComptePret)
                .ThenInclude(c => c.Client)
                .ToListAsync();

            return amortissements.Select(a => a.ToDto()).ToList();
        }

        // Récupérer par ID
        public async Task<AmortissementDto> GetByIdAsync(int idAmortissement)
        {
            var amortissement = await _context.Amortissements
                .Include(a => a.ComptePret)
                .ThenInclude(c => c.Client)
                .FirstOrDefaultAsync(a => a.IdAmortissement == idAmortissement);

            return amortissement?.ToDto();
        }

        // Récupérer tous les amortissements d'un compte
        public async Task<List<AmortissementDto>> GetByCompteIdAsync(int idCompte)
        {
            var amortissements = await _context.Amortissements
                .Where(a => a.IdCompte == idCompte)
                .Include(a => a.ComptePret)
                .ThenInclude(c => c.Client)
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
            _context.Amortissements.Update(amortissement);
            await _context.SaveChangesAsync();
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
