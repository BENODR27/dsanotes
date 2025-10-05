import java.util.*;

class program {
    static int find(int num, int mul) {
        int n1 = Math.abs(num / mul);
        int closestNumber1 = mul * n1;
        int closestNumber2 = mul * (n1 + 1);

        return ((closestNumber1 - num) < (closestNumber2 - num)) ? closestNumber1 : closestNumber2;
    }

    public static void main(String[] args) {
        int num = 13;
        int mul = 4;
        System.out.println(find(num, mul));
    }
}

// Divide N by M to see how many times M fits into N.

// 13 ÷ 4 = 3 (we ignore the remainder)

// Multiply M by that number to get the first guess:

// 4 × 3 = 12 → This is one number close to 13 that divides evenly by 4.

// Try one more guess:

// What if we go one step up? 4 × (3 + 1) = 16

// Now we have two guesses:

// 12 and 16

// Which one is closer to 13?

// 13 - 12 = 1
// 16 - 13 = 3

// So, 12 is closer!