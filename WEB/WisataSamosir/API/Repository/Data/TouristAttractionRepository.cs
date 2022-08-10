using API.Context;
using API.Model;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class TouristAttractionRepository : GeneralRepository<TouristAttraction, MyContext, int>
    {
        private readonly MyContext context;
        public TouristAttractionRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;
        }
        public async Task<TouristAttraction> AddTouristAttraction(TouristAttraction touristAttraction)
        {
            await context.Set<TouristAttraction>().AddAsync(touristAttraction);
            try
            {
                var result = await context.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                Console.Write(ex);
            }
            return touristAttraction;
        }

        public async Task<List<TouristAttractionVM>> GetTourismCategory(int category)
        {
            List<TouristAttractionVM> data = await Task.Run(() => (from t in context.TouristAttractions
                                                        where t.CategoryId==category
                                                        select new TouristAttractionVM
                                                        {
                                                            Id = t.Id,
                                                            Name = t.Name,
                                                            Location = t.Location,
                                                            Description = t.Description,
                                                            Latitude = t.Latitude,
                                                            Longitude = t.Longitude

                                                        }
                                                        ).ToList());
            return data;
        }

        public async Task<List<TouristAttractionVM>> GetTouristAttractions()
        {
            List<TouristAttractionVM> data = await Task.Run(() => (from t in context.TouristAttractions
                                                                 where t.CategoryId == 0 || t.CategoryId ==1
                                                                 select new TouristAttractionVM
                                                                 {
                                                                     Id = t.Id,
                                                                     Name = t.Name,
                                                                     Location = t.Location,                                                                     Description = t.Description,
                                                                     Latitude = t.Latitude,
                                                                     Longitude = t.Longitude

                                                                 }
                                                        ).ToList());

            return data;
        }
        public async Task<List<TouristAttractionVM>> GetFoodDestination()
        {
            List<TouristAttractionVM> data = await Task.Run(() => (from t in context.TouristAttractions
                                                                 where t.CategoryId == 2
                                                                 select new TouristAttractionVM
                                                                 {
                                                                     Id = t.Id,
                                                                     Name = t.Name,
                                                                     Location = t.Location,
                                                                     Description = t.Description,
                                                                     Latitude = t.Latitude,
                                                                     Longitude = t.Longitude
                                                                 }
                                                                ).ToList());

            return data;
        }
        public async Task<List<TouristAttractionVM>> GetSouvenirDestination()
        {
            List<TouristAttractionVM> data = await Task.Run(() => (from t in context.TouristAttractions
                                                                   where t.CategoryId == 4
                                                                   select new TouristAttractionVM
                                                                   {
                                                                       Id = t.Id,
                                                                       Name = t.Name,
                                                                       Location = t.Location,
                                                                       Description = t.Description,
                                                                       Latitude = t.Latitude,
                                                                       Longitude = t.Longitude
                                                                   }
                                                                ).ToList());

            return data;
        }
        public async Task<List<TouristAttractionVM>> GetHotelDestination()
        {
            List<TouristAttractionVM> data = await Task.Run(() => (from t in context.TouristAttractions
                                                                   where t.CategoryId == 3
                                                                   select new TouristAttractionVM
                                                                   {
                                                                       Id = t.Id,
                                                                       Name = t.Name,
                                                                       Location = t.Location,
                                                                       Description = t.Description,
                                                                       Latitude = t.Latitude,
                                                                       Longitude = t.Longitude
                                                                   }
                                                                ).ToList());

            return data;
        }
        public async Task<TouristAttractionVM> GetTouristAttractions(int id)
        {
            TouristAttractionVM data = await Task.Run(() => (from t in context.TouristAttractions
                                                                   where t.Id == id
                                                                   select new TouristAttractionVM
                                                                   {
                                                                       Id = t.Id,
                                                                       Name = t.Name,
                                                                       Location = t.Location,
                                                                       Description = t.Description,
                                                                       Latitude = t.Latitude,
                                                                       Longitude = t.Longitude

                                                                   }
                                                        ).FirstOrDefault());

            return data;
        }
    }
}
