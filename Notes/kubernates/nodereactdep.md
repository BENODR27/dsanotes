### **🚀 Deploy Node.js & React.js on AWS EKS with Kubernetes, Docker, CI/CD, and Autoscaling**  

This **step-by-step** guide will help you deploy **Node.js and React.js** applications on **AWS EKS (Kubernetes)** with **Docker, GitHub Actions (CI/CD), and Autoscaling**.

---

# **🛠️ Prerequisites**
Ensure you have the following tools installed:
1. **AWS CLI** → [Download Here](https://aws.amazon.com/cli/)
2. **Kubectl** (Kubernetes CLI) → [Download Here](https://kubernetes.io/docs/tasks/tools/)
3. **eksctl** (EKS Management CLI) → [Download Here](https://eksctl.io/)
4. **Docker** → [Download Here](https://www.docker.com/)
5. **Git & GitHub Account**  

---

## **Step 1: Set Up AWS EKS Cluster**
1️⃣ **Login to AWS CLI**  
```sh
aws configure
```
Enter:
- **AWS Access Key**  
- **AWS Secret Key**  
- **Region** (e.g., `us-east-1`)

2️⃣ **Create an EKS Cluster & Node Group**  
```sh
eksctl create cluster \
  --name my-cluster \
  --region us-east-1 \
  --nodegroup-name my-nodes \
  --node-type t3.medium \
  --nodes 2 \
  --nodes-min 2 \
  --nodes-max 10 \
  --managed
```
- Creates a cluster named `my-cluster`
- Uses `t3.medium` EC2 instances
- Enables autoscaling (min 2, max 10 nodes)

---

## **Step 2: Create & Push Docker Images**
### **1️⃣ Create `Dockerfile` for Backend (Node.js)**
Inside `backend/`, create a `Dockerfile`:
```dockerfile
FROM node:18
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 4000
CMD ["node", "server.js"]
```

### **2️⃣ Create `Dockerfile` for Frontend (React.js)**
Inside `frontend/`, create a `Dockerfile`:
```dockerfile
FROM node:18
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npx", "serve", "-s", "build"]
```

### **3️⃣ Build & Push Docker Images to AWS ECR**
1. **Create ECR Repositories**
```sh
aws ecr create-repository --repository-name my-backend
aws ecr create-repository --repository-name my-frontend
```
2. **Authenticate Docker with ECR**
```sh
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com
```
3. **Build & Push Images**
```sh
docker build -t my-backend ./backend
docker tag my-backend:latest <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-backend:latest
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-backend:latest

docker build -t my-frontend ./frontend
docker tag my-frontend:latest <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-frontend:latest
docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-frontend:latest
```

---

## **Step 3: Deploy to Kubernetes (AWS EKS)**
### **1️⃣ Create Kubernetes Deployment for Backend**
Create `backend-deployment.yaml`:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-backend:latest
        ports:
        - containerPort: 4000
---
apiVersion: v1
kind: Service
metadata:
  name: backend-service
spec:
  selector:
    app: backend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 4000
  type: LoadBalancer
```

### **2️⃣ Create Kubernetes Deployment for Frontend**
Create `frontend-deployment.yaml`:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: frontend
        image: <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-frontend:latest
        ports:
        - containerPort: 3000
---
apiVersion: v1
kind: Service
metadata:
  name: frontend-service
spec:
  selector:
    app: frontend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 3000
  type: LoadBalancer
```

### **3️⃣ Apply Deployments**
```sh
kubectl apply -f backend-deployment.yaml
kubectl apply -f frontend-deployment.yaml
```

---

## **Step 4: Enable Autoscaling (HPA & Cluster Autoscaler)**
### **1️⃣ Install Metrics Server**
```sh
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

### **2️⃣ Set Up Horizontal Pod Autoscaler (HPA)**
```sh
kubectl autoscale deployment backend --cpu-percent=50 --min=2 --max=100
kubectl autoscale deployment frontend --cpu-percent=50 --min=2 --max=100
```

### **3️⃣ Set Up Cluster Autoscaler**
```sh
kubectl apply -f https://raw.githubusercontent.com/kubernetes/autoscaler/master/cluster-autoscaler/deploy/cluster-autoscaler-autodiscover.yaml
```

---

## **Step 5: Configure GitHub Actions for CI/CD**
Create `.github/workflows/deploy.yml`:
```yaml
name: Deploy to AWS EKS (Production)

on:
  push:
    branches:
      - production

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v3

      - name: Configure AWS Credentials
        run: |
          aws configure set aws_access_key_id ${{ secrets.AWS_ACCESS_KEY }}
          aws configure set aws_secret_access_key ${{ secrets.AWS_SECRET_KEY }}
          aws configure set default.region us-east-1

      - name: Build & Push Docker Images
        run: |
          docker build -t my-backend ./backend
          docker tag my-backend:latest <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-backend:latest
          docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-backend:latest

          docker build -t my-frontend ./frontend
          docker tag my-frontend:latest <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-frontend:latest
          docker push <AWS_ACCOUNT_ID>.dkr.ecr.us-east-1.amazonaws.com/my-frontend:latest

      - name: Deploy to EKS
        run: |
          kubectl apply -f backend-deployment.yaml
          kubectl apply -f frontend-deployment.yaml
          kubectl rollout status deployment/backend
          kubectl rollout status deployment/frontend
```

---

## **🎯 Final Summary**
✔ **AWS EKS for Kubernetes cluster**  
✔ **Dockerized Node.js & React.js apps**  
✔ **CI/CD with GitHub Actions**  
✔ **HPA for autoscaling pods**  
✔ **Cluster Autoscaler for EC2 instances**  

🔥 **Now, your application scales from 20 to 1000 users automatically!** 🚀 Let me know if you need any modifications! 😊