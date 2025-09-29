using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;   // <-- nécessaire pour ToListAsync()
using Epargne.Data;
using Epargne.Models;

namespace Epargne.Services
{
    public class PersonService
    {
        private readonly AppDbContext _context;

        public PersonService(AppDbContext context)
        {
            _context = context;
        }

        public async Task<List<Person>> GetAllAsync() =>
            await _context.Persons.ToListAsync();

        public async Task AddAsync(Person person)
        {
            await _context.Persons.AddAsync(person);
            await _context.SaveChangesAsync();
        }
    }
}
