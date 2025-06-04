Here's a complete explanation and **Java 8 code** for the classic problem:

---

## ✅ 3. **Group Anagrams**

---

### 🔍 Problem:

Given an array of strings, group the **anagrams** together.

👉 An **anagram** is a word formed by rearranging the letters of another word, e.g., `"eat"` and `"tea"`.

---

### 🧠 Example:

```java
Input: ["eat", "tea", "tan", "ate", "nat", "bat"]

Output:
[
  ["eat","tea","ate"],
  ["tan","nat"],
  ["bat"]
]
```

---

## ✅ Java 8 Code

```java
import java.util.*;

public class GroupAnagrams {

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String str : strs) {
            // Step 1: Sort the string to get the key
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);

            // Step 2: Add to map
            map.computeIfAbsent(sorted, k -> new ArrayList<>()).add(str);
        }

        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        GroupAnagrams ga = new GroupAnagrams();
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = ga.groupAnagrams(input);

        for (List<String> group : result) {
            System.out.println(group);
        }
    }
}
```

---

### 🧸 Step-by-Step (Like Explaining to a Child)

1. Imagine two words: `"tea"` and `"eat"`. Rearranged, they both become `"aet"`. That’s their **sorted version**.
2. If two words become the **same** after sorting → they are **anagrams**.
3. We sort every word and use the **sorted word as a key** in a map.
4. We **group all original words** by that key.
5. Finally, we return all grouped values as the result.

---

### 🧮 Dry Run:

```java
Input: ["eat", "tea", "tan", "ate", "nat", "bat"]

After Sorting:
"eat" → "aet"
"tea" → "aet"
"tan" → "ant"
"ate" → "aet"
"nat" → "ant"
"bat" → "abt"

Final Map:
"aet" → ["eat", "tea", "ate"]
"ant" → ["tan", "nat"]
"abt" → ["bat"]
```

---

### ✅ Output:

```
[eat, tea, ate]
[tan, nat]
[bat]
```

---

### ⏱ Time & Space Complexity:

| Metric | Value                                                              |
| ------ | ------------------------------------------------------------------ |
| Time   | O(n \* k log k) → where `n` = #strings, `k` = avg length of string |
| Space  | O(n \* k)                                                          |

---

Would you like:

- ✅ A version using Java 8 **streams**?
- ✅ A solution that maintains **original input order**?
- ✅ An **optimized** version using **frequency array** as key instead of sorting?

Let me know!
