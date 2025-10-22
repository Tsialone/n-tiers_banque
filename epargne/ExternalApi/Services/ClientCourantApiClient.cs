using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Collections.Generic;
using Epargne.ExternalApi.DTO;

namespace Epargne.ExternalApi.Services
{
    public class ClientCourantApiClient
    {
        private readonly HttpClient _httpClient;

        public ClientCourantApiClient(HttpClient httpClient)
        {
            _httpClient = httpClient;
            _httpClient.BaseAddress = new Uri("http://172.17.0.1:8080/client-ejb/api/");
        }

        // Récupérer tous les clients
        public async Task<List<ClientCourantDto>> GetAllAsync()
        {
            return await _httpClient.GetFromJsonAsync<List<ClientCourantDto>>("clients-courants");
        }

        // Récupérer un client par ID
        public async Task<ClientCourantDto> GetByIdAsync(int id)
        {
            return await _httpClient.GetFromJsonAsync<ClientCourantDto>($"clients-courants/{id}");
        }

        public async Task<ClientCourantDto> GetAuth()
        {
            return await _httpClient.GetFromJsonAsync<ClientCourantDto>($"clients-courants/session_auth");
        }

        // Créer un nouveau client
        public async Task<ClientCourantDto> CreateAsync(ClientCourantDto dto)
        {
            var response = await _httpClient.PostAsJsonAsync("clients-courants", dto);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<ClientCourantDto>();
        }

        // Mettre à jour un client existant
        public async Task<ClientCourantDto> UpdateAsync(int id, ClientCourantDto dto)
        {
            var response = await _httpClient.PutAsJsonAsync($"clients-courants/{id}", dto);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<ClientCourantDto>();
        }

        // Supprimer un client
        public async Task DeleteAsync(int id)
        {
            var response = await _httpClient.DeleteAsync($"clients-courants/{id}");
            response.EnsureSuccessStatusCode();
        }
    }
}
