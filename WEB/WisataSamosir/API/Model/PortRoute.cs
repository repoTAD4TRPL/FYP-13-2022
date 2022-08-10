using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    [Table("tb_port_route")]
    public class PortRoute
    {
        [Key]
        [Column("id")]
        public int Id { get; set; }
        [Column("route_name"), MaxLength(250)]
        public string RouteName{ get; set; }
        [Column("description")]
        public string Description { get; set; }
        [Column("harbor_start"), MaxLength(25)]
        public int Harbor_start {get; set; }
        [Column("harbor_end"), MaxLength(25)]
        public int Harbor_end { get; set; }
        public virtual ICollection<Schedule> Schedules{ get; set; }

    }
}
