using System;

namespace Pret.DTO
{
    public class TransactionPretCreateDto
    {
        public int IdCompte { get; set; }
        public int? IdAmortissement { get; set; }

        public string Libelle { get; set; }
        public decimal Montant { get; set; }
        public string TypeTransaction { get; set; } // decaissement, remboursement, interet
    }
}
