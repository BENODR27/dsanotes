Here’s the complete **Java 8 solution** for the classic problem:

---

## 🔟 Problem: **Clone a Graph**

You're given a **reference to a node** in a **connected undirected graph**. Each node has a value and a list of its neighbors.

Return a **deep copy** (clone) of the graph.

---

### 📘 Node Definition:

```java
class Node {
    public int val;
    public List<Node> neighbors;

    public Node() {}
    public Node(int val) {
        this.val = val;
        this.neighbors = new ArrayList<>();
    }
    public Node(int val, List<Node> neighbors) {
        this.val = val;
        this.neighbors = neighbors;
    }
}
```

---

### ✅ Java 8 Solution Using BFS (HashMap to track cloned nodes)

```java
import java.util.*;

class Node {
    public int val;
    public List<Node> neighbors;

    public Node() {
        neighbors = new ArrayList<>();
    }

    public Node(int val) {
        this.val = val;
        neighbors = new ArrayList<>();
    }

    public Node(int val, List<Node> neighbors) {
        this.val = val;
        this.neighbors = neighbors;
    }
}

public class CloneGraph {

    public Node cloneGraph(Node node) {
        if (node == null) return null;

        // Map to keep track of cloned nodes
        Map<Node, Node> visited = new HashMap<>();

        // Start with BFS
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);

        // Clone the first node and put into map
        visited.put(node, new Node(node.val));

        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            for (Node neighbor : curr.neighbors) {
                // If neighbor hasn’t been cloned yet
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, new Node(neighbor.val));
                    queue.add(neighbor);
                }

                // Add the cloned neighbor to the current cloned node’s neighbor list
                visited.get(curr).neighbors.add(visited.get(neighbor));
            }
        }

        return visited.get(node); // return the cloned entry point
    }

    // Optional: You can add helper methods to print or test the graph here.
}
```

---

## 🧸 Step-by-Step Explanation (Like a Child)

Imagine a city where every house has **a number (val)** and **a list of roads (neighbors)** to other houses.

You want to **build a new city**, copying **every house and road** without connecting to the old ones.

1. 🎯 Start at the first house.
2. 🧱 Make a copy of that house (same number).
3. 🗺️ If you visit a house and haven’t copied it yet, **copy it and remember**.
4. 🔁 Go through every road, and for every connected house:

   - 📌 If the neighbor is new, copy it and visit it later.
   - 🧷 Connect your current copied house to the copied neighbor.

5. 💫 Do this until every house is copied and connected.

---

## 🧪 Example:

Input graph:

```
1 -- 2
|    |
4 -- 3
```

You should create **an identical-looking graph** with **new objects** and no links to the original.

---

## ⏱️ Time & Space Complexity

| Type  | Complexity |
| ----- | ---------- |
| Time  | O(N + E)   |
| Space | O(N)       |

- **N** = number of nodes
- **E** = number of edges

---

Would you like:

- The **DFS version** of this solution?
- A **JUnit test case** to validate this graph cloning?

Let me know!
