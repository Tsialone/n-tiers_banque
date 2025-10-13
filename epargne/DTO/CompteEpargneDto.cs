using System;
using System.Collections.Generic;

namespace Epargne.DTO
{
    public class CompteEpargneDto
    {
        public int IdCompte { get; set; }
        public int IdClient { get; set; }

        public decimal CapitalEpargne { get; set; }
        public string Libelle { get; set; }
        public DateOnly DateOuverture { get; set; }
        public decimal Retrait  { get; set; } = 50.00m;

        public decimal TauxInteret { get; set; }

        public List<TransactionEpargneDto> Transactions { get; set; }
    }
}
