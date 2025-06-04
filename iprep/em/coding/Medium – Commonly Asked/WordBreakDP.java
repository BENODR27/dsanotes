import java.util.*;

public class WordBreakDP {
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict); // Fast lookup
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true; // Empty string is always "breakable"

        // Try to break s[0...i)
        for (int i = 1; i <= s.length(); i++) {
            // Try all positions j before i
            for (int j = 0; j < i; j++) {
                // Check if s[0..j) is breakable AND s[j..i) is in wordDict
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // No need to check further
                }
            }
        }

        return dp[s.length()];
    }

    public static void main(String[] args) {
        String s = "leetcode";
        List<String> wordDict = Arrays.asList("leet", "code");
        System.out.println(wordBreak(s, wordDict)); // true
    }
}
