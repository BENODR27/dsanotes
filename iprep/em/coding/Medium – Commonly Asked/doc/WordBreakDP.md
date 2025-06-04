Here's a complete breakdown of the **Word Break Problem** (Dynamic Programming version), using **Java 8** — with simple **child-friendly explanation**, code, and step-by-step logic.

---

## ✅ 8. Word Break Problem (DP)

### 📘 Problem:

Given a string `s` and a dictionary of strings `wordDict`, return **true** if `s` can be **segmented** into a space-separated sequence of one or more dictionary words.

---

### 🔤 Example:

Input:

```java
s = "leetcode"
wordDict = ["leet", "code"]
```

Output:

```
true
```

Explanation:
We can split `"leetcode"` as `"leet code"` which are both in the dictionary.

---

### 💡 Intuition:

We check if we can break the string into **smaller words** that are in the dictionary.

Use **Dynamic Programming** to keep track of breakable parts.

---

### ✅ Java 8 Code (Using DP):

```java
import java.util.*;

public class WordBreakDP {
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);  // Fast lookup
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true; // Empty string is always "breakable"

        // Try to break s[0...i)
        for (int i = 1; i <= s.length(); i++) {
            // Try all positions j before i
            for (int j = 0; j < i; j++) {
                // Check if s[0..j) is breakable AND s[j..i) is in wordDict
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // No need to check further
                }
            }
        }

        return dp[s.length()];
    }

    public static void main(String[] args) {
        String s = "leetcode";
        List<String> wordDict = Arrays.asList("leet", "code");
        System.out.println(wordBreak(s, wordDict));  // true
    }
}
```

---

### 🧒 Step-by-Step Explanation:

Imagine `dp[i]` means: “Can I break the string `s[0...i-1]` into words from dictionary?”

1. **Initialize** `dp[0] = true` — because empty string is "valid".
2. For every position `i` in the string:

   - Check all `j < i` where `dp[j] = true` and `s[j...i)` is in the dictionary.
   - If both are true, we mark `dp[i] = true`.

3. Finally, return `dp[s.length()]`.

---

### 🔍 Trace for `"leetcode"`:

- `dp[0] = true`
- `dp[4] = true` because `"leet"` is in the dictionary
- `dp[8] = true` because `"code"` is in the dictionary and `dp[4] = true`

✅ So the answer is **true**.

---

### 🚀 Time & Space Complexity:

- **Time**: `O(n^2)` where `n = s.length()`
- **Space**: `O(n)` for the `dp` array

---

