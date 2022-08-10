using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.ViewModel
{
    public class ScheduleViewVM
    {

       
        public int Id { get; set; }
        public string Session { get; set; }
        public string Time { get; set; }
        public string RouteName{ get; set; }
        public int PortRoute_id { get; set; }
    }
}
