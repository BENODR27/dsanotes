Here’s the full **Java 8** implementation of **Shortest Path in a Grid** using **BFS** (Breadth-First Search), followed by an explanation **step-by-step like you're a beginner**.

---

## 🧩 Problem: Shortest Path in Grid (with obstacles)

You're given a 2D grid where:

- `0` = empty cell
- `1` = obstacle

You can move in 4 directions (up, down, left, right).
You must **find the shortest path from the top-left to the bottom-right**, avoiding obstacles. Return the number of steps or `-1` if no path exists.

---

### 🔢 Example:

```text
Input:
[
  [0, 0, 0],
  [1, 1, 0],
  [0, 0, 0]
]

Output: 4

Explanation:
Path = (0,0) → (0,1) → (0,2) → (1,2) → (2,2)
```

---

## ✅ Java 8 Solution Using BFS:

```java
import java.util.*;

public class ShortestPathInGrid {

    public int shortestPath(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0][0] == 1) return -1;

        int rows = grid.length;
        int cols = grid[0].length;

        // Direction vectors for up, down, left, right
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};

        // Queue to store (row, col, steps)
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0, 0});  // Start at (0,0) with 0 steps

        boolean[][] visited = new boolean[rows][cols];
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0], c = curr[1], steps = curr[2];

            // If we reached bottom-right cell
            if (r == rows - 1 && c == cols - 1) {
                return steps;
            }

            for (int[] dir : directions) {
                int nr = r + dir[0], nc = c + dir[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
                    grid[nr][nc] == 0 && !visited[nr][nc]) {

                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc, steps + 1});
                }
            }
        }

        return -1; // No path found
    }

    public static void main(String[] args) {
        ShortestPathInGrid sp = new ShortestPathInGrid();

        int[][] grid = {
            {0, 0, 0},
            {1, 1, 0},
            {0, 0, 0}
        };

        System.out.println(sp.shortestPath(grid));  // Output: 4
    }
}
```

---

## 🧸 Step-by-Step Explanation Like a Beginner

1. 🏁 **Start** at the top-left (0,0). You want to reach the bottom-right (m-1,n-1).
2. 🚧 **Avoid** any cell with `1` — these are obstacles.
3. ➕ **Use a queue** to keep track of where you're going next.
4. 👣 **Every time you move**, add `1` to your steps.
5. ✅ If you reach the end, return how many steps it took.
6. ❌ If you go through the whole grid and never reach the end, return `-1`.

---

## ⏱️ Time & Space Complexity

| Measure          | Value      |
| ---------------- | ---------- |
| Time Complexity  | `O(m * n)` |
| Space Complexity | `O(m * n)` |

---

## 🌟 Bonus (PriorityQueue/Heap)

If every cell has a **weight**, like this:

```java
int[][] grid = {
  {1, 2, 3},
  {4, 5, 1},
  {1, 5, 1}
};
```

You’d use **Dijkstra’s Algorithm** with a **PriorityQueue (Min Heap)** to find the minimum weight path (not just number of steps). Let me know if you want this version as well.

---

Would you like:

- JUnit test cases?
- Dijkstra/PriorityQueue version with weighted cells?
- A BFS maze solver with diagonal moves?

Let me know and I’ll provide that next.
