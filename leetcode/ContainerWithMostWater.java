package leetcode;

public class ContainerWithMostWater {
    // https://s3-lc-upload.s3.amazonaws.com/uploads/2018/07/17/question_11.jpg
    public int maxArea(int[] height) {
        int left = 0; // Pointer starting at the beginning
        int right = height.length - 1; // Pointer starting at the end
        int maxArea = 0; // Variable to store the maximum area

        // Use two-pointer approach
        while (left < right) {
            // Calculate the current area
            int currentArea = Math.min(height[left], height[right]) * (right - left);

            // Update the maximum area if the current area is larger
            maxArea = Math.max(maxArea, currentArea);

            // Move the pointer pointing to the shorter line inward
            if (height[left] < height[right]) {
                left++; // Move the left pointer to the right
            } else {
                right--; // Move the right pointer to the left
            }
        }

        return maxArea;
    }

    public static void main(String[] args) {
        ContainerWithMostWater solution = new ContainerWithMostWater();

        // Test Case 1
        int[] height1 = { 1, 8, 6, 2, 5, 4, 8, 3, 7 };
        System.out.println("Output: " + solution.maxArea(height1)); // Output: 49

        // Test Case 2
        int[] height2 = { 1, 1 };
        System.out.println("Output: " + solution.maxArea(height2)); // Output: 1
    }
}
