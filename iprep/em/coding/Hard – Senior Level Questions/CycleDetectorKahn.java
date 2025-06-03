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

        return count != V; // If some nodes remain → cycle exists
    }
    public static void main(String[] args) {
        CycleDetectorKahn cycleDetector = new CycleDetectorKahn();
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

        boolean hasCycle = cycleDetector.hasCycle(V, graph);
        System.out.println("Graph has cycle: " + hasCycle); // Output: true
    }
}
