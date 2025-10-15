using System;

namespace Pret.DTO
{
    public class ClientPretDto
    {
        public int IdClient { get; set; }

        public  string Nom { get; set; }
        public  string Prenom { get; set; }

        public DateOnly? DateNaissance { get; set; } 
    }
}
