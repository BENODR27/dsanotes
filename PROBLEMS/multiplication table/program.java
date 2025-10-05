import java.util.*;

class program {
    public static void printTable(int num) {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + "*" + num + "= " + i * num);
        }
    }

    public static void main(String[] args) {
        int n = 2;
        printTable(n);
    }
}