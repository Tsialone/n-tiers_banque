using System;

namespace Epargne.DTO
{
    public class TransactionEpargneDto
    {
        public int IdTransaction { get; set; }
        public int IdCompte { get; set; }
        public DateOnly DateTransaction { get; set; }
        public string Libelle { get; set; }
        public decimal Montant { get; set; }
        public string Sens { get; set; } // debit ou credit
    }
}
