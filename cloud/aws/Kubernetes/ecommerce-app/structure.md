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
