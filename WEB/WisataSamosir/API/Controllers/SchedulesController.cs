using API.Base;
using API.Model;
using API.Repository.Data;
using API.ViewModel;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SchedulesController : BaseController<Schedule, ScheduleRepository, int>
    {
        private readonly ScheduleRepository scheduleRepository;
        public SchedulesController(ScheduleRepository repository) : base(repository)
        {
            this.scheduleRepository  = repository;
        }
        [HttpGet("GetSchedule/{id}")]
        public async Task<ActionResult<ScheduleVM>> GetSchedule(int id)
        {
            var get = await scheduleRepository.GetSchedule(id);
            return Ok(get);
        }
        [HttpPost("AddSchedule")]
        public async Task<ActionResult<Schedule>> AddSchedule(ScheduleVM entity)
        {
            scheduleRepository.AddSchedule(entity);
            return Ok(entity);
        }

        [HttpDelete("DeleteSchedule/{id}")]
        public async Task<ActionResult<Schedule>> DeleteSchedule(int id)
        {
            var delete = await scheduleRepository.Delete(id);
            if (delete == null)
            {
                return NotFound();
            }
            return delete;
        }

        [HttpGet("GetSession/{id}")]
        public async Task<ActionResult<Schedule>> GetSession(int id)
        {
            var get = await scheduleRepository.Get(id);
            return (get != null) ? (ActionResult)Ok(get) : NotFound();
        }
    }
}
