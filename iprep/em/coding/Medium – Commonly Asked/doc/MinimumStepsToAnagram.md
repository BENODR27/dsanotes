Here’s the complete solution for the problem **“Minimum Steps to Make Two Strings Anagram”** using **Java 8**, explained step-by-step in a beginner-friendly way:

---

## ✅ 4. Minimum Steps to Make Two Strings Anagram

---

### 🔍 Problem:

Given two strings `s` and `t`, return the **minimum number of characters** you need to delete from both strings to make them **anagrams** of each other.

---

### 🧠 Example:

```text
Input: s = "bab", t = "aba"
Output: 0
Explanation: They are already anagrams.

Input: s = "leetcode", t = "practice"
Output: 5
Explanation: Delete 'l', 'd', 'p', 'r', 'a'
```

---

## ✅ Java 8 Code

```java
import java.util.*;

public class MinimumStepsToAnagram {

    public static int minSteps(String s, String t) {
        int[] count = new int[26]; // For 'a' to 'z'

        // Count characters from string s
        for (char ch : s.toCharArray()) {
            count[ch - 'a']++;
        }

        // Subtract characters found in string t
        for (char ch : t.toCharArray()) {
            count[ch - 'a']--;
        }

        // Sum up absolute differences
        int steps = 0;
        for (int c : count) {
            steps += Math.abs(c);
        }

        return steps;
    }

    public static void main(String[] args) {
        String s1 = "leetcode";
        String s2 = "practice";
        System.out.println("Minimum steps: " + minSteps(s1, s2)); // Output: 5
    }
}
```

---

### 🧸 Step-by-Step Explanation (like for a child):

1. ✏️ Imagine each string is a pile of letters.
2. You want to make **both piles look the same** (same letters, same counts).
3. Count how many of each letter you have in each string.
4. Then for each letter from 'a' to 'z':

   - If `s` has more than `t`, you must **delete the extras from `s`**.
   - If `t` has more than `s`, delete from `t`.

5. Add up all those deletions → that’s the **minimum steps needed**.

---

### 🧮 Dry Run:

```text
s = "leetcode"
t = "practice"

Count of 'e': 3 (in s), 1 (in t) → delete 2 'e's
Count of 'l': 1 (in s), 0 (in t) → delete 1 'l'
...
Total deletions: 5
```

---

### ⏱ Time & Space Complexity:

| Metric | Value                             |
| ------ | --------------------------------- |
| Time   | O(n + m)                          |
| Space  | O(1) → Only 26 letters (constant) |

---

Would you like:

- ✅ A version that tracks **which characters** were deleted?
- ✅ The code rewritten using Java 8 **Streams**?
- ✅ A variation that only allows deleting from one string?

Let me know and I’ll include it!
