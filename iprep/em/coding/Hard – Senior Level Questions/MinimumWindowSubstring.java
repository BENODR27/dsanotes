import java.util.*;

public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length())
            return "";

        Map<Character, Integer> targetMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> windowMap = new HashMap<>();
        int left = 0, right = 0, minLen = Integer.MAX_VALUE;
        int matchCount = 0;
        int start = 0;

        while (right < s.length()) {
            char ch = s.charAt(right);
            windowMap.put(ch, windowMap.getOrDefault(ch, 0) + 1);

            if (targetMap.containsKey(ch) &&
                    windowMap.get(ch).intValue() <= targetMap.get(ch).intValue()) {
                matchCount++;
            }

            // Try to shrink the window from the left
            while (matchCount == t.length()) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                char leftChar = s.charAt(left);
                windowMap.put(leftChar, windowMap.get(leftChar) - 1);

                if (targetMap.containsKey(leftChar) &&
                        windowMap.get(leftChar).intValue() < targetMap.get(leftChar).intValue()) {
                    matchCount--;
                }
                left++;
            }

            right++;
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);

    }

    public static void main(String[] args) {
        MinimumWindowSubstring mws = new MinimumWindowSubstring();
        String s = "QWERQTY";
        String t = "QRQ";
        String result = mws.minWindow(s, t);
        System.out.println("Minimum window substring: " + result); // Output: "BANC"
    }
}
