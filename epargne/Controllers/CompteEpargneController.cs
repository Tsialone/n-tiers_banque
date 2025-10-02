using System;
using Microsoft.AspNetCore.Mvc;
using Epargne.Models;
using Epargne.Services;
using System.Threading.Tasks;
using System.Linq;
using Epargne.DTO;
using Epargne.Mappers;
using Epargne.ExternalApi.Services;
namespace Epargne.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CompteEpargneController : ControllerBase
    {
        private readonly CompteEpargneService _service;
        private readonly TransactionCourantApiClient _transactionCourantApiClient;


        public CompteEpargneController(CompteEpargneService service, TransactionCourantApiClient transactionCourantApiClient)
        {
            _service = service;
            _transactionCourantApiClient = transactionCourantApiClient;
        }
        // GET api/compteEpargne
        [HttpGet]
        public async Task<IActionResult> GetAllDto()
        {
            var comptesDto = await _service.GetAllDtoAsync();
            return Ok(comptesDto);
        }
        // [HttpGet]
        // public async Task<IActionResult> GetAll() =>
        //     Ok(await _service.GetAllAsync());

        [HttpGet("byCompte")]
        public async Task<IActionResult> GetById([FromQuery] int idCompte)
        {
            var compte = await _service.GetByIdAsync(idCompte);
            var compte_mapped = CompteEpargneMapper.ToDto(compte);

            if (compte == null) return NotFound();
            return Ok(compte_mapped);
        }
        [HttpGet("byClientAndCompte")]
        public async Task<IActionResult> GetByClientId([FromQuery] int idClient, [FromQuery] int idCompte)
        {
            var comptes = await _service.GetByClientIdAndCompteIdAsync(idClient, idCompte);
            if (comptes == null || !comptes.Any()) return NotFound();
            var comptesDto = comptes.Select(c => c.ToDto()).ToList();
            return Ok(comptesDto);
        }

        [HttpGet("byClient")]
        public async Task<IActionResult> GetByClientId([FromQuery] int idClient)
        {
            var comptes = await _service.GetByClientIdAsync(idClient);
            if (comptes == null || !comptes.Any()) return NotFound();
            var comptesDto = comptes.Select(c => c.ToDto()).ToList();
            return Ok(comptesDto);
        }


        [HttpPost]
        public async Task<IActionResult> Add([FromBody] CompteEpargneCreateDto dto , [FromQuery] int idCompteCourant)
        {
            if (dto == null) return NotFound();
            await using var dbTransaction = await _service.Context.Database.BeginTransactionAsync();
            try
            {
                var compte_epargne_created = await _service.AddAsync(dto);
                var transaction_courant_created = await _transactionCourantApiClient.CreateAsync(
                     new TransactionCourantDto
                     {
                         IdCompte =  idCompteCourant ,
                         DateTransaction = dto.DateOuverture,
                         Libelle = "depot initial de: " + dto.Libelle,
                         Montant = dto.CapitalEpargne,
                         Sens = "debit"
                     }
                );
                await dbTransaction.CommitAsync();
                // await dbTransaction.RollbackAsync();

                return Ok( new   { success =  "Creation du compte epargne et transaction compte couant reussi"});

            }
            catch (Exception ex)
            {
                await dbTransaction.RollbackAsync();
                return StatusCode(500, new { error = ex.Message });

            }

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
