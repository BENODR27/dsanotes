public class SubarraySum {
    public static boolean hasSubarrayWithSum(int[] nums, int targetSum) {
        int start = 0;
        int currentSum = 0;

        for (int end = 0; end < nums.length; end++) {
            currentSum += nums[end]; // Add current element to the window

            // Shrink window if currentSum is too large
            while (currentSum > targetSum && start < end) {
                currentSum -= nums[start];
                start++;
            }

            // Check if current window matches the target
            if (currentSum == targetSum) {
                System.out.println("Subarray found from index " + start + " to " + end);
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums = { 1, 4, 20, 3, 10, 5 };
        int target = 33;
        boolean found = hasSubarrayWithSum(nums, target);
        System.out.println("Result: " + found);
    }
}
