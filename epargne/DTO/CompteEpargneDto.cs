using System;
using System.Collections.Generic;

namespace Epargne.DTO
{
    public class CompteEpargneDto
    {
        public int IdCompte { get; set; }
        public int IdClient { get; set; }

        public string ClientNom { get; set; }
        public string ClientPrenom { get; set; }

        public DateOnly DateOuverture { get; set; }
        public decimal TauxInteret { get; set; }

        public List<TransactionEpargneDto> Transactions { get; set; }
    }
}
