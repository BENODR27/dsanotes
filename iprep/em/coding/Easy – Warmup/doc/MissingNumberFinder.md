### ✅ 4. **Find the Missing Number in an Array** — Java 8 Solution + Step-by-Step Explanation

This is a very common **HackerRank-style problem** asked in real coding rounds, including those at Emirates Group IT.

---

## 📘 Problem Statement:

You are given an array of length `n` containing distinct numbers in the range `0` to `n`. That means **one number is missing** from the array. Your task is to **find that missing number**.

---

### 🔤 Example:

```java
Input: nums = [3, 0, 1]
Output: 2

Input: nums = [0, 1]
Output: 2
```

---

## 🧒 Step-by-Step Like a Child:

Imagine you’re collecting cards numbered from `0` to `n`.

Let’s say `n = 3`. So you expect cards: `[0, 1, 2, 3]` (total sum = 6)

But you only got `[0, 1, 3]` (sum = 4).
If you subtract: `6 - 4 = 2` → That’s the missing card ✅

---

## ✅ Java 8 Code (Math-Based Efficient Way):

```java
public class MissingNumberFinder {
    public static int findMissingNumber(int[] nums) {
        int n = nums.length;
        int expectedSum = n * (n + 1) / 2; // Total sum from 0 to n
        int actualSum = 0;

        for (int num : nums) {
            actualSum += num;
        }

        return expectedSum - actualSum;
    }

    public static void main(String[] args) {
        int[] nums = {3, 0, 1};
        System.out.println("Missing number is: " + findMissingNumber(nums));
    }
}
```

---

## 🧠 Time & Space Complexity:

- **Time:** `O(n)` — one pass to sum the array.
- **Space:** `O(1)` — no extra space used.

---

## 🧪 Alternate Method: XOR (Bit Manipulation)

```java
public static int findMissingUsingXOR(int[] nums) {
    int xor = 0;
    int n = nums.length;

    for (int i = 0; i < n; i++) {
        xor ^= i ^ nums[i];
    }

    return xor ^ n; // XOR with the last number in range
}
```

---

Would you like to continue with:

👉 **5. Merge Intervals**
👉 **Next Graph Problem**
👉 **Array XOR Problems**?
