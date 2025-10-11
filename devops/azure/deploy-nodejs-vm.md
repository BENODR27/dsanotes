Absolutely! Let’s go **step by step** to deploy a Node.js + Express app on an **Azure Virtual Machine (VM)**. I’ll cover the **latest approach in 2025** with full setup, from VM creation to running your app.

---

## **Step 1: Create an Azure VM**

1. **Log in to Azure Portal**: [https://portal.azure.com](https://portal.azure.com)

2. **Create VM**:

   - **Resource Group**: Create new or use existing.
   - **VM Name**: e.g., `node-express-vm`
   - **Region**: Choose the nearest region.
   - **Image**: Choose `Ubuntu 22.04 LTS` (recommended for Node.js).
   - **Size**: e.g., `Standard B1s` for testing.
   - **Authentication**: Use SSH public key (preferred) or password.
   - **Inbound Ports**: Allow `SSH (22)` and later `HTTP (80)`.

3. Click **Review + Create** and then **Create**.

---

## **Step 2: Connect to the VM**

1. Open your terminal (Linux/macOS) or **PowerShell / WSL** (Windows):

   ```bash
   ssh azureuser@<VM_PUBLIC_IP>
   ```

   Replace `azureuser` with your username and `<VM_PUBLIC_IP>` with the VM’s public IP.

2. Update packages:

   ```bash
   sudo apt update && sudo apt upgrade -y
   ```

---

## **Step 3: Install Node.js and NPM**

1. Install **Node.js (LTS latest)** using NodeSource:

   ```bash
   curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -
   sudo apt-get install -y nodejs
   ```

2. Verify installation:

   ```bash
   node -v
   npm -v
   ```

---

## **Step 4: Install PM2 (Process Manager)**

PM2 keeps your Node.js app running even after the terminal closes or VM restarts.

```bash
sudo npm install -g pm2
pm2 -v
```

---

## **Step 5: Upload or Clone Your Node.js App**

1. **Option 1: Git Clone**

   ```bash
   sudo apt install git -y
   git clone <your-repo-url>
   cd <your-repo-folder>
   npm install
   ```

2. **Option 2: SCP Upload**

   ```bash
   scp -r ./my-node-app azureuser@<VM_PUBLIC_IP>:~/my-node-app
   ```

---

## **Step 6: Start App with PM2**

```bash
pm2 start index.js --name myapp
pm2 save
pm2 startup
```

- `index.js` → your main entry file.
- `pm2 startup` ensures the app restarts on VM reboot.

---

## **Step 7: Setup Nginx as Reverse Proxy (Optional but Recommended)**

1. Install Nginx:

   ```bash
   sudo apt install nginx -y
   ```

2. Configure Nginx:

   ```bash
   sudo nano /etc/nginx/sites-available/default
   ```

   Replace content with:

   ```nginx
   server {
       listen 80;

       server_name <YOUR_DOMAIN_OR_IP>;

       location / {
           proxy_pass http://localhost:3000;
           proxy_http_version 1.1;
           proxy_set_header Upgrade $http_upgrade;
           proxy_set_header Connection 'upgrade';
           proxy_set_header Host $host;
           proxy_cache_bypass $http_upgrade;
       }
   }
   ```

   - Replace `3000` with your Express app port.

3. Test Nginx config and restart:

   ```bash
   sudo nginx -t
   sudo systemctl restart nginx
   sudo ufw allow 'Nginx Full'
   ```

---

## **Step 8: Open Ports in Azure VM**

1. Go to your **VM → Networking → Inbound Port Rules**
2. Add a rule:

   - **Port**: 80 (HTTP)
   - **Source**: Any

3. Optional: Also open 443 for HTTPS.

---

## **Step 9: Test Your App**

- Open browser and navigate to:

  ```
  http://<VM_PUBLIC_IP>
  ```

- You should see your Express app running.

---

## **Step 10: (Optional) Setup SSL with Let’s Encrypt**

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d yourdomain.com
sudo systemctl reload nginx
```

---

✅ **Your Node.js + Express app is now running on Azure VM!**

It will survive reboots (thanks to PM2) and can be accessed via HTTP/HTTPS.

---
