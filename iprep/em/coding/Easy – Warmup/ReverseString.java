public class ReverseString {
    public static void reverseString(char[] s) {
        int left = 0, right = s.length - 1;
        while (left < right) {
            // swap characters
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;

            left++;
            right--;
        }
    }

    public static void main(String[] args) {
        char[] input = { 'h', 'e', 'l', 'l', 'o' };
        reverseString(input);
        System.out.println(input); // Output: olleh
    }
}
