
using Microsoft.EntityFrameworkCore;
using StockMate.API.Data;

var builder = WebApplication.CreateBuilder(args);

// Register controllers
builder.Services.AddControllers();

// Read MySQL connection string
var connectionString = builder.Configuration
    .GetConnectionString("DefaultConnection")!;

// Register Entity Framework Core with MySQL
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseMySql(
        connectionString,
        ServerVersion.AutoDetect(connectionString)
    ));

var app = builder.Build();

app.MapControllers();

app.Run();
