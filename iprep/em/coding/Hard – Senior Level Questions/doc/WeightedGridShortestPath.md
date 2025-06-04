Perfect! Here's the **Java 8** version of the **Shortest Path in a Weighted Grid** using **Dijkstra’s Algorithm** with a **Min Heap (`PriorityQueue`)**.

---

## 🧩 Problem: Shortest Path in a Grid with Weights

You're given a 2D grid where:

- Each cell has a **positive integer weight** — the **cost to step on that cell**.
- You can move only in **4 directions**: up, down, left, right.
- **Find the minimum cost** to go from **top-left `(0, 0)`** to **bottom-right `(m-1, n-1)`**.

---

### 🔢 Example Input:

```java
int[][] grid = {
    {1, 2, 3},
    {4, 8, 2},
    {1, 5, 3}
};
```

### ✅ Expected Output:

`11`

**Path**: (0,0)->(0,1)->(0,2)->(1,2)->(2,2)
**Total Cost** = 1 + 2 + 3 + 2 + 3 = **11**

---

## ✅ Java 8 Solution Using Dijkstra's Algorithm

```java
import java.util.*;

public class WeightedGridShortestPath {

    public int minPathCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // Min heap: stores [cost, row, col]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{grid[0][0], 0, 0}); // Starting at (0,0) with initial cost

        // Store minimum cost to reach each cell
        int[][] dist = new int[m][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = grid[0][0];

        // 4 directions: right, down, left, up
        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cost = curr[0], r = curr[1], c = curr[2];

            // If reached bottom-right
            if (r == m - 1 && c == n - 1) return cost;

            for (int[] dir : dirs) {
                int nr = r + dir[0], nc = c + dir[1];

                if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                    int newCost = cost + grid[nr][nc];

                    // Relax edge if better cost found
                    if (newCost < dist[nr][nc]) {
                        dist[nr][nc] = newCost;
                        pq.offer(new int[]{newCost, nr, nc});
                    }
                }
            }
        }

        return -1; // should not happen for valid inputs
    }

    public static void main(String[] args) {
        WeightedGridShortestPath solver = new WeightedGridShortestPath();
        int[][] grid = {
            {1, 2, 3},
            {4, 8, 2},
            {1, 5, 3}
        };
        System.out.println("Minimum Path Cost: " + solver.minPathCost(grid));  // Output: 11
    }
}
```

---

## 🧸 Step-by-Step Like You're a Child

1. 🏁 Start at cell (0,0) — your cost is the number on that cell.
2. 🔀 You can go up, down, left, or right.
3. 🏗️ Every time you step into a new cell, **you pay its cost**.
4. 💰 Keep track of the **cheapest cost** to each cell (like a savings account).
5. ✅ If you find a cheaper way to a cell, update it.
6. 🥇 When you reach the bottom-right corner, you return the total cost it took.

---

## ⏱️ Time & Space Complexity

| Measure          | Value                                          |
| ---------------- | ---------------------------------------------- |
| Time Complexity  | `O(m * n * log(mn))` (from the priority queue) |
| Space Complexity | `O(m * n)` for the distance matrix and queue   |

---

## 🧪 Optional JUnit Test Stub

Would you like me to add **JUnit test cases** for this as well?

Or, would you like:

- Diagonal moves support?
- Real-world use-case simulation (e.g., travel map)?
- Visual representation (grid path printing)?

Let me know!
