To **deploy Spring Boot microservices with Spring Cloud using Docker on AWS**, here's a **complete professional step-by-step guide** using:

* 🐳 Dockerized services
* 🧑‍💻 AWS EC2 (Ubuntu)
* 🌐 Route 53 for custom domain
* 🔒 HTTPS via Let’s Encrypt (optional)

---

## ✅ OVERVIEW

| Step | Task                                           |
| ---- | ---------------------------------------------- |
| 1️⃣  | Create EC2 (Ubuntu) and open ports             |
| 2️⃣  | Install Docker & Docker Compose                |
| 3️⃣  | Dockerize your Spring Boot microservices       |
| 4️⃣  | Upload and run containers using Docker Compose |
| 5️⃣  | Set up Route 53 with 3rd-party domain          |
| 6️⃣  | Add A records for each service                 |
| 7️⃣  | (Optional) Use NGINX + Certbot for HTTPS       |

---

## 🔧 STEP 1: Launch Ubuntu EC2 on AWS

1. Go to AWS EC2 Console → Launch Instance
2. Choose:

   * **Ubuntu 22.04**
   * **Instance type**: t2.medium or higher
   * **Storage**: ≥ 20 GB
   * **Key pair**: Select or create one
3. Under **Security Group**, allow:

   * TCP: 22 (SSH), 80 (HTTP), 443 (HTTPS)
   * Custom TCP: 8080, 8761, 8888, etc. as needed
4. Click **Launch**

---

## 🐳 STEP 2: Install Docker & Docker Compose

SSH into EC2:

```bash
ssh -i your-key.pem ubuntu@<EC2_PUBLIC_IP>
```

Install Docker:

```bash
sudo apt update
sudo apt install docker.io -y
sudo systemctl start docker
sudo systemctl enable docker
```

Install Docker Compose:

```bash
sudo apt install docker-compose -y
```

Verify:

```bash
docker --version
docker-compose --version
```

---

## 📦 STEP 3: Dockerize Spring Boot Microservices

In each Spring Boot project (e.g., `eureka-server`, `config-server`, `service-a`, `gateway`), add a `Dockerfile`:

```Dockerfile
FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Build JAR locally:

```bash
mvn clean package -DskipTests
```

---

## 📁 STEP 4: Upload Files to EC2

Upload each microservice folder or JAR file to the EC2:

```bash
scp -i your-key.pem -r ./eureka-server ubuntu@<EC2_PUBLIC_IP>:~/apps/
```

Or zip locally and unzip remotely.

---

## 🧱 STEP 5: Create `docker-compose.yml` (on EC2)

Inside `~/apps`:

```yaml
version: '3.8'

services:
  eureka:
    build: ./eureka-server
    ports:
      - "8761:8761"

  config:
    build: ./config-server
    ports:
      - "8888:8888"
    depends_on:
      - eureka

  service-a:
    build: ./service-a
    ports:
      - "8081:8081"
    depends_on:
      - config
      - eureka

  gateway:
    build: ./gateway
    ports:
      - "8080:8080"
    depends_on:
      - service-a
      - eureka
```

> ✅ **Tip**: Add `restart: always` and environment variables if needed.

---

## ▶️ STEP 6: Build and Run Containers

```bash
cd ~/apps
sudo docker-compose up -d --build
```

Check status:

```bash
sudo docker ps
```

---

## 🌐 STEP 7: Setup Custom Domain with Route 53

### 7.1 Create Hosted Zone

1. Go to **Route 53 → Hosted Zones**
2. Click **Create hosted zone**

   * Domain: `yourdomain.com`
   * Type: Public
3. Note down the **NS records**

---

## 🔁 STEP 8: Point 3rd-Party Domain to AWS

Go to your domain provider (GoDaddy, Namecheap):

* Update **nameservers** to the Route 53 NS records

---

## 🌍 STEP 9: Add A Records to Route 53

For each service:

| Subdomain                | Record Type | Value           |
| ------------------------ | ----------- | --------------- |
| `eureka.yourdomain.com`  | A           | `EC2 public IP` |
| `config.yourdomain.com`  | A           | `EC2 public IP` |
| `api.yourdomain.com`     | A           | `EC2 public IP` |
| `gateway.yourdomain.com` | A           | `EC2 public IP` |

TTL: 300

---

## 🔐 STEP 10 (Optional): HTTPS via NGINX + Certbot

### Install NGINX:

```bash
sudo apt install nginx -y
```

### Configure reverse proxy:

```nginx
# /etc/nginx/sites-available/api.conf
server {
    listen 80;
    server_name api.yourdomain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

Enable and restart:

```bash
sudo ln -s /etc/nginx/sites-available/api.conf /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Install Certbot:

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d api.yourdomain.com
```

---

## ✅ Final Checklist

| Component     | Access URL                                                             |
| ------------- | ---------------------------------------------------------------------- |
| Eureka Server | [http://eureka.yourdomain.com:8761](http://eureka.yourdomain.com:8761) |
| Config Server | [http://config.yourdomain.com:8888](http://config.yourdomain.com:8888) |
| API Gateway   | [http://gateway.yourdomain.com](http://gateway.yourdomain.com)         |
| Service A     | [http://api.yourdomain.com:8081](http://api.yourdomain.com:8081)       |

---

## 🚀 Want to go next-level?

Would you like:

* **Docker + ECS (Elastic Container Service)**
* **Docker + Kubernetes (EKS)**
* **Terraform to automate entire setup**
* **CI/CD using GitHub Actions or Jenkins to deploy Docker images to EC2/ECS**

Let me know and I’ll generate that guide/code next.
