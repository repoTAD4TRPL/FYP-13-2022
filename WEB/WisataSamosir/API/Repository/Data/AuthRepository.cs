using API.Context;
using API.Model;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{

    public class AuthRepository : GeneralRepository<Account, MyContext, int>
    {
        private readonly MyContext context;
        public AuthRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;
        }

        public int Login(LoginVM loginVM)
        {
            //return 100 = Email ga ketemu
            //return 200 = Password salah
            var checkEmail = context.Accounts.Where(e => e.Email == loginVM.Email).FirstOrDefault();
            if (checkEmail == null)
            {
                return 100;
            }
            if (!BCrypt.Net.BCrypt.Verify(loginVM.Password, checkEmail.Password))
            {
                return 200;
            }
            return 1;
        }
    }
}
