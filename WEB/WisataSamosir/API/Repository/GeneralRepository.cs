using API.Context;
using API.Repository.Interface;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository
{
    public class GeneralRepository<TEntity, TContext, TId> : IRepository<TEntity, TId>
       where TEntity : class
       where TContext : MyContext
    {
        private readonly MyContext myContext;
        public GeneralRepository(MyContext myContext)
        {
            this.myContext = myContext;
        }

        public async Task<TEntity> Delete(TId id)
        {
            var entity = await Get(id);
            if (entity == null)
            {
                return entity;
            }
            myContext.Set<TEntity>().Remove(entity);
            await myContext.SaveChangesAsync();
            return entity;
        }

        public virtual async Task<List<TEntity>> Get()
        {
            return await myContext.Set<TEntity>().ToListAsync();
        }

        public async Task<TEntity> Get(TId id)
        {
            return await myContext.Set<TEntity>().FindAsync(id);
        }

        public async Task<TEntity> Post(TEntity entity)
        {
            await myContext.Set<TEntity>().AddAsync(entity);
            try
            {
                var result = await myContext.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                Console.Write(ex);
            }
            return entity;
        }

        public async Task<TEntity> Put(TEntity entity)
        {
            myContext.Entry(entity).State = EntityState.Modified;
            await myContext.SaveChangesAsync();
            return entity;
        }
    }
}
