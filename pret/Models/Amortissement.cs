using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pret.Models
{
    [Table("amortissements")]
    public class Amortissement
    {
        [Key]
        [Column("id_amortissement")]
        public int IdAmortissement { get; set; }

        [Required]
        [Column("id_compte")]
        public int IdCompte { get; set; }

        [Required]
        [Column("mois")]
        public int Mois { get; set; }

        [Required]
        [Column("mensualite")]
        public decimal Mensualite { get; set; }

        [Required]
        [Column("interet")]
        public decimal Interet { get; set; }

        [Required]
        [Column("capital")]
        public decimal Capital { get; set; }

        [Required]
        [Column("reste_du")]
        public decimal ResteDu { get; set; }

        [Required]
        [Column("created_at")]
        public DateOnly CreatedAt { get; set; }

        [Column("statut")]
        public string Statut { get; set; } = "en_attente";

        // Navigation vers le compte prêt
        [ForeignKey("IdCompte")]
        public ComptePret ComptePret { get; set; }
    }
}
