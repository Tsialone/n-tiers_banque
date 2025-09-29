namespace Epargne.DTO
{
    public class TransactionEpargneCreateDto
    {
        public int IdCompte { get; set; }
        public string Libelle { get; set; }
        public decimal Montant { get; set; }
        public string Sens { get; set; } // debit ou credit
    }
}
