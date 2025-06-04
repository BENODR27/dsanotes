Here's a complete **Java 8 solution** to the **"Serialize and Deserialize Binary Tree"** problem, explained **step by step like you're a beginner** — clear and easy to follow.

---

## ✅ Problem: Serialize and Deserialize Binary Tree

### 📌 What it means:

- **Serialization** = Turning a tree into a string.
- **Deserialization** = Turning that string back into the original tree.

This is useful when you want to **store** a tree or **send it over a network**, and then **rebuild it** later.

---

### 🎯 Goal:

Implement two functions:

```java
String serialize(TreeNode root);
TreeNode deserialize(String data);
```

---

### 🌳 Example Tree:

```
    1
   / \
  2   3
     / \
    4   5
```

### 🔁 After Serialization:

`"1,2,#,#,3,4,#,#,5,#,#"`
(`#` means `null`)

---

## 🚀 Java 8 Code (Preorder DFS-based)

```java
import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

public class Codec {

    // Serializes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        buildString(root, sb);
        return sb.toString();
    }

    private void buildString(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#,"); // null node
        } else {
            sb.append(node.val).append(",");
            buildString(node.left, sb);   // serialize left
            buildString(node.right, sb);  // serialize right
        }
    }

    // Deserializes your encoded data to tree.
    public TreeNode deserialize(String data) {
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
        return buildTree(nodes);
    }

    private TreeNode buildTree(Queue<String> nodes) {
        String val = nodes.poll();
        if (val.equals("#")) return null;

        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = buildTree(nodes);   // deserialize left
        node.right = buildTree(nodes);  // deserialize right
        return node;
    }
}
```

---

## 🧠 Step-by-Step Explanation (Like You're a Child)

### 🎨 Imagine:

You’re drawing a tree and writing down **all the steps** to build it later.

---

### ✅ Serialization (`serialize()`)

1. **Visit node** → write its number (e.g., `1`)
2. **Go left** → write that too
3. **If nothing’s there**, write `#`
4. **Then go right** and do the same

➡️ Output becomes a list of numbers and `#`

Example:
For this tree:

```
    1
   / \
  2   3
     / \
    4   5
```

Serialization = `1,2,#,#,3,4,#,#,5,#,#`

---

### ✅ Deserialization (`deserialize()`)

1. Read values **from left to right**
2. When you see a number → make a node
3. When you see `#` → it’s an empty child
4. Go in order, and **build the tree back**

---

### 🧪 Dry Run

```
Serialized: 1,2,#,#,3,4,#,#,5,#,#
Queue: [1,2,#,#,3,4,#,#,5,#,#]
```

We read:

- 1 → new node
- 2 → left of 1
- # → left of 2 is null
- # → right of 2 is null
- 3 → right of 1
  ...and so on.

---

## 🧵 Complexity

| Type  | Value |
| ----- | ----- |
| Time  | O(n)  |
| Space | O(n)  |

Because every node is visited once in serialization and deserialization.

---

## 🧰 Real-World Analogy

Imagine saving a **tree drawing** into a sentence — and then giving the sentence to your friend, who **draws the exact same tree** based on your words. That’s what this code does.

---

## 📄 Bonus

Would you like:

- A **level-order (BFS)** based version?
- A **visual dry-run diagram**?
- **JUnit test cases**?

Let me know — I’ll prepare that for you!
