import java.util.Arrays;
import java.util.Base64;

public class StreamTest {
    interface GreetingService {
        void greet(String message);

        static void stop() {
            System.out.println("Vehicle stopped");
        }
    }

    public static void main(String[] args) {

        // Example usage of the Stream API
        String[] words = { "fgdfgsg", "qrqr", "fdafsa", "qeqwe" };

        // Convert to uppercase and print each word
        Arrays.stream(words)
                .filter(e -> e.startsWith("q"))
                .map(String::toLowerCase)
                .forEach(System.out::println);

        // Count the number of words that start with 'a'
        long count = Arrays.stream(words)
                .filter(word -> word.startsWith("q"))
                .count();

        System.out.println("Number of words qrqrw with 'q': " + count);
        GreetingService service = msg -> System.out.println("Hello " + msg);

        service.greet("beno");
        System.out.println(Base64.getEncoder().encodeToString("Hello0 Worlddjfffffffffffffffffff".getBytes()));

    }
}