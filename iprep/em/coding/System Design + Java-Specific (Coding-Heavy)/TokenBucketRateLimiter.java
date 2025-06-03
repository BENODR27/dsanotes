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
