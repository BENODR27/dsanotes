Here’s a **clear explanation with Java 8 code** to **detect a cycle in a directed graph**, using **both**:

### ✅ 1. Depth First Search (DFS)

### ✅ 2. Kahn’s Algorithm (BFS Topological Sort)

We’ll explain both step by step like you're learning from scratch.

---

## 🚩 Problem:

Given a directed graph, **return true if there is a cycle**, else false.

---

## 📘 Input Format (Adjacency List):

```java
int V = 4;
List<List<Integer>> graph = Arrays.asList(
    Arrays.asList(1),     // Node 0 → 1
    Arrays.asList(2),     // Node 1 → 2
    Arrays.asList(3),     // Node 2 → 3
    Arrays.asList(1)      // Node 3 → 1 → Cycle!
);
```

---

## ✅ Approach 1: DFS with Recursion Stack

### 📌 Idea:

- Use **DFS**.
- Keep a `visited[]` array.
- Keep a `recStack[]` (to track recursion path).
- If during DFS we revisit a node in `recStack`, → **Cycle!**

---

### ✅ Java Code (DFS):

```java
public class CycleDetectorDFS {

    public boolean hasCycle(int V, List<List<Integer>> graph) {
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];

        for (int node = 0; node < V; node++) {
            if (!visited[node]) {
                if (dfs(node, graph, visited, recStack))
                    return true;
            }
        }
        return false;
    }

    private boolean dfs(int node, List<List<Integer>> graph, boolean[] visited, boolean[] recStack) {
        visited[node] = true;
        recStack[node] = true;

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor] && dfs(neighbor, graph, visited, recStack)) {
                return true;
            } else if (recStack[neighbor]) {
                return true;  // back edge found!
            }
        }

        recStack[node] = false;  // done exploring this path
        return false;
    }
}
```

---

### 🔍 Step-by-Step:

1. **Start DFS** at node 0.
2. Add it to `visited[]` and `recStack[]`.
3. Visit its neighbor (node 1), do same...
4. If at any point, you revisit a node **already in `recStack[]`**, → it's a **cycle!**
5. Remove node from recStack after exploring.

---

### 🧠 Time & Space:

| Metric | Value    |
| ------ | -------- |
| Time   | O(V + E) |
| Space  | O(V)     |

---

## ✅ Approach 2: Kahn’s Algorithm (Topological Sort)

### 📌 Idea:

- **Count in-degrees** of all nodes.
- Use **BFS** to process nodes with `in-degree = 0`.
- If processed nodes < total nodes → **cycle exists**!

---

### ✅ Java Code (Kahn’s Algorithm):

```java
public class CycleDetectorKahn {

    public boolean hasCycle(int V, List<List<Integer>> graph) {
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : graph.get(u)) {
                inDegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0)
                queue.add(i);
        }

        int count = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            count++;
            for (int neighbor : graph.get(node)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0)
                    queue.add(neighbor);
            }
        }

        return count != V;  // If some nodes remain → cycle exists
    }
}
```

---

### 🧠 Step-by-Step:

1. Count **in-degrees** for each node.
2. Start with nodes that have **0 in-degree**.
3. As you remove them, **reduce in-degree** of neighbors.
4. If some nodes are **never removed**, → **they’re part of a cycle**.

---

## ✅ Sample Test:

```java
public class Main {
    public static void main(String[] args) {
        int V = 4;
        List<List<Integer>> graph = Arrays.asList(
            Arrays.asList(1), // 0 → 1
            Arrays.asList(2), // 1 → 2
            Arrays.asList(3), // 2 → 3
            Arrays.asList(1)  // 3 → 1 (Cycle!)
        );

        CycleDetectorDFS dfsDetector = new CycleDetectorDFS();
        System.out.println("DFS Cycle Detected: " + dfsDetector.hasCycle(V, graph));

        CycleDetectorKahn kahnDetector = new CycleDetectorKahn();
        System.out.println("Kahn’s Algorithm Cycle Detected: " + kahnDetector.hasCycle(V, graph));
    }
}
```

---

## 🔚 Summary:

| Approach         | Detects Cycles? | Technique       | Good For           |
| ---------------- | --------------- | --------------- | ------------------ |
| DFS + RecStack   | ✅ Yes          | Backtracking    | Simple & recursive |
| Kahn's Algorithm | ✅ Yes          | Topo Sort (BFS) | Iterative & safe   |

---

Let me know if you want:

- Visual tree for dry run
- JUnit tests
- Graph as adjacency matrix

Would you like me to cover **"Union Find"** cycle detection in **undirected graphs** too?
