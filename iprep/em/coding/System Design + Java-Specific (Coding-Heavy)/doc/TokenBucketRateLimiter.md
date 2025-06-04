Here’s a complete explanation and **Java 8 implementation** of a **Rate Limiter** using both **Leaky Bucket** and **Token Bucket** approaches, specifically tailored for **coding-heavy system design rounds** like those at **Emirates Group IT**.

---

## ✅ 1. System Design: **Rate Limiter**

### What is a Rate Limiter?

A **rate limiter** controls how frequently a user/service can access an API within a specific time window — e.g., "100 requests per minute."

---

## 💡 Two Common Algorithms:

### 1. **Leaky Bucket**:

- Think of a bucket leaking water at a constant rate.
- Incoming requests are "poured" into the bucket.
- If the bucket overflows => reject the request.

### 2. **Token Bucket**:

- A bucket is filled with **tokens** at a fixed rate.
- Each request **consumes one token**.
- If no tokens available => reject the request.

---

## 🧪 Use Case (API Throttling):

> Only allow **N requests per user/IP per second/minute**.

---

## ✅ Java 8: **Token Bucket Implementation**

### 🔧 Requirements:

- Add tokens at a fixed interval.
- Allow a request only if at least 1 token is available.
- Each token = permission to send 1 request.

---

### 🔽 Java Code

```java
import java.util.concurrent.*;

public class TokenBucketRateLimiter {
    private final int capacity;
    private final int refillRate; // tokens per second
    private double currentTokens;
    private long lastRefillTimestamp;

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.currentTokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refill();

        if (currentTokens >= 1) {
            currentTokens -= 1;
            return true; // Allow request
        }
        return false; // Reject request
    }

    private void refill() {
        long now = System.nanoTime();
        double tokensToAdd = ((now - lastRefillTimestamp) / 1e9) * refillRate;
        currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
        lastRefillTimestamp = now;
    }

    // For test/demo
    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1); // 5 tokens, refill 1/sec

        for (int i = 1; i <= 10; i++) {
            Thread.sleep(300); // simulate 300ms between requests
            System.out.println("Request " + i + ": " + limiter.allowRequest());
        }
    }
}
```

---

## 🧸 Step-by-Step (Like a Child)

- 🎈 You start with a bucket full of tokens (say 5).
- ⏳ Every second, a new token is added (up to 5 max).
- 🤚 Each request takes 1 token. If none left, it gets rejected.
- 📏 This keeps your system fair — no one can overuse the API.

---

## ⚙️ Time Complexity

| Operation      | Complexity |
| -------------- | ---------- |
| `allowRequest` | O(1)       |
| `refill`       | O(1)       |

---

## 🧪 Unit Test Stub (JUnit 5)

```java
@Test
public void testRateLimiter() throws InterruptedException {
    TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(2, 1);

    assertTrue(limiter.allowRequest());
    assertTrue(limiter.allowRequest());
    assertFalse(limiter.allowRequest());

    Thread.sleep(1000); // wait for refill
    assertTrue(limiter.allowRequest());
}
```

---

## 🆚 Want Leaky Bucket Version?

If you'd like the **Leaky Bucket** version (queue + fixed drain rate), just say the word — I’ll share a Java version with complete explanation.

Would you also like:

- Redis-backed distributed rate limiter?
- OpenShift/Kubernetes-compatible version for microservices?

Let me know!
