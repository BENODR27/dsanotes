Excellent — now we’re entering one of the **most fundamental and powerful AWS database services**:
🔹 **Amazon DynamoDB** — the **fully managed, serverless NoSQL database** that powers applications like **Amazon.com, Netflix, Lyft, and Airbnb**.

Let’s go step by step — from **architecture and internals** to **use cases, setup, performance, and real professional usage** (with Node.js code examples).

---

## 🧠 What Is DynamoDB?

> **Amazon DynamoDB** is a **fully managed NoSQL key-value and document database** that delivers **single-digit millisecond latency** at any scale.

You don’t manage servers, clusters, replication, or scaling — AWS does all of it.

---

## ⚙️ Core Features

| Feature               | Description                                      |
| --------------------- | ------------------------------------------------ |
| **Fully managed**     | No servers, patching, or scaling worries         |
| **NoSQL model**       | Key-value and document-oriented                  |
| **High availability** | Data automatically replicated across 3 AZs       |
| **Scalability**       | Scales to millions of requests per second        |
| **Performance**       | Millisecond latency for reads/writes             |
| **Global tables**     | Multi-region replication                         |
| **Streams**           | Real-time event capture for changes              |
| **Integrations**      | AWS Lambda, API Gateway, Kinesis, S3, Glue, etc. |

---

## 🧩 Basic Concepts

| Concept               | Description                              |
| --------------------- | ---------------------------------------- |
| **Table**             | A collection of items (like a SQL table) |
| **Item**              | A single record (like a row)             |
| **Attribute**         | A field or column within an item         |
| **Primary Key**       | Uniquely identifies each item            |
| **Partition Key**     | Determines data placement                |
| **Sort Key**          | Optional, for composite key sorting      |
| **Secondary Indexes** | Additional query flexibility (GSI, LSI)  |

---

### 🔑 Primary Key Types

#### 1. **Partition Key Only**

Each item is uniquely identified by one attribute.

Example:

```json
{
  "userId": "U1001",
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Partition Key:** `userId`

All items with the same `userId` go to the same partition.

#### 2. **Partition + Sort Key**

Allows multiple related items per partition.

Example:

```json
{
  "userId": "U1001",
  "orderId": "O5001",
  "amount": 120.0
}
```

**Primary Key:** `(userId, orderId)`
You can then **query all orders** for a user easily.

---

## 🧮 Capacity Modes

| Mode            | Description                                        | Use Case                          |
| --------------- | -------------------------------------------------- | --------------------------------- |
| **On-Demand**   | Auto-scales read/write capacity automatically      | Variable or unpredictable traffic |
| **Provisioned** | Manually set Read/Write Capacity Units (RCUs/WCUs) | Steady predictable workload       |

---

## ⚡ Performance and Scaling

| Component                      | Purpose                                                 |
| ------------------------------ | ------------------------------------------------------- |
| **Partitions**                 | Data automatically distributed by hash of partition key |
| **Hot partitions**             | Avoid by distributing keys evenly                       |
| **Adaptive capacity**          | Auto-allocates throughput to busy partitions            |
| **DAX (DynamoDB Accelerator)** | In-memory cache to reduce read latency to microseconds  |

---

## 🗃️ Example: Table Design

### Table: `Orders`

| Attribute | Type   | Key           |
| --------- | ------ | ------------- |
| userId    | String | Partition Key |
| orderId   | String | Sort Key      |
| amount    | Number |               |
| status    | String |               |

---

### Example JSON Item

```json
{
  "userId": "U1001",
  "orderId": "ORD-987",
  "amount": 129.99,
  "status": "DELIVERED"
}
```

---

## 🧰 Step-by-Step Setup in AWS Console

1. Go to **AWS Console → DynamoDB**
2. Click **Create Table**
3. Table name: `Orders`
4. Partition key: `userId` (String)
5. Sort key: `orderId` (String)
6. Capacity mode: **On-Demand**
7. Create Table ✅

---

## 💻 Node.js Example (Using AWS SDK v3)

Install dependencies:

```bash
npm install @aws-sdk/client-dynamodb @aws-sdk/lib-dynamodb
```

---

### **1. Initialize DynamoDB Client**

```javascript
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import {
  DynamoDBDocumentClient,
  PutCommand,
  GetCommand,
  QueryCommand,
} from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({ region: "us-east-1" });
const ddb = DynamoDBDocumentClient.from(client);
```

---

### **2. Insert Item (PutCommand)**

```javascript
const addOrder = async () => {
  const params = {
    TableName: "Orders",
    Item: {
      userId: "U1001",
      orderId: "O5001",
      amount: 150,
      status: "PLACED",
      createdAt: new Date().toISOString(),
    },
  };
  await ddb.send(new PutCommand(params));
  console.log("✅ Order added successfully");
};

addOrder();
```

---

### **3. Read Item (GetCommand)**

```javascript
const getOrder = async () => {
  const params = {
    TableName: "Orders",
    Key: {
      userId: "U1001",
      orderId: "O5001",
    },
  };
  const result = await ddb.send(new GetCommand(params));
  console.log("📦 Order:", result.Item);
};

getOrder();
```

---

### **4. Query by Partition Key**

```javascript
const getUserOrders = async (userId) => {
  const params = {
    TableName: "Orders",
    KeyConditionExpression: "userId = :uid",
    ExpressionAttributeValues: {
      ":uid": userId,
    },
  };
  const result = await ddb.send(new QueryCommand(params));
  console.log("🧾 Orders:", result.Items);
};

getUserOrders("U1001");
```

---

## 🔄 DynamoDB Streams

> Streams capture item-level changes (insert/update/delete) — can trigger **AWS Lambda** in real time.

**Use Case:** Sync DynamoDB → Elasticsearch, S3 backup, or trigger order processing.

---

### Example: Enable Stream + Lambda

1. Enable **Stream** in table settings
   Stream view: `NEW_AND_OLD_IMAGES`
2. Create **Lambda function**
3. Add **DynamoDB trigger**
4. Lambda code:

```javascript
exports.handler = async (event) => {
  for (const record of event.Records) {
    console.log("Change type:", record.eventName);
    console.log("New item:", record.dynamodb.NewImage);
  }
};
```

---

## 🌍 Global Tables (Multi-Region Replication)

> Automatically replicates data across multiple AWS regions.

### Benefits

- Active-Active global writes
- Low latency for users worldwide
- Disaster recovery built-in

---

## 🧮 Pricing (Simplified)

| Component  | Description                                | Example          |
| ---------- | ------------------------------------------ | ---------------- |
| Read/Write | Pay per request (on-demand) or provisioned | 1M reads ≈ $1.25 |
| Storage    | $0.25 per GB-month                         | 100 GB = $25/mo  |
| Streams    | $0.02 per 100,000 reads                    | Low cost         |
| DAX        | Additional cost for cache cluster          | Optional         |

---

## 🧠 Best Practices (Professional Level)

✅ **Design by access pattern** (not relational)
✅ **Use composite keys** to avoid scans
✅ **Avoid “hot partitions”** → distribute keys evenly
✅ **Prefer Query over Scan** (Scan = expensive)
✅ **Use GSIs** for secondary query patterns
✅ **Use TTL** for expiring data automatically
✅ **Use DAX** for read-heavy apps
✅ **Enable Streams** for event-driven design
✅ **Backup regularly** using point-in-time recovery (PITR)

---

## 🏗️ Real-World Architecture (E-Commerce Example)

```
User → API Gateway → Lambda (Node.js) → DynamoDB (Orders Table)
                                ↓
                            DynamoDB Streams
                                ↓
                            Lambda Worker
                                ↓
                            SNS / SQS / Analytics
```

- **Frontend (Angular/React)** calls API Gateway
- **Backend (Lambda)** writes to DynamoDB
- **Streams + Lambda** handle async workflows (email, notifications, analytics)

---

## 🔐 Security

| Feature                         | Description                        |
| ------------------------------- | ---------------------------------- |
| **IAM Policies**                | Control access to tables and items |
| **Encryption at rest**          | AES-256 by default                 |
| **In-transit encryption**       | TLS/SSL enforced                   |
| **Fine-grained access control** | Restrict items by user ID          |
| **VPC endpoints**               | Private access from within VPC     |

---

## ✅ Summary

| Feature      | DynamoDB                                   |
| ------------ | ------------------------------------------ |
| Type         | Managed NoSQL (Key-Value + Document)       |
| Scale        | Infinite (auto-scaling)                    |
| Latency      | Single-digit ms                            |
| Cost         | Pay per request                            |
| Integrations | Lambda, S3, Kinesis, API Gateway           |
| Best For     | E-commerce, IoT, gaming, serverless apps   |
| Example Use  | User sessions, carts, orders, leaderboards |

---

Would you like me to show you a **complete production-grade architecture + code** for an **e-commerce backend** using:

- DynamoDB (Orders, Products, Users)
- Lambda + API Gateway
- DynamoDB Streams for real-time updates
- Node.js SDK
  ?

That would be the **professional enterprise setup** for serverless apps (like Amazon’s internal model).
