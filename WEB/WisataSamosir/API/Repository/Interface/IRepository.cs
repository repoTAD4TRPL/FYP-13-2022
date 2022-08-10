using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Repository.Interface
{
    public interface IRepository<T, X> where T : class
    {
        Task<List<T>> Get();
        Task<T> Get(X id);
        Task<T> Post(T entity);
        Task<T> Put(T entity);
        Task<T> Delete(X id);
    }
}
