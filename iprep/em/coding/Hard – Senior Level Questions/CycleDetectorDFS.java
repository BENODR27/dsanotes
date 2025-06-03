import java.util.ArrayList;
import java.util.List;

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
                return true; // back edge found!
            }
        }

        recStack[node] = false; // done exploring this path
        return false;
    }

    public static void main(String[] args) {
        CycleDetectorDFS cycleDetector = new CycleDetectorDFS();
        List<List<Integer>> graph = new ArrayList<>();
        int V = 4; // Number of vertices

        // Initialize the graph
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        // Add edges (example with a cycle)
        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(0);
        graph.get(3).add(1);
        System.out.println("Graph: " + graph);
        boolean hasCycle = cycleDetector.hasCycle(V, graph);
        System.out.println("Graph has cycle: " + hasCycle); // Output: true
    }
}
