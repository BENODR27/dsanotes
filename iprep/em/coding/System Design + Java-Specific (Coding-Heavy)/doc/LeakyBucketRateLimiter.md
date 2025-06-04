Great! Let's now walk through the **Leaky Bucket** implementation and then explain a **Distributed Rate Limiter using Redis**, both used in high-scale systems like **Emirates Group IT**, Netflix, or Amazon.

---

## ✅ 1. **Leaky Bucket Algorithm** – Java 8

### 🔧 Analogy:

- Imagine a **bucket with a hole** at the bottom.
- **Requests are added to the bucket** (like water).
- **They leak out** at a constant rate.
- If the **bucket overflows** (too many requests at once), we **reject** the request.

---

### 🧠 Key Properties:

| Property       | Description                                    |
| -------------- | ---------------------------------------------- |
| Queue (Bucket) | Stores incoming requests temporarily.          |
| Leak Rate      | Constant rate at which requests are processed. |
| Capacity       | Max size of the queue (bucket).                |
| Overflow       | If full ⇒ reject new requests.                 |

---

### ✅ Java 8 Code: Leaky Bucket

```java
import java.util.LinkedList;
import java.util.Queue;

public class LeakyBucketRateLimiter {

    private final int capacity;
    private final int leakRatePerSecond; // how many requests can be processed per second
    private final Queue<Long> queue; // holds timestamps
    private long lastLeakTimestamp;

    public LeakyBucketRateLimiter(int capacity, int leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRatePerSecond = leakRatePerSecond;
        this.queue = new LinkedList<>();
        this.lastLeakTimestamp = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        leakOldRequests();  // Remove old requests from queue (simulate leak)

        if (queue.size() < capacity) {
            queue.offer(System.nanoTime());
            return true;
        }
        return false;
    }

    private void leakOldRequests() {
        long now = System.nanoTime();
        double secondsPassed = (now - lastLeakTimestamp) / 1_000_000_000.0;
        int tokensToRemove = (int)(secondsPassed * leakRatePerSecond);

        while (tokensToRemove-- > 0 && !queue.isEmpty()) {
            queue.poll(); // leak out one request
        }

        if (tokensToRemove > 0) {
            lastLeakTimestamp = now;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(5, 1); // bucket size 5, leaks 1/sec

        for (int i = 0; i < 10; i++) {
            if (limiter.allowRequest()) {
                System.out.println("✅ Request " + i + " allowed");
            } else {
                System.out.println("❌ Request " + i + " denied (Rate Limit)");
            }
            Thread.sleep(200); // simulate 200ms between requests
        }
    }
}
```

---

## 🧠 Step-by-Step Like a Child:

1. You have a **cup with a hole** at the bottom.
2. Every second, **some water leaks out** (requests processed).
3. If too many people pour water in (requests), **the cup overflows** ⇒ We **reject** extra requests.
4. Old water leaks out automatically.
5. We use a **queue to simulate water** and **remove it over time**.

---

## 🌍 2. **Distributed Rate Limiter Using Redis**

Used in high-scale systems like Emirates Group IT to:

- Apply limits **across multiple servers**
- Handle **global users**, **API keys**, etc.

### 📦 Key Redis Concepts:

- `SET`, `INCR`, `EXPIRE`
- Atomic operations using **Lua scripting**

---

### ✅ Use Case: Limit to 10 requests/min per user

### Example Redis Commands (pseudocode):

```bash
INCR user:123
EXPIRE user:123 60
```

---

### ✅ Java Pseudocode Using Redisson or Jedis:

```java
public boolean isAllowed(String userId) {
    String key = "rate_limit:" + userId;
    long count = redis.incr(key);

    if (count == 1) {
        redis.expire(key, 60); // 60 seconds
    }

    return count <= 10;
}
```

This ensures:

- **Up to 10 requests per 60 seconds per user**
- Auto-reset after 1 minute
- Works across all instances in a **distributed system**

---

## ✅ Summary Table

| Method       | Best For            | Allows Bursts? | Distributed?     |
| ------------ | ------------------- | -------------- | ---------------- |
| Token Bucket | Most common         | ✅ Yes         | ❌ Not by itself |
| Leaky Bucket | Smooth flow         | ❌ No          | ❌               |
| Redis + Lua  | Distributed systems | ✅ Yes         | ✅ Yes           |

---

Would you like a **full working example using Redisson or Spring Boot with Redis** to demonstrate distributed rate limiting?
