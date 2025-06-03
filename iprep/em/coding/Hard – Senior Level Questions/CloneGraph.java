import java.util.*;

class Node {
    public int val;
    public List<Node> neighbors;

    public Node() {
        neighbors = new ArrayList<>();
    }

    public Node(int val) {
        this.val = val;
        neighbors = new ArrayList<>();
    }

    public Node(int val, List<Node> neighbors) {
        this.val = val;
        this.neighbors = neighbors;
    }
}

public class CloneGraph {

    public Node cloneGraph(Node node) {
        if (node == null)
            return null;

        // Map to keep track of cloned nodes
        Map<Node, Node> visited = new HashMap<>();

        // Start with BFS
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);

        // Clone the first node and put into map
        visited.put(node, new Node(node.val));

        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            for (Node neighbor : curr.neighbors) {
                // If neighbor hasn’t been cloned yet
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, new Node(neighbor.val));
                    queue.add(neighbor);
                }

                // Add the cloned neighbor to the current cloned node’s neighbor list
                visited.get(curr).neighbors.add(visited.get(neighbor));
            }
        }

        return visited.get(node); // return the cloned entry point
    }

    public static void main(String[] args) {
        // Example usage
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        node1.neighbors.add(node2);
        node1.neighbors.add(node4);
        node2.neighbors.add(node1);
        node2.neighbors.add(node3);
        node3.neighbors.add(node2);
        node3.neighbors.add(node4);
        node4.neighbors.add(node1);
        node4.neighbors.add(node3);

        CloneGraph cg = new CloneGraph();
        Node clonedGraph = cg.cloneGraph(node1);

        // You can add code to print the cloned graph to verify correctness
    }
}
