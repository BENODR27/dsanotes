import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketRateLimiter {

    private final int capacity; // Max tokens
    private final int refillRatePerSecond; // How many tokens added per second
    private double currentTokens; // Current tokens in bucket
    private long lastRefillTimestamp; // When we last refilled

    public TokenBucketRateLimiter(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.currentTokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refill(); // Refill tokens based on elapsed time

        if (currentTokens >= 1) {
            currentTokens -= 1;
            return true; // Allow request
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
