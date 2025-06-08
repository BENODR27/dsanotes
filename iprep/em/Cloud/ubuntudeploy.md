Got it! Here’s a **step-by-step guide to deploy Spring Boot microservices with Spring Cloud on Ubuntu** — covering the essential components and how to run them on your server.

---

# Step 1: Prepare Ubuntu Environment

1. **Update packages**

```bash
sudo apt update
sudo apt upgrade -y
```

2. **Install Java (OpenJDK 17 recommended for Spring Boot 3.x)**

```bash
sudo apt install openjdk-17-jdk -y
```

Verify Java installation:

```bash
java -version
```

3. **Install Maven** (if building from source)

```bash
sudo apt install maven -y
```

Verify Maven:

```bash
mvn -version
```

---

# Step 2: Prepare Your Spring Cloud Microservices

Assuming you have these Spring Boot microservices:

* **Eureka Server** (Service Discovery)
* **Config Server** (Externalized Configuration)
* **Microservice A** and **Microservice B** (business logic)
* **API Gateway** (Spring Cloud Gateway or Zuul)
* (Optional) **Spring Cloud Sleuth / Zipkin** for tracing

---

# Step 3: Build Your Microservices

If you have your source code on Ubuntu:

Navigate to each service directory and run:

```bash
mvn clean package -DskipTests
```

This will create a runnable `.jar` file in `target/`.

Example:

```bash
cd /home/ubuntu/microservices/eureka-server
mvn clean package -DskipTests
```

Repeat for other microservices.

---

# Step 4: Run Spring Cloud Services in Order

You need to start **Eureka Server** first, then **Config Server**, followed by your microservices and API Gateway.

Example commands:

```bash
java -jar /home/ubuntu/microservices/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
```

In a new terminal (or better, use systemd or screen/tmux to run in background):

```bash
java -jar /home/ubuntu/microservices/config-server/target/config-server-0.0.1-SNAPSHOT.jar
```

Then start your microservices, e.g.,

```bash
java -jar /home/ubuntu/microservices/service-a/target/service-a-0.0.1-SNAPSHOT.jar
java -jar /home/ubuntu/microservices/service-b/target/service-b-0.0.1-SNAPSHOT.jar
```

Finally, start your API Gateway:

```bash
java -jar /home/ubuntu/microservices/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
```

---

# Step 5: Optional - Run services in background

Use `nohup` or `screen` to keep services running after logout.

Example with `nohup`:

```bash
nohup java -jar eureka-server-0.0.1-SNAPSHOT.jar > eureka.log 2>&1 &
```

Or install and use `screen`:

```bash
sudo apt install screen
screen -S eureka
java -jar eureka-server-0.0.1-SNAPSHOT.jar
# Press Ctrl+A then D to detach
```

---

# Step 6: Automate with systemd Service (Recommended for Production)

Create a systemd service file for each microservice, e.g., `/etc/systemd/system/eureka.service`

```ini
[Unit]
Description=Eureka Server
After=network.target

[Service]
User=ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/microservices/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Then:

```bash
sudo systemctl daemon-reload
sudo systemctl start eureka
sudo systemctl enable eureka
```

Repeat for each service, changing names and paths accordingly.

---

# Step 7: Open Ports on Firewall (if enabled)

Check if UFW is active:

```bash
sudo ufw status
```

Allow necessary ports (e.g., 8761 for Eureka, 8888 for Config Server, your microservices ports):

```bash
sudo ufw allow 8761/tcp
sudo ufw allow 8888/tcp
sudo ufw allow 8081/tcp
sudo ufw allow 8082/tcp
sudo ufw allow 8080/tcp
```

---

# Step 8: Verify Deployment

* Visit Eureka UI at `http://your-server-ip:8761`
* Check that microservices are registered.
* Access your API Gateway or microservices endpoints.

---

# Bonus Tips

* Use **Docker** to containerize your microservices and deploy with Docker Compose or Kubernetes on Ubuntu for easier management.
* Use **Spring Profiles** (`application-dev.yml`, `application-prod.yml`) to separate configurations.
* Monitor logs via `journalctl -u eureka.service -f` or log files.
* Secure endpoints (Spring Security, OAuth2) as needed.

---

If you want, I can help you create:

* Sample systemd service files for your microservices,
* Example Dockerfiles and Docker Compose setup,
* Or commands/scripts for automating the whole deployment.

Just ask!
