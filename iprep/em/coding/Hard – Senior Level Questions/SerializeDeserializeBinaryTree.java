import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}
// **Serialize and Deserialize Binary Tree**

public class SerializeDeserializeBinaryTree {

    // Serializes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        buildString(root, sb);
        return sb.toString();
    }

    private void buildString(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#,"); // null node
        } else {
            sb.append(node.val).append(",");
            buildString(node.left, sb); // serialize left
            buildString(node.right, sb); // serialize right
        }
    }

    // Deserializes your encoded data to tree.
    public TreeNode deserialize(String data) {
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
        return buildTree(nodes);
    }

    private TreeNode buildTree(Queue<String> nodes) {
        String val = nodes.poll();
        if (val.equals("#"))
            return null;

        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = buildTree(nodes); // deserialize left
        node.right = buildTree(nodes); // deserialize right
        return node;
    }

    public static void main(String[] args) {
        SerializeDeserializeBinaryTree codec = new SerializeDeserializeBinaryTree();

        // Create a sample tree:
        // 1
        // / \
        // 2 3
        // / \
        // 4 5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        String serialized = codec.serialize(root);
        System.out.println("Serialized tree: " + serialized); // Output: "1,2,4,#,#,5,#,#,3,#,#"

        TreeNode deserializedRoot = codec.deserialize(serialized);
        String deserializedSerialized = codec.serialize(deserializedRoot);
        System.out.println("Deserialized and re-serialized tree: " + deserializedSerialized); // Should match original
                                                                                              // serialization
    }
}
