using API.Context;
using API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class StatusRepository : GeneralRepository<Status, MyContext, int>
    {
        private readonly MyContext context;
        public StatusRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;
        }

        
    }
}
