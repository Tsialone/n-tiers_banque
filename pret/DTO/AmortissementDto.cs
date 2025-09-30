using System;

namespace Pret.DTO
{
    public class AmortissementDto
    {
        public int IdAmortissement { get; set; }
        public int IdCompte { get; set; }

        public int Mois { get; set; }
        public decimal Mensualite { get; set; }
        public decimal Interet { get; set; }
        public decimal Capital { get; set; }
        public decimal ResteDu { get; set; }
        public string Statut { get; set; }

        public int CompteIdClient { get; set; }
        public string CompteClientNom { get; set; }
        public string CompteClientPrenom { get; set; }
    }
}
