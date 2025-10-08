import java.util.*;

class program {
    static boolean isValidTriangle(int a, int b, int c) {

        return (a + b <= c || b + c <= a || c + a <= b) ? false : true;
    }

    public static void main(String[] args) {
        System.out.println(isValidTriangle(7, 10, 5));
    }
}