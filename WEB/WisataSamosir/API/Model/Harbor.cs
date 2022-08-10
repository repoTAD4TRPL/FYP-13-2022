using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    [Table("tb_harbor")]
    public class Harbor
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Column("harbor_name"), MaxLength(250)]
        public string Harbor_Name { get; set; }
        [Column("description")]
        public string Description { get; set; }
        [Column("phone")]
        public string Phone { get; set; }
        [Column("harbor_type"), MaxLength(25)]
        public string Harbor_type { get; set; }
        [Column("harbor_activity"), MaxLength(25)]
        public string Harbor_Activity { get; set; }
        [Column("latitude")]
        public double Latitude { get; set; }
        [Column("longitude")]
        public double Longitude { get; set; }
        [Column("location"), MaxLength(255)]
        public string Location { get; set; }
        public int Route_id { get; set; }
        public int AccountId { get; set; }
        [ForeignKey("id_account")]  
        public virtual Account Account{ get; set; }
    }
}
