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
    public class TransactionPretController : ControllerBase
    {
        private readonly TransactionPretService _service;



        public TransactionPretController
            (
            TransactionPretService service
            )
        {
            _service = service;
        }


        // GET api/comptePret
        [HttpGet("byIdCompteAndAmort")]
        public async Task<IActionResult> GetAllByIdCompteAndIdAmort([FromQuery] int idCompte, [FromQuery] int ? idAmortissement)
        {   
            var  comptes =  await _service.GetAllDtoByIdCompteAndIdAmortAsync(idCompte , idAmortissement);
            return Ok(comptes);
        }
        
        //  [HttpGet ("byIdCompte")]
        // public async Task<IActionResult> GetAllByIdCompte([FromQuery]  int idCompte )
        // {
        //     var comptes = await _service.GetAllDtoByIdCompteAndIdAmortAsync(idCompte , idAmortissement);
        //     return Ok(comptes);
        // }
    }
}
