using System.Collections.Generic;       // pour List<>
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;  
using Pret.Data;
using Pret.Models;           // pour Task<>


namespace Pret.Services
{
    public class UtilisateurService
    {
        private readonly AppDbContext _context;

        public UtilisateurService(AppDbContext context)
        {
            _context = context;
        }

        public async Task<List<Utilisateur>> GetAllAsync() => await _context.Utilisateurs.ToListAsync();

        public async Task AddAsync(Utilisateur utilisateur)
        {
            _context.Utilisateurs.Add(utilisateur);
            await _context.SaveChangesAsync();
        }
    }
}

