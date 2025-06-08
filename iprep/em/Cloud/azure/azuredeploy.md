Sure! Here’s a **step-by-step guide to create an Ubuntu environment in Microsoft Azure and deploy your Spring Boot microservices with Spring Cloud**.

---

# Part 1: Create Ubuntu VM in Azure

### Step 1: Sign in to Azure Portal

* Go to [https://portal.azure.com](https://portal.azure.com)
* Login with your Microsoft Azure account.

---

### Step 2: Create a Virtual Machine (VM)

1. Click **Create a resource** (top left).
2. Search for **Ubuntu Server** and select the latest LTS version (e.g., Ubuntu 22.04 LTS).
3. Click **Create**.

---

### Step 3: Configure VM Basics

* **Subscription:** Choose your subscription.
* **Resource Group:** Create new or select existing.
* **Virtual machine name:** e.g., `springboot-vm`
* **Region:** Choose your preferred region.
* **Image:** Ubuntu Server 22.04 LTS (default)
* **Size:** Choose a size (e.g., Standard B1s or higher depending on your need)
* **Authentication type:** Choose **SSH public key** (recommended) or **Password**.
* If SSH key, upload your public SSH key (e.g., from your local `~/.ssh/id_rsa.pub`).
* **Username:** e.g., `azureuser`

---

### Step 4: Configure Disks, Networking, and other settings

* Use defaults or customize (disk type SSD recommended).
* **Inbound port rules:** Select **Allow selected ports** and check **SSH (22)**. Also add your microservices ports later (optional, can add in Network Security Group after VM creation).

---

### Step 5: Review + Create

* Review all settings.
* Click **Create**.
* Wait for VM deployment to complete.

---

### Step 6: Connect to Ubuntu VM via SSH

From your local machine terminal:

```bash
ssh azureuser@<your-vm-public-ip>
```

Example:

```bash
ssh azureuser@52.174.23.45
```

---

# Part 2: Setup Ubuntu environment on Azure VM

Once connected to your VM terminal:

1. Update packages:

```bash
sudo apt update && sudo apt upgrade -y
```

2. Install Java (OpenJDK 17):

```bash
sudo apt install openjdk-17-jdk -y
```

3. Install Maven (if building on VM):

```bash
sudo apt install maven -y
```

4. (Optional) Install Git to clone repositories:

```bash
sudo apt install git -y
```

---

# Part 3: Deploy Your Spring Boot Microservices

### Option 1: Copy JAR files from local machine to VM

On your local machine, copy your built microservices jar files using `scp`:

```bash
scp target/service-a-0.0.1-SNAPSHOT.jar azureuser@<vm-ip>:/home/azureuser/
```

Repeat for other microservices.

---

### Option 2: Clone source code on VM and build there

If you have Git repo:

```bash
git clone https://github.com/yourusername/yourrepo.git
cd yourrepo/service-a
mvn clean package -DskipTests
```

---

### Step 4: Run microservices on VM

Run the microservices jar files:

```bash
java -jar service-a-0.0.1-SNAPSHOT.jar
```

Run your Eureka Server, Config Server, other microservices, and API Gateway in order.

---

# Part 4: Configure Azure Networking

### Step 1: Open firewall ports (Network Security Group)

1. In Azure Portal, go to your VM resource.
2. Click **Networking** -> **Add inbound port rule**.
3. Add rules for your services, e.g.:

   * Port 8761 (Eureka)
   * Port 8888 (Config Server)
   * Ports 8080, 8081, 8082 (your microservices)
   * Port 80 or 443 (if you expose API Gateway via HTTP/HTTPS)

---

# Part 5: (Optional) Set up systemd for auto start

Create systemd service files on VM for each microservice (as explained earlier), enable and start them so they run on boot.

---

# Part 6: Verify Deployment

* Access your services via `http://<vm-ip>:<port>`
* Check Eureka dashboard at `http://<vm-ip>:8761`
* Access API Gateway routes

---

# Bonus: Automate with Azure CLI (optional)

You can create VM and configure it using Azure CLI:

```bash
az login
az group create --name myResourceGroup --location eastus
az vm create --resource-group myResourceGroup --name springboot-vm --image UbuntuLTS --admin-username azureuser --generate-ssh-keys
az vm open-port --port 8761 --resource-group myResourceGroup --name springboot-vm
az vm open-port --port 8888 --resource-group myResourceGroup --name springboot-vm
# Add other ports as needed
```

---

If you want, I can help you with:

* Scripts to automate microservices deployment on Ubuntu VM
* Dockerizing microservices and deploying containers on Azure VM or Azure Kubernetes Service (AKS)
* Setting up CI/CD pipelines for Azure

Just ask!


To **create a DNS hosting zone in Azure for a custom domain from a third-party provider (e.g., GoDaddy, Namecheap)** and integrate it with your Azure-hosted services (like a VM running Spring Boot microservices), follow these **professional step-by-step instructions**.

---

## ✅ Step 1: Buy a Domain from a Third-Party Provider

If you already own a domain like `myappdomain.com` from GoDaddy, Namecheap, etc., proceed to the next step.

---

## ✅ Step 2: Create a DNS Zone in Azure

### Option A: Azure Portal

1. Go to [Azure Portal](https://portal.azure.com)
2. Search for **DNS zones** → Click **+ Create**
3. Fill the details:

   * **Resource Group**: Choose existing or create new
   * **Name**: `yourdomain.com` (replace with your actual domain)
   * Click **Review + Create**

### Option B: Azure CLI (alternative)

```bash
az network dns zone create --resource-group MyResourceGroup --name yourdomain.com
```

---

## ✅ Step 3: Copy Azure Name Servers

Once the DNS Zone is created:

1. Open the DNS zone (e.g., `yourdomain.com`)
2. Look for **Name servers** (usually something like):

   * `ns1-08.azure-dns.com`
   * `ns2-08.azure-dns.net`
   * `ns3-08.azure-dns.org`
   * `ns4-08.azure-dns.info`

Copy these for the next step.

---

## ✅ Step 4: Update Name Servers on Third-Party Registrar

### Example: GoDaddy

1. Log in to your domain registrar (GoDaddy, Namecheap, etc.)
2. Go to **DNS settings** for your domain
3. Choose **Use custom name servers**
4. Paste Azure name servers (all 4)
5. Save and apply changes

🔁 DNS propagation may take **a few minutes to a few hours** globally.

---

## ✅ Step 5: Add DNS Records to Azure

You need to add **A or CNAME records** in the Azure DNS zone to point to your Azure VM.

### A. Find Your Azure VM’s Public IP

1. Go to Azure Portal → Virtual Machines → Your VM
2. Copy the **public IP address**

### B. Add a DNS Record in Azure DNS Zone

#### Example: Point `api.yourdomain.com` to your VM

1. In your Azure DNS Zone, click **+ Record Set**
2. Fill in:

   * **Name**: `api` (this means `api.yourdomain.com`)
   * **Type**: `A`
   * **TTL**: default (3600)
   * **IP address**: your VM’s public IP
3. Click **OK**

✅ This creates an A record: `api.yourdomain.com` → your VM IP

You can do the same for:

* `www` (e.g., `www.yourdomain.com`)
* root domain with `@`
* `gateway`, `eureka`, etc.

---

## ✅ Step 6: Test DNS Mapping

Use `nslookup` or browser:

```bash
nslookup api.yourdomain.com
```

Or access in browser:

```
http://api.yourdomain.com:8080
```

If you want HTTPS, use Let's Encrypt or Azure Application Gateway.

---

## ✅ Step 7: (Optional) Use Custom Domain with HTTPS

1. Install **Nginx or Apache** as reverse proxy on your VM.
2. Use **Certbot (Let's Encrypt)** to generate a free SSL certificate.

```bash
sudo apt install nginx
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d api.yourdomain.com
```

This will:

* Configure Nginx reverse proxy
* Add SSL certificate
* Auto-renew via cron job

---

## ✅ Example DNS Configuration Summary

| Record Type | Name | Value / IP   | Description                                             |
| ----------- | ---- | ------------ | ------------------------------------------------------- |
| A           | @    | 52.174.23.45 | yourdomain.com → VM IP                                  |
| A           | api  | 52.174.23.45 | api.yourdomain.com → VM IP                              |
| A           | www  | 52.174.23.45 | [www.yourdomain.com](http://www.yourdomain.com) → VM IP |

---

## ✅ Optional: Automate with Azure CLI

```bash
az network dns record-set a add-record \
  --resource-group MyResourceGroup \
  --zone-name yourdomain.com \
  --record-set-name api \
  --ipv4-address 52.174.23.45
```

---

## ✅ Final Notes

* Always restart your services if you change ports or use HTTPS.
* Consider using an **Azure Application Gateway** or **Azure Front Door** for load balancing and WAF.
* For microservices architecture, you can route:

  * `eureka.yourdomain.com`
  * `config.yourdomain.com`
  * `gateway.yourdomain.com`

---

Would you like a script that:

* Automatically deploys your DNS zone and record sets?
* Sets up Nginx reverse proxy with HTTPS for your Spring Boot apps?

Let me know and I’ll generate it for you.
