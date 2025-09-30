using System;
using System.Text.Json.Serialization;
public class TransactionCourantDto
{
    [JsonPropertyName("idTransaction")]
    public int IdTransaction { get; set; }

    [JsonPropertyName("idCompte")]
    public int IdCompte { get; set; }

    [JsonPropertyName("dateTransaction")]
    public DateOnly DateTransaction { get; set; }

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; }

    [JsonPropertyName("montant")]
    public decimal Montant { get; set; }

    [JsonPropertyName("sens")]
    public string Sens { get; set; }
}
