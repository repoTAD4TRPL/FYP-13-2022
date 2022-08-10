using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    [Table("tb_category")]
    public class Category
    {
        public enum CaregoryEnum
        {
            SocialCulture = 0,
            NaturalTourism = 1,
            Food = 2,
            Hotel = 3,
            Souvenir = 4
        }
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Column("name", TypeName = "enum('SocialCulture','NaturalTourism', 'Food', 'Hotel','Souvenir')")]
        public CaregoryEnum Name { get; set; }
        public virtual ICollection<TouristAttraction> TouristAttractions { get; set; }

    }
}
