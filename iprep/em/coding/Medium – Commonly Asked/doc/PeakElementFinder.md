Here's a complete breakdown of the **Find Peak Element** problem using **Binary Search** in **Java 8**, with a simple step-by-step explanation like you're teaching a beginner or a child.

---

## ✅ 9. Find Peak Element

### 📘 Problem:

A **peak element** is an element that is **greater than its neighbors**.

You are given an array `nums`, and you need to return the **index of any one peak element**.

> You may assume that `nums[-1] = -∞` and `nums[n] = -∞`.

---

### 🔤 Example:

Input:

```java
nums = [1, 2, 1, 3, 5, 6, 4]
```

Output:

```
5
```

Explanation:

* Element `6` at index 5 is a peak because `6 > 5` and `6 > 4`.

---

### ❗ Constraints:

* You must solve it in **logarithmic time** → that means use **Binary Search**!

---

## ✅ Java 8 Code (Binary Search):

```java
public class PeakElementFinder {

    public static int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[mid + 1]) {
                // We are in a decreasing slope → peak is on the left or mid
                right = mid;
            } else {
                // We are in an increasing slope → peak is on the right
                left = mid + 1;
            }
        }

        return left; // or right; both are equal now
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 1, 3, 5, 6, 4};
        System.out.println("Peak found at index: " + findPeakElement(nums));
    }
}
```

---

## 🧒 Step-by-Step Explanation (Child-Friendly):

1. You are climbing a mountain range made of numbers.

2. A **peak** is where the number is **higher than the number before and after**.

3. Instead of walking step-by-step (O(n)), we can use **binary search** and jump smarter.

4. At each step:

   * Look at the **middle number**.
   * If the middle number is **greater than the next**, then the peak is on the **left side**.
   * If it is **smaller**, then the peak is on the **right side**.

5. We **narrow** the search window until only **one number is left** — this is the peak!

---

### 📈 Visualization:

```
Index:       0   1   2   3   4   5   6
Values:      1   2   1   3   5   6   4
```

* Start with `left = 0`, `right = 6`
* Check `mid = 3 → nums[3] = 3, nums[4] = 5 → 3 < 5 → move right`
* Next `left = 4`, `right = 6`
* Check `mid = 5 → nums[5] = 6, nums[6] = 4 → 6 > 4 → move left`
* Now `left = right = 5 → DONE`

Peak at index 5 (value 6)

---

### 🧠 Time & Space Complexity:

* **Time**: `O(log n)` — due to binary search
* **Space**: `O(1)` — no extra space

