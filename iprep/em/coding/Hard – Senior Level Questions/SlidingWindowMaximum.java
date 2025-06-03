import java.util.*;

public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0)
            return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // stores indexes

        for (int i = 0; i < n; i++) {
            // 1. Remove elements not in the current window
            if (!deque.isEmpty() && deque.peek() < i - k + 1) {
                deque.poll(); // remove index from front
            }

            // 2. Remove smaller values from the back (not useful)
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast(); // remove from back
            }

            // 3. Add current element index
            deque.offer(i);

            // 4. Store result from the front of deque (max in current window)
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peek()];
            }
        }

        return result;
    }
    public static void main(String[] args) {
        SlidingWindowMaximum swm = new SlidingWindowMaximum();
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        int[] result = swm.maxSlidingWindow(nums, k);
        System.out.println(Arrays.toString(result)); // Output: [3, 3, 5, 5, 6, 7]
    }
}
