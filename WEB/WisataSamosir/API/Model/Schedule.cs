using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace API.Model
{
    [Table("tb_schedule")]
    public class Schedule
    {
        [Key]
        [Column("id")]
        public int Id{ get; set; }
        [Column("session"), MaxLength(25)]
        public string Session{ get; set; }
        [Column("time"), MaxLength(25)]
        [DataType(DataType.Time)]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:HH:mm}")]
        public TimeSpan Time { get; set; }
        public int PortRoute_id { get; set; }
        [ForeignKey("PortRoute_id")]
        public virtual PortRoute PortRoute { get; set; }
    }
}
