using System;

namespace Pret.DTO
{
    public class ComptePretCreateDto
    {
        public int IdClient { get; set; }

        public DateOnly DateOuverture { get; set; } = DateOnly.FromDateTime(DateTime.Now);

        public decimal Montant { get; set; }

        public decimal TauxInteret { get; set; }

        public int DureeMois { get; set; }

        public DateOnly DateEcheance { get; set; }

        public string Statut { get; set; } = "actif";
    }
}
