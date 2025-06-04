Here is the **"Two Sum"** problem along with a clean and professional **Java solution** suitable for HackerRank or LeetCode-style assessments like those at **Emirates Group IT**.

---

## ✅ Problem: Two Sum

**Difficulty**: Easy
**Asked in**: Amazon, Google, Emirates Group IT (type)

---

### ❓ Statement

Given an array of integers `nums` and an integer `target`, return **indices** of the two numbers such that they add up to the target.

You may assume that each input would have **exactly one solution**, and you may not use the same element twice.

Return the answer in any order.

---

### 🧪 Example

```
Input: nums = [2, 7, 11, 15], target = 9
Output: [0, 1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
```

---

## ✅ Java Solution (Optimized Using HashMap – O(n))

```java
import java.util.*;

public class TwoSumSolution {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(); // number → index
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];

            // Check if complement exists in map
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }

            // Otherwise, store this number and its index
            map.put(nums[i], i);
        }
        // By problem constraint, this will never be reached
        throw new IllegalArgumentException("No solution found");
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSum(nums, target);
        System.out.println(Arrays.toString(result));  // Output: [0, 1]
    }
}
```

---

### ✅ Time and Space Complexity

| Type             | Value                                      |
| ---------------- | ------------------------------------------ |
| Time Complexity  | O(n) – single pass through array           |
| Space Complexity | O(n) – for storing elements in the HashMap |

---

### 🧪 JUnit Test Case

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TwoSumSolutionTest {

    @Test
    public void testTwoSum() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = TwoSumSolution.twoSum(nums, target);
        assertArrayEquals(new int[]{0, 1}, result);
    }
}
```

---

Would you like:

- A version using **Java Streams**?
- A **brute-force version**?
- A **PDF with 5 variations** of this problem?

Let me know!
