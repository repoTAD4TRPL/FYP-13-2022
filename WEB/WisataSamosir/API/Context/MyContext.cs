using API.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace API.Context
{
    public class MyContext : DbContext
    {
        public DbSet<Account> Accounts { get; set; }
        public DbSet<Harbor> Harbors { get; set; }
        public DbSet<PortRoute> PortRoutes { get; set; }
        public DbSet<Role> Roles { get; set; }
        public DbSet<Schedule> Schedules { get; set; }
        public DbSet<Status> Statuses { get; set; }
        public DbSet<TouristAttraction> TouristAttractions { get; set; }

        public MyContext(DbContextOptions<MyContext> options) : base(options)
        {

        }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {

            modelBuilder.Entity<Role>()
                .HasMany(a => a.Accounts)
                .WithOne(a => a.Role);

            modelBuilder.Entity<Status>()
                .HasMany(a => a.Accounts)
                .WithOne(a => a.Status);

            modelBuilder.Entity<PortRoute>()
             .HasMany(a => a.Schedules)
             .WithOne(a => a.PortRoute);

            modelBuilder.Entity<Account>()
            .HasMany(a => a.Harbors)
            .WithOne(a => a.Account);

            modelBuilder.Entity<Account>()
            .HasMany(a => a.TouristAttractions)
            .WithOne(a => a.Account);

            modelBuilder.Entity<Category>()
           .HasMany(a => a.TouristAttractions)
           .WithOne(a => a.Category);

        }
    }
}
