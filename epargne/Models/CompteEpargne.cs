using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;
namespace Epargne.Models
{
    [Table("comptes_epargne")]
    public class CompteEpargne
    {
        [Key]
        [Column("id_compte")]
        public int IdCompte { get; set; }

        [Column("id_client")]
        public int IdClient { get; set; }

        // Navigation property vers Client
        [ForeignKey("IdClient")]
        public ClientEpargne Client { get; set; }

        [Column("date_ouverture")]
        public DateOnly DateOuverture { get; set; }

        [Column("taux_interet")]
        public decimal TauxInteret { get; set; }

        // Navigation property vers les transactions
        public ICollection<TransactionEpargne> Transactions { get; set; }
    }
   
}
