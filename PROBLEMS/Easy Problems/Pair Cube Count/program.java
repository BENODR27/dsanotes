import java.util.*;

//a^3 + b^3 = n
class program {
    static int findCubePairCount(int n) {
        int count = 0;
        for (int i = 1; i <= Math.cbrt(n); i++) {
            int cb = i * i * i; // i becomes a
            int diff = n - cb; // diff become b
            int cbrtDiff = (int) Math.cbrt(diff);

            if (cbrtDiff * cbrtDiff * cbrtDiff == diff) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(findCubePairCount(9));

    }
}