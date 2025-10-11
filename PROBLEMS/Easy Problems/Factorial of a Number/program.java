import java.util.*;

// Factorial of a number n (written as n!) means:
// multiply all whole numbers from 1 to n.
public class program {
    static int findFactorial(int num) {
        int res = 1;
        if (num == 0)
            return res;
        for (int i = 2; i <= num; i++) {
            res *= i;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(findFactorial(3));
    }
}