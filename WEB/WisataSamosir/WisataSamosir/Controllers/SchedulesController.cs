using API.Model;
using API.ViewModel;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WisataSamosir.Base.Controllers;
using WisataSamosir.Repository.Data;

namespace WisataSamosir.Controllers
{
    public class SchedulesController : BaseController<Schedule, ScheduleRepository, int>
    {
        private readonly ScheduleRepository scheduleRepository;
        public SchedulesController(ScheduleRepository repository) : base(repository)
        {
            this.scheduleRepository = repository;
        }
        [HttpPost]
        public JsonResult AddSchedule(ScheduleVM entity)
        {
            var result = scheduleRepository.AddSchedule(entity);
            return Json(result);
        }
        [HttpGet("[controller]/{id}")]
        public IActionResult Index(int id)
        {
            ViewBag.id = id;
            return View();
        }
        public IActionResult Schedule(int id)
        {
            ViewBag.id = id;
            return View("index");
        }
        [HttpGet("[controller]/GetScedule/{id}")]
        public async Task<JsonResult> GetSchedule (int id)
        {
            var result = await scheduleRepository.GetSchedule(id);
            return Json(result);
        }

        [HttpDelete]
        public JsonResult DeleteSession(int id)
        {
            var result = scheduleRepository.Delete(id);
            return Json(result);
        }
    }
}
