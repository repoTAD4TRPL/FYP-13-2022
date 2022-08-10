using API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WisataSamosir.Base.Urls;

namespace WisataSamosir.Repository.Data
{
    public class StatusRepository : GeneralRepository<Status, int>
    {
        public StatusRepository(Address address, string request) : base(address, request)
        {
        }
    }
}
