# 🛒 Foodcity Grocery Stock Billing System

Welcome to the official setup guide! My goal is to help you get this project running on your computer in **less than 5 minutes**, even if you have never used Docker before.

---

## Why we used Docker? Why not SQL manually? This is relatable of DevOps and Reliable Software Engineering.

---

![WhaleDockerGIF](https://github.com/user-attachments/assets/aadb0432-3c9c-4932-9038-7a0ba0f00f1c)



---

## ⚙️ Here are the 3 Professional Reasons:

1. "Environment Consistency" (The 'No Errors' Argument)
Tell them: "Even if we both use MySQL, your version might be 5.7 and mine might be 8.4, or your settings might be different from mine. By using Docker, I am providing the exact same environment. It ensures that 'It works on my machine' means 'It works on yours too' without any version errors."

2. "Automation & Efficiency" (The 'One Click' Argument)
Tell them: "Manually executing SQL commands in Workbench requires me to manually create the database, name it correctly, and set the right characterset. Docker automates the entire infrastructure. With one command (docker compose up), the server is started, the database is created, and the data is imported instantly. It’s faster and reduces human error."

3. "Clean Development Environment" (The 'Isolation' Argument)
Tell them: "Docker keeps the project isolated. If I use a normal local SQL database, it might conflict with other projects or services running on your computer. With Docker, when the lecturer is done marking the project, they can just stop the container, and their computer stays clean. No database 'trash' is left behind."

---
💡 The "Secret" Technical Detail:
---

If they press you and say "But isn't it just running the same SQL script?"

You say:

"Exactly! But Docker is the engine that makes sure that script runs in a perfectly configured environment with the correct username, password, and port (3306) every single time, without me having to tell the user what those settings are."

Think of it like this:

SQL Scripts are the "Food Recipes."
Workbench is "Doing the cooking manually."
Docker is the "Instant Chef" that comes with its own kitchen, ingredients, and stove to make sure the dish is served perfectly without you lifting a finger. 👨‍🍳

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
