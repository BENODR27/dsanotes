import java.util.*;

public class WeightedGridShortestPath {

    public int minPathCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // Min heap: stores [cost, row, col]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[] { grid[0][0], 0, 0 }); // Starting at (0,0) with initial cost

        // Store minimum cost to reach each cell
        int[][] dist = new int[m][n];
        for (int[] row : dist)
            Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = grid[0][0];

        // 4 directions: right, down, left, up
        int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cost = curr[0], r = curr[1], c = curr[2];

            // If reached bottom-right
            if (r == m - 1 && c == n - 1)
                return cost;

            for (int[] dir : dirs) {
                int nr = r + dir[0], nc = c + dir[1];

                if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                    int newCost = cost + grid[nr][nc];

                    // Relax edge if better cost found
                    if (newCost < dist[nr][nc]) {
                        dist[nr][nc] = newCost;
                        pq.offer(new int[] { newCost, nr, nc });
                    }
                }
            }
        }

        return -1; // should not happen for valid inputs
    }

    public static void main(String[] args) {
        WeightedGridShortestPath solver = new WeightedGridShortestPath();
        int[][] grid = {
                { 1, 2, 3 },
                { 4, 8, 2 },
                { 1, 5, 3 }
        };
        System.out.println("Minimum Path Cost: " + solver.minPathCost(grid)); // Output: 11
    }
}
