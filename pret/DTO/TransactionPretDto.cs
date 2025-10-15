using System;

namespace Pret.DTO
{
    public class TransactionPretDto
    {
        public int IdTransaction { get; set; }
        public int IdCompte { get; set; }
        public int? IdAmortissement { get; set; }

        public DateOnly DateTransaction { get; set; }
        public  string Libelle { get; set; }
        public decimal Montant { get; set; }
        public  string TypeTransaction { get; set; }

        public int CompteIdClient { get; set; }
        // public string CompteClientNom { get; set; }
        // public string CompteClientPrenom { get; set; }
    }
}
