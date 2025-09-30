using Microsoft.AspNetCore.Mvc;
using Pret.DTO;
using Pret.Mappers;
using Pret.Models;
using Pret.Services;
using System;
using System.Linq;
using System.Threading.Tasks;

namespace Pret.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ComptePretController : ControllerBase
    {
        private readonly ComptePretService _service;

        public ComptePretController(ComptePretService service)
        {
            _service = service;
        }

        // GET api/comptePret
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var comptes = await _service.GetAllDtoAsync();
            return Ok(comptes);
        }

        // soumission d'un pret
        [HttpPost("askPret")]
        public async Task<IActionResult> askPret([FromBody] ComptePretCreateDto dto)
        {
            // var compte = await _service.GetByIdAsync(idCompte);
            // if (compte == null) return NotFound();
            return Ok(dto);
        }

        // GET api/comptePret/{id}
        [HttpGet("{idCompte}")]
        public async Task<IActionResult> GetById(int idCompte)
        {
            var compte = await _service.GetByIdAsync(idCompte);
            if (compte == null) return NotFound();
            return Ok(compte);
        }

        // GET api/comptePret/byClient?idClient=1
        [HttpGet("byClient")]
        public async Task<IActionResult> GetByClientId([FromQuery] int idClient)
        {
            var comptes = await _service.GetByClientIdAsync(idClient);
            if (!comptes.Any()) return NotFound();
            return Ok(comptes);
        }

        // POST api/comptePret
        [HttpPost]
        public async Task<IActionResult> Add([FromBody] ComptePretCreateDto dto)
        {
            if (dto == null) return BadRequest();

            try
            {
                var created = await _service.AddAsync(dto);
                return Ok(created.ToDto());
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = ex.Message });
            }
        }

        // PUT api/comptePret/{id}
        [HttpPut("{idCompte}")]
        public async Task<IActionResult> Update(int idCompte, [FromBody] ComptePret compte)
        {
            if (idCompte != compte.IdCompte) return BadRequest();

            await _service.UpdateAsync(compte);
            return Ok();
        }

        // DELETE api/comptePret/{id}
        [HttpDelete("{idCompte}")]
        public async Task<IActionResult> Delete(int idCompte)
        {
            await _service.DeleteAsync(idCompte);
            return Ok();
        }
    }
}
