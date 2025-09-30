using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pret.Models
{
    [Table("comptes_pret")]
    public class ComptePret
    {
        [Key]
        [Column("id_compte")]
        public int IdCompte { get; set; }

        [Required]
        [Column("id_client")]
        public int IdClient { get; set; }

        [Required]
        [Column("date_ouverture")]
        public DateOnly DateOuverture { get; set; } = DateOnly.FromDateTime(DateTime.Now);

        [Required]
        [Column("montant")]
        public decimal Montant { get; set; }

        [Required]
        [Column("taux_interet")]
        public decimal TauxInteret { get; set; }

        [Required]
        [Column("duree_mois")]
        public int DureeMois { get; set; }

        [Required]
        [Column("date_echeance")]
        public DateOnly DateEcheance { get; set; }

        [Column("statut")]
        public string Statut { get; set; } = "actif";

        // Navigation vers le client
        [ForeignKey("IdClient")]
        public ClientPret Client { get; set; }
    }
}
