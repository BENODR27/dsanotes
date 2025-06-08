Here is a **complete professional step-by-step guide** to deploy your **Spring Boot microservices with Spring Cloud** on **AWS**, including **domain integration with Route 53** for a **third-party domain** (e.g., GoDaddy, Namecheap).

---

## ✅ OVERVIEW

| Step | Task                                             |
| ---- | ------------------------------------------------ |
| 1️⃣  | Launch Ubuntu EC2 instance                       |
| 2️⃣  | Install Java, Git, Maven                         |
| 3️⃣  | Deploy Spring Boot microservices                 |
| 4️⃣  | Configure a custom domain using Route 53         |
| 5️⃣  | Update third-party domain nameservers            |
| 6️⃣  | Map DNS records to EC2                           |
| 7️⃣  | (Optional) Use HTTPS via Let’s Encrypt (Certbot) |

---

## 🔧 STEP 1: Launch Ubuntu EC2 Instance

1. Go to [EC2 Dashboard](https://console.aws.amazon.com/ec2/)
2. Click **Launch instance**
3. Fill in details:

   * **Name**: `springboot-vm`
   * **AMI**: Ubuntu 22.04 LTS
   * **Instance type**: t2.medium (or higher)
   * **Key pair**: Create new or select existing
   * **Network settings**:

     * Allow ports: SSH (22), HTTP (80), HTTPS (443), and custom (e.g., 8080)
   * **Storage**: Minimum 20 GB
4. Click **Launch instance**

---

## 🖥️ STEP 2: Install Java, Git, Maven

SSH into your EC2 instance:

```bash
ssh -i your-key.pem ubuntu@<EC2_PUBLIC_IP>
```

Install:

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install openjdk-17-jdk maven git -y
```

Verify:

```bash
java -version
mvn -version
```

---

## 🚀 STEP 3: Deploy Spring Boot Microservices

### Option 1: Upload `.jar`

```bash
scp -i your-key.pem target/service-a.jar ubuntu@<EC2_PUBLIC_IP>:/home/ubuntu/
```

### Option 2: Git clone & build

```bash
git clone https://github.com/your-org/your-repo.git
cd service-a
mvn clean package -DskipTests
```

### Run:

```bash
java -jar target/service-a.jar
```

Repeat for Config Server, Eureka Server, Gateway, etc.

---

## 🌐 STEP 4: Set Up Route 53 for Custom Domain

### 4.1 Open AWS Route 53 → Click **Hosted Zones**

* Click **Create hosted zone**
* **Domain name**: yourdomain.com
* Type: **Public hosted zone**
* Click **Create**

You’ll get 4 **name servers (NS)** like:

```
ns-123.awsdns-XX.org
ns-456.awsdns-XX.co.uk
...
```

---

## 🔁 STEP 5: Update Nameservers on Third-Party Registrar

Go to your domain registrar (GoDaddy, Namecheap, etc.):

1. Go to DNS/Nameserver settings
2. Select **Use custom nameservers**
3. Paste AWS Route 53 nameservers
4. Save and wait for propagation (usually under 1 hour)

---

## 🧭 STEP 6: Create A Records in Route 53

Go to Hosted Zone → yourdomain.com → **Create record**

### Example: Point `api.yourdomain.com` to EC2

| Field       | Value              |
| ----------- | ------------------ |
| Record name | `api`              |
| Record type | `A - IPv4 address` |
| Value       | your EC2 public IP |
| TTL         | 300                |

Repeat for:

* `eureka.yourdomain.com`
* `config.yourdomain.com`
* `gateway.yourdomain.com`

---

## 🔒 STEP 7: Enable HTTPS with Certbot (Optional)

### Install Nginx

```bash
sudo apt install nginx -y
```

### Install Certbot

```bash
sudo apt install certbot python3-certbot-nginx -y
```

### Run Certbot

```bash
sudo certbot --nginx -d api.yourdomain.com
```

Certbot:

* Auto-generates HTTPS cert
* Configures Nginx
* Auto-renews every 60–90 days

---

## 🛠️ STEP 8: (Optional) Systemd for Auto Start

Create a service file:

```bash
sudo nano /etc/systemd/system/service-a.service
```

```ini
[Unit]
Description=Spring Boot Service A
After=network.target

[Service]
User=ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/service-a.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

Enable:

```bash
sudo systemctl daemon-reload
sudo systemctl enable service-a
sudo systemctl start service-a
```

---

## ✅ FINAL DOMAIN MAPPINGS

| Subdomain               | Points to            | Example                        |
| ----------------------- | -------------------- | ------------------------------ |
| `eureka.yourdomain.com` | Eureka Server        | `http://eureka.yourdomain.com` |
| `config.yourdomain.com` | Config Server        | `http://config.yourdomain.com` |
| `api.yourdomain.com`    | Gateway or Service A | `http://api.yourdomain.com`    |

---

## ✅ Optional: Automate with CloudFormation / Terraform?

Would you like:

* A Terraform template to provision EC2 + Route 53 records?
* A Docker + ECS Fargate version of the deployment?
* Kubernetes deployment with Spring Cloud + EKS?

Let me know and I’ll generate that next.
