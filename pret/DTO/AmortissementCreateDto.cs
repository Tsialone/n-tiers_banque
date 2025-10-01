using System;

namespace Pret.DTO
{
    public class AmortissementCreateDto
    {
        public int IdCompte { get; set; }
        public int Mois { get; set; }
        public decimal Mensualite { get; set; }
        public decimal Interet { get; set; }
        public decimal Capital { get; set; }
        public decimal ResteDu { get; set; }
        public DateOnly CreatedAt { get; set; }

        public string Statut { get; set; } = "en_attente";
    }
}
