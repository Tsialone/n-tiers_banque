using System;

namespace Pret.ExternalApi.DTO
{
    public class CompteCourantDto
    {
        public int IdCompte { get; set; }
        public  string Nom { get; set; }
        public double Solde { get; set; }
        public double DecouvertAutorise { get; set; }
    }

    
}
