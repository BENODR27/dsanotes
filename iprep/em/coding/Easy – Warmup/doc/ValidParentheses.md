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
Sure! Let's go through your `ValidParentheses` Java code **line by line**, **like explaining to a beginner or a child**. This problem checks if brackets (like `()`, `{}`, `[]`) are correctly opened and closed.

---

### ✅ Full Code Recap

```java
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;
```

📌 **Line 1-3**:
We're importing:

- `Deque`: a double-ended queue used as a **stack** here.
- `ArrayDeque`: the actual class we use to make a fast stack.
- `Map`: for storing valid bracket pairs like `')' -> '('`.

---

```java
public class ValidParentheses {
```

🧱 **Line 4**:
This is the name of the class: `ValidParentheses`. This is our main logic container.

---

```java
    private static final Map<Character, Character> PAIRS = Map.of(
            ')', '(',
            '}', '{',
            ']', '[');
```

📌 **Line 5-8**:
We create a `Map` called `PAIRS` that matches each **closing bracket** to its corresponding **opening bracket**.

So:

- `)` matches `(`
- `}` matches `{`
- `]` matches `[`

This will help us later when checking if the most recent opening bracket matches the current closing one.

---

```java
    public static boolean isValid(String s) {
```

🧠 **Line 9**:
We define a method `isValid` that takes a string `s` like `"{[()]}"` and returns `true` if the brackets are properly matched, otherwise `false`.

---

```java
        Deque<Character> stack = new ArrayDeque<>();
```

📌 **Line 10**:
We create a `stack` using `Deque`. This is where we **store opening brackets** as we find them.

Imagine a stack like a pile of books—you can only see and remove the top one.

---

```java
        for (char ch : s.toCharArray()) {
```

🔁 **Line 11**:
We go **character by character** through the string, using a for-each loop.

Example for `"{[()]}"`:

- First char: `{`
- Then: `[`
- Then: `(`
- Then: `)`
- Then: `]`
- Then: `}`

---

```java
            if (PAIRS.containsValue(ch)) {
                stack.push(ch);
```

🧠 **Line 12-13**:
If the current character is an **opening bracket** (`(`, `{`, or `[`):

- We `push()` it onto the stack.
- This means: we’re waiting for a matching closing bracket later.

---

```java
            } else if (PAIRS.containsKey(ch)) {
```

💡 **Line 14**:
If the character is a **closing bracket** (`)`, `}`, or `]`), then:

We **need to check if the top of the stack has the matching opening bracket**.

---

```java
                if (stack.isEmpty() || stack.pop() != PAIRS.get(ch)) {
                    return false;
                }
```

🛑 **Line 15-16**:
Two checks:

- If `stack` is empty → no opening bracket to match → `false`
- `stack.pop()` takes out the most recent opening bracket, and we check if it matches the one expected from the `PAIRS` map.

If not matching → return `false` (invalid).

---

```java
        }
```

🧼 **Line 17**:
End of for-loop. We’ve checked all characters.

---

```java
        return stack.isEmpty();
```

✅ **Line 18**:
At the end:

- If the `stack` is empty → all brackets were matched → return `true`
- If not → some opening brackets were never closed → return `false`

---

```java
    }

    public static void main(String[] args) {
        String input = "{[()]}";
        System.out.println("Is valid? " + isValid(input));
    }
}
```

🧪 **Lines 20-24**:
This is our `main` method (entry point):

- We give it the string `"{[()]}"`.
- It calls `isValid(...)` and prints the result.

This input returns:

```
Is valid? true
```

Because all brackets are properly opened and closed in the right order.

---

### 🔁 Stack Simulation (Step-by-Step)

For `"{[()]}"`, this happens:

| Char | Stack   | Action            |
| ---- | ------- | ----------------- |
| `{`  | `{`     | Push opening `{`  |
| `[`  | `{ [`   | Push opening `[`  |
| `(`  | `{ [ (` | Push opening `(`  |
| `)`  | `{ [`   | Pop `(` (matched) |
| `]`  | `{`     | Pop `[` (matched) |
| `}`  | `empty` | Pop `{` (matched) |

Stack is empty at the end → ✅ valid!

---

### ✅ Final Result:

The code checks bracket matching using a **stack** and a **map**, ensuring every opening has a matching closing bracket in the right order.

Would you like a **custom visual animation or Java 8 Streams version** of this too?
