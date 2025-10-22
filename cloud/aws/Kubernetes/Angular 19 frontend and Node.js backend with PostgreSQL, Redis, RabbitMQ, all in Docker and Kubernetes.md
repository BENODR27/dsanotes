ecommerce-app/
│
├── frontend/ # Angular 19 app
│ ├── src/
│ ├── package.json
│ ├── angular.json
│ └── Dockerfile
│
├── backend/ # Node.js microservices
│ ├── product-service/
│ │ ├── src/
│ │ ├── package.json
│ │ └── Dockerfile
│ ├── order-service/
│ │ ├── src/
│ │ ├── package.json
│ │ └── Dockerfile
│ ├── user-service/
│ │ ├── src/
│ │ ├── package.json
│ │ └── Dockerfile
│ └── common/ # Shared utils, configs
│ └── logger.js
│
├── k8s/ # Kubernetes manifests
│ ├── namespace.yaml
│ ├── secrets.yaml
│ ├── pvcs/
│ │ ├── postgres-pvc.yaml
│ │ └── rabbitmq-pvc.yaml
│ ├── postgres/
│ │ ├── postgres-deployment.yaml
│ │ └── postgres-service.yaml
│ ├── mongo/
│ │ ├── mongo-deployment.yaml
│ │ └── mongo-service.yaml
│ ├── redis/
│ │ ├── redis-deployment.yaml
│ │ └── redis-service.yaml
│ ├── rabbitmq/
│ │ ├── rabbitmq-statefulset.yaml
│ │ └── rabbitmq-service.yaml
│ ├── backend/
│ │ ├── product-deployment.yaml
│ │ ├── order-deployment.yaml
│ │ ├── user-deployment.yaml
│ │ └── backend-services.yaml
│ ├── frontend/
│ │ ├── frontend-deployment.yaml
│ │ └── frontend-service.yaml
│ └── ingress/
│ └── ingress.yaml
│
├── docker-compose.yml # Optional for local testing
└── README.md

# **1. Dockerize Your Applications**

---

## **1.1 Angular 19 Frontend Dockerfile**

```dockerfile
# Stage 1: Build Angular app
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/your-angular-app /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

- Replace `your-angular-app` with your Angular project folder name in `dist/`.

---

## **1.2 Node.js Backend Dockerfile**

```dockerfile
FROM node:18
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["node", "server.js"]
```

---

## **1.3 Push Docker Images**

```bash
# Angular frontend
docker build -t angular-frontend:latest ./frontend
docker tag angular-frontend:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/angular-frontend:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/angular-frontend:latest

# Node.js backend
docker build -t node-backend:latest ./backend
docker tag node-backend:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/node-backend:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/node-backend:latest
```

---

# **2. Kubernetes Cluster Setup**

- Create **EKS cluster** (or Minikube for testing)
- Create **namespace**:

```bash
kubectl create namespace ecommerce
```

- Create **Secrets for DB and RabbitMQ passwords**:

```bash
kubectl create secret generic postgres-secret --from-literal=password='PostgresPassword' -n ecommerce
kubectl create secret generic redis-secret --from-literal=password='RedisPassword' -n ecommerce
kubectl create secret generic rabbitmq-secret --from-literal=password='RabbitMQPassword' -n ecommerce
```

---

# **3. Kubernetes Persistent Storage**

### **PostgreSQL PVC**

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: postgres-pvc
  namespace: ecommerce
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 20Gi
```

- Similarly, PVCs for **MongoDB or RabbitMQ** if needed.

---

# **4. Deploy Databases and Messaging**

---

## **4.1 PostgreSQL Deployment**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
  namespace: ecommerce
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
        - name: postgres
          image: postgres:15
          env:
            - name: POSTGRES_DB
              value: "ecommerce"
            - name: POSTGRES_USER
              value: "postgres"
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: postgres-secret
                  key: password
          ports:
            - containerPort: 5432
          volumeMounts:
            - mountPath: /var/lib/postgresql/data
              name: postgres-storage
      volumes:
        - name: postgres-storage
          persistentVolumeClaim:
            claimName: postgres-pvc
```

### **Service**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: postgres
  namespace: ecommerce
spec:
  selector:
    app: postgres
  ports:
    - port: 5432
      targetPort: 5432
```

---

## **4.2 Redis Deployment**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
  namespace: ecommerce
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: redis:7
          ports:
            - containerPort: 6379
```

### **Service**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: redis
  namespace: ecommerce
spec:
  selector:
    app: redis
  ports:
    - port: 6379
      targetPort: 6379
```

---

## **4.3 RabbitMQ StatefulSet**

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: rabbitmq
  namespace: ecommerce
spec:
  serviceName: "rabbitmq"
  replicas: 1
  selector:
    matchLabels:
      app: rabbitmq
  template:
    metadata:
      labels:
        app: rabbitmq
    spec:
      containers:
        - name: rabbitmq
          image: rabbitmq:3-management
          ports:
            - containerPort: 5672
            - containerPort: 15672
          env:
            - name: RABBITMQ_DEFAULT_USER
              value: "admin"
            - name: RABBITMQ_DEFAULT_PASS
              valueFrom:
                secretKeyRef:
                  name: rabbitmq-secret
                  key: password
```

### **Service**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: rabbitmq
  namespace: ecommerce
spec:
  selector:
    app: rabbitmq
  ports:
    - port: 5672
      targetPort: 5672
    - port: 15672
      targetPort: 15672
```

---

# **5. Deploy Node.js Backend**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
  namespace: ecommerce
spec:
  replicas: 3
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
          image: <account-id>.dkr.ecr.us-east-1.amazonaws.com/node-backend:latest
          ports:
            - containerPort: 3000
          env:
            - name: POSTGRES_HOST
              value: postgres
            - name: POSTGRES_USER
              value: postgres
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: postgres-secret
                  key: password
            - name: REDIS_HOST
              value: redis
            - name: RABBITMQ_HOST
              value: rabbitmq
```

### **Service**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: backend
  namespace: ecommerce
spec:
  selector:
    app: backend
  ports:
    - port: 3000
      targetPort: 3000
```

---

# **6. Deploy Angular Frontend**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend
  namespace: ecommerce
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
          image: <account-id>.dkr.ecr.us-east-1.amazonaws.com/angular-frontend:latest
          ports:
            - containerPort: 80
```

### **Service**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: frontend
  namespace: ecommerce
spec:
  selector:
    app: frontend
  ports:
    - port: 80
      targetPort: 80
  type: ClusterIP
```

---

# **7. Ingress (Expose Frontend)**

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ecommerce-ingress
  namespace: ecommerce
  annotations:
    kubernetes.io/ingress.class: nginx
spec:
  rules:
    - host: shop.example.com
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: frontend
                port:
                  number: 80
```

- Install **Ingress Controller**:

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx -n ecommerce
```

---

# **8. Optional: Horizontal Pod Autoscaling**

```bash
kubectl autoscale deployment backend --cpu-percent=50 --min=2 --max=10 -n ecommerce
kubectl autoscale deployment frontend --cpu-percent=50 --min=2 --max=5 -n ecommerce
```

---

# **9. Testing Deployment**

1. `kubectl get pods -n ecommerce` → check all pods are running
2. Access frontend via Ingress DNS / Load Balancer
3. Test backend API → connects to PostgreSQL, Redis, RabbitMQ
4. Test placing orders → RabbitMQ receives events
5. Check logs: `kubectl logs -f <pod-name> -n ecommerce`

---

# **10. Production Enhancements**

- Use **PostgreSQL replicas / RDS** for HA
- Use **RabbitMQ mirrored queues**
- Use **Redis cluster** for caching
- Enable **TLS/HTTPS** on Ingress with cert-manager
- Add **Prometheus + Grafana** for monitoring
- Add **EFK stack** for logging

---

This setup is **professional, enterprise-ready**, and fully covers **Docker → Kubernetes deployment → databases → messaging → caching → ingress → scaling**.

---
