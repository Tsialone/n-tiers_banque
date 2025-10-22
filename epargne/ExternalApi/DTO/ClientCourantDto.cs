using System;

namespace Epargne.ExternalApi.DTO
{
    public class ClientCourantDto
    {
        public int IdClient { get; set; }          // équivalent de Integer idClient
        public string Nom { get; set; }            // équivalent de String nom
        public string Prenoms { get; set; }        // équivalent de String prenoms
        public string DateNaissance { get; set; }  // String ou format ISO (yyyy-MM-dd)
    }
}
