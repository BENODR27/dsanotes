import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                .skip(2)
                .filter(e -> e.startsWith("q"))
                .map(e -> e.toUpperCase())
                .forEach(System.out::println);

        // Count the number of words that start with 'a'
        long count = Arrays.stream(words)
                .filter(word -> word.startsWith("q"))
                .count();

        System.out.println("Number of words qrqrw with 'q': " + count);
        GreetingService service = msg -> System.out.println("Hello " + msg);

        service.greet("beno");
        System.out.println(Base64.getEncoder().encodeToString("Hello0 Worlddjfffffffffffffffffff".getBytes()));

        List<Integer> numbers = Arrays.asList(1, 2,2, 3, 4,4, 5);
        IntSummaryStatistics stats = numbers.stream()
                .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println(stats);

        CompletableFuture.supplyAsync(() -> "Data")
                .thenApply(data -> {
                    System.out.println("Processing " + data);
                    return data;
                })
                .thenAccept(System.out::println);

        int sum = String.valueOf(12345).chars().map(Character::getNumericValue).sum();
        System.out.println("Sum of digits: " + sum);
        String input = "swiss";

        Character result = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(e -> e.getKey())
                .findFirst()
                .orElse(null);

        System.out.println("First non-repeating character: " + result);
         Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicatesAlt = numbers.stream()
            .filter(n -> !seen.add(n))
            .collect(Collectors.toSet());
       

        System.out.println("Duplicates: " + duplicatesAlt);

    }
}