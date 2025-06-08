import java.util.*;

public class GroupAnagrams {

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String str : strs) {
            // Step 1: Sort the string to get the key
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);

            // Step 2: Add to map
            map.computeIfAbsent(sorted, k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        GroupAnagrams ga = new GroupAnagrams();
        String[] input = { "eat", "tea", "tan", "ate", "nat", "bat" };
        List<List<String>> result = ga.groupAnagrams(input);

        for (List<String> group : result) {
            System.out.println(group);
        }
    }
}
