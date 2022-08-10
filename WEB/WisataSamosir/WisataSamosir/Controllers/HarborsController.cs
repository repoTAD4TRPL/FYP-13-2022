using API.Model;
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
    public class HarborsController : BaseController<Harbor,HarborRepository, int>
    {
        private readonly HarborRepository harborRepository;
        public HarborsController(HarborRepository repository) : base(repository)
        {
            this.harborRepository = repository;
        }

        [HttpPut]
        public JsonResult UpdateHarbor(Harbor entity)
        {
            var result = harborRepository.Update(entity);
            return Json(result);
        }
        [HttpGet("[controller]/GetHarborUser/{id}")]
        public async Task<ActionResult<Harbor>> GetHarborUser(int id)
        {
            var result = await harborRepository.GetHarborUser(id);
            return Json(result);
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
    }
}
