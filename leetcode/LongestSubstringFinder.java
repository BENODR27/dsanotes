package leetcode;

import java.util.HashMap;

public class LongestSubstringFinder {
    // https://leetcode.com/problems/longest-substring-without-repeating-characters/description/
    public int lengthOfLongestSubstring(String s) {
        // HashMap to store the latest index of each character
        HashMap<Character, Integer> map = new HashMap<>();

        int maxLength = 0; // To store the maximum length of substring found
        int start = 0; // Start of the sliding window

        // Iterate through the string with 'end' as the end of the sliding window
        for (int end = 0; end < s.length(); end++) {
            char currentChar = s.charAt(end);

            // If the character is found in the map and is within the current window
            if (map.containsKey(currentChar) && map.get(currentChar) >= start) {
                // Move the start pointer to the right of the last occurrence of currentChar
                start = map.get(currentChar) + 1;
            }

            // Update the latest index of the current character
            map.put(currentChar, end);

            // Calculate the length of the current substring and update maxLength
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        LongestSubstringFinder finder = new LongestSubstringFinder();

        // Test Case 1
        String s1 = "abcabcbb";
        System.out.println("Output: " + finder.lengthOfLongestSubstring(s1)); // Output: 3

        // Test Case 2
        String s2 = "bbbbb";
        System.out.println("Output: " + finder.lengthOfLongestSubstring(s2)); // Output: 1

        // Test Case 3
        String s3 = "pwwkew";
        System.out.println("Output: " + finder.lengthOfLongestSubstring(s3)); // Output: 3
    }
}