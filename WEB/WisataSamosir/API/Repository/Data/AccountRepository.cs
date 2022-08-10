using API.Context;
using API.Model;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Data
{
    public class AccountRepository : GeneralRepository<Account, MyContext, int>
    {
        private readonly MyContext context;
        public AccountRepository(MyContext myContext) : base(myContext)
        {
            this.context = myContext;
        }
        public async Task<List<AccountListVM>> GetAccount()
        {
            List<AccountListVM> data = await Task.Run(()=>(from a in context.Accounts
                        join r in context.Roles
                        on a.Role_id equals r.Id
                        join s in context.Statuses
                        on a.Status_id equals s.Id
                        select new AccountListVM
                        {
                            Id = a.Id,
                            Username = a.Username,
                            Email = a.Email,
                            Password = a.Password,
                            Phone = a.Phone,
                            MitraName = a.MitraName,
                            Role = r.Role_Name.ToString(),
                            Status = s.Status_Name.ToString()

                        }

                        ).ToList());

            return data;
        }
        public int Register(AccountVM accountVM)
        {
            Account account = new Account();
            account.Email = accountVM.Email;
            var checkEmail = context.Accounts.Where(e => e.Email == accountVM.Email).Count();
            if (checkEmail != 0)
            {
                return 100;
            }
            account.Username = accountVM.Username;
            var checkUsername = context.Accounts.Where(e => e.Username == accountVM.Username).Count();
            if (checkUsername != 0)
            {
                return 200;
            }
            account.Password = BCrypt.Net.BCrypt.HashPassword(accountVM.Password);
            account.Role_id = accountVM.Role;
            account.Status_id = accountVM.Status;
            account.Phone = accountVM.Phone;
            account.Name = accountVM.Name;
            account.MitraName = accountVM.MitraName;
            context.Accounts.Add(account);
            return context.SaveChanges();
        }
        public int Update(AccountVM accountVM)
        {
            var account = context.Accounts.Find(accountVM.Id);
            account.Email = accountVM.Email;
            account.Password = account.Password;
            account.Role_id = accountVM.Role;
            account.Status_id = accountVM.Status;
            account.Phone = accountVM.Phone;
            account.MitraName = accountVM.MitraName;
            context.Accounts.Update(account);
            return context.SaveChanges();

        }
        public async Task<AccountVM> UpdateAccount(AccountVM entity)
        {
            var batch = context.Accounts.Find(entity.Id);
            batch.UpdateAccount(entity.Username, entity.Email, entity.Password, entity.Role, entity.Status, entity.Phone,entity.MitraName);
            await context.SaveChangesAsync();
            return entity;
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
        public async Task<AccountListVM> Detail(int id)
        {
            var data = (from a in context.Accounts
                        where a.Id == id
                        join r in context.Roles
                        on a.Role_id equals r.Id
                        join s in context.Statuses
                        on a.Status_id equals s.Id
                        select new AccountListVM
                        {
                            Id = a.Id,
                            Name = a.Name,
                            MitraName = a.MitraName,
                            Username = a.Username,
                            Password = a.Password,
                            Email = a.Email,
                            Phone = a.Phone,
                            Role = r.Role_Name.ToString(),
                            Status = s.Status_Name.ToString()
                        }
                        ).FirstOrDefault();
            return data;
        }
    }
}
