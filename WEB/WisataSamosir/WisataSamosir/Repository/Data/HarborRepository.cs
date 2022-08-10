using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using WisataSamosir.Base.Urls;

namespace WisataSamosir.Repository.Data
{
    public class HarborRepository : GeneralRepository<Harbor, int>
    {
        private readonly Address address;
        private readonly string request;
        private readonly IHttpContextAccessor _contextAccessor;
        private readonly HttpClient httpClient;
        public HarborRepository(Address address, IHttpContextAccessor context, string request = "Harbors/") : base(address, request)
        {
            this.address = address;
            this.request = request;
            _contextAccessor = context;
            httpClient = new HttpClient
            {
                BaseAddress = new Uri(address.link)
            };
        }
        public HttpStatusCode Update(Harbor entity)
        {
            StringContent content = new StringContent(JsonConvert.SerializeObject(entity), Encoding.UTF8, "application/json");
            var result = httpClient.PutAsync(request+ "UpdateHarbor", content).Result;
            return result.StatusCode;
        }
        public async Task<List<Harbor>> GetHarborUser(int id)
        {
            List<Harbor> entity = null;

            using (var response = await httpClient.GetAsync(request + "GetHarborUser/" + id))
            {
                string apiResponse = await response.Content.ReadAsStringAsync();
                entity = JsonConvert.DeserializeObject<List<Harbor>>(apiResponse);
            }
            return entity;
        }
    }
}
