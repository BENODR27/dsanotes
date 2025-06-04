import java.util.*;

public class LongestSubstringWithoutRepeating {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int maxLength = 0;
        int start = 0; // left boundary of the window

        for (int end = 0; end < s.length(); end++) {
            char currentChar = s.charAt(end);

            if (map.containsKey(currentChar)) {
                // move start to the right of previous occurrence
                start = Math.max(map.get(currentChar) + 1, start);
            }

            map.put(currentChar, end); 
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        LongestSubstringWithoutRepeating solver = new LongestSubstringWithoutRepeating();
        System.out.println(solver.lengthOfLongestSubstring("abcabcbb")); // 3
        System.out.println(solver.lengthOfLongestSubstring("bbbbb")); // 1
        System.out.println(solver.lengthOfLongestSubstring("pwwkew")); // 3
    }
}
