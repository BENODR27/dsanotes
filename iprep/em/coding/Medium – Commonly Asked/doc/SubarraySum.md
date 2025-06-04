Here's a complete explanation of **"Subarray with Given Sum"** — a common interview problem often asked in companies like Emirates Group IT.

---

## ✅ 10. Subarray with Given Sum (Java 8)

### 📘 Problem Statement:

Given an array of **positive integers**, and a target sum `k`, find if there exists a **contiguous subarray** that sums up to `k`.

You can also be asked to **return the subarray** or its **start and end indices**.

---

### 🔤 Example:

```java
Input: nums = [1, 4, 20, 3, 10, 5], k = 33
Output: true
Explanation: Subarray [20, 3, 10] sums to 33.
```

---

## 🧒 Step-by-Step (Child-Friendly) Explanation:

Think of this like:

- You’re holding a sliding window (a bag).
- You add elements to the bag until the total weight is too much.
- If the bag is too heavy (sum > target), you drop from the left.
- If the bag matches the weight (sum == target), you win!

---

### ✅ Java 8 Code using Sliding Window (for positive numbers only):

```java
public class SubarraySum {
    public static boolean hasSubarrayWithSum(int[] nums, int targetSum) {
        int start = 0;
        int currentSum = 0;

        for (int end = 0; end < nums.length; end++) {
            currentSum += nums[end];  // Add current element to the window

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
        int[] nums = {1, 4, 20, 3, 10, 5};
        int target = 33;
        boolean found = hasSubarrayWithSum(nums, target);
        System.out.println("Result: " + found);
    }
}
```

---

### 🧠 Time & Space Complexity:

- **Time:** `O(n)` — each element is visited at most twice.
- **Space:** `O(1)` — constant space used.

---

