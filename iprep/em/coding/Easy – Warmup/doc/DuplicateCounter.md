### ✅ 5. **Counting Duplicates in a String** — Java 8 Solution + Step-by-Step Explanation

This is a common **string manipulation** question asked in coding tests like **HackerRank**, **LeetCode**, and **real-world coding rounds** like **Emirates Group IT**.

---

## 📘 Problem Statement:

Given a string, count **how many characters appear more than once**, **case-insensitive**.

---

### 🔤 Example:

```java
Input:  "Programming"
Output: {g=2, r=2, m=2}

Input:  "AaBbCcA"
Output: {a=2}

Input:  "abcdef"
Output: {}  // No duplicates
```

---

## 🧒 Step-by-Step Explanation (Like Teaching a Child):

1. First, **ignore case**: Treat `'A'` and `'a'` the same.
2. Create a **map** to count how many times each character appears.
3. Loop through the characters:

   - If it exists, increase the count.
   - Otherwise, start with count 1.

4. After the loop, filter out characters whose count is **>1**.
5. Return or print the duplicates and their counts.

---

## ✅ Java 8 Code:

```java
import java.util.*;

public class DuplicateCounter {
    public static Map<Character, Integer> countDuplicates(String input) {
        Map<Character, Integer> countMap = new HashMap<>();

        // Convert to lowercase for case-insensitive comparison
        input = input.toLowerCase();

        // Count each character
        for (char ch : input.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) { // Optional: skip spaces/punctuations
                countMap.put(ch, countMap.getOrDefault(ch, 0) + 1);
            }
        }

        // Filter and collect characters that appear more than once
        Map<Character, Integer> duplicates = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicates;
    }

    public static void main(String[] args) {
        String input = "Programming";
        Map<Character, Integer> result = countDuplicates(input);
        System.out.println("Duplicates: " + result);
    }
}
```

---

## 🧠 Time & Space Complexity:

- **Time:** `O(n)` — where `n` is the length of the string.
- **Space:** `O(1)` — max 26 lowercase letters or 62 alphanumeric characters.

---

Would you like to:

👉 See the **top 10 string interview questions** (like this one)?
👉 Move on to the **next array challenge** (e.g., "Merge Intervals")?
