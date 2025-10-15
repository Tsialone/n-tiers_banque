using System;

namespace Pret.Views
{
    public class ComptePretView
    {
        public int IdCompte { get; set; }
        public int IdClient { get; set; }

        // public string ClientNom { get; set; }
        public string Libelle { get; set; }

        // public string ClientPrenom { get; set; }

        public DateOnly DateOuverture { get; set; }
        public decimal CapitalEmprunte { get; set; }
        public decimal TauxInteret { get; set; }
        public int DureeMois { get; set; }
        public DateOnly? DateEcheance { get; set; }
        public string Statut { get; set; }
        public decimal Solde { get; set; } = 0.0m;
    }
}
