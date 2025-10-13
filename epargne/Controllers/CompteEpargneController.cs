using System;
using Microsoft.AspNetCore.Mvc;
using Epargne.Models;
using Epargne.Services;
using System.Threading.Tasks;
using System.Linq;
using Epargne.DTO;
using Epargne.Mappers;
using Epargne.ExternalApi.Services;
using Epargne.Views;
using System.Collections.Generic;
using Epargne.Utils;
namespace Epargne.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CompteEpargneController : ControllerBase
    {
        private readonly CompteEpargneService _service;
        private readonly TransactionCourantApiClient _transactionCourantApiClient;

        private readonly TransactionEpargneService _transactionEpargneService;

        public CompteEpargneController(
            CompteEpargneService service,
            TransactionCourantApiClient transactionCourantApiClient,
            TransactionEpargneService transactionEpargneService
            )
        {
            _service = service;
            _transactionCourantApiClient = transactionCourantApiClient;
            _transactionEpargneService = transactionEpargneService;
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
            var comptes = await _service.GetByClientIdAsync(idClient, DateUtils.Today());
            if (comptes == null || !comptes.Any()) return NotFound();
            var comptesDto = comptes.Select(c => c.ToDto()).ToList();
            return Ok(comptesDto);
        }


        [HttpGet("byClientSolde")]
        public async Task<IActionResult> GetByClientIdWithSolde([FromQuery] int idClient, [FromQuery] DateOnly? date)
        {
            List<CompteEpargneView> compteEpargneViews = new List<CompteEpargneView>();


            try
            {
                var comptes = await _service.GetByClientIdAsync(idClient, date);
                if (comptes == null || !comptes.Any()) return Ok(comptes);
                var comptesDto = comptes.Select(c => c.ToDto()).ToList();
                var temp_date = date ?? DateUtils.Today();
                Console.WriteLine("mtfffffffffff " + temp_date);
                foreach (var compte in comptesDto)
                {
                    var solde = await _transactionEpargneService.getSoldeByClientAndEpargneAndDate(idClient, compte.IdCompte, temp_date);
                    compteEpargneViews.Add(
                        new CompteEpargneView
                        {
                            IdCompte = compte.IdCompte,
                            IdClient = compte.IdClient,
                            CapitalEpargne = compte.CapitalEpargne,
                            Libelle = compte.Libelle,
                            DateOuverture = compte.DateOuverture,
                            TauxInteret = compte.TauxInteret,
                            Retrait = compte.Retrait,
                            Transactions = compte.Transactions,
                            Solde = (decimal)solde
                        }

                    );
                }
                // var comptesDto = await _service.GetAllDtoAsync();
                // return  Ok(comptes);
                return Ok(compteEpargneViews);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = ex.Message });
            }
        }


        [HttpPost]
        public async Task<IActionResult> Add([FromBody] CompteEpargneCreateDto dto, [FromQuery] int idCompteCourant)
        {
            // if (dto == null) return NotFound();
            await using var dbTransaction = await _service.Context.Database.BeginTransactionAsync();
            try
            {
                var compte_epargne_created = await _service.AddAsync(dto);
                var transaction_courant_created = await _transactionCourantApiClient.CreateAsync(
                     new TransactionCourantDto
                     {
                         IdCompte = idCompteCourant,
                         DateTransaction = dto.DateOuverture,
                         Libelle = "depot initial de: " + dto.Libelle,
                         Montant = dto.CapitalEpargne,
                         Sens = "debit"
                     }
                );
                await dbTransaction.CommitAsync();
                // await dbTransaction.RollbackAsync();

                return Ok(new { success = "Creation du compte epargne et transaction compte couant reussi" });

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
