import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;

public class ValidParentheses {
    private static final Map<Character, Character> PAIRS = Map.of(
            ')', '(',
            '}', '{',
            ']', '[');

    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char ch : s.toCharArray()) {
            if (PAIRS.containsValue(ch)) {
                stack.push(ch);
            } else if (PAIRS.containsKey(ch)) {
                if (stack.isEmpty() || stack.pop() != PAIRS.get(ch)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String input = "{[()]}";
        System.out.println("Is valid? " + isValid(input));
    }
}
