﻿using API.Context;
using API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class RoleRepository : GeneralRepository<Role, MyContext, int>
    {
        public RoleRepository(MyContext myContext) : base(myContext)
        {
        }
    }
}
