using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.ViewModel
{
    public class AccountListVM
    {
        public int Id{ get; set; }
        public string Name { get; set; }
        public string Username { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Phone { get; set; }
        public string MitraName { get; set; }
        public string Role { get; set; }
        public string Status { get; set; }
    }
}
