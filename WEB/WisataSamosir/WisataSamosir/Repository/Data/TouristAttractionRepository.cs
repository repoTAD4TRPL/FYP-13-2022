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
    public class TouristAttractionRepository : GeneralRepository<TouristAttraction, int>
    {
        private readonly Address address;
        private readonly string request;
        private readonly IHttpContextAccessor _contextAccessor;
        private readonly HttpClient httpClient;
        public TouristAttractionRepository(Address address, IHttpContextAccessor context, string request = "TouristAttractions/") : base(address, request)
        {
            this.address = address;
            this.request = request;
            _contextAccessor = context;
            httpClient = new HttpClient
            {
                BaseAddress = new Uri(address.link)
            };
        }
        public HttpStatusCode AddTouristAttraction(TouristAttraction entity)
        {
           
            StringContent content = new StringContent(JsonConvert.SerializeObject(entity), Encoding.UTF8, "application/json");
            var result = httpClient.PostAsync(request + "AddTouristAttractions/", content).Result;
            return result.StatusCode;
        }
        public HttpStatusCode UpdateTouristAttractions(TouristAttraction entity)
        {
            StringContent content = new StringContent(JsonConvert.SerializeObject(entity), Encoding.UTF8, "application/json");
            var result = httpClient.PutAsync(request+ "Put", content).Result;
            return result.StatusCode;
        }
        public async Task<List<TouristAttraction>> GetTouristAttraction()
        {
            List<TouristAttraction> entities = new List<TouristAttraction>();

            using (var response = await httpClient.GetAsync(request + "GetTouristAttraction"))
            {
                string apiResponse = await response.Content.ReadAsStringAsync();
                entities = JsonConvert.DeserializeObject<List<TouristAttraction>>(apiResponse);
            }
            return entities;
        }
        public async Task<List<TouristAttraction>> GetFoodDestination()
        {
            List<TouristAttraction> entities = new List<TouristAttraction>();

            using (var response = await httpClient.GetAsync(request + "GetFoodDestination"))
            {
                string apiResponse = await response.Content.ReadAsStringAsync();
                entities = JsonConvert.DeserializeObject<List<TouristAttraction>>(apiResponse);
            }
            return entities;
        }
    }
}
