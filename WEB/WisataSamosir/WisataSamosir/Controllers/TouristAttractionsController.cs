using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using WisataSamosir.Base.Controllers;
using WisataSamosir.Repository.Data;

namespace WisataSamosir.Controllers
{
    [Authorize]
    public class TouristAttractionsController : BaseController<TouristAttraction, TouristAttractionRepository, int>
    {
        private readonly TouristAttractionRepository touristAttractionRepository;

        public TouristAttractionsController(TouristAttractionRepository repository) : base(repository)
        {
            this.touristAttractionRepository = repository;
        }
        [HttpPost]
        public JsonResult AddTouristAttraction(TouristAttraction entity)
        {
            var result = touristAttractionRepository.AddTouristAttraction(entity);
            return Json(result);
        }

        [HttpPut]
        public JsonResult UpdateTouristAttractions(TouristAttraction entity)
        {
            var result = touristAttractionRepository.UpdateTouristAttractions(entity);
            return Json(result);
        }
        [HttpPost]
        public JsonResult PostAdd(TouristAttraction entity)
        {
            var result = touristAttractionRepository.AddTouristAttraction(entity);
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
            //var user = User.Identity.Name;
            //ViewBag.Roles = user;
            //return View();
            return RedirectToAction("");
        }
        public async Task<JsonResult> GetTouristAttraction()
        {
            var result = await touristAttractionRepository.GetTouristAttraction();
            return Json(result);
        }
        public async Task<JsonResult> GetFoodDestination()
        {
            var result = await touristAttractionRepository.GetFoodDestination();
            return Json(result);
        }


    }

}
