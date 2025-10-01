using System;
using Pret.Utils;


namespace Pret.DTO
{
    public class ComptePretCreateDto
    {
        public int IdClient { get; set; }

        public DateOnly DateOuverture { get; set; } =  DateUtils.Today() ;

        public string Libelle { get; set; }

        public decimal CapitalEmprunte { get; set; }

        public decimal TauxInteret { get; set; }

        public int DureeMois { get; set; }

        public DateOnly DateEcheance { get; set; }

        public string Statut { get; set; } = "actif";
    }
}
