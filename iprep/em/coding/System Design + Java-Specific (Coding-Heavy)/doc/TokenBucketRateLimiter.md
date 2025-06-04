### ✅ 1. **Design a Rate Limiter** (Leaky Bucket / Token Bucket) — Java 8 System Design + Code

Rate limiters are commonly asked in **Emirates Group IT**, **FAANG**, and **high-scale system design interviews**.

---

### 🔧 Problem Statement:

Design a **Rate Limiter** that allows **N requests per second** for a user or API key. Block excess requests.

---

## 🧠 Two Popular Algorithms:

| Algorithm        | Analogy                                              | Control Style              |
| ---------------- | ---------------------------------------------------- | -------------------------- |
| **Token Bucket** | Tokens are added per second; requests consume tokens | **Bursty Traffic Allowed** |
| **Leaky Bucket** | Requests drip at a fixed rate                        | **Smooth Rate, No Burst**  |

---

## ✅ We’ll implement **Token Bucket** in Java 8

---

### 💡 Token Bucket Intuition:

- **Bucket holds tokens** (max size = `capacity`)
- Every **1/N seconds**, we **refill tokens**
- Each request **takes 1 token**
- If token available ⇒ ✅ Allow
- If not ⇒ ❌ Reject (Rate Limit)

---

## ✅ Java 8 Code: Token Bucket Rate Limiter

```java
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketRateLimiter {

    private final int capacity;               // Max tokens
    private final int refillRatePerSecond;    // How many tokens added per second
    private double currentTokens;             // Current tokens in bucket
    private long lastRefillTimestamp;         // When we last refilled

    public TokenBucketRateLimiter(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.currentTokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refill();  // Refill tokens based on elapsed time

        if (currentTokens >= 1) {
            currentTokens -= 1;
            return true;  // Allow request
        } else {
            return false; // Reject due to rate limit
        }
    }

    private void refill() {
        long now = System.nanoTime();
        double secondsPassed = (now - lastRefillTimestamp) / 1_000_000_000.0;

        double tokensToAdd = secondsPassed * refillRatePerSecond;
        if (tokensToAdd > 0) {
            currentTokens = Math.min(capacity, currentTokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 2); // 5 tokens max, 2 tokens/sec

        for (int i = 0; i < 10; i++) {
            if (limiter.allowRequest()) {
                System.out.println("Request " + i + " allowed");
            } else {
                System.out.println("Request " + i + " denied (rate limited)");
            }
            Thread.sleep(300); // simulate time gap
        }
    }
}
```

---

### 🧒 Step-by-Step Like a Child:

1. Imagine a **bucket of coins (tokens)**.
2. Every second, we **add 2 coins**.
3. Every time someone sends a request, we ask:

   > Do we have a coin?

4. If yes ⇒ take a coin ⇒ ✅ Allow the request.
5. If no coins ⇒ ❌ Deny it (Too many requests).
6. We **refill the bucket every second** (like a water drip).

---

### ⏱ Time Complexity:

- `O(1)` per request (because refill logic is constant-time)

---

### 🧠 Use Cases in Real World:

- API Gateways (AWS API Gateway, Google Cloud Endpoint)
- User login rate-limiting
- SMS/OTP limits per user/IP/device

---

Would you like a **Leaky Bucket version**, or want to implement **Distributed Rate Limiting with Redis** (used in Emirates-scale systems)?
