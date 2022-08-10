using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    [Table("tb_destination")]
    public class TouristAttraction
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Column("location"), MaxLength(255)]
        public string Location{ get; set; }
        [Column("name"), MaxLength(100)]
        public string Name { get; set; }
        [Column("description")]
        public string Description {get; set; }
        [Column("latitude")]
        public double Latitude { get; set; }
        [Column("longitude")]
        public double Longitude { get; set; }
        [Column("image")]
        public byte[] Image{ get; set; }
        public int AccountId { get; set; }
        public virtual Account Account { get; set; }
        public int CategoryId { get; set; }
        public virtual Category Category { get; set; }

    }
}
