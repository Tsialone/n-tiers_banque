using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Epargne.Models
{
    public class Person
    {
        [Key]
        [Column("id")] 
        public int Id { get; set; }

        [Column("name")] // colonne 'name'
        public string Name { get; set; }

        [Column("age")]  // colonne 'age'
        public int Age { get; set; }
    }
}
