using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pret.Models
{
    [Table("clients_pret")]  // correspond à la table PostgreSQL
    public class ClientPret
    {
        [Key]
        [Column("id_client")]
        public int IdClient { get; set; }

        [Required]
        [Column("nom")]
        public string Nom { get; set; }

        [Required]
        [Column("prenom")]
        public string Prenom { get; set; }

        [Column("date_naissance")]
        public DateOnly? DateNaissance { get; set; }  // nullable
    }
}
