using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WisataSamosir.Base.Controllers;
using WisataSamosir.Repository.Data;

namespace WisataSamosir.Controllers
{
    public class PortRoutesController : BaseController<PortRoute, PortRouteRepository, int>
    {
        private readonly PortRouteRepository portRouteRepository;
        public PortRoutesController(PortRouteRepository repository) : base(repository)
        {
            this.portRouteRepository = repository;
        }

        public IActionResult Index()
        {
            ViewBag.Roles = HttpContext.Session.GetString("role");
            ViewBag.JWToken = HttpContext.Session.GetString("JWToken");
            ViewBag.Id = HttpContext.Session.GetString("id");
            if (User.Identity.IsAuthenticated)
            {
                return View();
            }
            return RedirectToAction("Index", "Auths");
        }
        public IActionResult Schedule(int id)
        {
            ViewBag.id = id;
            return View("Schedule");
        }
        [HttpGet("[controller]/GetPortRouteUser/{id}")]
        public async Task<ActionResult<Harbor>> GetPortRouteUser(int id)
        {
            var result = await portRouteRepository.GetPortRouteUser(id);
            return Json(result);
        }
        [HttpPost]
        public JsonResult AddPortRoute(PortRouteVM portRouteVM)
        {
            var result = portRouteRepository.AddPortRoute(portRouteVM);
            return Json(result);
        }

    }
}
