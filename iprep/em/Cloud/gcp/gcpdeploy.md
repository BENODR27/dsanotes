Deploying Spring Boot microservices with Spring Cloud in **GCP (Google Cloud Platform)** involves:

---

## ✅ OVERVIEW: What We’ll Do

| Step | Action                                          |
| ---- | ----------------------------------------------- |
| 1️⃣  | Create a GCP Project & VM (Ubuntu)              |
| 2️⃣  | Install Java, Git, Maven                        |
| 3️⃣  | Deploy Spring Boot Microservices                |
| 4️⃣  | Set up GCP Cloud DNS with your 3rd-party domain |
| 5️⃣  | Map DNS records to VM IP                        |
| 6️⃣  | (Optional) Enable HTTPS with Let's Encrypt      |

---

# 🔧 STEP 1: Create Ubuntu VM in GCP

### 1. Go to GCP Console:

👉 [https://console.cloud.google.com](https://console.cloud.google.com)

### 2. Create a New Project (if needed)

### 3. Enable Compute Engine

### 4. Create a VM Instance

* **Name:** `springboot-vm`
* **Region:** your preferred region
* **Series:** E2
* **Machine Type:** `e2-medium` or higher
* **Boot disk:** Ubuntu 22.04 LTS
* **Firewall:** Enable HTTP & HTTPS
* **SSH Key:** Use your public key (or auto-generate)

Click **Create**.

---

# 🖥️ STEP 2: Connect and Setup VM

### SSH into VM:

From browser or terminal:

```bash
ssh username@<external-ip>
```

### Install Java, Git, Maven:

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install openjdk-17-jdk maven git -y
```

---

# 🚀 STEP 3: Deploy Spring Boot Microservices

### Option A: Copy `.jar` from local

```bash
scp target/service-a.jar username@<vm-ip>:/home/username/
```

### Option B: Clone from Git + Build on VM

```bash
git clone https://github.com/your-org/your-repo.git
cd service-a
mvn clean package -DskipTests
```

### Run:

```bash
java -jar target/service-a.jar
```

Repeat for other microservices (Eureka, Config, API Gateway, etc.)

---

# 🌐 STEP 4: Configure Custom Domain (Cloud DNS)

### ✅ 4.1 Buy a domain (e.g., GoDaddy, Namecheap)

### ✅ 4.2 Create DNS Zone in GCP

1. Go to **Cloud DNS** → Create Zone
2. Set:

   * **Zone name:** `yourdomain-com`
   * **DNS name:** `yourdomain.com.`
   * Type: Public
3. Click **Create**

### ✅ 4.3 Note GCP name servers

GCP will give 4 NS records:

```
ns-cloud-a1.googledomains.com
ns-cloud-a2.googledomains.com
...
```

---

# 🔁 STEP 5: Point Domain to GCP DNS

Go to your domain registrar (GoDaddy, Namecheap, etc.):

* Update **Name Server (NS)** to the GCP ones
* This connects your domain with GCP DNS

⏳ *Wait for propagation (usually < 1 hour)*

---

# ✏️ STEP 6: Create DNS Records (e.g., A Records)

Go back to Cloud DNS → your zone → **Add record set**

### Example: Point `api.yourdomain.com` to VM IP

* **Name:** `api`
* **Type:** `A`
* **TTL:** `300`
* **IPv4 address:** VM external IP

Repeat for:

* `eureka.yourdomain.com`
* `config.yourdomain.com`
* `gateway.yourdomain.com`

---

# 🔒 STEP 7 (Optional): HTTPS with Let's Encrypt

### Install Nginx:

```bash
sudo apt install nginx -y
```

### Install Certbot:

```bash
sudo apt install certbot python3-certbot-nginx -y
```

### Obtain SSL:

```bash
sudo certbot --nginx -d api.yourdomain.com
```

🛡️ Certbot will:

* Configure Nginx
* Install HTTPS cert
* Auto-renew every 90 days

---

# 📦 OPTIONAL: Auto-Start with systemd

```bash
sudo nano /etc/systemd/system/service-a.service
```

Paste:

```ini
[Unit]
Description=Service A
After=network.target

[Service]
User=username
ExecStart=/usr/bin/java -jar /home/username/service-a.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

Then run:

```bash
sudo systemctl daemon-reexec
sudo systemctl enable service-a
sudo systemctl start service-a
```

---

## ✅ Summary of Endpoints

| URL                      | Maps To       |
| ------------------------ | ------------- |
| `eureka.yourdomain.com`  | Eureka server |
| `config.yourdomain.com`  | Config server |
| `gateway.yourdomain.com` | API Gateway   |
| `api.yourdomain.com`     | Service A/B/C |

---

Would you like:

* A Terraform or Bash script to automate this entire GCP setup?
* Dockerized version with deployment to GCP Compute Engine or Cloud Run?
* Kubernetes (GKE) version for full Spring Cloud microservices setup?

Let me know!
