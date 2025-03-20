public class Solution {
    public static int climbStairs(int n) {
        if (n <= 2)
            return n;

        int first = 1, second = 2;
        for (int i = 3; i <= n; i++) {
            int temp = first + second;
            first = second;
            second = temp;
        }
        return second;
    }

    public static void main(String[] args) {
        System.out.println("Ways to climb 3" + " steps: " + climbStairs(3));
        System.out.println("Ways to climb 2" + " steps: " + climbStairs(2));

    }
}
