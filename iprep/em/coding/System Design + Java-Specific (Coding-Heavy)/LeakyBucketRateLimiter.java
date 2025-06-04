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
