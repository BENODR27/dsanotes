import java.util.*;

class program {
    static int find(int a1, int a2, int n) {
        return a1 + (n - 1) * (a2 - a1);
    }

    public static void main(String[] args) {
        System.out.println(find(2, 3, 4));
    }
}
