class Solution {
    public String reverseWords(String s) {
        // Trim leading and trailing spaces and split the string by spaces
        String[] words = s.trim().split("\\s+");

        // Use a StringBuilder to reverse the words
        StringBuilder reversed = new StringBuilder();

        // Iterate through the words array in reverse order
        for (int i = words.length - 1; i >= 0; i--) {
            reversed.append(words[i]);
            if (i != 0) {
                reversed.append(" "); // Add a space between words
            }
        }

        return reversed.toString();
    }
    public static void main(String[] args) {
        Solution solution = new Solution();
        String s = "  Hello World  ";
        String result = solution.reverseWords(s);
        System.out.println(result); // Output: "World Hello"
    }
}
