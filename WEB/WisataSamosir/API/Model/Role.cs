using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    public enum RoleEnum
    {
        SUPERADMIN = 0,
        ADMIN = 1,
    }
    [Table("tb_role")]
    public class Role
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Column("role_name", TypeName = "enum('SUPERADMIN','ADMIN')")]
        public RoleEnum Role_Name { get; set; }
        public virtual ICollection<Account> Accounts { get; set; }
    }
}
