### ✅ 6. **First Non-Repeating Character** — Java 8 Solution + Step-by-Step Explanation

This is one of the **most commonly asked string problems** in coding rounds — including **HackerRank**, **LeetCode**, and companies like **Emirates Group IT**.

---

## 📘 Problem Statement:

Given a string, **find the first character** that does **not repeat**. Return the character or its index.

---

### 🔤 Examples:

```java
Input: "leetcode"
Output: 'l'

Input: "loveleetcode"
Output: 'v'

Input: "aabb"
Output: No non-repeating character (return -1 or '_')
```

---

## 🧒 Step-by-Step Like Teaching a Child:

1. Imagine reading a line of people holding alphabet boards.
2. You count how many times each letter appears using a map.
3. Then, you walk from left to right and ask:

   - “Are you the only one holding this letter?”

4. First one who says "Yes" is the winner.

---

## ✅ Java 8 Code (Using `LinkedHashMap` to Maintain Order):

```java
import java.util.*;

public class FirstUniqueChar {
    public static char firstNonRepeatingChar(String s) {
        Map<Character, Integer> countMap = new LinkedHashMap<>();

        // Count each character
        for (char ch : s.toCharArray()) {
            countMap.put(ch, countMap.getOrDefault(ch, 0) + 1);
        }

        // Find the first character with count 1
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }

        return '_'; // If no non-repeating character found
    }

    public static void main(String[] args) {
        String input = "loveleetcode";
        char result = firstNonRepeatingChar(input);
        System.out.println("First non-repeating character: " + result);
    }
}
```

---

## ✅ Variant: Return Index Instead of Char

```java
public static int firstUniqueCharIndex(String s) {
    Map<Character, Integer> countMap = new HashMap<>();

    for (char c : s.toCharArray()) {
        countMap.put(c, countMap.getOrDefault(c, 0) + 1);
    }

    for (int i = 0; i < s.length(); i++) {
        if (countMap.get(s.charAt(i)) == 1) {
            return i;
        }
    }

    return -1;
}
```

---

## 🧠 Time & Space Complexity:

- **Time:** `O(n)` — One pass to count, one pass to check.
- **Space:** `O(1)` — Max 26–128 unique characters (constant).

---

Would you like to try the **next string problem**:
👉 **7. Longest Common Prefix**
👉 or jump to **System Design - Rate Limiter / Message Queue**?
