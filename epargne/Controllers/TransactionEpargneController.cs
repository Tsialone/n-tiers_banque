using Microsoft.AspNetCore.Mvc;
using Epargne.Models;
using Epargne.Services;
using Epargne.DTO;
using System.Threading.Tasks;
using System.Linq;
using System;
using Epargne.ExternalApi.Services;
using Epargne.ExternalApi.DTO;
using System.Net.Http;
using Epargne.Data;
using Epargne.Utils;


namespace Epargne.Controllers
{

    [ApiController]
    [Route("api/[controller]")]
    public class TransactionEpargneController : ControllerBase
    {
        private readonly TransactionEpargneService _service;
        private readonly CompteCourantApiClient _apiCompteCourant;
        private readonly TransactionCourantApiClient _apiTransactionCourant;


        public TransactionEpargneController(TransactionEpargneService service, CompteCourantApiClient apiCompteCourant, TransactionCourantApiClient apiTransactionCourant)
        {
            _service = service;
            _apiCompteCourant = apiCompteCourant;
            _apiTransactionCourant = apiTransactionCourant;

        }
        // getSolde d'epargne
        [HttpGet("solde")]
        public async Task<IActionResult> fetchSoldeByClientAndEpargneAndDate(
            [FromQuery] int idClient,
            [FromQuery] int idCompteEpargne,
            [FromQuery] DateOnly? date
            )
        {
            try
            {
                // Appel vers l'API Java
                var effectiveDate = date ?? DateUtils.Today();
                var solde = await _service.getSoldeByClientAndEpargneAndDate(idClient, idCompteEpargne, effectiveDate);

                return Ok(solde);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = ex.Message });
            }
        }
        // Exemple d'appel pour créer une transaction courante via l'API Java
        // [HttpPost("create-transaction-courant")]
        // public async Task<IActionResult> CreateTransactionCourant([FromBody] TransactionCourantDto dto)
        // {
        //     if (dto == null)
        //         return BadRequest("TransactionCourantDto est null");

        //     try
        //     {
        //         // Appel vers l'API Java
        //         var created = await _apiTransactionCourant.CreateAsync(dto);
        //         return Ok(created);
        //     }
        //     catch (Exception ex)
        //     {
        //         return StatusCode(500, new { error = ex.Message });
        //     }
        // }



        [HttpGet("compteCourant")]
        public async Task<IActionResult> GetComptes()
        {
            var comptes = await _apiCompteCourant.GetAllAsync();
            return Ok(comptes);
        }
        // GET api/transactionEpargne/byClientAndCompte?idClient=1&idCompte=2
        [HttpGet("byClientAndCompte")]
        public async Task<IActionResult> GetByIdClientAndIdCompte([FromQuery] int idClient, [FromQuery] int idCompte)
        {
            var transactions = await _service.GetByIdClientAndIdCompteAsync(idClient, idCompte);
            if (!transactions.Any()) return NotFound();
            return Ok(transactions);
        }

        [HttpGet("byClient")]
        public async Task<IActionResult> GetByIdClient([FromQuery] int idClient)
        {
            var transactions = await _service.GetByIdClient(idClient);
            // if (!transactions.Any()) return NotFound();
            return Ok(transactions);
        }

        [HttpGet("byEpargne")]
        public async Task<IActionResult> GetByEpargne([FromQuery] int idEpargne)
        {
            var transactions = await _service.GetByIdEpargne(idEpargne);
            // if (!transactions.Any()) return NotFound();
            return Ok(transactions);
        }

        [HttpGet]
        public async Task<IActionResult> GetAllDto()
        {
            var transactionsDto = await _service.GetAllDtoAsync();
            return Ok(transactionsDto);
        }

        // [HttpGet]
        // public async Task<IActionResult> GetAll() =>
        //     Ok(await _service.GetAllAsync());

        [HttpGet("{idTransaction}")]
        public async Task<IActionResult> GetById(int idTransaction)
        {
            var transaction = await _service.GetByIdAsync(idTransaction);
            if (transaction == null) return NotFound();
            return Ok(transaction);
        }

        [HttpGet("byCompte")]
        public async Task<IActionResult> GetByCompteId([FromQuery] int idCompte)
        {
            var transactions = await _service.GetByCompteIdAsync(idCompte);
            if (transactions == null || !transactions.Any()) return NotFound();
            return Ok(transactions);
        }
        [HttpPost]
        public async Task<IActionResult> Add([FromBody] TransactionEpargneCreateDto dto)
        {
            // if (dto == null) return BadRequest();

            await using var dbTransaction = await _service.Context.Database.BeginTransactionAsync();

            try
            {
                var transaction = new TransactionEpargne
                {
                    IdCompte = dto.IdCompte,
                    Libelle = dto.Libelle,
                    Montant = dto.Montant,
                    Sens = dto.Sens,
                    DateTransaction = dto.DateTransaction
                };

                Console.WriteLine("debugggggggggggggg" + transaction);

                var created = await _service.AddAsync(transaction);

                // string courant_sens = dto.Sens == "debit" ? "credit" : "debit";
                // Console.WriteLine("itooooooooooooooo e " + idCompteCourant);
                // var transaction_courant = new TransactionCourantDto
                // {
                //     IdCompte = idCompteCourant,
                //     DateTransaction = dto.DateTransaction,
                //     Libelle = dto.Libelle + " compte epargne: " + dto.IdCompte,
                //     Montant = dto.Montant,
                //     Sens = courant_sens
                // };

                // var created_courant = await _apiTransactionCourant.CreateAsync(transaction_courant);

                await dbTransaction.CommitAsync();

                return Ok(new { success = "Transaction reussi" });
            }
            catch (Exception ex)
            {
                await dbTransaction.RollbackAsync();
                return StatusCode(500, new { error = ex.Message });
            }
        }
        // [HttpPost]
        // public async Task<IActionResult> Add([FromBody] TransactionEpargneCreateDto dto, [FromQuery] int idCompteCourant)
        // {
        //     // if (dto == null) return BadRequest();

        //     await using var dbTransaction = await _service.Context.Database.BeginTransactionAsync();

        //     try
        //     {
        //         var transaction = new TransactionEpargne
        //         {
        //             IdCompte = dto.IdCompte,
        //             Libelle = dto.Libelle,
        //             Montant = dto.Montant,
        //             Sens = dto.Sens,
        //             DateTransaction = dto.DateTransaction
        //         };

        //         Console.WriteLine("debugggggggggggggg" + transaction);

        //         var created = await _service.AddAsync(transaction);

        //         string courant_sens = dto.Sens == "debit" ? "credit" : "debit";
        //         Console.WriteLine("itooooooooooooooo e " + idCompteCourant);
        //         var transaction_courant = new TransactionCourantDto
        //         {
        //             IdCompte = idCompteCourant,
        //             DateTransaction = dto.DateTransaction,
        //             Libelle = dto.Libelle + " compte epargne: " + dto.IdCompte,
        //             Montant = dto.Montant,
        //             Sens = courant_sens
        //         };

        //         var created_courant = await _apiTransactionCourant.CreateAsync(transaction_courant);

        //         await dbTransaction.CommitAsync();

        //         return Ok(new { success = "Transaction reussi" });
        //     }
        //     catch (Exception ex)
        //     {
        //         await dbTransaction.RollbackAsync();
        //         return StatusCode(500, new { error = ex.Message });
        //     }
        // }


        // [HttpPost]
        // public async Task<IActionResult> Add([FromBody] TransactionEpargne transaction)
        // {
        //     await _service.AddAsync(transaction);
        //     return Ok();
        // }

        [HttpPut("{idTransaction}")]
        public async Task<IActionResult> Update(int idTransaction, [FromBody] TransactionEpargne transaction)
        {
            if (idTransaction != transaction.IdTransaction) return BadRequest();
            await _service.UpdateAsync(transaction);
            return Ok();
        }

        [HttpDelete("{idTransaction}")]
        public async Task<IActionResult> Delete(int idTransaction)
        {
            await _service.DeleteAsync(idTransaction);
            return Ok();
        }
    }
}
