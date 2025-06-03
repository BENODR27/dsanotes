package iprep.em.coding;

public class ShortestPathInGrid {

import java.util.*;

public class ShortestPathInGrid {

    public int shortestPath(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0][0] == 1)
            return -1;

        int rows = grid.length;
        int cols = grid[0].length;

        // Direction vectors for up, down, left, right
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // Queue to store (row, col, steps)
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { 0, 0, 0 }); // Start at (0,0) with 0 steps

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
                    queue.offer(new int[] { nr, nc, steps + 1 });
                }
            }
        }

        return -1; // No path found
    }

    public static void main(String[] args) {
        ShortestPathInGrid sp = new ShortestPathInGrid();

        int[][] grid = {
                { 0, 0, 0 },
                { 1, 1, 0 },
                { 0, 0, 0 }
        };

        System.out.println(sp.shortestPath(grid)); // Output: 4
    }
}

}
