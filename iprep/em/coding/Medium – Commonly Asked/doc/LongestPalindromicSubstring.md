Here's a full solution to the **Longest Palindromic Substring** problem using **Java 8**, with an easy-to-understand explanation step-by-step — like teaching a beginner.

---

## ✅ 5. Longest Palindromic Substring

---

### 🔍 Problem Statement:

Given a string `s`, return the **longest palindromic substring** in `s`.

---

### 🧠 Example:

```java
Input: "babad"
Output: "bab"
(Note: "aba" is also a valid answer)

Input: "cbbd"
Output: "bb"
```

---

## ✅ Java 8 Code (Expand Around Center Approach)

```java
public class LongestPalindromicSubstring {

    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";

        int start = 0, end = 0;

        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);     // Odd length
            int len2 = expandAroundCenter(s, i, i + 1); // Even length
            int len = Math.max(len1, len2);

            if (len > end - start) {
                // Update start and end to new longest palindrome bounds
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }

        return s.substring(start, end + 1);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    public static void main(String[] args) {
        String input = "babad";
        System.out.println("Longest Palindromic Substring: " + longestPalindrome(input));
    }
}
```

---

### 🧸 Step-by-Step Explanation (like for a child):

1. 🪞 A **palindrome** is a word that reads the same backward and forward — like `"madam"` or `"racecar"`.
2. We **check every letter in the string** as a possible **center of a palindrome**.
3. From that center, we **expand outward** in both directions to see how far the palindrome goes.
4. We check:

   - 🔹 **Odd-length** palindromes: `racecar` → center is `e`.
   - 🔸 **Even-length** palindromes: `abba` → center is between two `b`s.

5. For each possible center, we keep track of the **longest palindrome** seen so far.

---

### 🧮 Dry Run (for `"babad"`):

- Check center at index 0 → palindrome: `"b"`
- Check center at index 1 → palindrome: `"bab"` (expandable)
- Check center at index 2 → palindrome: `"aba"`
- Longest found: `"bab"` or `"aba"` → both are valid answers.

---

### ⏱ Time & Space Complexity:

| Metric | Value                                           |
| ------ | ----------------------------------------------- |
| Time   | O(n²) → due to expanding around every character |
| Space  | O(1) → constant space used                      |

---

### ✅ Want more?

I can also give:

- 🧠 A **Dynamic Programming (DP)** version
- 🚀 A **Manacher’s Algorithm** version (O(n) time)
- 📊 Visualization of how the center expansion works

Let me know which version you’d like next!
