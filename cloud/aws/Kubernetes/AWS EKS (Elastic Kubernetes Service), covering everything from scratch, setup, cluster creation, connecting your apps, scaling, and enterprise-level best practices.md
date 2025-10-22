Absolutely! Let’s go **step by step** through **AWS EKS (Elastic Kubernetes Service)**, covering **everything from scratch, setup, cluster creation, connecting your apps, scaling, and enterprise-level best practices**.

---

# **AWS EKS Full Usage Guide**

---

## **Step 0: Prerequisites**

- **AWS Account** with admin access
- **AWS CLI** installed and configured (`aws configure`)
- **kubectl** installed
- **eksctl** installed (simplifies EKS creation)
- **Helm** installed (optional, for managing apps)
- **Docker** installed (for building container images)

---

## **Step 1: Create IAM Role for EKS Cluster**

1. Go to **AWS IAM → Roles → Create Role**
2. Select **EKS → EKS Cluster**
3. Attach policy: `AmazonEKSClusterPolicy`
4. Name it: `EKS-Cluster-Role`
5. Note down the **ARN** (used for cluster creation)

---

## **Step 2: Create IAM Role for Worker Nodes**

1. Go to **IAM → Roles → Create Role**
2. Select **EC2 → EC2 Instance**
3. Attach policies:

   - `AmazonEKSWorkerNodePolicy`
   - `AmazonEC2ContainerRegistryReadOnly`
   - `AmazonEKS_CNI_Policy`

4. Name it: `EKS-Worker-Role`

---

## **Step 3: Create VPC for EKS (Optional)**

EKS needs a VPC with **subnets, route tables, and internet gateways**. You can:

- Use **eksctl default VPC creation**
- Or create **custom VPC** manually via AWS console

**eksctl command (recommended for beginners):**

```bash
eksctl create cluster \
  --name ecommerce-cluster \
  --version 1.27 \
  --region us-east-1 \
  --nodegroup-name standard-nodes \
  --nodes 3 \
  --nodes-min 3 \
  --nodes-max 6 \
  --node-type t3.medium \
  --managed
```

- This creates:

  - EKS cluster
  - Managed node group
  - VPC with subnets
  - Security groups
  - IAM roles attached

---

## **Step 4: Configure kubectl**

After cluster creation:

```bash
aws eks --region us-east-1 update-kubeconfig --name ecommerce-cluster
kubectl get nodes
```

- Verify nodes are **READY**
- Now `kubectl` can manage your cluster

---

## **Step 5: Install EKS Add-ons**

- **VPC CNI Plugin** (for networking)
- **CoreDNS** (service discovery)
- **kube-proxy** (service proxy)
- Managed automatically for new clusters

Optional add-ons:

```bash
eksctl utils associate-iam-oidc-provider --cluster ecommerce-cluster --approve
helm repo add aws-vpc-cni https://aws.github.io/eks-charts
```

---

## **Step 6: Create Namespaces**

```bash
kubectl create namespace ecommerce
kubectl create namespace monitoring
```

- Namespaces help **isolate environments** (dev, staging, prod)

---

## **Step 7: Deploy Persistent Storage**

- Use **EBS** for persistent volumes

**PostgreSQL PVC Example:**

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

- For MongoDB, Redis, RabbitMQ → similar PVCs

---

## **Step 8: Deploy Microservices**

- Build Docker images
- Push to **ECR** (Elastic Container Registry):

```bash
aws ecr create-repository --repository-name order-service
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com
docker tag order-service:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/order-service:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/order-service:latest
```

- Create **Deployment & Service manifests** in Kubernetes
- Apply:

```bash
kubectl apply -f order-service-deployment.yaml
kubectl apply -f order-service-service.yaml
```

- Repeat for **frontend, other microservices, Redis, RabbitMQ, databases**

---

## **Step 9: Set Up Ingress Controller**

- Install **NGINX Ingress** via Helm:

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx -n ecommerce
```

- Create **Ingress resource** to expose services:

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
                name: angular-frontend
                port:
                  number: 80
```

---

## **Step 10: Autoscaling**

- **Horizontal Pod Autoscaler (HPA):**

```bash
kubectl autoscale deployment order-service --cpu-percent=50 --min=2 --max=10 -n ecommerce
```

- **Cluster Autoscaler:** automatically scales EKS nodes

---

## **Step 11: Monitoring & Logging**

- Install **Prometheus + Grafana**:

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/prometheus -n monitoring
```

- **EFK Stack** for logs (Elasticsearch + Fluentd + Kibana)
- Monitor **CPU, memory, Pod health**

---

## **Step 12: Secrets & ConfigMaps**

- Store sensitive configs like DB passwords:

```bash
kubectl create secret generic postgres-secret --from-literal=password='YourPostgresPassword' -n ecommerce
kubectl create configmap order-service-config --from-literal=DB_HOST='postgres' -n ecommerce
```

- Use in Deployments via `envFrom`

---

## **Step 13: CI/CD Integration**

- **Build Docker images** in pipeline
- **Push to ECR**
- **Deploy with kubectl or Helm**
- Automate **rolling updates** → zero downtime

---

## **Step 14: Security Best Practices**

- Use **RBAC** → control access per service account
- Use **NetworkPolicies** → isolate microservices
- Enable **TLS via cert-manager** for ingress
- Use **IAM roles for service accounts** → least privilege access to AWS resources

---

## **Step 15: High Availability & Enterprise Considerations**

- Multi-AZ node groups
- PostgreSQL with replicas or RDS
- MongoDB replica sets
- RabbitMQ cluster with mirrored queues
- Redis cluster for sharding
- Backups & disaster recovery
- Monitoring and alerts (CloudWatch + Prometheus)

---

## ✅ **Step 16: Testing**

1. Test connectivity between services
2. Test order placement → RabbitMQ message delivery
3. Test scaling under load
4. Verify persistent storage for databases
5. Verify ingress routing and TLS

---

This **full step-by-step guide** covers everything from **IAM roles → VPC → EKS cluster → Docker images → microservices → databases → ingress → autoscaling → monitoring → security**.

---

I can also create a **professional EKS deployment architecture diagram** showing:

- Nodes, Pods, Services
- Databases, Redis, RabbitMQ
- Ingress, Autoscaling, Monitoring
- Secrets & ConfigMaps

This will give a **visual blueprint for enterprise deployment**.

Do you want me to create that diagram?
