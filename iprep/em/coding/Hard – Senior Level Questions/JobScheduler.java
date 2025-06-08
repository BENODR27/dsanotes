import java.util.*;

public class JobScheduler {
    public int leastInterval(char[] tasks, int n) {
        // Step 1: Count frequency of each task
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }

        // Step 2: Sort frequencies in descending order
        Arrays.sort(freq);
        int maxFreq = freq[25] - 1; // most frequent task, minus 1 (last task doesn’t need trailing idle)
        int idleSlots = maxFreq * n;

        // Step 3: Fill idle slots with remaining tasks
        for (int i = 24; i >= 0 && freq[i] > 0; i--) {
            idleSlots -= Math.min(freq[i], maxFreq);
        }

        // Step 4: Return total length (tasks + remaining idle slots)
        return idleSlots > 0 ? idleSlots + tasks.length : tasks.length;
    }

    public static void main(String[] args) {
        JobScheduler scheduler = new JobScheduler();
        char[] tasks = { 'A', 'A', 'A', 'B', 'B', 'B' };
        int n = 2;
        System.out.println("Minimum intervals: " + scheduler.leastInterval(tasks, n)); // Output: 8
    }
}
