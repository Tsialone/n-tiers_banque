using Microsoft.AspNetCore.Mvc;
using Pret.DTO;
using Pret.Models;
using Pret.Services;
using System;
using System.Linq;
using System.Threading.Tasks;
using Pret.Mappers;

namespace Pret.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AmortissementController : ControllerBase
    {
        private readonly AmortissementService _service;

        public AmortissementController(AmortissementService service)
        {
            _service = service;
        }

        // GET api/amortissement
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var amortissements = await _service.GetAllDtoAsync();
            return Ok(amortissements);
        }

        // GET api/amortissement/{id}
        [HttpGet("{idAmortissement}")]
        public async Task<IActionResult> GetById(int idAmortissement)
        {
            var amortissement = await _service.GetByIdAsync(idAmortissement);
            if (amortissement == null) return NotFound();
            return Ok(amortissement);
        }

        // GET api/amortissement/byCompte?idCompte=1
        [HttpGet("byCompte")]
        public async Task<IActionResult> GetByCompteId([FromQuery] int idCompte)
        {
            var amortissements = await _service.GetByCompteIdAsync(idCompte);
            if (!amortissements.Any()) return NotFound();
            return Ok(amortissements);
        }

        // POST api/amortissement
        [HttpPost]
        public async Task<IActionResult> Add([FromBody] AmortissementCreateDto dto)
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

        // PUT api/amortissement/{id}
        [HttpPut("{idAmortissement}")]
        public async Task<IActionResult> Update(int idAmortissement, [FromBody] Amortissement amortissement)
        {
            if (idAmortissement != amortissement.IdAmortissement) return BadRequest();

            await _service.UpdateAsync(amortissement);
            return Ok();
        }

        // DELETE api/amortissement/{id}
        [HttpDelete("{idAmortissement}")]
        public async Task<IActionResult> Delete(int idAmortissement)
        {
            await _service.DeleteAsync(idAmortissement);
            return Ok();
        }
    }
}
