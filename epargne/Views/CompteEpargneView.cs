using System;
using System.Collections.Generic;
using Epargne.DTO;
using Epargne.Models;

namespace Epargne.Views
{
    public class CompteEpargneView
    {
        public int IdCompte { get; set; }
        public int IdClient { get; set; }

        public decimal CapitalEpargne { get; set; }
        public string Libelle { get; set; }
        public DateOnly DateOuverture { get; set; }
        public decimal TauxInteret { get; set; }
        public decimal Retrait { get; set; }

        public decimal Solde { get; set; } = 0.0m;

        public List<TransactionEpargneDto> Transactions { get; set; }
    }
}
