import java.util.*;

public class PairSumFinder {
    public List<List<Integer>> findPairs(int[] nums, int k) {
        Set<Integer> seen = new HashSet<>();
        Set<String> uniquePairs = new HashSet<>();
        List<List<Integer>> result = new ArrayList<>();

        for (int num : nums) {
            int complement = k - num;

            if (seen.contains(complement)) {
                int a = Math.min(num, complement);
                int b = Math.max(num, complement);
                String pairKey = a + ":" + b;

                if (!uniquePairs.contains(pairKey)) {
                    result.add(Arrays.asList(a, b));
                    uniquePairs.add(pairKey);
                }
            }

            seen.add(num);
        }

        return result;
    }

    public static void main(String[] args) {
        PairSumFinder finder = new PairSumFinder();
        int[] nums = { 1, 5, 7, -1, 5 };
        int k = 6;
        List<List<Integer>> pairs = finder.findPairs(nums, k);
        for (List<Integer> pair : pairs) {
            System.out.println(pair);
        }
    }
}
