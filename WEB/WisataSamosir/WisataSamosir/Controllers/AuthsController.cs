using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WisataSamosir.Base.Controllers;
using WisataSamosir.Handler;
using WisataSamosir.Repository.Data;

namespace WisataSamosir.Controllers
{
    public class AuthsController : BaseController<Account, AuthRepository,int>
    {
        private readonly AuthRepository repository;

        public AuthsController(AuthRepository repository)   :  base(repository)
        {
            this.repository = repository;
        }

        public IActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Login(string Email, string Password)
        {
            LoginVM loginVM = new LoginVM();
            loginVM.Email = Email;
            loginVM.Password = Password;
            var jwtToken = await repository.Login(loginVM);
            var token = jwtToken.Token;

            if (token == null)
            {
                return RedirectToAction("Index", "Auths", new { err = jwtToken.Message });
            }
            else
            {
            HttpContext.Session.SetString("JWToken", token);
            string Roles = JWTHandler.GetClaim(token, "role");
            string Id = JWTHandler.GetClaim(token, "id");
            HttpContext.Session.SetString("role", Roles);
            HttpContext.Session.SetString("id", Id);
            return RedirectToAction("Index", "Dashboards");
            }
            
        }
        [HttpGet("Unauthorized/")]
        public IActionResult Unauthorized()
        {
            return View("401");
        }

        [HttpGet("Notfound/")]
        public IActionResult Notfound()
        {
            return View("404");
        }
        [HttpGet("Forbidden/")]
        public IActionResult Forbidden()
        {
            return View("403");
        }
        public IActionResult ForgetPassword()
        {
            return View();
        }
        [Authorize]
        [HttpGet("Logout/")]
        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Index","Auths");
        }
    }
}
