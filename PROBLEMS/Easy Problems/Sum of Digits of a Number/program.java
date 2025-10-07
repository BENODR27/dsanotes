import java.util.*;

class program {
    static int findSum(int n) {
        int sum = 0;
        while (n != 0) {
            sum += n % 10; // 12345 % 10 = 5
            n /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {

        int n = 12345;
        System.out.println(findSum(n));

    }
}