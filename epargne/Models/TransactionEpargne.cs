using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Epargne.Models
{
    [Table("transactions_epargne")]
    public class TransactionEpargne
    {
        [Key]
        [Column("id_transaction")]
        public int IdTransaction { get; set; }

        [Column("id_compte")]
        public int IdCompte { get; set; }

        // Navigation property vers le compte
        [ForeignKey("IdCompte")]
        public CompteEpargne Compte { get; set; }

        [Column("date_transaction")]
        public DateOnly DateTransaction { get; set; }

        [Column("libelle")]
        public string Libelle { get; set; }

        [Column("montant")]
        public decimal Montant { get; set; }

        [Column("sens")]
        public string Sens { get; set; } // "debit" ou "credit"
    }
   
}
