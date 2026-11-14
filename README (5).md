# StockMate

Full-stack inventory management application built with an **Android (Java)** app, an **ASP.NET Core Web API** backend and a **MySQL** database.

StockMate helps a small business keep track of its products and stock from a mobile phone. The Android app talks to the Web API over HTTP (JSON), and the API stores everything in MySQL.

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
| Mobile app | Android, Java, Android Studio |
| Backend | ASP.NET Core Web API (C#) |
| Database | MySQL |
| Data format | JSON over REST |
| Tools | Git, GitHub, Postman, MySQL Workbench, Visual Studio |

---

## Architecture

```
+----------------+      HTTP / JSON      +---------------------+      SQL      +-----------+
|  Android App   |  <----------------->  |  ASP.NET Core API   |  <--------->  |   MySQL   |
|  (Java)        |                       |  (StockMateBackend) |               | stockmate_db |
+----------------+                       +---------------------+               +-----------+
```

1. The user performs an action in the Android app (for example, viewing products).
2. The app sends a REST request to the ASP.NET Core Web API.
3. The API reads or writes data in the `stockmate_db` MySQL database.
4. The API returns a JSON response and the app displays it.

---

## Features

<!-- EDIT: keep only the features your app really has, and add any that are missing. -->

- Add, view, update and delete products
- Track stock quantity and prices
- REST API with clean separation between the mobile app and the backend
- MySQL database for persistent storage
- Simple and easy-to-use Android interface

---

## Project Structure

```
StockMate/
├── StockMate/            # Android app (Java)
├── StockMateBackend/     # ASP.NET Core Web API (C#)
├── project db.sql        # MySQL database script
└── .gitignore
```

---

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (with an emulator or a physical Android device)
- [.NET SDK](https://dotnet.microsoft.com/download) (the version your backend project targets)
- [MySQL Server](https://dev.mysql.com/downloads/) and MySQL Workbench
- Git

### 1. Clone the repository

```bash
git clone https://github.com/saloni-0405/StockMate.git
cd StockMate
```

### 2. Set up the database

1. Start MySQL Server.
2. Open MySQL Workbench and run the script `project db.sql` to create the `stockmate_db` database.
3. Make sure the `Products` table exists (created by the script or by the backend migrations).

### 3. Run the backend (ASP.NET Core Web API)

1. Open the `StockMateBackend` folder in Visual Studio (or VS Code).
2. Open `appsettings.json` and set your MySQL connection string:

   ```json
   "ConnectionStrings": {
     "DefaultConnection": "server=localhost;port=3306;database=stockmate_db;user=YOUR_USER;password=YOUR_PASSWORD"
   }
   ```

3. Run the API:

   ```bash
   cd StockMateBackend
   dotnet restore
   dotnet run
   ```

4. Note the URL printed in the terminal (for example `http://localhost:5000`). You can test it in Postman or in the browser.

### 4. Run the Android app

1. Open the `StockMate` folder in Android Studio and wait for Gradle sync to finish.
2. Set the API base URL in the app's network/config class:
   - Android emulator: `http://10.0.2.2:<port>/`
   - Physical device: `http://<your-computer-ip>:<port>/` (phone and computer must be on the same Wi-Fi)
3. Click **Run** and choose an emulator or your device.

---

## API Endpoints

<!-- EDIT: replace with the routes in your controllers. -->

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get one product |
| POST | `/api/products` | Add a new product |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |

Example request body (`POST /api/products`):

```json
{
  "name": "Notebook",
  "quantity": 50,
  "price": 40.0
}
```

---

## Screenshots

<!-- EDIT: add screenshots to a "screenshots" folder in the repo, then link them like this: -->
<!-- ![Home screen](screenshots/home.png) -->

_Screenshots coming soon._

---

## Future Improvements

- User login and role-based access
- Low-stock alerts and notifications
- Search, filter and sort for products
- Reports and sales analytics
- Barcode scanning
- Cloud deployment of the API and database

---

## Author

**Saloni Paygude**

- GitHub: [saloni-0405](https://github.com/saloni-0405)
- LinkedIn: [saloni-paygude](https://linkedin.com/in/saloni-paygude)

If you like this project, please give it a star on GitHub.
