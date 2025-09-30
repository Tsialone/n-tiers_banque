using Epargne.Models;  // pour Person
using Microsoft.EntityFrameworkCore;

namespace Epargne.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Person> Persons { get; set; }
        public DbSet<ClientEpargne> ClientsEpargne { get; set; }
        public DbSet<CompteEpargne> ComptesEpargne { get; set; }
        public DbSet<TransactionEpargne> TransactionsEpargne { get; set; }



        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Person>().ToTable("persons");
            modelBuilder.Entity<ClientEpargne>().ToTable("clients_epargne");
            modelBuilder.Entity<CompteEpargne>().ToTable("comptes_epargne");
            modelBuilder.Entity<TransactionEpargne>().ToTable("transactions_epargne");
        }
    }
}
