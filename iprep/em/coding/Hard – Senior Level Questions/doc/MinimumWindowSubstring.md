Here's a **complete step-by-step guide** to solve the **Minimum Window Substring** problem using **Java 8**, explained **like you're just starting out** — clear, slow, and easy.

---

## ✅ Problem: Minimum Window Substring

### 📌 Given:

Two strings:

- `s`: the main text
- `t`: the pattern (must include all characters in `t`, even duplicates)

### 🎯 Goal:

Find the **smallest window in `s`** that contains **all characters of `t`** (including duplicates).

---

### 🔍 Example:

```java
Input: s = "ADOBECODEBANC", t = "ABC"
Output: "BANC"
```

---

## 🚀 Java 8 Code

```java
import java.util.*;

public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        Map<Character, Integer> targetMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> windowMap = new HashMap<>();
        int left = 0, right = 0, minLen = Integer.MAX_VALUE;
        int matchCount = 0;
        int start = 0;

        while (right < s.length()) {
            char ch = s.charAt(right);
            windowMap.put(ch, windowMap.getOrDefault(ch, 0) + 1);

            if (targetMap.containsKey(ch) &&
                windowMap.get(ch).intValue() <= targetMap.get(ch).intValue()) {
                matchCount++;
            }

            // Try to shrink the window from the left
            while (matchCount == t.length()) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                char leftChar = s.charAt(left);
                windowMap.put(leftChar, windowMap.get(leftChar) - 1);

                if (targetMap.containsKey(leftChar) &&
                    windowMap.get(leftChar).intValue() < targetMap.get(leftChar).intValue()) {
                    matchCount--;
                }
                left++;
            }

            right++;
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
}
```

---

## 🧠 Step-by-Step Explanation (Like Teaching a Child)

### 📌 Imagine:

You’re holding a **magnifying glass** (window) and looking at a big sentence (string `s`) to find **all the letters** from a word (`t`).

Let’s say:

- `s = "ADOBECODEBANC"`
- `t = "ABC"`
  You want to find the **smallest snippet** of `s` that contains **A, B, and C**.

---

### 1️⃣ Count What You Need (from `t`)

We use `targetMap` to **count each letter** we want from `t`.

```java
Map<Character, Integer> targetMap = new HashMap<>();
```

For "ABC", it becomes:

```java
{A=1, B=1, C=1}
```

---

### 2️⃣ Expand the Window to the Right

We move the `right` pointer through `s` to **collect letters** using a `windowMap`.

```java
windowMap.put(ch, windowMap.getOrDefault(ch, 0) + 1);
```

When a character in `s` is also in `t` and we haven’t collected too many of it, we increase the **match count**.

---

### 3️⃣ Check: "Do I Have Everything?"

If `matchCount == t.length()` → 🎉 we have a **valid window**!

---

### 4️⃣ Shrink from the Left (Minimize It)

Now we try to **make the window smaller** from the left using the `left` pointer.

Each time we remove a character, we check:

> Did we remove something important?

If yes, decrease `matchCount` and stop shrinking.

---

### 5️⃣ Keep Track of the Best Window

We update `minLen` and `start` whenever we find a smaller valid window.

---

### 6️⃣ Final Answer

If we found something, we return the shortest valid window using:

```java
s.substring(start, start + minLen)
```

---

## 🧪 Dry Run (Quick Glance)

```java
s = "ADOBECODEBANC"
t = "ABC"
```

Valid windows:

- "ADOBEC" ❌ (too big)
- "BANC" ✅ (smallest with A, B, C)

---

## 🧵 Time and Space Complexity

| Type  | Complexity                                |
| ----- | ----------------------------------------- |
| Time  | O(n + m) — n = s.length(), m = t.length() |
| Space | O(m) — hashmap space for `t` and window   |

---

## 🧰 Tools Used

| Tool           | Description                                 |
| -------------- | ------------------------------------------- |
| `HashMap`      | To count letters needed & in current window |
| `Two Pointers` | To expand and shrink the window             |
| `Substring()`  | To get the final answer                     |

---

Would you like:

- **PDF summary with dry run**?
- **More HackerRank problems like this** (String sliding window)?
- **Java 8 Stream version** (less optimal)?

Let me know and I’ll provide!
