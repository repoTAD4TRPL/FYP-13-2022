using API.Context;
using API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class HarborRepository : GeneralRepository<Harbor, MyContext, int>
    {
        private readonly MyContext context;
        public HarborRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;


        }
        //public int InsertClass(Harbor harbor)
        //{
        //    clas.Class_id = idHandler.GenerateIdClass(clas.Class_Name, clas.CT_id);
        //    context.Classes.Add(clas);
        //    return context.SaveChanges();
        //}

        public virtual async Task<List<Harbor>> GetHarborUser(int account_id)
        {
            var data = await Task.Run(() => (from h in context.Harbors
                                             join a in context.Accounts
                                             on h.AccountId equals a.Id
                                             where h.AccountId == account_id
                                             select h)  .ToList()) ;
            return data;
        }
    }
}
