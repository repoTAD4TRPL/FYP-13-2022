using API.Base;
using API.Model;
using API.Repository.Data;
using API.ViewModel;
using Microsoft.AspNetCore.Authorization;
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
    public class AccountsController : BaseController<Account, AccountRepository, int>
    {
        private readonly AccountRepository accountRepository;
        public AccountsController(AccountRepository repository) : base(repository)
        {
            this.accountRepository = repository;
        }
        [HttpGet("GetAccount")]
        public async Task<ActionResult<AccountListVM>> GetAccount()
        {
            var get = await accountRepository.GetAccount();
            return Ok(get);
        }

        [HttpPost("UpdateAccount")]
        public async Task<ActionResult<AccountVM>> UpdateAccount(AccountVM accountVM)
        {
            var get = await accountRepository.UpdateAccount(accountVM);
            return Ok(get);
        }

        [HttpPost("Register")]
        public ActionResult Register(AccountVM accountVM)
        {
            try
            {
                int output = 0;
                if (ModelState.IsValid)
                {
                    output = accountRepository.Register(accountVM);
                }
                else
                {
                    return BadRequest(new
                    {
                        status = HttpStatusCode.BadRequest,
                        message = "Check Format",
                    });
                }
                if (output == 100)
                {
                    return BadRequest(new
                    {
                        status = HttpStatusCode.BadRequest,
                        message = "Duplicate Email",
                    });
                }
                else if (output == 200)
                {
                    return BadRequest(new
                    {
                        status = HttpStatusCode.BadRequest,
                        message = "Duplicate Username",
                    });
                }
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

        [HttpGet("GetAccount/{id}")]
        public async Task<ActionResult<AccountListVM>> GetAccount(int id)
        {
            var get = await accountRepository.Detail(id);
            return (get != null) ? (ActionResult)Ok(get) : NotFound();
        }


    }
}
