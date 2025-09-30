using Microsoft.EntityFrameworkCore;
using Pret.Data;
using Pret.DTO;
using Pret.Mappers;
using Pret.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Pret.Services
{
    public class ComptePretService
    {
        private readonly AppDbContext _context;

        public ComptePretService(AppDbContext context)
        {
            _context = context;
        }

        public AppDbContext Context => _context;

        // Récupérer tous les comptes
        public async Task<List<ComptePretDto>> GetAllDtoAsync()
        {
            var comptes = await _context.ComptePrets
                .Include(c => c.Client)
                .ToListAsync();

            return comptes.Select(c => c.ToDto()).ToList();
        }

        // Récupérer par ID
        public async Task<ComptePretDto> GetByIdAsync(int idCompte)
        {
            var compte = await _context.ComptePrets
                .Include(c => c.Client)
                .FirstOrDefaultAsync(c => c.IdCompte == idCompte);

            return compte?.ToDto();
        }

        // Récupérer tous les comptes d’un client
        public async Task<List<ComptePretDto>> GetByClientIdAsync(int idClient)
        {
            var comptes = await _context.ComptePrets
                .Where(c => c.IdClient == idClient)
                .Include(c => c.Client)
                .ToListAsync();

            return comptes.Select(c => c.ToDto()).ToList();
        }

        // Ajouter un compte depuis l'entité
        public async Task<ComptePret> AddAsync(ComptePret compte)
        {
            await _context.ComptePrets.AddAsync(compte);
            await _context.SaveChangesAsync();
            return compte;
        }

        // Ajouter depuis DTO de création
        public async Task<ComptePret> AddAsync(ComptePretCreateDto dto)
        {
            var entity =  ComptePretMapper.ToEntity(dto);
            await _context.ComptePrets.AddAsync(entity);
            await _context.SaveChangesAsync();
            return entity;
        }

        // Mettre à jour
        public async Task UpdateAsync(ComptePret compte)
        {
            _context.ComptePrets.Update(compte);
            await _context.SaveChangesAsync();
        }

        // Supprimer
        public async Task DeleteAsync(int idCompte)
        {
            var compte = await _context.ComptePrets.FindAsync(idCompte);
            if (compte != null)
            {
                _context.ComptePrets.Remove(compte);
                await _context.SaveChangesAsync();
            }
        }
    }
}
