public class NumberOfIslandsDFS {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0)
            return 0;

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

    public static void main(String[] args) {
        NumberOfIslandsDFS islandsCounter = new NumberOfIslandsDFS();
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


