using Microsoft.AspNetCore.Mvc;
using Pret.DTO;
using Pret.Mappers;
using Pret.Models;
using Pret.Services;
using System;
using System.Linq;
using System.Threading.Tasks;
using Pret.ExternalApi.Services;
using Pret.ExternalApi.DTO;
using Pret.Utils;
namespace Pret.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ComptePretController : ControllerBase
    {
        private readonly ComptePretService _service;
        private readonly TransactionPretService _transactionPretService;
        private readonly TransactionCourantApiClient _transactionCourantApiClient;
        private readonly AmortissementService _amortissementService;





        public ComptePretController(
            ComptePretService service,
            TransactionPretService transactionPretService,
            TransactionCourantApiClient transactionCourantApiClient,
            AmortissementService amortissementService
            )
        {
            _service = service;
            _transactionPretService = transactionPretService;
            _transactionCourantApiClient = transactionCourantApiClient;
            _amortissementService = amortissementService;
        }

        [HttpPost("askPret")]
        public async Task<IActionResult> askPret([FromBody] ComptePretCreateDto dto)
        {
            // if (dto == null) return BadRequest();

            // ouverture du transaction
            await using var dbTransaction = await _service.Context.Database.BeginTransactionAsync();

            try
            {
                // enregistrement du compte
                Console.WriteLine("creation du compte pret.......");
                var created = await _service.AddAsync(dto);

                Console.WriteLine("xxxxxxxxxxxxxxxxxxxxxxx");
                Console.WriteLine(dto);

                // enregistrement du transaction
                Console.WriteLine("creation des  transactions.......");
                Console.WriteLine("creation de la transaction compte pret.......");
                await _transactionPretService.AddAsync(
                    new TransactionPretCreateDto
                    {
                        IdCompte = created.IdCompte,
                        IdAmortissement = null,
                        Libelle = "decaissement pret: " + created.Libelle,
                        Montant = created.CapitalEmprunte,
                        TypeTransaction = "decaissement"
                    }
                );
                // Console.WriteLine("creation de la transaction compte courant.......");
                // await _transactionCourantApiClient.CreateAsync(
                //     new TransactionCourantDto
                //     {
                //         IdCompte = idCompteCourant,
                //         DateTransaction = DateUtils.Today(),
                //         Libelle = "decaissement pret: " + created.Libelle,
                //         Montant = created.CapitalEmprunte,
                //         Sens = "credit"
                //     }
                // );

                // creation du tableau d'amortissement
                Console.WriteLine("Création d'amortissement.......");

                decimal capital_emprunte = created.CapitalEmprunte;
                decimal taux_interet_mensuel = created.TauxInteret / (12 * 100);
                int nbr_mois = created.DureeMois;

                // Conversion en double pour les calculs
                double c = (double)capital_emprunte;
                double t = (double)taux_interet_mensuel;

                // Formule de mensualité
                double mensualite = (c * t) / (1 - Math.Pow(1 + t, -nbr_mois));
                Console.WriteLine($"Mensualité calculée = {mensualite:F2}");

                // Variables de suivi
                double reste_du = c;

                for (int mois = 1; mois <= nbr_mois; mois++)
                {
                    double interet = reste_du * t;
                    double capital_rembourse = mensualite - interet;
                    reste_du -= capital_rembourse;

                    DateOnly date_prevu = created.DateOuverture.AddMonths(mois);



                    // Affichage console
                    Console.WriteLine($"Mois {mois}: Mensualité={mensualite:F2}, " +
                                      $"Intérêt={interet:F2}, " +
                                      $"Capital remboursé={capital_rembourse:F2}, " +
                                      $"Reste dû={reste_du:F2}, " +
                                      $"Prevu ={date_prevu}"
                                       );
                    // sauvegarde
                    var amortissement_created = await _amortissementService.AddAsync(
                         new AmortissementCreateDto
                         {
                             IdCompte = created.IdCompte,
                             Mois = mois,
                             Mensualite = (decimal)mensualite,
                             Interet = (decimal)interet,
                             Capital = (decimal)capital_rembourse,
                             ResteDu = (decimal)reste_du,
                             CreatedAt = date_prevu
                         }
                     );
                }
                // fermeture du tansaction
                await dbTransaction.CommitAsync();
                // await dbTransaction.RollbackAsync();

                return StatusCode(200, new { success = "Demande de pret reussi! " });

            }
            catch (Exception ex)
            {
                await dbTransaction.RollbackAsync();

                return StatusCode(500, new { error = ex.Message });
            }

            // var compte = await _service.GetByIdAsync(idCompte);
            // if (compte == null) return NotFound();
        }


        // soumission d'un pret
        // [HttpPost("askPret")]
        // public async Task<IActionResult> askPret([FromBody] ComptePretCreateDto dto, [FromQuery] int idCompteCourant)
        // {
        //     // if (dto == null) return BadRequest();

        //     // ouverture du transaction
        //     await using var dbTransaction = await _service.Context.Database.BeginTransactionAsync();

        //     try
        //     {
        //         // enregistrement du compte
        //         Console.WriteLine("creation du compte pret.......");
        //         var created = await _service.AddAsync(dto);

        //         Console.WriteLine("xxxxxxxxxxxxxxxxxxxxxxx");
        //         Console.WriteLine(dto);

        //         // enregistrement du transaction
        //         Console.WriteLine("creation des  transactions.......");
        //         Console.WriteLine("creation de la transaction compte pret.......");
        //         await _transactionPretService.AddAsync(
        //             new TransactionPretCreateDto
        //             {
        //                 IdCompte = created.IdCompte,
        //                 IdAmortissement = null,
        //                 Libelle = "decaissement pret: " + created.Libelle,
        //                 Montant = created.CapitalEmprunte,
        //                 TypeTransaction = "decaissement"
        //             }
        //         );
        //         Console.WriteLine("creation de la transaction compte courant.......");
        //         await _transactionCourantApiClient.CreateAsync(
        //             new TransactionCourantDto
        //             {
        //                 IdCompte = idCompteCourant,
        //                 DateTransaction = DateUtils.Today(),
        //                 Libelle = "decaissement pret: " + created.Libelle,
        //                 Montant = created.CapitalEmprunte,
        //                 Sens = "credit"
        //             }
        //         );

        //         // creation du tableau d'amortissement
        //         Console.WriteLine("Création d'amortissement.......");

        //         decimal capital_emprunte = created.CapitalEmprunte;
        //         decimal taux_interet_mensuel = created.TauxInteret / (12 * 100);
        //         int nbr_mois = created.DureeMois;

        //         // Conversion en double pour les calculs
        //         double c = (double)capital_emprunte;
        //         double t = (double)taux_interet_mensuel;

        //         // Formule de mensualité
        //         double mensualite = (c * t) / (1 - Math.Pow(1 + t, -nbr_mois));
        //         Console.WriteLine($"Mensualité calculée = {mensualite:F2}");

        //         // Variables de suivi
        //         double reste_du = c;

        //         for (int mois = 1; mois <= nbr_mois; mois++)
        //         {
        //             double interet = reste_du * t;
        //             double capital_rembourse = mensualite - interet;
        //             reste_du -= capital_rembourse;

        //             DateOnly date_prevu = created.DateOuverture.AddMonths(mois);



        //             // Affichage console
        //             Console.WriteLine($"Mois {mois}: Mensualité={mensualite:F2}, " +
        //                               $"Intérêt={interet:F2}, " +
        //                               $"Capital remboursé={capital_rembourse:F2}, " +
        //                               $"Reste dû={reste_du:F2}, " +
        //                               $"Prevu ={date_prevu}"
        //                                );
        //             // sauvegarde
        //             var amortissement_created = await _amortissementService.AddAsync(
        //                  new AmortissementCreateDto
        //                  {
        //                      IdCompte = created.IdCompte,
        //                      Mois = mois,
        //                      Mensualite = (decimal)mensualite,
        //                      Interet = (decimal)interet,
        //                      Capital = (decimal)capital_rembourse,
        //                      ResteDu = (decimal)reste_du,
        //                      CreatedAt = date_prevu
        //                  }
        //              );
        //         }
        //         // fermeture du tansaction
        //         await dbTransaction.CommitAsync();
        //         // await dbTransaction.RollbackAsync();

        //         return StatusCode(200, new { success = "Demande de pret reussi! " });

        //     }
        //     catch (Exception ex)
        //     {
        //         await dbTransaction.RollbackAsync();

        //         return StatusCode(500, new { error = ex.Message });
        //     }

        //     // var compte = await _service.GetByIdAsync(idCompte);
        //     // if (compte == null) return NotFound();
        // }

        // GET api/comptePret
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var comptes = await _service.GetAllDtoAsync();
            return Ok(comptes);
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
            try
            {
                var comptes = await _service.GetByClientIdAsync(idClient);
                if (!comptes.Any()) return NotFound();
                return Ok(comptes);
            }
            catch (Exception ex)
            {

                return StatusCode(500, new { error = ex.Message });
            }
        }

        // GET api/comptePret/byClient?idClient=1
        [HttpGet("byClientSolde")]
        public async Task<IActionResult> GetByClientIdSolde([FromQuery] int idClient, DateOnly? date)
        {
            try
            {
                var comptes = await _service.GetByClientIdAndDateWithSoldeAsync(idClient, date);
                // if (!comptes.Any()) return NotFound();
                return Ok(comptes);
            }
            catch (Exception ex)
            {

                return StatusCode(500, new { error = ex.Message });
            }

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
