using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.ViewModel
{
    public class RouteItemVM
    {
        public string Session { get; set; }
        public string RouteName { get; set; }
        public TimeSpan Time { get; set; }
        public string Harbor_start{ get; set; }
        public string Harbor_end { get; set; }
    }
}
