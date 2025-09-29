using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pret.Models
{
    public class Utilisateur
    {
        [Key]
        [Column("id")]
        public int id { get; set; }

        [Column] // colonne 'name'
        public string nom { get; set; }

        [Column("date_naissance")]  // colonne 'age'
        public DateTime? date_naissance { get; set; }  
    }
}
