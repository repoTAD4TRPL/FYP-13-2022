using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WisataSamosir.Controllers
{
    [Authorize]
    public class Dashboards : Controller
    {
        public IActionResult Index()
        {
            ViewBag.Roles = HttpContext.Session.GetString("role");
            ViewBag.JWToken = HttpContext.Session.GetString("JWToken");
            if (User.Identity.IsAuthenticated)
            {
                return View();

            }
            //return View();
            return RedirectToAction("Index","Auths");
        }
    }
}
