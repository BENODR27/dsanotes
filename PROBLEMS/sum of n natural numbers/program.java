import java.util.*;

//natural numbers as the non-negative integers 0, 1, 2, 3, ...,
//n * (n + 1) / 2 ----- T- O1 , S - O1
class program {
    static int findSum(int num) {
        int sum = 0;
        for (int i = 1; i <= num; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        int n = 2;
        System.out.println(findSum(n));
    }
}