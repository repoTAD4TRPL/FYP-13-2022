using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Principal;
using System.Text;
using System.Threading.Tasks;
using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;
using WisataSamosir.Base.Urls;

namespace WisataSamosir.Repository.Data
{
    public class AccountRepository : GeneralRepository<Account, int>
    {
        private readonly Address address;
        private readonly string request;
        private readonly IHttpContextAccessor _contextAccessor;
        private readonly HttpClient httpClient;
        public AccountRepository(Address address, IHttpContextAccessor context, string request = "Accounts/") : base(address, request)
        {
            this.address = address;
            this.request = request;
            _contextAccessor = context;
            httpClient = new HttpClient
            {
                BaseAddress = new Uri(address.link)
            };
        }
        public async Task<List<AccountListVM>> GetAccount()
        {
            List<AccountListVM> entities = new List<AccountListVM>();

            using (var response = await httpClient.GetAsync(request+"GetAccount"))
            {
                string apiResponse = await response.Content.ReadAsStringAsync();
                entities = JsonConvert.DeserializeObject<List<AccountListVM>>(apiResponse);
            }
            return entities;
        }
        public async Task<AccountListVM> GetAccount(int id)
        {
            AccountListVM entities = null;

            using (var response = await httpClient.GetAsync(request + "GetAccount/"+ id))
            {
                string apiResponse = await response.Content.ReadAsStringAsync();
                entities = JsonConvert.DeserializeObject<AccountListVM>(apiResponse);
            }
            return entities;
        }
        public HttpStatusCode UpdateAccount(AccountListVM accountListVM)
        {
            StringContent content = new StringContent(JsonConvert.SerializeObject(accountListVM), Encoding.UTF8, "application/json");
            var result = httpClient.PostAsync(request + "UpdateAccount/", content).Result;
            return result.StatusCode;
        }
        public HttpStatusCode AddAccount (AccountVM accountVM)
        {
            StringContent content = new StringContent(JsonConvert.SerializeObject(accountVM), Encoding.UTF8, "application/json");
            var result = httpClient.PostAsync(request + "Register/" , content).Result;
            return result.StatusCode;

        }
    }
}
