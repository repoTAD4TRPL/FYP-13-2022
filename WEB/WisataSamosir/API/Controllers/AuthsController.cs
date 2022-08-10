using API.Base;
using API.Context;
using API.Model;
using API.Repository.Data;
using API.ViewModel;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Net;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthsController : BaseController<Account, AuthRepository, int>
    {
        private readonly AuthRepository authRepository;
        public IConfiguration _configuration;
        private readonly MyContext myContext;


        public AuthsController(AuthRepository repository, IConfiguration _configuration, MyContext context) : base(repository)
        {
            this.authRepository = repository;
            this._configuration = _configuration;
            this.myContext = context;
        }

        [HttpPost("Login")]
        public ActionResult Login(LoginVM loginVM)
        {
            int output = authRepository.Login(loginVM);
            if (output == 100)
            {
                return NotFound(new
                {
                    status = HttpStatusCode.NotFound,
                    message = "Email not found"
                });
            }
            else if (output == 200)
            {
                return BadRequest(new
                {
                    status = HttpStatusCode.BadRequest,
                    message = "Wrong Password",
                });
            }
            else
            {
                var data = (from a in myContext.Accounts
                            join ar in myContext.Roles on
                            a.Role_id equals ar.Id
                            where a.Email == $"{loginVM.Email}"
                            select new RoleVM
                            {
                                Role = ar.Role_Name.ToString()
                            }).FirstOrDefault();
                var data_id = (from a in myContext.Accounts
                            where a.Email == $"{loginVM.Email}"

                            select a.Id).FirstOrDefault().ToString();
                var claim = new List<Claim>();
                claim.Add(new Claim("id", data_id));
                claim.Add(new Claim("email", loginVM.Email));
                claim.Add(new Claim("role", data.Role));
                var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));

                var signIn = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

                var token = new JwtSecurityToken(_configuration["Jwt:Issuer"],
                                                 _configuration["Jwt:Audience"],
                                                 claim, expires: DateTime.UtcNow.AddDays(1),
                                                 signingCredentials: signIn);

                return Ok(new
                {
                    token = new JwtSecurityTokenHandler().WriteToken(token),
                    role = data.Role,
                    status = HttpStatusCode.OK,
                    message = "Login Success !"
                }) ;
            }
        }


    }
}
