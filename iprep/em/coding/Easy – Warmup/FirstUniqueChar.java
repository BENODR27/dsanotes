import java.util.*;

public class FirstUniqueChar {
    public static char firstNonRepeatingChar(String s) {
        Map<Character, Integer> countMap = new LinkedHashMap<>();

        // Count each character
        for (char ch : s.toCharArray()) {
            countMap.put(ch, countMap.getOrDefault(ch, 0) + 1);
        }

        // Find the first character with count 1
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }

        return '_'; // If no non-repeating character found
    }

    public static void main(String[] args) {
        String input = "loveleetcode";
        char result = firstNonRepeatingChar(input);
        System.out.println("First non-repeating character: " + result);
    }
}
