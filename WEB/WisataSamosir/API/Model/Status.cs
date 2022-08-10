using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    public enum StatusEnum
    {
        ACTIVE = 0,
        EXPIRED = 1,
    }
    [Table("tb_status")]
    public class Status
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Column("status_name", TypeName = "enum('ACTIVE','EXPIRED')")]
        public StatusEnum Status_Name { get; set; }
        public virtual ICollection<Account> Accounts { get; set; }

    }
}
