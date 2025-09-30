using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pret.Models
{
    [Table("transactions_pret")]
    public class TransactionPret
    {
        [Key]
        [Column("id_transaction")]
        public int IdTransaction { get; set; }

        [Required]
        [Column("id_compte")]
        public int IdCompte { get; set; }

        [Column("id_amortissement")]
        public int? IdAmortissement { get; set; }

        [Required]
        [Column("date_transaction")]
        public DateTime DateTransaction { get; set; } = DateTime.Now;

        [Required]
        [Column("libelle")]
        public string Libelle { get; set; }

        [Required]
        [Column("montant")]
        public decimal Montant { get; set; }

        [Required]
        [Column("type_transaction")]
        public string TypeTransaction { get; set; } // decaissement, remboursement, interet

        // Navigation
        [ForeignKey("IdCompte")]
        public ComptePret ComptePret { get; set; }

        [ForeignKey("IdAmortissement")]
        public Amortissement Amortissement { get; set; }
    }
}
