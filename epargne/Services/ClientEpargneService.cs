using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Epargne.Data;
using Epargne.Models;

namespace Epargne.Services
{
    public class ClientEpargneService
    {
        private readonly AppDbContext _context;

        public ClientEpargneService(AppDbContext context)
        {
            _context = context;
        }
        // Récupérer un client par son id
        public async Task<ClientEpargne> GetByIdClientAsync(int idClient)
        {
            return await _context.ClientsEpargne
                .FirstOrDefaultAsync(c => c.IdClient == idClient);
        }

        public async Task<List<ClientEpargne>> GetAllAsync() =>
            await _context.ClientsEpargne.ToListAsync();

        public async Task<ClientEpargne> GetByIdAsync(int id) =>
            await _context.ClientsEpargne.FindAsync(id);

        public async Task AddAsync(ClientEpargne client)
        {
            await _context.ClientsEpargne.AddAsync(client);
            await _context.SaveChangesAsync();
        }

        public async Task UpdateAsync(ClientEpargne client)
        {
            _context.ClientsEpargne.Update(client);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var client = await _context.ClientsEpargne.FindAsync(id);
            if (client != null)
            {
                _context.ClientsEpargne.Remove(client);
                await _context.SaveChangesAsync();
            }
        }
    }
}
