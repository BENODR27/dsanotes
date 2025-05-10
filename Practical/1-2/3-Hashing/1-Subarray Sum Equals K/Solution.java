import java.util.HashMap;

class Solution {
    public int subarraySum(int[] nums, int k) {
        // HashMap to store the cumulative sum and its frequency
        HashMap<Integer, Integer> prefixSumMap = new HashMap<>();
        prefixSumMap.put(0, 1); // Base case: a sum of 0 occurs once

        int count = 0;
        int cumulativeSum = 0;

        for (int num : nums) {
            cumulativeSum += num; // Update the cumulative sum

            // Check if (cumulativeSum - k) exists in the map
            if (prefixSumMap.containsKey(cumulativeSum - k)) {
                count += prefixSumMap.get(cumulativeSum - k);
            }

            // Update the frequency of the current cumulative sum in the map
            prefixSumMap.put(cumulativeSum, prefixSumMap.getOrDefault(cumulativeSum, 0) + 1);
        }

        return count;
    }
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 1, 1};
        int k = 2;
        int result = solution.subarraySum(nums, k);
        System.out.println(result); // Output: 2
    }
}
