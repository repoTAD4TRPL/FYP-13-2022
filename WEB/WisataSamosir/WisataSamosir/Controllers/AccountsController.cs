using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WisataSamosir.Base.Controllers;
using WisataSamosir.Repository.Data;

namespace WisataSamosir.Controllers
{
    [Authorize]
    public class AccountsController : BaseController<Account, AccountRepository, int>
    {
       
        private readonly AccountRepository accountRepository;
        public AccountsController(AccountRepository repository) : base(repository)
        {
            this.accountRepository = repository;
        }
        [HttpGet]
        public async Task<JsonResult> GetAccount()
        {
            var result = await accountRepository.GetAccount();
            return Json(result);
        }
        [HttpGet("[controller]/GetDetail/{id}")]
        public async Task<JsonResult> GetDetail(int id)
        {
            var result = await accountRepository.GetAccount(id);
            return Json(result);
        }
        [HttpPut]
        public JsonResult UpdateAccount(AccountListVM accountListVM)
        {
            var result = accountRepository.UpdateAccount(accountListVM);
            return Json(result);
        }
        [HttpPost]
        public JsonResult AddAccount (AccountVM accountVM)
        {
            var result = accountRepository.AddAccount(accountVM);
            return Json(result);
        } 

        public async Task<IActionResult> IndexAsync()
        {
            ViewBag.Roles = HttpContext.Session.GetString("role");
            ViewBag.JWToken = HttpContext.Session.GetString("JWToken");
            if (User.Identity.IsAuthenticated)
            {
                return  View();

            }
            return RedirectToAction("");
        }
       
    }
}
