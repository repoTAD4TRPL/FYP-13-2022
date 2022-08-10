 using API.Context;
using API.Model;
using API.ViewModel;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class PortRouteRepository : GeneralRepository<PortRoute, MyContext, int>
    {
        private readonly MyContext context;

        public PortRouteRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;

        }
        public int AddPortRoute(PortRouteVM portRouteVM)
        {
            var harbor_start = context.Harbors.Where(x => x.Id == portRouteVM.Harbor_start).FirstOrDefault();
            var harbor_end = context.Harbors.Where(x => x.Id == portRouteVM.Harbor_end).FirstOrDefault();
            PortRoute portRoute = new PortRoute()
            {
                RouteName = harbor_start.Harbor_Name + "-" + harbor_end.Harbor_Name,
                Description = portRouteVM.Description,
                Harbor_start = portRouteVM.Harbor_start,
                Harbor_end = portRouteVM.Harbor_end

            };
            context.PortRoutes.Add(portRoute);
            context.SaveChanges();
            int[] id_portroute = new int[] { harbor_start.Id, harbor_end.Id};
            for (int i = 0; i < id_portroute.Length; i++)
            {
                var entity = context.Harbors.Find(id_portroute[i]);
                entity.Route_id = portRoute.Id;
            }
            return context.SaveChanges();
        }  
        public async Task<List<RouteVM>> GetRoute(int id)
        {
            List<RouteVM> data = await Task.Run(() => (from s in context.Schedules
                                                       where s.PortRoute_id == id
                                                       select new RouteVM
                                                       {
                                                           Session = s.Session,
                                                           Time = s.Time
                                                       }
                        ).ToList());
            return data;
        }
       public async Task<RouteItemVM> GetRoute(string route_name)
        {
            RouteItemVM data = await Task.Run(() => (from p in context.PortRoutes
                                                       join h in context.Harbors
                                                       on p.Harbor_start equals h.Id
                                                       into porthar
                                                       from ed in porthar.DefaultIfEmpty()
                                                       where p.RouteName == route_name
                                                         select new RouteItemVM
                                                           {
                                                               RouteName = p.RouteName
                                                           }).FirstOrDefault());
            return data;        
        }

        public async Task<List<HarborVM>> GetHarborRoute(int id)
        {
            List<HarborVM> data = await Task.Run(() => (from h in context.Harbors
                              where h.Route_id == id
                             select new HarborVM
                             {
                                 HarborName = h.Harbor_Name,
                                 Latitude = h.Latitude,
                                 Longitude = h.Longitude
                             }).ToList());
            return data;
        }

        public virtual async Task<List<PortRoute>> GetPortRouteUser(int account_id)
        {
            var data = await Task.Run(() => (from p in context.PortRoutes
                                             join h in context.Harbors
                                             on p.Id equals h.Route_id
                                             join a in context.Accounts
                                             on h.AccountId equals a.Id
                                             where h.AccountId == account_id
                                             select p).Distinct().ToList());
            return data;
        }

        public async Task<List<Harbor>> GetHarborPortRoute(int id)
        {
            List<Harbor> data = await Task.Run(() => (from p in context.PortRoutes
                                                                 join h in context.Harbors
                                                                 on p.Id equals h.Route_id
                                                                 where h.Route_id == id
                                                                 select h).ToList()) ;
            return data;
        }
        public async Task<List<Harbor>> GetHarborPortRoutehome(int id)
        {
            List<Harbor> data = await Task.Run(() => (from p in context.PortRoutes
                                                      join h in context.Harbors
                                                      on p.Id equals h.Route_id
                                                      where h.Route_id == id
                                                      select h).ToList());
            return data;
        }

    }

}
