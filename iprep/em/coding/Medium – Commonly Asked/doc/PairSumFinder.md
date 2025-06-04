Here’s the **Java 8 solution** to the classic problem:

---

## ✅ 2. **Find All Pairs in Array That Sum to K**

---

### 🔍 Problem:

Given an array of integers and a target value `K`, return **all unique pairs** of numbers whose **sum is equal to `K`**.

---

### 🧠 Example:

```text
Input: nums = [1, 5, 7, -1, 5], K = 6
Output: [ (1, 5), (7, -1) ]
```

---

## ✅ Java 8 Code (with Step-by-Step Explanation)

```java
import java.util.*;

public class PairSumFinder {
    public List<List<Integer>> findPairs(int[] nums, int k) {
        Set<Integer> seen = new HashSet<>();
        Set<String> uniquePairs = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (int num : nums) {
            int complement = k - num;

            if (seen.contains(complement)) {
                int a = Math.min(num, complement);
                int b = Math.max(num, complement);
                String pairKey = a + ":" + b;

                if (!uniquePairs.contains(pairKey)) {
                    result.add(Arrays.asList(a, b));
                    uniquePairs.add(pairKey);
                }
            }

            seen.add(num);
        }

        return result;
    }

    public static void main(String[] args) {
        PairSumFinder finder = new PairSumFinder();
        int[] nums = {1, 5, 7, -1, 5};
        int k = 6;
        List<List<Integer>> pairs = finder.findPairs(nums, k);
        for (List<Integer> pair : pairs) {
            System.out.println(pair);
        }
    }
}
```

---

### 🧸 Step-by-Step Explanation (Like Explaining to a Child)

1. 🎯 You’re looking for **two numbers** in the list that **add up to K**.
2. You **remember numbers you've already seen** using a `HashSet`.
3. For each number, you ask:

   > "What number would I need with this to make `K`?"

4. If you already saw that number before, 🎉 you found a pair!
5. To avoid duplicates like `(1, 5)` and `(5, 1)`:

   - Always save the smaller number first.
   - Use a `Set<String>` to track which pairs you've already stored.

---

### 🔁 Dry Run:

For input: `[1, 5, 7, -1, 5]`, K = 6

- `1`: Needs `5` → Not yet seen
- `5`: Needs `1` → Found it! Add `(1, 5)`
- `7`: Needs `-1` → Not seen yet
- `-1`: Needs `7` → Found it! Add `(-1, 7)`
- `5`: Needs `1` → Already added `(1, 5)`, skip

---

### ✅ Output:

```
[1, 5]
[-1, 7]
```

---

## 🧠 Time and Space Complexity:

| Complexity | Value |
| ---------- | ----- |
| Time       | O(n)  |
| Space      | O(n)  |

---

Would you like:

- ✅ This implemented using **Streams** (Java 8)?
- ✅ Count of total pairs?
- ✅ Version that returns **indices** of pairs instead of values?

Let me know, and I’ll extend it accordingly!
