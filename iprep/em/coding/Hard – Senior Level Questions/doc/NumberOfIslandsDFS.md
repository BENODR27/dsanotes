Here is a full solution for **"Number of Islands"** using **Java 8** with both **DFS** and **BFS** approaches, explained step by step like you're new to it.

---

## ✅ Problem Statement:

You're given a 2D grid of `'1'`s (land) and `'0'`s (water).
**An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.**

Return the **number of islands**.

---

### 🔢 Example:

```text
Input:
[
  ['1','1','0','0','0'],
  ['1','1','0','0','0'],
  ['0','0','1','0','0'],
  ['0','0','0','1','1']
]

Output: 3
```

---

## 🚀 DFS Approach (Depth-First Search)

```java
public class NumberOfIslandsDFS {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int numIslands = 0;

        // Loop through every cell in the grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // If it's land
                if (grid[i][j] == '1') {
                    numIslands++; // Found a new island
                    dfs(grid, i, j); // Sink the island
                }
            }
        }

        return numIslands;
    }

    private void dfs(char[][] grid, int i, int j) {
        // Boundary or water check
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1')
            return;

        // Mark the land as visited (sink it)
        grid[i][j] = '0';

        // Explore all 4 directions
        dfs(grid, i + 1, j); // down
        dfs(grid, i - 1, j); // up
        dfs(grid, i, j + 1); // right
        dfs(grid, i, j - 1); // left
    }
}
```

---

## 🧸 Explanation Like You're a Beginner

### 🔄 Outer Loop:

You go cell by cell in the grid:

- If you find `'1'`, that's land → it starts a new island.
- You start a **DFS** from this cell to **"sink"** the entire connected land.

### 🌊 DFS "Sink":

Think of DFS as:

- You jump on land.
- Then you keep jumping to connected land pieces (up, down, left, right).
- As you visit them, you turn them to water (`'0'`) so you don’t count them again.

---

## 📦 BFS Approach (Bonus)

Using a queue to simulate level-by-level (breadth-first) sinking.

```java
import java.util.*;

public class NumberOfIslandsBFS {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int numIslands = 0;
        int[] dr = {-1, 1, 0, 0}; // row directions
        int[] dc = {0, 0, -1, 1}; // column directions

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    numIslands++;
                    Queue<int[]> queue = new LinkedList<>();
                    queue.add(new int[]{i, j});
                    grid[i][j] = '0';

                    while (!queue.isEmpty()) {
                        int[] pos = queue.poll();
                        for (int d = 0; d < 4; d++) {
                            int r = pos[0] + dr[d];
                            int c = pos[1] + dc[d];
                            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length && grid[r][c] == '1') {
                                queue.add(new int[]{r, c});
                                grid[r][c] = '0';
                            }
                        }
                    }
                }
            }
        }

        return numIslands;
    }
}
```

---

## ✅ Test Input

```java
public static void main(String[] args) {
    NumberOfIslandsDFS sol = new NumberOfIslandsDFS();

    char[][] grid = {
        {'1','1','0','0','0'},
        {'1','1','0','0','0'},
        {'0','0','1','0','0'},
        {'0','0','0','1','1'}
    };

    System.out.println(sol.numIslands(grid)); // Output: 3
}
```

---

## ⏱️ Time & Space Complexity

| Approach | Time Complexity | Space Complexity         |
| -------- | --------------- | ------------------------ |
| DFS      | O(m × n)        | O(m × n) recursion stack |
| BFS      | O(m × n)        | O(min(m, n)) queue size  |

---

Would you like:

- A **visual step-by-step simulation** of how the DFS/BFS visits each land?
- **JUnit test cases** for these?
- The same code in **JavaScript/Python**?

Let me know!
