using API.Base;
using API.Model;
using API.Repository.Data;
using API.ViewModel;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PortRoutesController : BaseController<PortRoute, PortRouteRepository, int>
    {
        private readonly PortRouteRepository portRouteRepository;
        public PortRoutesController(PortRouteRepository repository) : base(repository)
        {
            this.portRouteRepository = repository;
        }
        [HttpPost("AddPortRoute")]
        public ActionResult AddPortRoute(PortRouteVM portRoute)
        {
            try
            {
                portRouteRepository.AddPortRoute(portRoute);
                return Ok(new
                {
                    statusCode = HttpStatusCode.OK,
                    message = "Success"
                });
            }
            catch
            {
                return BadRequest(new
                {
                    status = HttpStatusCode.BadRequest,
                    message = "Error duplicate data",
                });
            }
        }
        [HttpGet("getAllRoute/{id}")]
        public async Task<ActionResult<RouteVM>> GetAllRoute(int id)
        {
            var get = await portRouteRepository.GetRoute(id);                                             
            return Ok(get);
        }

        [HttpGet("GetHarborRoute/{id}")]
        public async Task<ActionResult<RouteVM>> GetHarborRoute(int id)
        {
            var get = await portRouteRepository.GetHarborRoute(id);
            return Ok(get);
        }
        [HttpGet("GetPortRouteUser/{id}")]
        public async Task<ActionResult<PortRoute>> GetPortRouteUser(int id)
        {
            var get = await portRouteRepository.GetPortRouteUser(id);
            return Ok(get);
        }
        [HttpGet("GetHarborPortRoute/{id}")]
        public async Task<ActionResult<PortRoute>> GetHarborPortRoute(int id)
        {
            var get = await portRouteRepository.GetHarborPortRoute(id);
            return Ok(get);
        }
    }
}
