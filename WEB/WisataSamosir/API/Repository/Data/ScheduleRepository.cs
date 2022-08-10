using API.Context;
using API.Model;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class ScheduleRepository : GeneralRepository<Schedule, MyContext, int>
    {
        private readonly MyContext context;
        public ScheduleRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;
        }
        public async Task<List<ScheduleViewVM>> GetSchedule(int port_id)
        {
            List<ScheduleViewVM> data = await Task.Run(() =>
                (from s in context.Schedules
                 join p in context.PortRoutes
                 on s.PortRoute_id equals p.Id
                 where s.PortRoute_id == port_id
                 select new ScheduleViewVM
                 {
                     PortRoute_id = p.Id,
                     Id = s.Id,
                     RouteName = p.RouteName,
                     Session = s.Session,
                     Time = s.Time.ToString()
                 }).ToList());

            return data;

        }
        public int AddSchedule(ScheduleVM scheduleVM)
        {
            Schedule schedule = new Schedule();
            schedule.Time = TimeSpan.Parse(scheduleVM.Time);
            schedule.Session = scheduleVM.Session;
            schedule.PortRoute_id = scheduleVM.PortRoute_id;
            context.Schedules.Add(schedule);
            return context.SaveChanges();
        }

        //public int AddSchedule(ScheduleVM scheduleVM)
        //{
        //    Schedule schedule = new Schedule();
        //    schedule.Time = TimeSpan.Parse(scheduleVM.Time);
        //    schedule.Session = scheduleVM.Session;
        //    schedule.PortRoute_id = scheduleVM.PortRoute_id;
        //    context.Schedules.Add(schedule);
        //    context.SaveChanges();
        //    return  1;
        //}


    }
}
