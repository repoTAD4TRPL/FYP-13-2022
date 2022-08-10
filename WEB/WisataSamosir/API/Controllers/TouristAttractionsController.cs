using API.Base;
using API.Model;
using API.Repository.Data;
using API.ViewModel;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TouristAttractionsController : BaseController<TouristAttraction, TouristAttractionRepository, int>
    {
        private readonly TouristAttractionRepository touristAttractionRepository; 
        public TouristAttractionsController(TouristAttractionRepository repository) : base(repository)
        {
            this.touristAttractionRepository = repository;
        }

        [HttpPost("AddTouristAttractions")]
        public async Task<ActionResult<TouristAttraction>> AddTouristAttractions(TouristAttraction entity)
        {
            await touristAttractionRepository.AddTouristAttraction(entity);
            return Ok(entity);
        }
        [HttpGet("GetCategoryTourism/{category}")]
        public async Task<ActionResult<TouristAttractionVM>> GetCategoryTourism(int category)
        {
            var get = await touristAttractionRepository.GetTourismCategory(category);
            return Ok(get);
        }
        [HttpGet("GetTouristAttraction")]
        public async Task<ActionResult<TouristAttractionVM>> GetTouristAttraction()
        {
            var get = await touristAttractionRepository.GetTouristAttractions();
            return Ok(get);
        }
        [HttpGet("GetFoodDestination")]
        public async Task<ActionResult<TouristAttractionVM>> GetFoodDestination()
        {
            var get = await touristAttractionRepository.GetFoodDestination();
            return Ok(get);
        }
        [HttpGet("GetSouvenirDestination")]
        public async Task<ActionResult<TouristAttractionVM>> GetSouvenirDestination()
        {
            var get = await touristAttractionRepository.GetSouvenirDestination();
            return Ok(get);
        }
        [HttpGet("GetHotelDestination")]
        public async Task<ActionResult<TouristAttractionVM>> GetHotelDestination()
        {
            var get = await touristAttractionRepository.GetHotelDestination();
            return Ok(get);
        }

        [HttpGet("GetTouristAttractions/{id}")]
        public async Task<ActionResult<TouristAttractionVM>> GetTouristAttractions(int id)
        {
            var get = await touristAttractionRepository.GetTouristAttractions(id);
            return (get != null) ? (ActionResult)Ok(get) : NotFound();
        }
    }
}
