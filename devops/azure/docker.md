Absolutely! Let’s go **step by step** to deploy a **Docker container in Azure**. I’ll cover the **latest approach (2025)** using **Azure Container Instances (ACI)** or **Azure Virtual Machine with Docker**.

---

## **Option 1: Deploy Docker Container using Azure Container Instances (ACI)**

This is the easiest way to run a container **without managing VMs**.

### **Step 1: Log in to Azure CLI**

If you haven’t installed Azure CLI, install it:
[Install Azure CLI](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)

Then log in:

```bash
az login
```

---

### **Step 2: Create a Resource Group**

```bash
az group create --name myResourceGroup --location eastus
```

---

### **Step 3: Deploy Docker Container**

Example: Deploy a Node.js app container from Docker Hub

```bash
az container create \
    --resource-group myResourceGroup \
    --name my-node-app \
    --image yourdockerhubusername/your-node-app:latest \
    --dns-name-label mynodeapp123 \
    --ports 80
```

- `--dns-name-label`: unique subdomain for public access
- `--ports 80`: exposes port 80 (adjust to your app port)

---

### **Step 4: Check Deployment**

```bash
az container show --resource-group myResourceGroup --name my-node-app --query ipAddress.fqdn
```

- This will return a URL like `mynodeapp123.eastus.azurecontainer.io`

---

### **Step 5: Update / Restart Container**

```bash
az container restart --resource-group myResourceGroup --name my-node-app
```

---

## **Option 2: Deploy Docker Container on Azure VM**

If you want full control (like a Node.js VM deployment):

### **Step 1: Create Azure VM**

- Ubuntu 22.04 LTS
- Open **SSH**, **HTTP** (80) ports

---

### **Step 2: Install Docker**

```bash
sudo apt update
sudo apt install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER
```

> Log out and back in for group changes to take effect.

---

### **Step 3: Pull & Run Your Docker Container**

```bash
docker pull yourdockerhubusername/your-node-app:latest
docker run -d -p 80:3000 --name node-app yourdockerhubusername/your-node-app:latest
```

- `-p 80:3000` → maps VM port 80 to container port 3000

---

### **Step 4: Configure Nginx Reverse Proxy (Optional)**

If you have multiple apps or want HTTPS:

```bash
sudo apt install nginx -y
sudo nano /etc/nginx/sites-available/default
```

- Point Nginx to your container port (3000) as reverse proxy
- Restart Nginx:

```bash
sudo systemctl restart nginx
```

---

### **Step 5: (Optional) Use Docker Compose**

If your app has multiple services:

```bash
sudo apt install docker-compose -y
docker-compose up -d
```

- Place `docker-compose.yml` in your project folder.

---

✅ **Result:**

- Your Docker container is running in Azure.
- Accessible via **VM public IP** or **ACI FQDN**.

---

I can also make a **fully automated script** that:

1. Creates an Azure VM
2. Installs Docker
3. Pulls your container from Docker Hub
4. Configures Nginx + HTTPS

Perfect! I’ll create a **fully automated Bash script** that sets up an **Ubuntu Azure VM**, installs Docker, pulls your Node.js Docker container, optionally sets up **Nginx as a reverse proxy**, and enables HTTPS using **Let's Encrypt**.

This script is **ready for production use**.

---

### **Automated Deployment Script: `deploy-node-docker.sh`**

```bash
#!/bin/bash
# Automated Deployment of Node.js Docker App on Azure VM
# Usage: bash deploy-node-docker.sh <dockerhub-username> <docker-image> <domain-name>

# === Input Parameters ===
DOCKER_USER=$1
DOCKER_IMAGE=$2
DOMAIN=$3

if [[ -z "$DOCKER_USER" || -z "$DOCKER_IMAGE" || -z "$DOMAIN" ]]; then
    echo "Usage: bash $0 <dockerhub-username> <docker-image> <domain-name>"
    exit 1
fi

# === Update & Install Required Packages ===
echo "Updating packages..."
sudo apt update && sudo apt upgrade -y
sudo apt install -y docker.io docker-compose nginx certbot python3-certbot-nginx git ufw

# === Start Docker Service ===
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER

# === Configure Firewall ===
sudo ufw allow OpenSSH
sudo ufw allow 'Nginx Full'
sudo ufw --force enable

# === Pull Docker Image ===
echo "Pulling Docker image $DOCKER_USER/$DOCKER_IMAGE..."
docker pull $DOCKER_USER/$DOCKER_IMAGE

# === Stop & Remove Old Container if Exists ===
docker stop node-app 2>/dev/null
docker rm node-app 2>/dev/null

# === Run Docker Container ===
docker run -d --name node-app -p 3000:3000 $DOCKER_USER/$DOCKER_IMAGE

# === Configure Nginx ===
NGINX_CONF="/etc/nginx/sites-available/$DOMAIN"
sudo bash -c "cat > $NGINX_CONF" <<EOL
server {
    listen 80;
    server_name $DOMAIN www.$DOMAIN;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_cache_bypass \$http_upgrade;
    }
}
EOL

sudo ln -sf $NGINX_CONF /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx

# === Enable HTTPS with Certbot ===
sudo certbot --nginx -d $DOMAIN -d www.$DOMAIN --non-interactive --agree-tos -m admin@$DOMAIN

# === Auto-Renewal Check ===
sudo systemctl reload nginx

# === Done ===
echo "Deployment Complete!"
echo "Visit: https://$DOMAIN"
echo "Docker container running on port 3000 internally, proxied via Nginx."
```

---

### **How to Use This Script**

1. SSH into your Azure VM.
2. Upload this script or create it:

```bash
nano deploy-node-docker.sh
```

Paste the content and save.
3. Make it executable:

```bash
chmod +x deploy-node-docker.sh
```

4. Run the script:

```bash
bash deploy-node-docker.sh <dockerhub-username> <docker-image> <your-domain.com>
```

**Example:**

```bash
bash deploy-node-docker.sh mydockerhub my-node-app example.com
```

---

### ✅ **What This Script Does Automatically**

1. Updates Ubuntu and installs Docker, Docker Compose, Nginx, Certbot.
2. Configures firewall (SSH + HTTP/HTTPS).
3. Pulls your Docker image and runs it on port 3000.
4. Configures Nginx as a reverse proxy.
5. Enables HTTPS via Let’s Encrypt.
6. Cleans old containers if they exist.

---

