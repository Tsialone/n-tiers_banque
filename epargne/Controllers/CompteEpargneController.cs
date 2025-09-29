using Microsoft.AspNetCore.Mvc;
using Epargne.Models;
using Epargne.Services;
using System.Threading.Tasks;
using System.Linq;
using Epargne.DTO;

namespace Epargne.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CompteEpargneController : ControllerBase
    {
        private readonly CompteEpargneService _service;

        public CompteEpargneController(CompteEpargneService service)
        {
            _service = service;
        }
        // GET api/compteEpargne
        [HttpGet("byDto")]
        public async Task<IActionResult> GetAllDto()
        {
            var comptesDto = await _service.GetAllDtoAsync();
            return Ok(comptesDto);
        }
        [HttpGet]
        public async Task<IActionResult> GetAll() =>
            Ok(await _service.GetAllAsync());

        [HttpGet("{idCompte}")]
        public async Task<IActionResult> GetById(int idCompte)
        {
            var compte = await _service.GetByIdAsync(idCompte);
            if (compte == null) return NotFound();
            return Ok(compte);
        }

        [HttpGet("byClient")]
        public async Task<IActionResult> GetByClientId([FromQuery] int idClient)
        {
            var comptes = await _service.GetByClientIdAsync(idClient);
            if (comptes == null || !comptes.Any()) return NotFound();
            return Ok(comptes);
        }

        [HttpPost]
        public async Task<IActionResult> Add([FromBody] CompteEpargne compte)
        {
            await _service.AddAsync(compte);
            return Ok();
        }

        [HttpPut("{idCompte}")]
        public async Task<IActionResult> Update(int idCompte, [FromBody] CompteEpargne compte)
        {
            if (idCompte != compte.IdCompte) return BadRequest();
            await _service.UpdateAsync(compte);
            return Ok();
        }

        [HttpDelete("{idCompte}")]
        public async Task<IActionResult> Delete(int idCompte)
        {
            await _service.DeleteAsync(idCompte);
            return Ok();
        }
    }
}
