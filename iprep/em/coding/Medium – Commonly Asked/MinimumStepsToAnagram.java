import java.util.*;

public class MinimumStepsToAnagram {

    public static int minSteps(String s, String t) {
        int[] count = new int[26]; // For 'a' to 'z'

        // Count characters from string s
        for (char ch : s.toCharArray()) {
            count[ch - 'a']++;
        }

        // Subtract characters found in string t
        for (char ch : t.toCharArray()) {
            count[ch - 'a']--;
        }

        // Sum up absolute differences
        int steps = 0;
        for (int c : count) {
            steps += Math.abs(c);
        }

        return steps;
    }

    public static void main(String[] args) {
        String s1 = "leetcode";
        String s2 = "practice";
        System.out.println("Minimum steps: " + minSteps(s1, s2)); // Output: 5
    }
}

// Input: s = "bab", t = "aba"
// Output: 0
// Explanation: They are already anagrams.

// Input: s = "leetcode", t = "practice"
// Output: 5
// Explanation: Delete 'l', 'd', 'p', 'r', 'a'
