using API.Repository.Interface;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;

namespace API.Base
{
    public class BaseController<TEntity, TRepository, TId> : Controller
      where TEntity : class
      where TRepository : IRepository<TEntity, TId>
    {
        private readonly TRepository repository;

        public BaseController(TRepository repository)
        {
            this.repository = repository;
        }

        [HttpGet]
        public async Task<ActionResult<TEntity>> Get()
        {
            var get = await repository.Get();
            return Ok(get);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<TEntity>> Get(TId id)
        {
            var get = await repository.Get(id);
            return (get != null) ? (ActionResult)Ok(get) : NotFound();
        }

        [HttpPost]
        public async Task<ActionResult<TEntity>> Post(TEntity entity)
        {
            await repository.Post(entity);
            return Ok(entity);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> Put(TEntity entity)
        {
            await repository.Put(entity);
            return Ok();
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult<TEntity>> Delete(TId id)
        {
            var delete = await repository.Delete(id);
            if (delete == null)
            {
                return NotFound();
            }
            return delete;
        }
    }
}
