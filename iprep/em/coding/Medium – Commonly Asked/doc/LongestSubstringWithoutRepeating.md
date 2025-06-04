Here’s the **Java 8 solution** with a **step-by-step explanation** (like explaining to a child) for:

---

### ✅ 1. **Longest Substring Without Repeating Characters**

#### 🔍 Problem:

Given a string `s`, find the length of the **longest substring** without repeating characters.

---

### ✨ Example:

```text
Input:  "abcabcbb"
Output: 3   // "abc"

Input: "bbbbb"
Output: 1   // "b"

Input: "pwwkew"
Output: 3   // "wke"
```

---

## ✅ Java 8 Code (Sliding Window)

```java
import java.util.*;

public class LongestSubstringWithoutRepeating {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int maxLength = 0;
        int start = 0; // left boundary of the window

        for (int end = 0; end < s.length(); end++) {
            char currentChar = s.charAt(end);

            if (map.containsKey(currentChar)) {
                // move start to the right of previous occurrence
                start = Math.max(map.get(currentChar) + 1, start);
            }

            map.put(currentChar, end); // update the index of the current character
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        LongestSubstringWithoutRepeating solver = new LongestSubstringWithoutRepeating();
        System.out.println(solver.lengthOfLongestSubstring("abcabcbb"));  // 3
        System.out.println(solver.lengthOfLongestSubstring("bbbbb"));     // 1
        System.out.println(solver.lengthOfLongestSubstring("pwwkew"));    // 3
    }
}
```

---

## 🧸 Step-by-Step Explanation (Like a Child)

1. 🎯 You want to find the **longest part of the string** where **no letter repeats**.

2. 🪟 Imagine a **sliding window** — you're sliding across the string from left to right.

3. 🧠 Keep track of:

   - The **letters you’ve seen** (`map`)
   - The **start of the current window**
   - The **length** of the longest unique part so far

4. ✋ If you see a **repeated letter** (like `a` again):

   - You **move the start** of your window past the previous `a`.

5. 🔄 You update the `maxLength` every time with the size of your window (`end - start + 1`).

---

### 🔁 Dry Run: `"abcabcbb"`

| Step | Char | Map             | Start | End | MaxLen | Comment                 |
| ---- | ---- | --------------- | ----- | --- | ------ | ----------------------- |
| 0    | a    | {a=0}           | 0     | 0   | 1      | New unique char         |
| 1    | b    | {a=0, b=1}      | 0     | 1   | 2      | Still unique            |
| 2    | c    | {a=0, b=1, c=2} | 0     | 2   | 3      | Still unique            |
| 3    | a    | {a=3,...}       | 1     | 3   | 3      | Repeat: move start      |
| 4    | b    | {b=4,...}       | 2     | 4   | 3      | Repeat: move start      |
| 5    | c    | {c=5,...}       | 3     | 5   | 3      | Repeat: move start      |
| 6    | b    | {b=6,...}       | 4     | 6   | 3      | Repeat again            |
| 7    | b    | {b=7,...}       | 7     | 7   | 3      | Only one char in window |

Final result: `3` (substring: `"abc"`)

---

## ✅ Time and Space Complexity

| Complexity | Value |                                        |
| ---------- | ----- | -------------------------------------- |
| Time       | O(n)  |                                        |
| Space      | O(k)  | (k = charset size, usually 128 or 256) |

---

Want me to provide:

- 📁 Unit Tests?
- 🧪 Full LeetCode test cases?
- 🧠 Follow-up variations (e.g., longest substring with at most K repeats)?

Let me know!
