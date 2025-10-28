import java.util.*;

class program {
    static boolean isPrime(int num) {
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println(isPrime(41));
    }
}

// 2 × 2 = 4 → 4 ≤ 41 ✅ (check 2)

// 3 × 3 = 9 → 9 ≤ 41 ✅ (check 3)

// 4 × 4 = 16 → 16 ≤ 41 ✅ (check 4)

// 5 × 5 = 25 → 25 ≤ 41 ✅ (check 5)

// 6 × 6 = 36 → 36 ≤ 41 ✅ (check 6)

// 7 × 7 = 49 → 49 ❌ (stop here)

import java.util.stream.IntStream;

public class Program {

    static boolean isPrime(int num) {
        if (num <= 1) return false; // handle edge cases
        return IntStream.rangeClosed(2, (int) Math.sqrt(num))
                        .noneMatch(i -> num % i == 0);
    }

    public static void main(String[] args) {
        System.out.println(isPrime(41));  // true
        System.out.println(isPrime(42));  // false
    }
}
