# 🛒 Foodcity Grocery Stock Billing System

Welcome to the official setup guide! My goal is to help you get this project running on your computer in **less than 5 minutes**, even if you have never used Docker before.

---

## 🛠 Prerequisites (One-time Setup)

Before we start, you need to have a few tools installed:

1. **JDK 17 or higher** (To run the Java code).
2. **NetBeans IDE** (To open the project).
3. **[Docker Desktop](https://www.docker.com/products/docker-desktop/)** (This is our "Magic Database Engine").

---

## 🗺 The 3-Step Roadmap

### Step 1: Start the "Magic" Database Engine

Instead of manually creating tables and importing SQL files, we use **Docker**. Docker will create a "Container" (a mini virtual server) that contains the exact MySQL version and data we need.

1. Open a **Command Prompt** or **Terminal** in this project folder.
2. Type the following command and press Enter:
   ```bash
   docker compose up -d
   ```
3. **What is happening?** Docker is reading the `docker-compose.yml` file, starting a MySQL server, creating the database named `foodcity_db`, and importing all your tables from `database_schema.sql` automatically.

### Step 2: Fix the "Port 3306" Conflict (IMPORTANT)

**If you already have MySQL installed on your computer, please read this.**
Both your local MySQL and our Docker MySQL want to use the same "door" (Port 3306). Only one can use it at a time.

- Open **Services** on Windows (search for "Services" in the Start menu).
- Find **MySQL80** (or similar).
- Right-click it and select **Stop**.
- Now run the Docker command from Step 1 again.

### Step 3: Run the Java App

1. Open the project in **NetBeans**.
2. Right-click the project -> **Properties** -> **Libraries** -> Ensure all JARs from the `/Lib` folder are added.
3. Run the project! It is already configured to find the Docker database using:
   - **Port:** 3306
   - **Password:** `VBNM2006`

---

## 🔍 How to see the Data (Optional)

If you want to use **MySQL Workbench** to see the tables inside Docker:

1. Open Workbench.
2. Create a **New Connection**.
3. Hostname: `localhost` | Port: `3306` | Password: `VBNM2006`.
4. Click **Connect**. You will see all the Foodcity tables there!

---

## 💡 Troubleshooting

- **"I want to start fresh"**: If you mess up the data and want a clean start, run:
  `docker compose down -v` then `docker compose up -d`.
- **"Docker command not found"**: Ensure Docker Desktop is actually running in your system tray (the little whale icon).
