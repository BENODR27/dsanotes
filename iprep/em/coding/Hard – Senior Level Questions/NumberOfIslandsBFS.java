import java.util.*;

public class NumberOfIslandsBFS {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0)
            return 0;
        int numIslands = 0;
        int[] dr = { -1, 1, 0, 0 }; // row directions
        int[] dc = { 0, 0, -1, 1 }; // column directions

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    numIslands++;
                    Queue<int[]> queue = new LinkedList<>();
                    queue.add(new int[] { i, j });
                    grid[i][j] = '0';

                    while (!queue.isEmpty()) {
                        int[] pos = queue.poll();
                        for (int d = 0; d < 4; d++) {
                            int r = pos[0] + dr[d];
                            int c = pos[1] + dc[d];
                            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length && grid[r][c] == '1') {
                                queue.add(new int[] { r, c });
                                grid[r][c] = '0';
                            }
                        }
                    }
                }
            }
        }

        return numIslands;
    }

    public static void main(String[] args) {
        NumberOfIslandsBFS islandsCounter = new NumberOfIslandsBFS();
        char[][] grid = {
                { '1', '1', '0', '0', '0' },
                { '1', '1', '0', '1', '1' },
                { '0', '0', '0', '1', '0' },
                { '0', '1', '1', '0', '0' }
        };
        int result = islandsCounter.numIslands(grid);
        System.out.println("Number of islands: " + result); // Output: 3
    }
}
