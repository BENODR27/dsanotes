Great! Let's now move to **Dockerized Spring Boot Microservices deployed on Kubernetes in AWS (EKS)** — the full **professional step-by-step deployment guide**, including:

* 🐳 Docker
* ☁️ AWS EKS (Kubernetes)
* 🕸️ Spring Cloud Microservices
* 🌐 Custom Domain with Route 53
* 🔐 HTTPS via NGINX Ingress + Cert Manager (optional)

---

## 🔭 OVERVIEW

| Step | Task                                                  |
| ---- | ----------------------------------------------------- |
| 1️⃣  | Build Docker images of Spring Boot microservices      |
| 2️⃣  | Push images to AWS Elastic Container Registry (ECR)   |
| 3️⃣  | Create EKS Cluster (via AWS Console or eksctl)        |
| 4️⃣  | Set up `kubectl` access                               |
| 5️⃣  | Create Kubernetes YAML files (Deployments + Services) |
| 6️⃣  | Apply configurations                                  |
| 7️⃣  | Expose with Ingress Controller (NGINX)                |
| 8️⃣  | Route 53 setup with custom domain                     |
| 9️⃣  | Add HTTPS with Cert Manager (optional)                |

---

## ⚙️ STEP 1: Dockerize Your Spring Boot Microservices

Each service (e.g., `eureka`, `config`, `service-a`, `gateway`) must have:

### `Dockerfile`:

```dockerfile
FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Build and tag Docker images:

```bash
docker build -t eureka-service .
```

---

## ☁️ STEP 2: Push Images to AWS ECR

### 1. Create ECR Repo:

```bash
aws ecr create-repository --repository-name eureka-service
```

### 2. Authenticate:

```bash
aws ecr get-login-password | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.<region>.amazonaws.com
```

### 3. Tag and push:

```bash
docker tag eureka-service:latest <AWS_ACCOUNT_ID>.dkr.ecr.<region>.amazonaws.com/eureka-service
docker push <AWS_ACCOUNT_ID>.dkr.ecr.<region>.amazonaws.com/eureka-service
```

Repeat for other services.

---

## ☸️ STEP 3: Create EKS Cluster

Install `eksctl`:

```bash
brew install eksctl   # macOS
# OR
curl --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /usr/local/bin
```

### Create EKS Cluster:

```bash
eksctl create cluster \
--name springboot-cluster \
--region us-east-1 \
--nodegroup-name workers \
--node-type t3.medium \
--nodes 3
```

---

## 🔁 STEP 4: Configure `kubectl`

```bash
aws eks --region us-east-1 update-kubeconfig --name springboot-cluster
kubectl get nodes
```

---

## 📄 STEP 5: Create Kubernetes YAMLs

### Example: `eureka-deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka
spec:
  replicas: 1
  selector:
    matchLabels:
      app: eureka
  template:
    metadata:
      labels:
        app: eureka
    spec:
      containers:
        - name: eureka
          image: <ECR_IMAGE_URL>
          ports:
            - containerPort: 8761
---
apiVersion: v1
kind: Service
metadata:
  name: eureka-service
spec:
  type: ClusterIP
  selector:
    app: eureka
  ports:
    - port: 8761
      targetPort: 8761
```

Repeat for:

* `config-server`
* `gateway-service`
* `service-a`, etc.

---

## 🌐 STEP 6: Install NGINX Ingress Controller

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.9.4/deploy/static/provider/aws/deploy.yaml
```

Wait and verify:

```bash
kubectl get pods -n ingress-nginx
```

---

## 📁 STEP 7: Configure Ingress Resource

### ingress.yaml:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: app-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx
  rules:
    - host: gateway.yourdomain.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: gateway-service
                port:
                  number: 8080
```

Apply:

```bash
kubectl apply -f ingress.yaml
```

---

## 🔁 STEP 8: Setup Route 53

1. Go to **Route 53 → Hosted Zones**
2. Add an **A Record**:

   * Name: `gateway.yourdomain.com`
   * Type: A
   * Target: ELB DNS from `kubectl get svc -n ingress-nginx`

---

## 🔐 STEP 9 (Optional): Enable HTTPS with Cert Manager

### 1. Install Cert Manager:

```bash
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.14.3/cert-manager.yaml
```

### 2. ClusterIssuer:

```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
spec:
  acme:
    server: https://acme-v02.api.letsencrypt.org/directory
    email: your-email@example.com
    privateKeySecretRef:
      name: letsencrypt-prod
    solvers:
      - http01:
          ingress:
            class: nginx
```

Apply:

```bash
kubectl apply -f clusterissuer.yaml
```

### 3. Secure Ingress:

Add TLS block to your `ingress.yaml`:

```yaml
tls:
  - hosts:
      - gateway.yourdomain.com
    secretName: gateway-tls
```

Annotate Ingress:

```yaml
metadata:
  annotations:
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
```

---

## ✅ Final Access URLs

| Service       | URL                                                              |
| ------------- | ---------------------------------------------------------------- |
| Eureka        | [http://eureka.yourdomain.com](http://eureka.yourdomain.com)     |
| Config Server | [http://config.yourdomain.com](http://config.yourdomain.com)     |
| Gateway       | [https://gateway.yourdomain.com](https://gateway.yourdomain.com) |
| API Service   | [https://api.yourdomain.com](https://api.yourdomain.com)         |

---

## 🛠️ CI/CD + GitHub Actions (Optional)

Would you like me to generate:

* A **GitHub Actions workflow** to build & push images to ECR?
* Automatically deploy to EKS via `kubectl`?

Let me know and I’ll prepare that next.
 