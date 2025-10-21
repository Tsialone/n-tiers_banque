using Microsoft.AspNetCore.Mvc;
using Epargne.Models;
using Epargne.Services;
using System.Threading.Tasks;

namespace Epargne.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ClientEpargneController : ControllerBase
    {
        private readonly ClientEpargneService _service;

        public ClientEpargneController(ClientEpargneService service)
        {
            _service = service;
        }
        [HttpGet("where")]
        public async Task<IActionResult> GetByIdClient([FromQuery] int idClient)
        {
            var client = await _service.GetByIdClientAsync(idClient);
            if (client == null)
                return NotFound();
            
            return Ok(client);
        }

        [HttpGet]
        public async Task<IActionResult> GetAll() => Ok(await _service.GetAllAsync());

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var client = await _service.GetByIdAsync(id);
            if (client == null) return NotFound();
            return Ok(client);
        }

        [HttpPost]
        public async Task<IActionResult> Add([FromBody] ClientEpargne client)
        {
            await _service.AddAsync(client);
            return Ok();
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, [FromBody] ClientEpargne client)
        {
            if (id != client.IdClient) return BadRequest();
            await _service.UpdateAsync(client);
            return Ok();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            await _service.DeleteAsync(id);
            return Ok();
        }
    }
}
