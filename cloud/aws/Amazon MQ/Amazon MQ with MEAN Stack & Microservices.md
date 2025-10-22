Absolutely! Let’s build a **full professional MEAN stack + Amazon MQ example** for **order processing with inventory management**, handling **race conditions** and **asynchronous microservices**. I’ll provide **backend Node.js + MQ + PostgreSQL** code and explain the setup.

---

# **Architecture**

```
Angular Frontend
       │ HTTP POST /orders
       ▼
Node.js / Express Backend
  - Inventory check (PostgreSQL)
  - Reserve stock (atomic transaction)
  - Publish to Amazon MQ
       │
       ▼
Amazon MQ Broker (RabbitMQ)
       │
       ▼
Worker Service (Node.js)
  - Consume order
  - Process payment / update order status
  - Rollback stock if failed
       │
       ▼
SQL DB (PostgreSQL)
```

---

# **Step 1: PostgreSQL Schema**

```sql
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    stock INT NOT NULL
);

CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(product_id),
    quantity INT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

- **Stock** is checked and decremented atomically.

---

# **Step 2: Backend – Node.js / Express Setup**

```bash
npm init -y
npm install express pg amqplib body-parser
```

**db.js** – PostgreSQL connection

```javascript
import pkg from "pg";
const { Pool } = pkg;

export const pool = new Pool({
  user: "postgres",
  host: "your-db-host",
  database: "ordersdb",
  password: "password",
  port: 5432,
});
```

**mqPublisher.js** – Publish to Amazon MQ

```javascript
import amqp from "amqplib";
const RABBIT_URL =
  "amqps://user:password@b-1234abcd-1.mq.us-east-1.amazonaws.com:5671";

export async function enqueueOrder(order) {
  const connection = await amqp.connect(RABBIT_URL);
  const channel = await connection.createChannel();
  const queue = "orderQueue";
  await channel.assertQueue(queue, { durable: true });
  channel.sendToQueue(queue, Buffer.from(JSON.stringify(order)), {
    persistent: true,
  });
  await channel.close();
  await connection.close();
}
```

**orderRouter.js** – API with inventory check & enqueue

```javascript
import express from "express";
import { pool } from "./db.js";
import { enqueueOrder } from "./mqPublisher.js";

const router = express.Router();

router.post("/orders", async (req, res) => {
  const { productId, quantity } = req.body;

  const client = await pool.connect();
  try {
    await client.query("BEGIN");

    // Atomic stock decrement
    const result = await client.query(
      `UPDATE products SET stock = stock - $1
       WHERE product_id = $2 AND stock >= $1
       RETURNING stock`,
      [quantity, productId]
    );

    if (result.rowCount === 0) {
      await client.query("ROLLBACK");
      return res.status(400).json({ message: "Out of stock" });
    }

    // Create order in DB
    const orderResult = await client.query(
      `INSERT INTO orders (product_id, quantity, status)
       VALUES ($1, $2, 'pending') RETURNING order_id`,
      [productId, quantity]
    );

    const order = {
      orderId: orderResult.rows[0].order_id,
      productId,
      quantity,
    };

    // Publish order to MQ
    await enqueueOrder(order);

    await client.query("COMMIT");
    res.status(200).json({ message: "Order placed successfully", order });
  } catch (err) {
    await client.query("ROLLBACK");
    console.error(err);
    res.status(500).json({ message: "Failed to place order" });
  } finally {
    client.release();
  }
});

export default router;
```

**server.js**

```javascript
import express from "express";
import bodyParser from "body-parser";
import orderRouter from "./orderRouter.js";

const app = express();
app.use(bodyParser.json());

app.use("/api", orderRouter);

app.listen(3000, () => console.log("Server running on port 3000"));
```

---

# **Step 3: Worker Service – Node.js Consumer**

```javascript
import amqp from "amqplib";
import { pool } from "./db.js";

const RABBIT_URL =
  "amqps://user:password@b-1234abcd-1.mq.us-east-1.amazonaws.com:5671";

async function startWorker() {
  const connection = await amqp.connect(RABBIT_URL);
  const channel = await connection.createChannel();
  const queue = "orderQueue";
  await channel.assertQueue(queue, { durable: true });

  console.log("Worker listening for orders...");

  channel.consume(queue, async (msg) => {
    if (msg !== null) {
      const order = JSON.parse(msg.content.toString());
      console.log("Processing order:", order);

      const client = await pool.connect();
      try {
        await client.query("BEGIN");

        // Simulate order processing (e.g., payment)
        const paymentSuccess = Math.random() > 0.1; // 90% success

        if (!paymentSuccess) {
          // Rollback stock
          await client.query(
            "UPDATE products SET stock = stock + $1 WHERE product_id = $2",
            [order.quantity, order.productId]
          );
          await client.query(
            "UPDATE orders SET status = $1 WHERE order_id = $2",
            ["failed", order.orderId]
          );
        } else {
          await client.query(
            "UPDATE orders SET status = $1 WHERE order_id = $2",
            ["completed", order.orderId]
          );
        }

        await client.query("COMMIT");
        channel.ack(msg);
      } catch (err) {
        await client.query("ROLLBACK");
        console.error("Error processing order:", err);
        // Optionally push to Dead Letter Queue
        channel.nack(msg, false, false);
      } finally {
        client.release();
      }
    }
  });
}

startWorker().catch(console.error);
```

---

# **Step 4: Angular Frontend Example**

```typescript
// order.service.ts
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: "root" })
export class OrderService {
  constructor(private http: HttpClient) {}

  placeOrder(productId: number, quantity: number) {
    return this.http.post("/api/orders", { productId, quantity });
  }
}
```

- Frontend submits orders → backend handles inventory atomically → MQ handles async processing.

---

# **Step 5: Key Professional Features**

✅ Atomic inventory management → prevents overselling
✅ Asynchronous processing → decouples frontend and workers
✅ Durable queues → message persistence in MQ
✅ Rollback on failures → maintains stock consistency
✅ Dead-letter queues → for failed order handling
✅ Scalable → multiple workers can consume MQ messages
✅ Monitoring → use CloudWatch metrics + MQ management console

