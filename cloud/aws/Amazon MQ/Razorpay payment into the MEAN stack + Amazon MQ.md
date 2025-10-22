Perfect! Let’s integrate **Razorpay payment** into the **MEAN stack + Amazon MQ** setup while keeping **inventory safe** and **orders consistent**. I’ll provide a **full professional setup with Node.js backend, Razorpay integration, inventory check, and MQ message enqueueing**.

---

# **Architecture with Razorpay**

```
Angular Frontend
       │ HTTP POST /create-order
       ▼
Node.js Backend
  - Inventory check (PostgreSQL)
  - Reserve stock (atomic transaction)
  - Create Razorpay Order
  - Return payment details to frontend
       │
Frontend → Razorpay Payment
       │
       ▼
Webhook Callback / Payment Confirmation
       │
Node.js Backend
  - Verify payment
  - Publish order to Amazon MQ
       │
Worker Service → Process order asynchronously
```

**Key Idea:**

* **Reserve stock before payment** → avoid overselling
* **Publish to MQ only after payment success** → prevents processing unpaid orders

---

# **Step 1: Install Razorpay SDK**

```bash
npm install razorpay
```

---

# **Step 2: Node.js Backend – Create Razorpay Order**

**config.js**

```javascript
export const RAZORPAY_KEY_ID = "YOUR_KEY_ID";
export const RAZORPAY_KEY_SECRET = "YOUR_KEY_SECRET";
```

**paymentController.js**

```javascript
import Razorpay from "razorpay";
import { pool } from "./db.js";
import { enqueueOrder } from "./mqPublisher.js";
import { RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET } from "./config.js";

const razorpay = new Razorpay({
  key_id: RAZORPAY_KEY_ID,
  key_secret: RAZORPAY_KEY_SECRET
});

// Step 1: Create Razorpay Order
export async function createOrder(req, res) {
  const { productId, quantity } = req.body;
  const client = await pool.connect();

  try {
    await client.query('BEGIN');

    // Atomic stock reservation
    const result = await client.query(
      `UPDATE products SET stock = stock - $1
       WHERE product_id = $2 AND stock >= $1
       RETURNING stock`,
      [quantity, productId]
    );

    if (result.rowCount === 0) {
      await client.query('ROLLBACK');
      return res.status(400).json({ message: 'Out of stock' });
    }

    // Create a DB order in 'pending' state
    const orderResult = await client.query(
      `INSERT INTO orders (product_id, quantity, status)
       VALUES ($1, $2, 'pending') RETURNING order_id`,
      [productId, quantity]
    );

    const orderId = orderResult.rows[0].order_id;

    // Razorpay order creation
    const razorpayOrder = await razorpay.orders.create({
      amount: 100 * quantity, // Amount in smallest currency unit (paise)
      currency: "INR",
      receipt: `order_rcpt_${orderId}`,
      payment_capture: 1
    });

    await client.query('COMMIT');

    res.status(200).json({
      orderId,
      razorpayOrderId: razorpayOrder.id,
      amount: razorpayOrder.amount,
      currency: razorpayOrder.currency,
      key: RAZORPAY_KEY_ID
    });
  } catch (err) {
    await client.query('ROLLBACK');
    console.error(err);
    res.status(500).json({ message: 'Failed to create order' });
  } finally {
    client.release();
  }
}
```

---

# **Step 3: Angular Frontend – Payment Integration**

```typescript
// order.service.ts
placeOrder(productId: number, quantity: number) {
  return this.http.post('/api/create-order', { productId, quantity });
}

// payment.component.ts
pay(order) {
  const options = {
    key: order.key,
    amount: order.amount,
    currency: order.currency,
    name: 'My Shop',
    description: 'Order Payment',
    order_id: order.razorpayOrderId,
    handler: this.paymentSuccess.bind(this)
  };

  const rzp = new Razorpay(options);
  rzp.open();
}

paymentSuccess(response) {
  // Send payment verification to backend
  this.http.post('/api/verify-payment', response).subscribe(res => {
    console.log('Payment verified:', res);
  });
}
```

---

# **Step 4: Backend – Verify Payment & Publish to MQ**

**verifyPayment.js**

```javascript
import crypto from 'crypto';
import { pool } from './db.js';
import { enqueueOrder } from './mqPublisher.js';

export async function verifyPayment(req, res) {
  const { razorpay_order_id, razorpay_payment_id, razorpay_signature, orderId } = req.body;

  // Step 1: Verify signature
  const body = razorpay_order_id + '|' + razorpay_payment_id;
  const expectedSignature = crypto.createHmac('sha256', process.env.RAZORPAY_KEY_SECRET)
                                  .update(body.toString())
                                  .digest('hex');

  if (expectedSignature !== razorpay_signature) {
    return res.status(400).json({ message: 'Invalid payment signature' });
  }

  const client = await pool.connect();
  try {
    await client.query('BEGIN');

    // Update order status to 'paid'
    await client.query(
      'UPDATE orders SET status = $1 WHERE order_id = $2',
      ['paid', orderId]
    );

    // Fetch order details
    const orderResult = await client.query(
      'SELECT * FROM orders WHERE order_id = $1',
      [orderId]
    );
    const order = orderResult.rows[0];

    // Publish order to MQ for async processing
    await enqueueOrder(order);

    await client.query('COMMIT');
    res.status(200).json({ message: 'Payment verified and order queued', order });
  } catch (err) {
    await client.query('ROLLBACK');
    console.error(err);
    res.status(500).json({ message: 'Failed to verify payment' });
  } finally {
    client.release();
  }
}
```

---

# **Step 5: Worker Service**

* Same as earlier, the **worker consumes the MQ message**, performs additional processing like **shipment, notifications**, or **logging**, but **stock was already reserved** before payment, ensuring no overselling.

---

# **Step 6: Best Practices**

✅ **Reserve stock before payment** → avoids overselling
✅ **Publish to MQ only after payment verification** → prevents unconfirmed orders
✅ **Use durable queues** → reliable message delivery
✅ **Handle payment failure** → revert stock if needed
✅ **Dead-letter queues** → for failed order processing
✅ **Monitor MQ + DB metrics** → CloudWatch + PostgreSQL monitoring

---

This is a **professional, production-ready setup** for **MEAN stack + Amazon MQ + Razorpay**, safe for **high-concurrency orders** and **microservice processing**.

---

I can also draw a **full architecture diagram** showing **Angular → Node.js → Razorpay → MQ → Worker → DB + Notifications**, highlighting **inventory reservation, payment verification, and MQ queue** flow.

Do you want me to create that diagram?
