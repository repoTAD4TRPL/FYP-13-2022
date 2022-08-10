using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    [Table("tb_account")]
    public class Account
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Required]
        [Column("name"), MaxLength(25)]
        public string Name{ get; set; }
        [Required]
        [Column("username"), MaxLength(25)]
        public string Username { get; set; }
        [Required]
        [Column("email"), MaxLength(25)]
        public string Email{ get; set; }
        [Required]
        [Column("password"), MaxLength(100)]
        public string Password { get; set; }
        [Column("phone")]
        public string Phone { get; set; }
        [Column("mitra_name"),MaxLength(50)]
        public string MitraName { get; set; }
        public int Role_id { get; set; }
        public int Status_id { get; set; }
             
        [ForeignKey("Role_id")]
        public virtual Role Role { get; set; }
        [ForeignKey("Status_id")]
        public virtual Status Status { get; set; }  
        public virtual ICollection<Harbor> Harbors{ get; set; }
        public virtual ICollection<TouristAttraction> TouristAttractions{ get; set; }

        public void UpdateAccount(string username, string email, string password, int role, int status,string phone, string mitra_name)
        {
            this.Username = username;
            this.Email = email;
            this.Password = password;
            this.Role_id = role;
            this.Status_id = status;
            this.Phone = phone;
            this.MitraName = mitra_name;
        }
    }
}
