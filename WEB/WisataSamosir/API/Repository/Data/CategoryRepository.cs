using API.Context;
using API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
   
    public class CategoryRepository : GeneralRepository<Category, MyContext, int>
    {
        private readonly MyContext context;

        public CategoryRepository(MyContext context) : base(context)
        {
            this.context = context;
        }
    }
}
