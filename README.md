# StockMate

Full-stack inventory management application built with an **Android (Java)** app, an **ASP.NET Core Web API** backend and a **MySQL** database.

StockMate helps a small business keep track of its products and stock from a mobile phone. The Android app sends HTTP requests (JSON) to the Web API, and the API stores everything in MySQL using Entity Framework Core.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Screenshots](#screenshots)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## Tech Stack

| Layer | Technology |
| --- | --- |
| Mobile app | Android, Java, Android Studio (`HttpURLConnection`, `org.json`) |
| Backend | ASP.NET Core Web API (C#, .NET 10) |
| ORM | Entity Framework Core with the Pomelo MySQL provider |
| Database | MySQL (`stockmate_db`) |
| Data format | JSON over REST |
| Tools | Git, GitHub, Android Studio |

---

## Architecture

```
+--------------+                 +----------------------+           +--------------+
|  Android App |   HTTP / JSON   |  ASP.NET Core API    |    SQL    |    MySQL     |
|  (Java)      | <-------------> |  (StockMateBackend)  | <-------> | stockmate_db |
+--------------+                 +----------------------+           +--------------+
```

1. The user taps a button in the Android app (add, load, update or delete).
2. The app sends a REST request to the `api/product` endpoint on a background thread.
3. The API uses Entity Framework Core to read or write the `Products` table in MySQL.
4. The API returns JSON and a status code, and the app shows the result on screen.

---

## Features

- Add a new product (name, quantity, price)
- View all products with ID, name, quantity and price
- Update a product by its ID
- Delete a product by its ID
- Input checks in the app (empty fields, negative values, invalid numbers)
- Network calls run on a background thread, and buttons are disabled while a request is running
- Database tables created with Entity Framework Core migrations
- Database connection string kept in .NET User Secrets (no password in the repository)

---

## Project Structure

```
StockMate/
├── StockMate/                          # Android app (Java)
│   └── app/src/main/
│       ├── java/com/demo/stockmate/    # MainActivity.java
│       ├── res/layout/                 # activity_main.xml
│       └── AndroidManifest.xml
├── StockMateBackend/
│   └── StockMate.API/                  # ASP.NET Core Web API (C#)
│       ├── Controllers/                # ProductController.cs
│       ├── Models/                     # Product.cs
│       ├── Data/                       # ApplicationDbContext.cs
│       ├── Migrations/                 # EF Core migrations
│       └── Program.cs
├── project db.sql                      # creates the stockmate_db database
└── .gitignore
```

---

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) with an emulator or a physical Android device
- [.NET SDK](https://dotnet.microsoft.com/download) (the project targets `net10.0`)
- [MySQL Server](https://dev.mysql.com/downloads/) and MySQL Workbench
- EF Core command-line tool: `dotnet tool install --global dotnet-ef`
- Git

### 1. Clone the repository

```bash
git clone https://github.com/saloni-0405/StockMate.git
cd StockMate
```

### 2. Create the database

Run `project db.sql` in MySQL Workbench. It creates the `stockmate_db` database. The `Products` table is created in step 3 by the migration.

### 3. Run the backend (ASP.NET Core Web API)

The connection string is stored in User Secrets, so set it once on your computer:

```bash
cd StockMateBackend/StockMate.API
dotnet user-secrets set "ConnectionStrings:DefaultConnection" "server=localhost;port=3306;database=stockmate_db;user=YOUR_USER;password=YOUR_PASSWORD"
```

Create the table and start the API:

```bash
dotnet restore
dotnet ef database update
dotnet run
```

The API runs at `http://localhost:5049`. Test it in the browser at `http://localhost:5049/api/product`.

### 4. Run the Android app

1. Open the `StockMate` folder (the Android project) in Android Studio and wait for Gradle sync.
2. Open `MainActivity.java` and set the `API_URL` constant:
   - Android emulator: `http://10.0.2.2:5049/api/product`
   - Physical phone: `http://<your-computer-ip>:5049/api/product` (phone and computer on the same Wi-Fi)
   - Alternative: keep `http://127.0.0.1:5049/api/product` and run `adb reverse tcp:5049 tcp:5049`
3. Click **Run**.

> The app allows plain `http` traffic (`usesCleartextTraffic`) for local development. Use `https` in production.

---

## API Endpoints

Base URL: `/api/product`

| Method | Endpoint | Description | Success response |
| --- | --- | --- | --- |
| GET | `/api/product` | Get all products | 200 OK |
| GET | `/api/product/{id}` | Get one product | 200 OK, or 404 if not found |
| POST | `/api/product` | Add a product | 201 Created |
| PUT | `/api/product/{id}` | Update a product | 200 OK, or 404 if not found |
| DELETE | `/api/product/{id}` | Delete a product | 200 OK, or 404 if not found |

Example request body (`POST /api/product`):

```json
{
  "name": "Notebook",
  "quantity": 50,
  "price": 40.0
}
```

---

## Screenshots

<!-- Add screenshots to a "screenshots" folder in the repo, then link them like this: -->
<!-- ![Home screen](screenshots/home.png) -->

_Screenshots coming soon._

---

## Future Improvements

- Show products in a RecyclerView list instead of plain text
- Server-side validation (`[Required]`, `[Range]`) and clearer error messages
- User login with JWT and role-based access
- Search, sort and pagination
- Low-stock alerts and reports
- Swagger / OpenAPI documentation
- Switch networking to Retrofit and JSON parsing to Gson
- Deploy the API and database to the cloud

---

## Author

**Saloni Paygude**

- GitHub: [saloni-0405](https://github.com/saloni-0405)
- LinkedIn: [saloni-paygude](https://linkedin.com/in/saloni-paygude)

If you like this project, please give it a star on GitHub.
