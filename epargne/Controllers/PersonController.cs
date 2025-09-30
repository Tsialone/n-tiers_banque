using Microsoft.AspNetCore.Mvc;
using Epargne.Models;
using Epargne.Services;
using System.Threading.Tasks;           // pour Task<>

namespace Epargne.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class PersonController : ControllerBase
    {
        private readonly PersonService _service;

        public PersonController(PersonService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _service.GetAllAsync());

        [HttpPost]
        public async Task<IActionResult> Add([FromBody] Person person)
        {
            await _service.AddAsync(person);
            return Ok();
        }
    }
}
