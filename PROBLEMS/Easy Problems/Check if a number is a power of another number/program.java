import java.util.*;

class program {
    static boolean isPower(int x, int y) {

        if (x == 1)
            return y == 1;
        long num = 1;
        while (num < y) {
            num *= x;
        }
        return num == y;
    }

    public static void main(String[] args) {
        System.out.println(isPower(2, 2));
    }
}

// public static boolean isPower(int x, int y) { 
// double res1 = Math.log(y) / Math.log(x);
// return res1 == Math.floor(res1);
// }
