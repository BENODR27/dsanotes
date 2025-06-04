Here is the **"Reverse a String"** problem along with multiple professional **Java solutions**, including an **in-place** version and a **recursive** one — as might be expected in **Emirates Group IT** HackerRank-style rounds.

---

## ✅ Problem: Reverse a String

**Difficulty**: Easy
**Category**: Strings, Two-Pointer, Recursion
**Asked in**: Emirates IT (Warmup), Amazon, TCS, Infosys, etc.

---

### ❓ Statement

Write a function that reverses a string. The input string is given as a **character array `char[] s`**.

You must **do this in-place**, without allocating extra memory for another array.

---

### 🧪 Example

```java
Input:  s = ['h','e','l','l','o']
Output: ['o','l','l','e','h']
```

---

## ✅ Java Solution #1 – In-Place Two-Pointer (O(n), no extra space)

```java
public class ReverseString {
    public static void reverseString(char[] s) {
        int left = 0, right = s.length - 1;
        while (left < right) {
            // swap characters
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;

            left++;
            right--;
        }
    }

    public static void main(String[] args) {
        char[] input = {'h', 'e', 'l', 'l', 'o'};
        reverseString(input);
        System.out.println(input); // Output: olleh
    }
}
```

---

## ✅ Java Solution #2 – Recursive Approach

```java
public class ReverseStringRecursive {
    public static void reverse(char[] s, int left, int right) {
        if (left >= right) return;

        // swap
        char temp = s[left];
        s[left] = s[right];
        s[right] = temp;

        reverse(s, left + 1, right - 1);
    }

    public static void main(String[] args) {
        char[] input = {'h', 'e', 'l', 'l', 'o'};
        reverse(input, 0, input.length - 1);
        System.out.println(input); // Output: olleh
    }
}
```

---

### ✅ Time and Space Complexity

| Type             | Value                                         |
| ---------------- | --------------------------------------------- |
| Time Complexity  | O(n) – single pass                            |
| Space Complexity | O(1) – in-place (or O(n) for recursion stack) |

---

### 🧪 JUnit Test Case

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReverseStringTest {
    @Test
    public void testReverse() {
        char[] input = {'h', 'e', 'l', 'l', 'o'};
        ReverseString.reverseString(input);
        assertArrayEquals(new char[] {'o','l','l','e','h'}, input);
    }
}
```

---

Would you like:

- A **LeetCode-style class-based solution**?
- A version using **StringBuilder / StringBuffer**?
- A **PDF of Top 20 String Problems** with code?

Let me know!
