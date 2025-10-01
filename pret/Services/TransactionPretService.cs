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
    public class TransactionPretService
    {
        private readonly AppDbContext _context;

        public TransactionPretService(AppDbContext context)
        {
            _context = context;
        }

        public AppDbContext Context => _context;

        // Récupérer tous les comptes
        public async Task<List<TransactionPretDto>> GetAllDtoAsync()
        {
            var transactions = await _context.TransactionPrets
                .ToListAsync();

            return transactions.Select(t => t.ToDto()).ToList();
        }


        // Ajouter depuis DTO de création
        public async Task<TransactionPret> AddAsync(TransactionPretCreateDto dto)
        {
            try
            {
                var entity = TransactionPretMapper.ToEntity(dto);
                await _context.TransactionPrets.AddAsync(entity);
                await _context.SaveChangesAsync();
                return entity;
            }
            catch (Exception ex)
            {
                var innerMessage = ex.InnerException != null ? ex.InnerException.Message : ex.Message;
                throw new Exception("Erreur : " + innerMessage, ex);
            }

        }


    }
}
