Okay, I can help you with common interview problem patterns and provide examples of their solutions in Java. This is a broad request, so I'll cover several fundamental categories with explanations and illustrative code.

### Common Interview Problem Patterns and Java Solutions

Here's a breakdown of some frequent problem patterns encountered in coding interviews, along with Java solutions.

**1. Array and String Manipulation**

* **Two Pointers:** This technique is often used to solve problems where you need to find pairs, subarrays, or manipulate elements within an array or string efficiently. Two pointers move towards each other or in the same direction to satisfy a certain condition.

    ```java
    class TwoPointers {
        // Example: Check if a sorted array contains a pair with a given sum
        public boolean hasPairWithSum(int[] arr, int targetSum) {
            int left = 0;
            int right = arr.length - 1;
            while (left < right) {
                int currentSum = arr[left] + arr[right];
                if (currentSum == targetSum) {
                    return true;
                } else if (currentSum < targetSum) {
                    left++;
                } else {
                    right--;
                }
            }
            return false;
        }

        // Example: Reverse a string
        public String reverseString(String s) {
            char[] charArray = s.toCharArray();
            int left = 0;
            int right = charArray.length - 1;
            while (left < right) {
                char temp = charArray[left];
                charArray[left++] = charArray[right];
                charArray[right--] = temp;
            }
            return new String(charArray);
        }
    }
    ```

* **Sliding Window:** This pattern is useful for problems involving finding subarrays or substrings of a specific size or satisfying a certain condition. You maintain a "window" of elements and slide it through the data structure.

    ```java
    class SlidingWindow {
        // Example: Find the maximum sum of a subarray of size k
        public int maxSubarraySum(int[] arr, int k) {
            if (arr.length < k || k <= 0) {
                throw new IllegalArgumentException("Invalid input");
            }
            int maxSum = 0;
            int currentSum = 0;
            // Calculate the sum of the first window
            for (int i = 0; i < k; i++) {
                currentSum += arr[i];
            }
            maxSum = currentSum;
            // Slide the window
            for (int i = k; i < arr.length; i++) {
                currentSum += arr[i] - arr[i - k];
                maxSum = Math.max(maxSum, currentSum);
            }
            return maxSum;
        }
    }
    ```

* **Prefix Sum:** This technique involves creating an auxiliary array where each element at index `i` stores the sum of all elements in the original array up to index `i`. This allows for efficient calculation of sums of subarrays.

    ```java
    class PrefixSum {
        // Example: Calculate the sum of a subarray between indices i and j
        public int subarraySum(int[] arr, int i, int j) {
            int[] prefixSum = new int[arr.length + 1];
            for (int k = 0; k < arr.length; k++) {
                prefixSum[k + 1] = prefixSum[k] + arr[k];
            }
            return prefixSum[j + 1] - prefixSum[i];
        }
    }
    ```

**2. Linked Lists**

* **Two Pointers:** Similar to arrays, two pointers can be used in linked lists for tasks like finding the middle node, detecting cycles, or finding the k-th to last element.

    ```java
    class LinkedListProblems {
        static class ListNode {
            int val;
            ListNode next;
            ListNode(int val) { this.val = val; }
        }

        // Example: Find the middle node of a linked list
        public ListNode findMiddle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }

        // Example: Detect a cycle in a linked list
        public boolean hasCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    return true;
                }
            }
            return false;
        }
    }
    ```

* **Dummy Node:** Using a dummy node at the beginning of the linked list can simplify operations like inserting or deleting at the head.

    ```java
    class LinkedListProblemsWithDummy {
        static class ListNode {
            int val;
            ListNode next;
            ListNode(int val) { this.val = val; }
        }

        // Example: Insert a new node at the beginning of a linked list
        public ListNode insertAtBeginning(ListNode head, int val) {
            ListNode dummy = new ListNode(-1);
            dummy.next = head;
            ListNode newNode = new ListNode(val);
            newNode.next = head;
            dummy.next = newNode;
            return dummy.next;
        }
    }
    ```

**3. Trees**

* **Depth-First Search (DFS):** This involves exploring as far as possible along each branch before backtracking. Common applications include traversing trees, searching for nodes, and checking properties of the tree.

    ```java
    class TreeProblems {
        static class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;
            TreeNode(int val) { this.val = val; }
        }

        // Example: Inorder traversal of a binary tree (recursive)
        public void inorderTraversal(TreeNode root) {
            if (root != null) {
                inorderTraversal(root.left);
                System.out.print(root.val + " ");
                inorderTraversal(root.right);
            }
        }
    }
    ```

* **Breadth-First Search (BFS):** This involves exploring all the nodes at the present depth prior to moving on to the nodes at the next depth level. It's often used for level-order traversal or finding the shortest path in a tree.

    ```java
    import java.util.LinkedList;
    import java.util.Queue;

    class TreeProblemsBFS {
        static class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;
            TreeNode(int val) { this.val = val; }
        }

        // Example: Level-order traversal of a binary tree
        public void levelOrderTraversal(TreeNode root) {
            if (root == null) {
                return;
            }
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                TreeNode current = queue.poll();
                System.out.print(current.val + " ");
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
        }
    }
    ```

* **Tree Recursion:** Many tree problems can be solved elegantly using recursion, where the solution for a node depends on the solutions of its children.

    ```java
    class TreeRecursion {
        static class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;
            TreeNode(int val) { this.val = val; }
        }

        // Example: Find the height of a binary tree
        public int height(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int leftHeight = height(root.left);
            int rightHeight = height(root.right);
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }
    ```

**4. Graphs**

* **Depth-First Search (DFS):** Used for graph traversal, finding connected components, detecting cycles, and topological sorting.

    ```java
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Stack;

    class GraphDFS {
        private int V; // Number of vertices
        private List<List<Integer>> adj; // Adjacency list representation

        public GraphDFS(int v) {
            V = v;
            adj = new ArrayList<>(v);
            for (int i = 0; i < v; ++i) {
                adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v) {
            adj.get(u).add(v);
            adj.get(v).add(u); // For undirected graph
        }

        public void DFS(int startNode) {
            boolean[] visited = new boolean[V];
            Stack<Integer> stack = new Stack<>();

            stack.push(startNode);
            visited[startNode] = true;

            while (!stack.isEmpty()) {
                int current = stack.pop();
                System.out.print(current + " ");

                for (int neighbor : adj.get(current)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        stack.push(neighbor);
                    }
                }
            }
            System.out.println();
        }
    }
    ```

* **Breadth-First Search (BFS):** Used for graph traversal, finding the shortest path in an unweighted graph, and level-order traversal.

    ```java
    import java.util.ArrayList;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Queue;

    class GraphBFS {
        private int V;
        private List<List<Integer>> adj;

        public GraphBFS(int v) {
            V = v;
            adj = new ArrayList<>(v);
            for (int i = 0; i < v; ++i) {
                adj.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v) {
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        public void BFS(int startNode) {
            boolean[] visited = new boolean[V];
            Queue<Integer> queue = new LinkedList<>();

            visited[startNode] = true;
            queue.offer(startNode);

            while (!queue.isEmpty()) {
                int current = queue.poll();
                System.out.print(current + " ");

                for (int neighbor : adj.get(current)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
            System.out.println();
        }
    }
    ```

**5. Dynamic Programming**

Dynamic programming is an optimization technique for problems that exhibit overlapping subproblems and optimal substructure. You solve smaller subproblems and store their results to avoid redundant computations.

* **Memoization (Top-Down):** Solve the problem using recursion but store the results of subproblems in a cache (e.g., a map or array) to avoid recomputing them.

    ```java
    import java.util.Arrays;

    class FibonacciMemoization {
        private int[] memo;

        public int fibonacci(int n) {
            memo = new int[n + 1];
            Arrays.fill(memo, -1);
            return fibonacciHelper(n);
        }

        private int fibonacciHelper(int n) {
            if (n <= 1) {
                return n;
            }
            if (memo[n] != -1) {
                return memo[n];
            }
            memo[n] = fibonacciHelper(n - 1) + fibonacciHelper(n - 2);
            return memo[n];
        }
    }
    ```

* **Tabulation (Bottom-Up):** Build a table of results for subproblems in a bottom-up manner, starting from the base cases.

    ```java
    class FibonacciTabulation {
        public int fibonacci(int n) {
            if (n <= 1) {
                return n;
            }
            int[] dp = new int[n + 1];
            dp[0] = 0;
            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }
    ```

**6. Backtracking**

Backtracking is a general algorithmic technique for finding all (or some) solutions to some computational problems, notably constraint satisfaction problems, that incrementally builds candidates to the solutions, and abandons a candidate ("backtracks") as soon as it determines that this candidate cannot possibly lead to a valid solution.

```java
import java.util.ArrayList;
import java.util.List;

class Backtracking {
    // Example: Generate all permutations of a string
    public List<String> permute(String s) {
        List<String> result = new ArrayList<>();
        permuteHelper(s.toCharArray(), 0, result);
        return result;
    }

    private void permuteHelper(char[] arr, int l, List<String> result) {
        if (l == arr.length - 1) {
            result.add(new String(arr));
        } else {
            for (int i = l; i < arr.length; i++) {
                swap(arr, l, i);
                permuteHelper(arr, l + 1, result);
                swap(arr, l, i); // Backtrack
            }
        }
    }

    private void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
```

**7. Sorting and Searching**

* **Common Sorting Algorithms:** Be familiar with algorithms like Merge Sort, Quick Sort, Heap Sort, Insertion Sort, and Bubble Sort, including their time and space complexities. Java's `Arrays.sort()` uses a highly optimized dual-pivot quicksort for primitive types and TimSort for object types.

* **Binary Search:** An efficient algorithm for finding an element in a sorted array.

    ```java
    class BinarySearch {
        public int binarySearch(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2; // Avoid potential overflow
                if (arr[mid] == target) {
                    return mid;
                } else if (arr[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return -1; // Target not found
        }
    }
    ```

**8. Greedy Algorithms**

Greedy algorithms make locally optimal choices at each step with the hope of finding a global optimum. They are often simpler to implement but don't always guarantee the best solution for all problems.

```java
import java.util.Arrays;

class ActivitySelection {
    // Example: Activity Selection Problem
    static class Activity {
        int start;
        int finish;

        public Activity(int start, int finish) {
            this.start = start;
            this.finish = finish;
        }
    }

    public static void selectActivities(Activity[] activities) {
        // Sort activities by finish time
        Arrays.sort(activities, (a, b) -> Integer.compare(a.finish, b.finish));

        System.out.println("Selected Activities:");
        if (activities.length > 0) {
            System.out.print("(" + activities[0].start + ", " + activities[0].finish + ") ");
            int lastFinishTime = activities[0].finish;
            for (int i = 1; i < activities.length; i++) {
                if (activities[i].start >= lastFinishTime) {
                    System.out.print("(" + activities[i].start + ", " + activities[i].finish + ") ");
                    lastFinishTime = activities[i].finish;
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Activity[] activities = {
                new Activity(1, 4),
                new Activity(3, 5),
                new Activity(0, 6),
                new Activity(5, 7),
                new Activity(3, 9),
                new Activity(5, 9),
                new Activity(6, 10),
                new Activity(8, 11),
                new Activity(8, 12),
                new Activity(2, 14),
                new Activity(12, 16)
        };
        selectActivities(activities);
    }
}
```

**9. Bit Manipulation**

Understanding bitwise operations (AND, OR, XOR, NOT, left shift, right shift) can lead to efficient solutions for certain problems.

```java
class BitManipulation {
    // Example: Check if a number is a power of 2