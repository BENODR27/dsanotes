Setting up **multiple `application.properties` (or `.yml`) files** for different environments (like `dev`, `test`, `staging`, `prod`) in a **Spring Boot eCommerce enterprise project** is a **professional best practice** that ensures flexibility, security, and scalability.

Here’s a **step-by-step guide** to set it up professionally:

---

## ✅ Step 1: Base Project Structure

Assume your project is a Maven or Gradle-based Spring Boot project.

```
ecommerce-backend/
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           ├── application.properties         <-- Base (default)
│           ├── application-dev.properties     <-- Development
│           ├── application-test.properties    <-- Testing
│           ├── application-stage.properties   <-- Staging
│           ├── application-prod.properties    <-- Production
```

---

## ✅ Step 2: Configure `application.properties`

```properties
# application.properties (default fallback)
spring.profiles.active=dev  # Default environment
```

---

## ✅ Step 3: Create Environment-Specific Properties

### 🟡 application-dev.properties

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/ecommercedb_dev
spring.datasource.username=devuser
spring.datasource.password=devpass
spring.jpa.hibernate.ddl-auto=update
logging.level.root=DEBUG
```

### 🟠 application-test.properties

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/ecommercedb_test
spring.datasource.username=testuser
spring.datasource.password=testpass
spring.jpa.hibernate.ddl-auto=create-drop
logging.level.root=INFO
```

### 🔵 application-stage.properties

```properties
server.port=8082
spring.datasource.url=jdbc:mysql://staging-host:3306/ecommercedb_stage
spring.datasource.username=stageuser
spring.datasource.password=stagepass
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=INFO
```

### 🔴 application-prod.properties

```properties
server.port=8083
spring.datasource.url=jdbc:mysql://prod-host:3306/ecommercedb_prod
spring.datasource.username=produser
spring.datasource.password=prodpass
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=ERROR
management.endpoints.web.exposure.include=health,info
```

---

## ✅ Step 4: Activate Profiles

You can activate a profile in **3 professional ways**:

### 1. 🌱 Local Dev (via application.properties)

```properties
spring.profiles.active=dev
```

---

### 2. 🧪 Command Line (great for Jenkins, Docker, Kubernetes, etc.)

```bash
java -jar ecommerce-backend.jar --spring.profiles.active=prod
```

---

### 3. 🐳 Docker or CI/CD (environment variable)

```bash
SPRING_PROFILES_ACTIVE=stage
```

Or in `docker-compose.yml`:

```yaml
environment:
  - SPRING_PROFILES_ACTIVE=prod
```

---

## ✅ Step 5: Use `@Profile` Annotation in Code (optional but powerful)

```java
@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public DataSource devDataSource() {
        // return custom DataSource bean
    }
}
```

---

## ✅ Step 6: (Optional) Use YAML Instead of Properties

If you prefer YAML:

```yaml
# application.yml (default)
spring:
  profiles:
    active: dev

---
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:mysql://localhost:3306/ecommercedb_dev
    username: devuser
    password: devpass
---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:mysql://prod-host:3306/ecommercedb_prod
    username: produser
    password: prodpass
```

---

## ✅ Step 7: Secure Secrets (Recommended for Enterprise)

Never hardcode passwords in properties/yml.

### 🔐 Use environment variables:

```properties
spring.datasource.password=${DB_PASSWORD}
```

Set the `DB_PASSWORD` in your OS environment, `.env`, or secrets manager (e.g., AWS Secrets Manager, Vault).

---

## ✅ Step 8: Professional CI/CD Integration

- Pass `--spring.profiles.active=prod` in your CI/CD pipeline.
- Use encrypted secrets in GitHub Actions, GitLab, Jenkins, etc.
- Use different `.env` or secrets per environment in Docker/Kubernetes.

---

Here's how to **professionally use `.env` file** to externalize sensitive data like `DB_PASSWORD` and access it in **Spring Boot** using `${...}` placeholders.

---

## ✅ Step-by-Step Guide to Use `.env` in Spring Boot with `application.properties`

---

### 🔹 Step 1: Create a `.env` File in the Project Root

```
ecommerce-backend/
├── .env
├── src/
│   └── main/
│       └── resources/
│           └── application-dev.properties
```

```env
# .env
DB_URL=jdbc:mysql://localhost:3306/ecommercedb_dev
DB_USERNAME=devuser
DB_PASSWORD=devpass
```

---

### 🔹 Step 2: Update `application-dev.properties` to Use Placeholders

```properties
# application-dev.properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

---

### 🔹 Step 3: Load the `.env` in Spring Boot (Java 11+ Professional Method)

Spring Boot **does not automatically load `.env`** like Node.js.

#### ✅ Option A: Use [**`dotenv-java`**](https://github.com/cdimascio/dotenv-java)

1. **Add dependency** (Maven):

```xml
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
```

2. **Load `.env` variables before Spring Boot runs**:

```java
public class EcommerceBackendApplication {
    public static void main(String[] args) {
        // Load .env variables
        io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(EcommerceBackendApplication.class, args);
    }
}
```

✅ This will allow `${DB_PASSWORD}`, `${DB_URL}`, etc., in your `application-*.properties` files to be replaced properly.

---

### 🔹 Step 4: Set Active Profile

```properties
# application.properties (root fallback)
spring.profiles.active=dev
```

Or via command line:

```bash
SPRING_PROFILES_ACTIVE=dev java -jar target/ecommerce-backend.jar
```

---

### 🔐 Bonus: .env in Docker / Kubernetes

If deploying with Docker:

```yaml
# docker-compose.yml
services:
  ecommerce-app:
    build: .
    env_file:
      - .env
    environment:
      - SPRING_PROFILES_ACTIVE=prod
```

In Kubernetes:

```yaml
envFrom:
  - configMapRef:
      name: ecommerce-config
  - secretRef:
      name: ecommerce-secrets
```

---

## ✅ Summary

| Component                  | Description                                    |
| -------------------------- | ---------------------------------------------- |
| `.env` file                | Stores environment variables securely          |
| `application-*.properties` | Uses `${VAR_NAME}` syntax for dynamic loading  |
| `dotenv-java`              | Loads `.env` into system properties for Spring |
| `System.setProperty()`     | Ensures Spring picks them up at runtime        |

---

Here's a **full working Spring Boot demo setup** using `.env` for managing environment-specific variables professionally — **with `application-dev.properties` and `dotenv-java` integration**, and including a `docker-compose.yml` for deployment.

---

## ✅ 1. Project Structure

```
ecommerce-backend/
├── .env
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/example/ecommerce/
        │       └── EcommerceApplication.java
        └── resources/
            ├── application.properties
            └── application-dev.properties
```

---

## ✅ 2. `.env` File

```env
DB_URL=jdbc:mysql://mysql:3306/ecommercedb_dev
DB_USERNAME=devuser
DB_PASSWORD=devpass
```

---

## ✅ 3. `application.properties`

```properties
# Default profile
spring.profiles.active=dev
```

---

## ✅ 4. `application-dev.properties`

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
```

---

## ✅ 5. `pom.xml` (with `dotenv-java`)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>ecommerce-backend</artifactId>
  <version>1.0.0</version>
  <dependencies>
    <!-- Spring Boot Starter Dependencies -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>runtime</scope>
    </dependency>
    <!-- dotenv-java for .env loading -->
    <dependency>
      <groupId>io.github.cdimascio</groupId>
      <artifactId>dotenv-java</artifactId>
      <version>3.0.0</version>
    </dependency>
  </dependencies>
</project>
```

---

## ✅ 6. `EcommerceApplication.java`

```java
package com.example.ecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        // Load .env variables into System properties
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

---

## ✅ 7. `Dockerfile`

```Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ecommerce-backend.jar app.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## ✅ 8. `docker-compose.yml`

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8
    container_name: ecommerce-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: ecommercedb_dev
      MYSQL_USER: devuser
      MYSQL_PASSWORD: devpass
    ports:
      - "3306:3306"

  ecommerce-app:
    build: .
    container_name: ecommerce-backend
    depends_on:
      - mysql
    env_file:
      - .env
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
```

---

## ✅ 9. Build & Run Commands

```bash
# Build the Spring Boot JAR
./mvnw clean package -DskipTests

# Build and run Docker containers
docker-compose up --build
```

---

## 🔐 Note on Security

For production, `.env` secrets should be:

- Encrypted or stored in secret managers (e.g., AWS Secrets Manager, HashiCorp Vault)
- Not committed to Git (`.env` should be in `.gitignore`)

---

Absolutely! Here's a comprehensive guide to setting up a **CI/CD pipeline using GitHub Actions** and deploying your **Spring Boot eCommerce application** to **Kubernetes**, with secure management of secrets using **Kubernetes Secrets** and **ConfigMaps**.

---

## 🔐 Secure Secrets Management in Kubernetes

To manage sensitive data like database credentials securely:

### 1. **Create Kubernetes Secrets**

Encode your credentials in base64:

```bash
echo -n 'devuser' | base64  # Output: ZGV2dXNlcg==
echo -n 'devpass' | base64  # Output: ZGV2cGFzcw==
```

Create a `secret.yaml` file:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: ecommerce-secrets
type: Opaque
data:
  DB_USERNAME: ZGV2dXNlcg==
  DB_PASSWORD: ZGV2cGFzcw==
```

Apply the secret:

```bash
kubectl apply -f secret.yaml
```

### 2. **Create a ConfigMap for Non-Sensitive Configurations**

Create a `configmap.yaml` file:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ecommerce-config
data:
  DB_URL: jdbc:mysql://mysql:3306/ecommercedb_dev
  SPRING_PROFILES_ACTIVE: dev
```

Apply the ConfigMap:

```bash
kubectl apply -f configmap.yaml
```

### 3. **Configure Deployment to Use Secrets and ConfigMap**

In your `deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ecommerce-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ecommerce-app
  template:
    metadata:
      labels:
        app: ecommerce-app
    spec:
      containers:
        - name: ecommerce-app
          image: your-dockerhub-username/ecommerce-backend:latest
          ports:
            - containerPort: 8080
          env:
            - name: DB_USERNAME
              valueFrom:
                secretKeyRef:
                  name: ecommerce-secrets
                  key: DB_USERNAME
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: ecommerce-secrets
                  key: DB_PASSWORD
            - name: DB_URL
              valueFrom:
                configMapKeyRef:
                  name: ecommerce-config
                  key: DB_URL
            - name: SPRING_PROFILES_ACTIVE
              valueFrom:
                configMapKeyRef:
                  name: ecommerce-config
                  key: SPRING_PROFILES_ACTIVE
```

In your `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}
```

---

## 🚀 Setting Up CI/CD with GitHub Actions

### 1. **Create GitHub Secrets**

In your GitHub repository, navigate to **Settings > Secrets and variables > Actions**, and add the following secrets:

- `DOCKER_USERNAME`: Your Docker Hub username
- `DOCKER_PASSWORD`: Your Docker Hub password or access token
- `KUBE_CONFIG_DATA`: Base64-encoded content of your Kubernetes `config` file

To encode your Kubernetes config:

```bash
cat ~/.kube/config | base64
```

### 2. **Define GitHub Actions Workflow**

Create a `.github/workflows/deploy.yml` file in your repository:

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Log in to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and Push Docker Image
        run: |
          docker build -t ${{ secrets.DOCKER_USERNAME }}/ecommerce-backend:latest .
          docker push ${{ secrets.DOCKER_USERNAME }}/ecommerce-backend:latest

      - name: Set up Kubernetes
        run: |
          echo "${{ secrets.KUBE_CONFIG_DATA }}" | base64 --decode > $HOME/.kube/config

      - name: Deploy to Kubernetes
        run: |
          kubectl apply -f k8s/deployment.yaml
          kubectl apply -f k8s/service.yaml
```

Ensure your `deployment.yaml` and `service.yaml` files are located in the `k8s/` directory of your repository.

---

## 🛡️ Best Practices

- **Avoid Hardcoding Secrets**: Always use Kubernetes Secrets and GitHub Secrets to manage sensitive information.
- **Use ConfigMaps for Non-Sensitive Configurations**: Separate your application's configuration from the codebase.
- **Restrict Access**: Implement Role-Based Access Control (RBAC) in Kubernetes to limit access to secrets.
- **Regularly Rotate Secrets**: Update your secrets periodically to enhance security.
- **Monitor and Audit**: Keep track of changes and access to your secrets and configurations.

---

## 📘 Additional Resources

- [Using Secrets in GitHub Actions](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Configuring Spring Boot on Kubernetes with Secrets](https://developers.redhat.com/blog/2017/10/04/configuring-spring-boot-kubernetes-secrets)
- [Spring Cloud Kubernetes - Secrets PropertySource](https://docs.spring.io/spring-cloud-kubernetes/reference/property-source-config/secrets-propertysource.html)

---

Absolutely! Here's a comprehensive guide to setting up a **CI/CD pipeline using GitHub Actions** for deploying your **Spring Boot eCommerce application** to **Kubernetes** on major cloud providers: **AWS (EKS)**, **Google Cloud (GKE)**, and **Azure (AKS)**. Each setup ensures secure management of secrets and configurations.

---

## 🚀 CI/CD Pipeline Overview

For each cloud provider, the CI/CD pipeline will:

1. **Build** the Spring Boot application using Maven.
2. **Dockerize** the application.
3. **Push** the Docker image to the respective container registry.
4. **Deploy** the application to the Kubernetes cluster.

---

## 🔐 Managing Secrets and Configurations

- **GitHub Secrets**: Store sensitive information like cloud credentials, registry credentials, and other secrets securely in your GitHub repository settings.
- **Kubernetes Secrets and ConfigMaps**: Manage application-specific configurations and secrets within your Kubernetes cluster.

---

## ☁️ Deployment to AWS EKS

### Prerequisites

- AWS account with EKS cluster set up.
- IAM user with appropriate permissions.
- Docker Hub or Amazon ECR repository.
- GitHub repository with your Spring Boot application.

### GitHub Secrets

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION`
- `ECR_REGISTRY` (e.g., `123456789012.dkr.ecr.us-west-2.amazonaws.com`)
- `ECR_REPOSITORY` (e.g., `ecommerce-backend`)

### GitHub Actions Workflow (`.github/workflows/deploy-eks.yml`)

```yaml
name: Deploy to AWS EKS

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Configure AWS Credentials
        uses: aws-actions/configure-aws-credentials@v2
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ secrets.AWS_REGION }}

      - name: Login to Amazon ECR
        run: |
          aws ecr get-login-password --region ${{ secrets.AWS_REGION }} | docker login --username AWS --password-stdin ${{ secrets.ECR_REGISTRY }}

      - name: Build, Tag, and Push Docker Image
        run: |
          docker build -t ${{ secrets.ECR_REGISTRY }}/${{ secrets.ECR_REPOSITORY }}:latest .
          docker push ${{ secrets.ECR_REGISTRY }}/${{ secrets.ECR_REPOSITORY }}:latest

      - name: Update Kubernetes Deployment
        run: |
          kubectl set image deployment/ecommerce-backend ecommerce-backend=${{ secrets.ECR_REGISTRY }}/${{ secrets.ECR_REPOSITORY }}:latest
```

---

## ☁️ Deployment to Google Cloud GKE

### Prerequisites

- Google Cloud account with GKE cluster set up.
- Service account with appropriate permissions.
- Docker Hub or Google Container Registry (GCR) repository.
- GitHub repository with your Spring Boot application.

### GitHub Secrets

- `GCP_PROJECT_ID`
- `GCP_SA_KEY` (Base64-encoded JSON key of the service account)

### GitHub Actions Workflow (`.github/workflows/deploy-gke.yml`)

```yaml
name: Deploy to GCP GKE

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Set up Cloud SDK
        uses: google-github-actions/setup-gcloud@v1
        with:
          project_id: ${{ secrets.GCP_PROJECT_ID }}
          service_account_key: ${{ secrets.GCP_SA_KEY }}
          export_default_credentials: true

      - name: Configure Docker for GCR
        run: gcloud auth configure-docker

      - name: Build, Tag, and Push Docker Image
        run: |
          docker build -t gcr.io/${{ secrets.GCP_PROJECT_ID }}/ecommerce-backend:latest .
          docker push gcr.io/${{ secrets.GCP_PROJECT_ID }}/ecommerce-backend:latest

      - name: Set up GKE Credentials
        run: |
          gcloud container clusters get-credentials your-cluster-name --zone your-cluster-zone --project ${{ secrets.GCP_PROJECT_ID }}

      - name: Update Kubernetes Deployment
        run: |
          kubectl set image deployment/ecommerce-backend ecommerce-backend=gcr.io/${{ secrets.GCP_PROJECT_ID }}/ecommerce-backend:latest
```

---

## ☁️ Deployment to Azure AKS

### Prerequisites

- Azure account with AKS cluster set up.
- Azure Container Registry (ACR) repository.
- GitHub repository with your Spring Boot application.

### GitHub Secrets

- `AZURE_CREDENTIALS` (JSON output from `az ad sp create-for-rbac`)
- `ACR_LOGIN_SERVER` (e.g., `yourregistry.azurecr.io`)
- `ACR_USERNAME`
- `ACR_PASSWORD`

### GitHub Actions Workflow (`.github/workflows/deploy-aks.yml`)

```yaml
name: Deploy to Azure AKS

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Build with Maven
        run: mvn clean package -DskipTests

      - name: Login to Azure
        uses: azure/login@v1
        with:
          creds: ${{ secrets.AZURE_CREDENTIALS }}

      - name: Login to ACR
        uses: azure/docker-login@v1
        with:
          login-server: ${{ secrets.ACR_LOGIN_SERVER }}
          username: ${{ secrets.ACR_USERNAME }}
          password: ${{ secrets.ACR_PASSWORD }}

      - name: Build, Tag, and Push Docker Image
        run: |
          docker build -t ${{ secrets.ACR_LOGIN_SERVER }}/ecommerce-backend:latest .
          docker push ${{ secrets.ACR_LOGIN_SERVER }}/ecommerce-backend:latest

      - name: Set up AKS Credentials
        run: |
          az aks get-credentials --resource-group your-resource-group --name your-aks-cluster

      - name: Update Kubernetes Deployment
        run: |
          kubectl set image deployment/ecommerce-backend ecommerce-backend=${{ secrets.ACR_LOGIN_SERVER }}/ecommerce-backend:latest
```

---

## 📘 Additional Resources

- **AWS EKS Deployment Guide**: [Deploying a Spring Boot Application to AWS EKS using Terraform, Amazon ECR, and GitHub Actions](https://rifkhan107.medium.com/deploying-a-spring-boot-application-to-aws-eks-using-terraform-amazon-ecr-and-github-actions-9edd71c2c3ab)
- **GCP GKE Deployment Guide**: [Deploying to Google Kubernetes Engine - GitHub Docs](https://docs.github.com/en/actions/use-cases-and-examples/deploying/deploying-to-google-kubernetes-engine)
- **Azure AKS Deployment Guide**: [Deploy a Spring Boot Application to an Azure Kubernetes Cluster using GitHub Actions](https://medium.com/@paul.jaworski/deploy-a-spring-boot-application-to-an-azure-kubernetes-cluster-using-github-actions-56bfd5181e18)

---

Great! Here's how to **set up Helm charts** for your Spring Boot eCommerce project and **integrate Prometheus and Grafana** for monitoring, step by step.

---

## ✅ Step 1: Setup Helm for Spring Boot Deployment

### 1. Install Helm (on your local or CI/CD agent)

```bash
brew install helm  # for macOS
# OR
choco install kubernetes-helm  # for Windows
```

### 2. Create Helm Chart for Your App

```bash
helm create ecommerce-app
```

This will generate a structure like:

```
ecommerce-app/
  ├── Chart.yaml
  ├── values.yaml
  ├── templates/
       ├── deployment.yaml
       ├── service.yaml
       └── ingress.yaml
```

### 3. Customize `values.yaml`

```yaml
image:
  repository: your-registry/ecommerce-backend
  tag: latest
  pullPolicy: IfNotPresent

replicaCount: 2

service:
  type: ClusterIP
  port: 8080

resources:
  limits:
    cpu: 500m
    memory: 512Mi
  requests:
    cpu: 250m
    memory: 256Mi
```

### 4. Modify `deployment.yaml`

Update image and ports using Helm template values:

```yaml
containers:
  - name: { { .Chart.Name } }
    image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
    ports:
      - containerPort: { { .Values.service.port } }
```

### 5. Install Helm Chart

```bash
helm install ecommerce-release ./ecommerce-app
```

---

## 📈 Step 2: Install Prometheus and Grafana with Helm

### 1. Add Prometheus & Grafana Repos

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
```

### 2. Install Prometheus

```bash
helm install prometheus prometheus-community/prometheus
```

### 3. Install Grafana

```bash
helm install grafana grafana/grafana \
  --set adminPassword='admin' \
  --set service.type=NodePort
```

> **Tip**: Use `--set persistence.enabled=true` to enable persistent dashboards.

### 4. Access Grafana

Find the port:

```bash
kubectl get svc grafana
```

Port-forward (optional):

```bash
kubectl port-forward svc/grafana 3000:80
```

Then visit: [http://localhost:3000](http://localhost:3000)
Login: `admin` / `admin`

---

## 📊 Step 3: Configure Spring Boot App for Prometheus Metrics

1. **Add Dependencies in `pom.xml`**

```xml
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

2. **Enable Actuator Metrics**

```yaml
# application.properties or YAML
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.prometheus.enabled=true
management.metrics.export.prometheus.enabled=true
```

3. **Check Metrics Endpoint**

```bash
curl http://<your-app-service>:8080/actuator/prometheus
```

---

## 🔗 Step 4: Connect Prometheus to Your App

Edit Prometheus config (via `values.yaml` override or config map):

```yaml
scrape_configs:
  - job_name: "spring-boot"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["<app-service-name>:8080"]
```

Apply it, then Prometheus will start collecting metrics.

---

## 📈 Step 5: Setup Grafana Dashboards

1. Login to Grafana.
2. Add **Prometheus** as a data source (`http://prometheus-server`).
3. Import dashboard ID `4701` (Spring Boot / Micrometer) from Grafana.com.

---

Would you like me to generate a complete sample Helm chart folder or YAML files ready for use in your project?
