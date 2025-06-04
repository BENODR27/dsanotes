Here’s a breakdown of **Matrix Rotation and Traversal Problems** using **Java 8**, explained clearly with full examples and step-by-step like you're learning from scratch.

---

## ✅ 7.1 Rotate a Matrix 90 Degrees (Clockwise)

### 🔄 Problem:

Given an **N x N matrix**, rotate it **90 degrees clockwise**, in-place.

### 📘 Example:

Input:

```
[
 [1, 2, 3],
 [4, 5, 6],
 [7, 8, 9]
]
```

Output:

```
[
 [7, 4, 1],
 [8, 5, 2],
 [9, 6, 3]
]
```

---

### ✅ Java 8 Code:

```java
public class MatrixRotation {
    public static void rotate(int[][] matrix) {
        int n = matrix.length;

        // Step 1: Transpose the matrix (swap rows with columns)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            reverseRow(matrix[i]);
        }
    }

    private static void reverseRow(int[] row) {
        int left = 0, right = row.length - 1;
        while (left < right) {
            int temp = row[left];
            row[left] = row[right];
            row[right] = temp;
            left++;
            right--;
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row)
                System.out.print(val + " ");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        rotate(matrix);
        printMatrix(matrix);
    }
}
```

---

### 👶 Step-by-Step Explanation

1. 🔄 **Transpose**: Turn rows into columns. E.g., (i,j) becomes (j,i).
2. ↔️ **Reverse**: Flip each row to get the 90° rotation.
3. ✅ This method works **in-place** (no extra space).

---

## ✅ 7.2 Spiral Matrix Traversal

### 🔄 Problem:

Given an `m x n` matrix, return all elements in **spiral order**.

### 📘 Example:

Input:

```
[
 [1, 2, 3],
 [4, 5, 6],
 [7, 8, 9]
]
```

Output:

```
[1, 2, 3, 6, 9, 8, 7, 4, 5]
```

---

### ✅ Java 8 Code:

```java
import java.util.*;

public class SpiralMatrix {
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();

        if (matrix == null || matrix.length == 0)
            return result;

        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            // Traverse top row
            for (int i = left; i <= right; i++)
                result.add(matrix[top][i]);
            top++;

            // Traverse right column
            for (int i = top; i <= bottom; i++)
                result.add(matrix[i][right]);
            right--;

            // Traverse bottom row
            if (top <= bottom) {
                for (int i = right; i >= left; i--)
                    result.add(matrix[bottom][i]);
                bottom--;
            }

            // Traverse left column
            if (left <= right) {
                for (int i = bottom; i >= top; i--)
                    result.add(matrix[i][left]);
                left++;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println(spiralOrder(matrix));
    }
}
```

---

### 👶 Step-by-Step

1. 🟪 Think of the matrix like a box with 4 walls:

   - Top, Bottom, Left, Right

2. 🔁 Move in spiral:

   - ➡️ Right → ⬇️ Down → ⬅️ Left → ⬆️ Up

3. 🧽 Shrink boundaries after each direction.
4. 🔚 Stop when no more rows/columns left.

---

## ✅ Common Interview Matrix Problems:

| Problem             | Type             |
| ------------------- | ---------------- |
| Rotate Matrix 90°   | In-place         |
| Spiral Order        | Traversal        |
| Search in 2D Matrix | Binary Search    |
| Set Matrix Zeroes   | Modify with flag |
| Diagonal Traverse   | Row/col control  |
| Word Search         | DFS/Backtracking |

---

Would you like **Set Matrix Zeroes** or **Search in a 2D Sorted Matrix** next (both common in Emirates/HackerRank rounds)?
