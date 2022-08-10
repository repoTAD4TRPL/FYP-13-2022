using API.Model;
using WisataSamosir.Repository.Data;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using API.Context;
using WisataSamosir.Base.Controllers;

namespace WisataSamosir.Controllers
{
    public class CategoriesController : BaseController<Category, CategoryRepository, int>
    {
        private readonly CategoryRepository categoryRepository;

        public CategoriesController(CategoryRepository repository) : base(repository)
        {
            this.categoryRepository = repository;
        }

        public IActionResult Index()
        {
            return View();
        }
    }
}
