using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Collections.Generic;
using Epargne.ExternalApi.DTO;
using System;

namespace Epargne.ExternalApi.Services
{
    public class CompteCourantApiClient
    {
        private readonly HttpClient _httpClient;

        public CompteCourantApiClient(HttpClient httpClient)
        {
            _httpClient = httpClient;
            // _httpClient.BaseAddress = new Uri("http://localhost:8080/server-ejb/api/"); 
            // _httpClient.BaseAddress = new Uri("http://host.docker.internal:8080/server-ejb/api/");
            _httpClient.BaseAddress = new Uri("http://172.17.0.1:8080/client-ejb/api/");

        }

        public async Task<List<CompteCourantDto>> GetAllAsync()
        {
            return await _httpClient.GetFromJsonAsync<List<CompteCourantDto>>("comptes-courants");
        }

        public async Task<CompteCourantDto> GetByIdAsync(int id)
        {
            return await _httpClient.GetFromJsonAsync<CompteCourantDto>($"compte-courant/{id}");
        }

        public async Task<CompteCourantDto> CreateAsync(CompteCourantDto dto)
        {
            var response = await _httpClient.PostAsJsonAsync("comptes-courants", dto);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<CompteCourantDto>();
        }
    }
}
