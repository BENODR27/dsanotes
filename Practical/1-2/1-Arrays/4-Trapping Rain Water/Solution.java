//note- if height is more than before that iteration don't store water(left and right both)

public class Solution {

    public static int trap(int[] height) {
        if (height == null || height.length < 3)
            return 0;

        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;
        System.out.println("left --" + left);
        System.out.println("right --" + right);
        System.out.println("------------------------------");

        while (left < right) {
            
            System.out.println("left i--" + left);
            System.out.println("right i--" + right);
            System.out.println("height left i--" + height[left]);
            System.out.println("height right i--" + height[right]);

            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    totalWater += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    totalWater += rightMax - height[right];
                }
                right--;
            }
            System.out.println("leftMax --" + leftMax);
            System.out.println("rightMax --" + rightMax);

            System.out.println("totalWater --" + totalWater);
            System.out.println("------------------------------");

        }

        return totalWater;
    }

    public static void main(String[] args) {
        int[] height1 = { 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1 };
        System.out.println(trap(height1)); // Output: 6

        // int[] height2 = { 4, 2, 0, 3, 2, 5 };
        // System.out.println(trap(height2)); // Output: 9
    }
}