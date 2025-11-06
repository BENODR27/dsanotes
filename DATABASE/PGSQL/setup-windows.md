Here’s a **step-by-step guide** to set up **PostgreSQL (Postgres) on Windows** and run it locally:

---

### 🧩 **Step 1: Download PostgreSQL**

1. Go to the official PostgreSQL website:
   👉 [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)
2. Click on **“Download the installer”** (provided by **EDB**).
3. Choose the latest stable version (e.g., PostgreSQL 16 or 17) and click **Download**.

---

### ⚙️ **Step 2: Run the Installer**

1. Run the downloaded `.exe` file.

2. Follow the setup wizard:

   - **Installation Directory:**
     Keep the default or choose your preferred folder (e.g., `C:\Program Files\PostgreSQL\17`)
   - **Components:**
     Keep all selected (PostgreSQL Server, pgAdmin, StackBuilder, Command Line Tools)
   - **Data Directory:**
     Usually defaults to something like `C:\Program Files\PostgreSQL\17\data`
   - **Password:**
     Set a strong password for the **postgres** superuser (you’ll need this later!)
   - **Port:**
     Default is **5432** (you can change it, but 5432 is standard)
   - **Locale:**
     Leave as default.

3. Click **Next → Next → Finish** to complete installation.

---

### 🧠 **Step 3: Verify Installation**

1. Open **pgAdmin** (it’s installed with Postgres).
2. It will prompt you for the **master password** (the one you set during installation).
3. You should see your **PostgreSQL server** in the left sidebar (e.g., “PostgreSQL 17”).

If it’s not connected:

- Right-click the server → **Connect Server**
- Enter your password → ✅ You’re in.

---

### 💻 **Step 4: Verify via Command Line**

1. Open **Command Prompt** or **PowerShell**.
2. Run:

   ```bash
   psql -U postgres
   ```

3. When prompted, enter your password.
4. You should see the Postgres shell:

   ```
   postgres=#
   ```

   That means your server is running locally.

---

### 🧱 **Step 5: Create a New Database (Optional)**

Inside the `psql` shell, run:

```sql
CREATE DATABASE testdb;
\c testdb
```

Now you’re connected to your new database.

---

### 🧰 **Step 6: (Optional) Add PostgreSQL to PATH**

If `psql` doesn’t work in Command Prompt:

1. Open **Environment Variables**:

   - Press **Win + S** → search “Environment Variables”.
   - Click **Edit the system environment variables** → **Environment Variables**.

2. Under **System Variables**, find **Path**, click **Edit** → **New**.
3. Add:

   ```
   C:\Program Files\PostgreSQL\17\bin
   ```

4. Click **OK** → restart your Command Prompt.

Now `psql` will work globally.

---

### 🧑‍💻 **Step 7: Connect via a GUI (Optional)**

You can also use:

- **pgAdmin** (installed with Postgres)
- **DBeaver**, **DataGrip**, or **TablePlus**

Use:

```
Host: localhost
Port: 5432
Username: postgres
Password: <your_password>
Database: postgres (or any)
```

---

✅ **Done!**
You now have PostgreSQL running locally on your Windows machine.

---

Would you like me to show how to **connect your local Postgres to a Node.js / Python app** next?
