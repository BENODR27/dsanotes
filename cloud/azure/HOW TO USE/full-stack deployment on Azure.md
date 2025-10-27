Got it! You want a **full-stack deployment on Azure** with:

- **Backend**: Spring Boot + Node.js
- **Frontend**: Angular 19 served via Nginx on Ubuntu
- **Database**: MySQL
- **VM**: Azure Ubuntu VM

I’ll break this into **step-by-step instructions**, from creating the VM to deploying all components.

---

## **Step 1: Create an Ubuntu VM in Azure**

1. Go to **Azure Portal → Virtual Machines → Create → Azure Virtual Machine**.
2. **Basics Tab**:

   - Subscription & Resource Group
   - VM Name (e.g., `fullstack-vm`)
   - Region: Choose closest to users
   - Image: `Ubuntu 22.04 LTS`
   - Size: Pick based on load (e.g., `Standard B2s` for dev/test)
   - Authentication: SSH Public Key (recommended)
   - Public Inbound Ports: Allow SSH (port 22)

3. **Networking Tab**:

   - Ensure the VM has a **public IP**.
   - NSG should allow **ports for HTTP 80, HTTPS 443, Node.js port (if needed)**.

4. Click **Review + Create → Create**.

---

## **Step 2: Connect to the VM**

```bash
ssh <username>@<public-ip>
```

Update Ubuntu:

```bash
sudo apt update && sudo apt upgrade -y
```

---

## **Step 3: Install Required Software**

### **Install Java (for Spring Boot)**

```bash
sudo apt install openjdk-17-jdk -y
java -version
```

### **Install Node.js (for Node backend & Angular CLI)**

```bash
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs
node -v
npm -v
```

Install Angular CLI globally:

```bash
npm install -g @angular/cli
```

### **Install MySQL**

```bash
sudo apt install mysql-server -y
sudo mysql_secure_installation
```

- Create DB and user:

```sql
CREATE DATABASE mydb;
CREATE USER 'appuser'@'%' IDENTIFIED BY 'StrongPassword';
GRANT ALL PRIVILEGES ON mydb.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
```

- Edit `/etc/mysql/mysql.conf.d/mysqld.cnf` to allow remote connections if needed (`bind-address = 0.0.0.0`)
- Restart MySQL: `sudo systemctl restart mysql`

---

### **Install Nginx**

```bash
sudo apt install nginx -y
sudo systemctl start nginx
sudo systemctl enable nginx
```

---

## **Step 4: Deploy Spring Boot Backend**

1. Copy your `.jar` file to VM using `scp`:

```bash
scp target/myapp.jar <username>@<public-ip>:/home/<username>/
```

2. Run Spring Boot app:

```bash
java -jar myapp.jar --server.port=8080
```

3. Optional: Use **systemd** to run as a service:

```bash
sudo nano /etc/systemd/system/springboot.service
```

```ini
[Unit]
Description=Spring Boot App
After=network.target

[Service]
User=<username>
WorkingDirectory=/home/<username>
ExecStart=/usr/bin/java -jar /home/<username>/myapp.jar
SuccessExitStatus=143
Restart=always

[Install]
WantedBy=multi-user.target
```

Enable service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable springboot
sudo systemctl start springboot
```

---

## **Step 5: Deploy Node.js Backend**

1. Copy Node.js app files to VM.
2. Install dependencies:

```bash
cd node-app
npm install
```

3. Start the app using **PM2** for process management:

```bash
sudo npm install -g pm2
pm2 start app.js --name node-backend
pm2 startup
pm2 save
```

---

## **Step 6: Deploy Angular 19 Frontend**

1. Build Angular app:

```bash
ng build --configuration production
```

- Output will be in `dist/<app-name>/`

2. Copy `dist/<app-name>` to `/var/www/html` (Nginx root):

```bash
sudo cp -r dist/<app-name>/* /var/www/html/
```

3. Configure Nginx (optional, reverse proxy for backend APIs):

```bash
sudo nano /etc/nginx/sites-available/default
```

```nginx
server {
    listen 80;

    root /var/www/html;
    index index.html index.htm;

    location /api/ {
        proxy_pass http://localhost:8080/; # Spring Boot
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

4. Restart Nginx:

```bash
sudo systemctl restart nginx
```

---

## **Step 7: Open Required Ports in NSG**

- Port 80 → HTTP (frontend)
- Port 443 → HTTPS (if using SSL)
- Port 8080 → Spring Boot (optional, if not reverse-proxied)
- Node.js port → optional (or reverse-proxied via Nginx)

---

## **Step 8: Test Deployment**

- Frontend → `http://<public-ip>`
- Backend API → `http://<public-ip>/api/...`

---

✅ **Optional Enhancements**

- Use **Let's Encrypt** for SSL.
- Use **PM2 + systemd** to manage Node.js app.
- Use **Azure Managed Database (Azure Database for MySQL)** instead of local MySQL for production.
- Use **Azure Bastion** for secure SSH/RDP access.

---

I can also create a **diagram showing the full architecture** with **Angular frontend, Nginx, Spring Boot, Node.js backend, MySQL DB on Ubuntu VM**, which is handy for documentation or interviews.

Do you want me to create that diagram?
