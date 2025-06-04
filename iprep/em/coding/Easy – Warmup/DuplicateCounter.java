import java.util.*;

public class DuplicateCounter {
    public static Map<Character, Integer> countDuplicates(String input) {
        Map<Character, Integer> countMap = new HashMap<>();

        // Convert to lowercase for case-insensitive comparison
        input = input.toLowerCase();

        // Count each character
        for (char ch : input.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) { // Optional: skip spaces/punctuations
                countMap.put(ch, countMap.getOrDefault(ch, 0) + 1);
            }
        }

        // Filter and collect characters that appear more than once
        Map<Character, Integer> duplicates = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicates;
    }

    public static void main(String[] args) {
        String input = "Programming";
        Map<Character, Integer> result = countDuplicates(input);
        System.out.println("Duplicates: " + result);
    }
}