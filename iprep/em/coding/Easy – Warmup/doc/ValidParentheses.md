### ✅ 3. **Valid Parentheses** — Java 8 Solution with Step-by-Step Explanation

This problem is extremely popular in interviews (like Emirates Group IT, Google, etc.) to test your understanding of stacks and balanced symbols.

---

## 📘 Problem Statement:

Given a string containing just the characters `'('`, `')'`, `'{'`, `'}'`, `'['`, and `']'`, determine if the input string is **valid**.

### ✔️ A string is valid if:

1. Open brackets must be closed by the same type of brackets.
2. Open brackets must be closed in the correct order.

---

### 🔤 Examples:

```java
Input: "()"         => true
Input: "()[]{}"     => true
Input: "(]"         => false
Input: "([)]"       => false
Input: "{[]}"       => true
```

---

## 🧒 Step-by-Step (Like Teaching a Child):

Imagine you’re stacking books:

- Every time you get a new open bracket (like `'('`), you place it on top of the stack.
- When you see a closing bracket (like `')'`), you peek at the top:

  - If it matches (like `'('` matches with `')'`), you remove it (pop it).
  - If it doesn't match, it’s invalid!

At the end:

- If your stack is **empty**, you matched everything ✅
- If your stack has leftovers, it's invalid ❌

---

## ✅ Java 8 Code:

```java
import java.util.*;

public class ValidParentheses {
    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        for (char ch : s.toCharArray()) {
            // If it's an opening bracket, push to stack
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
            } else {
                // If stack is empty, nothing to match with
                if (stack.isEmpty()) return false;

                // Pop and check if it matches the corresponding open bracket
                char open = stack.pop();
                if ((ch == ')' && open != '(') ||
                    (ch == '}' && open != '{') ||
                    (ch == ']' && open != '[')) {
                    return false;
                }
            }
        }

        // If the stack is empty, all brackets matched
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String input = "{[()]}";
        System.out.println("Is valid? " + isValid(input));
    }
}
```

---

## 🧠 Time & Space Complexity:

- **Time:** `O(n)` — we scan each character once.
- **Space:** `O(n)` — in the worst case, we put all opening brackets in the stack.

---

Would you like to move to:
✅ **4. Merge Intervals**
or
✅ **Negative number version of “Subarray With Given Sum”?**
