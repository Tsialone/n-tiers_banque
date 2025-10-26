using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;
using Epargne.Data;
using Epargne.Services;
using Microsoft.Extensions.Configuration;
using Microsoft.AspNetCore.Hosting;
using Epargne.ExternalApi.Services;

var builder = WebApplication.CreateBuilder(args);
builder.WebHost.ConfigureKestrel(options =>
{
    // options.ListenAnyIP(90); // écoute sur le port 90 dans le container
    options.ListenAnyIP(6000);
});

// EF Core PostgreSQL
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

// DI
builder.Services.AddScoped<PersonService>();
// builder.Services.AddScoped<ClientEpargneService>();
builder.Services.AddScoped<CompteEpargneService>();
builder.Services.AddScoped<TransactionEpargneService>();


// compte courant api
builder.Services.AddHttpClient<CompteCourantApiClient>();
builder.Services.AddHttpClient<TransactionCourantApiClient>();
builder.Services.AddHttpClient<ClientCourantApiClient>();






// Controllers + Swagger
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "Epargne API", Version = "v1" });
});

var app = builder.Build();

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "Epargne API v1");
});

app.MapControllers();

app.Run();
