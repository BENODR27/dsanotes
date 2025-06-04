### ✅ 7. **Merge Two Sorted Arrays** — Java 8 Solution with Simple Explanation

This is a **classic array problem** often asked in coding rounds (LeetCode, HackerRank, and companies like **Emirates Group IT**).

---

## 📘 Problem Statement

You are given **two sorted arrays** `nums1` and `nums2`:

- Merge `nums2` into `nums1` as one **sorted array**.
- `nums1` has enough space (size that is greater or equal to `m + n`) to hold additional elements from `nums2`.

```java
Input:
nums1 = [1,2,3,0,0,0], m = 3
nums2 = [2,5,6],       n = 3

Output:
[1,2,2,3,5,6]
```

---

## 🧒 Step-by-Step Like Teaching a Child

1. Imagine two rows of sorted numbers.
2. We want to **merge them** into a single row — and keep everything **in order**.
3. Since we have **extra space at the end of nums1**, we can **start filling from the back**.
4. Think of placing the **biggest number at the end**, then next biggest, and so on.

---

## ✅ Java 8 Code (Optimal - From End)

```java
public class MergeSortedArrays {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;             // last element in nums1 part
        int j = n - 1;             // last element in nums2
        int k = m + n - 1;         // last position in nums1 full array

        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--]; // place larger at end
            } else {
                nums1[k--] = nums2[j--];
            }
        }

        // If anything left in nums2
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
    }

    public static void main(String[] args) {
        int[] nums1 = {1,2,3,0,0,0};
        int m = 3;
        int[] nums2 = {2,5,6};
        int n = 3;

        merge(nums1, m, nums2, n);
        System.out.println("Merged array: " + java.util.Arrays.toString(nums1));
    }
}
```

---

## 💡 Output:

```
Merged array: [1, 2, 2, 3, 5, 6]
```

---

## 🧠 Time & Space Complexity:

- **Time:** O(m + n) — one pass through both arrays
- **Space:** O(1) — in-place merge (no extra space)

---

Would you like the **reverse version** (merge into a new array)?
Or move to the **next DSA coding pattern** like:
**8. Spiral Matrix / Diagonal Traversal**?
