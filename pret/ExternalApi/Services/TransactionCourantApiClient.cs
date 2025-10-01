using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Collections.Generic;
using Pret.ExternalApi.DTO;

namespace Pret.ExternalApi.Services
{
    public class TransactionCourantApiClient
    {
        private readonly HttpClient _httpClient;

        public TransactionCourantApiClient(HttpClient httpClient)
        {
            _httpClient = httpClient;
            _httpClient.BaseAddress = new Uri("http://172.17.0.1:8080/server-ejb/api/");
        }

        public async Task<List<TransactionCourantDto>> GetAllAsync()
        {
            return await _httpClient.GetFromJsonAsync<List<TransactionCourantDto>>("transactions-courant");
        }

        public async Task<TransactionCourantDto> GetByIdAsync(int id)
        {
            return await _httpClient.GetFromJsonAsync<TransactionCourantDto>($"transactions-courant/{id}");
        }

        public async Task<List<TransactionCourantDto>> GetByCompteIdAsync(int idCompte)
        {
            return await _httpClient.GetFromJsonAsync<List<TransactionCourantDto>>($"transactions-courant/compte/{idCompte}");
        }

        public async Task<TransactionCourantDto> CreateAsync(TransactionCourantDto dto)
        {
            var response = await _httpClient.PostAsJsonAsync("transactions-courant", dto);

            if (response.IsSuccessStatusCode)
            {
                return await response.Content.ReadFromJsonAsync<TransactionCourantDto>();
            }
            else
            {
                // Lire le corps JSON envoyé par l'API Java
                var errorContent = await response.Content.ReadAsStringAsync();
                // Lever une exception avec le message exact
                throw new Exception(errorContent);
            }
        }

    }
}
