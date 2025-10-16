using Microsoft.AspNetCore.Mvc;
using Pret.DTO;
using Pret.Models;
using Pret.Services;
using System;
using System.Linq;
using System.Threading.Tasks;
using Pret.Mappers;
using Pret.Utils;

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

        [HttpGet("rembourssement")]
        public async Task<IActionResult> rembourssementPret([FromQuery] int idComptePret, [FromQuery] int idCompteCourant, [FromQuery] DateOnly ? date)
        {

            try
            {   
                Console.WriteLine("hiohihihihihi "  +  date);
                // DateOnly temp_date = date ?? DateUtils.Today();
                var amortissements = await _service.rembourssementPret(idComptePret , idCompteCourant, date, 0.0);
                return Ok(amortissements);

            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = ex.Message });
            }

        }

        // getSolde de pret par compte de pret  et date
        // public async Task<List<AmortissementDto>> GetByPretAndDateAndStatus(int idComptePret, DateOnly date, string status)
        [HttpGet("solde")]
        public async Task<IActionResult> getSoldeByPretAndDateAndStatus([FromQuery] int idComptePret, [FromQuery] DateOnly? date)
        {
            string status = "en_attente";

            try
            {
                DateOnly temp_date = date ?? DateUtils.Today();
                var amortissements_filtred = await _service.GetByPretAndDateAndStatus(idComptePret, temp_date, status);
                return Ok(amortissements_filtred.FirstOrDefault()?.ResteDu ?? 0.0m);

            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = ex.Message });
            }

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
            var amortissements = await _service.GetByPretAndDateAndStatus(idCompte, default, "");
            // if (!amortissements.Any()) return NotFound();
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
        [HttpGet("payementPret")]
        public async Task<IActionResult> Update([FromQuery] int idAmortissement, double? montant)
        {
            // if (idAmortissement != amortissement.IdAmortissement) return BadRequest();
            var entity = await _service.GetByIdAsync(idAmortissement);
            if (entity == null) return NotFound();
            entity.Statut = "paye";
            await _service.UpdateAsync(entity);
            return Ok(new { success = "payement reussi!" });
        }

        // PUT api/amortissement/{id}
        [HttpPut("remboursser")]
        public async Task<IActionResult> Update([FromBody] AmortissementDto amortissement)
        {
            // if (idAmortissement != amortissement.IdAmortissement) return BadRequest();
            var entity = await _service.GetByIdAsync(amortissement.IdAmortissement);
            if (entity == null) return NotFound();
            entity.Statut = "paye";
            await _service.UpdateAsync(entity);
            return Ok(new { success = "payement reussi!" });
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
