using Microsoft.AspNetCore.Mvc;
using Pret.Models;
using Pret.Services;
using System;
using System.Threading.Tasks;           // pour Task<>

namespace Pret.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UtilisateurController : ControllerBase
    {
        private readonly UtilisateurService _service;

        public UtilisateurController(UtilisateurService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _service.GetAllAsync());

        [HttpPost]
        public async Task<IActionResult> Add([FromBody] Utilisateur utilisateur)
        {
            Console.WriteLine($"Nom: {utilisateur.Nom}, DateNaissance: {(utilisateur.DateNaissance.HasValue ? utilisateur.DateNaissance.Value.ToString("yyyy-MM-dd") : "null")}");
            await _service.AddAsync(utilisateur);
            return Ok();
        }

    }
}
