using System;

namespace Epargne.DTO
{
    public class CompteEpargneCreateDto
    {
        public int IdClient { get; set; }
        public decimal CapitalEpargne { get; set; }
        public string Libelle { get; set; }
        public DateOnly DateOuverture { get; set; } = DateOnly.FromDateTime(DateTime.Now);
        public decimal TauxInteret { get; set; } = 2.1M;

    }
}
