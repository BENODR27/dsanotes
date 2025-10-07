import java.util.*;

class program {
    static int reverseNum(int n) {
        int num = 0;
        while (n > 0) {
            num = num * 10 + n % 10; // is like 5*10 +4 = 50+4 = 54
            n /= 10;
        }
        return num;

    }

    public static void main(String args[]) {
        int n = 12345;
        System.out.println(reverseNum(n));
    }
}