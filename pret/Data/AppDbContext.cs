using Pret.Models;  // pour Person
using Microsoft.EntityFrameworkCore;

namespace Pret.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Amortissement> Amortissements { get; set; }
        // public DbSet<ClientPret> ClientPrets { get; set; }
        public DbSet<ComptePret> ComptePrets { get; set; }
        public DbSet<TransactionPret> TransactionPrets { get; set; }



        // protected override void OnModelCreating(ModelBuilder modelBuilder)
        // {
        //     modelBuilder.Entity<Person>().ToTable("persons");
        //     modelBuilder.Entity<ClientEpargne>().ToTable("clients_epargne");
        //     modelBuilder.Entity<CompteEpargne>().ToTable("comptes_epargne");
        //     modelBuilder.Entity<TransactionEpargne>().ToTable("transactions_epargne");
        // }
    }
}
