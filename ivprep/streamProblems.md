Here’s a powerful collection of **Java Stream-based problems** similar to:

```java
int sum = String.valueOf(12345).chars().map(Character::getNumericValue).sum();
```

These problems involve converting strings/numbers, transforming characters, and performing functional-style operations—frequently asked in coding rounds and interviews at top companies (Google, Amazon, TCS, Infosys, Accenture, Emirates Group IT, etc.).

---

## ✅ 1. **Sum of Digits in a Number**

```java
int sum = String.valueOf(98765)
                .chars()
                .map(Character::getNumericValue)
                .sum();
// Output: 35
```

---

## ✅ 2. **Count of Even Digits**

```java
long evenCount = String.valueOf(246813579)
                       .chars()
                       .map(Character::getNumericValue)
                       .filter(n -> n % 2 == 0)
                       .count();
// Output: 4
```

---

## ✅ 3. **Product of All Digits**

```java
int product = String.valueOf(1234)
                    .chars()
                    .map(Character::getNumericValue)
                    .reduce(1, (a, b) -> a * b);
// Output: 24
```

---

## ✅ 4. **Reverse Digits of a Number**

```java
String reversed = new StringBuilder(String.valueOf(12345))
                        .reverse()
                        .toString();
// Output: "54321"
```

(If you want it as `int`, use `Integer.parseInt(reversed)`)

---

## ✅ 5. **Check if Number is Palindrome**

```java
int number = 1221;
boolean isPalindrome = String.valueOf(number)
                            .equals(new StringBuilder(String.valueOf(number)).reverse().toString());
// Output: true
```

---

## ✅ 6. **Remove Duplicate Digits in a Number**

```java
String unique = String.valueOf(1122334455)
                      .chars()
                      .distinct()
                      .mapToObj(c -> String.valueOf((char) c))
                      .collect(Collectors.joining());
// Output: "12345"
```

---

## ✅ 7. **Sort Digits of a Number**

```java
String sorted = String.valueOf(4312)
                      .chars()
                      .sorted()
                      .mapToObj(c -> String.valueOf((char) c))
                      .collect(Collectors.joining());
// Output: "1234"
```

---

## ✅ 8. **Find the Largest Digit in a Number**

```java
int maxDigit = String.valueOf(3951724)
                     .chars()
                     .map(Character::getNumericValue)
                     .max()
                     .orElse(0);
// Output: 9
```

---

## ✅ 9. **Find the Smallest Digit in a Number**

```java
int minDigit = String.valueOf(3951724)
                     .chars()
                     .map(Character::getNumericValue)
                     .min()
                     .orElse(0);
// Output: 1
```

---

## ✅ 10. **Check if All Digits Are Unique**

````java
int num = 123456;
boolean allUnique = String.valueOf(num)
                          .chars()
                          .distinct()
                          .count() == String.valueOf(num).length();
Here is a curated list of **Java Stream-based problems** that are commonly asked in technical interviews, especially for mid-to-senior-level roles. Each includes a **problem statement** and a **professional solution** using Java 8+ Streams.

---

## ✅ 1. Check if a Number is Prime

**Already covered above.**

---

## ✅ 2. Find All Prime Numbers in a Range

### 🧩 Problem:

Find all prime numbers between 1 and 100 using Java Streams.

### ✅ Solution:

```java
import java.util.stream.IntStream;

public class PrimeRange {
    public static void main(String[] args) {
        IntStream.rangeClosed(2, 100)
                 .filter(PrimeRange::isPrime)
                 .forEach(System.out::println);
    }

    public static boolean isPrime(int num) {
        return num > 1 &&
               IntStream.rangeClosed(2, (int) Math.sqrt(num))
                        .noneMatch(i -> num % i == 0);
    }
}
````

---

## ✅ 3. Find First Non-Repeated Character in a String

### 🧩 Problem:

Given a string, find the first non-repeated character using Streams.

### ✅ Solution:

```java
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstUniqueChar {
    public static void main(String[] args) {
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
    }
}
```

---

## ✅ 4. Count Frequency of Characters in a String

### 🧩 Problem:

Given a string, count how many times each character appears.

### ✅ Solution:

```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CharFrequency {
    public static void main(String[] args) {
        String input = "banana";

        Map<Character, Long> freq = input.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        freq.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
```

---

## ✅ 5. Sort a List of Strings by Length

### 🧩 Problem:

Sort a list of strings by their length using streams.

### ✅ Solution:

```java
import java.util.Arrays;
import java.util.List;

public class SortByLength {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("apple", "banana", "kiwi", "grape");

        names.stream()
            .sorted((a, b) -> Integer.compare(a.length(), b.length()))
            .forEach(System.out::println);
    }
}
```

---

## ✅ 6. Reverse Each Word in a Sentence

### 🧩 Problem:

Given a sentence, reverse each word but keep the word order.

### ✅ Solution:

```java
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReverseWords {
    public static void main(String[] args) {
        String sentence = "hello world java";

        String result = Arrays.stream(sentence.split(" "))
            .map(word -> new StringBuilder(word).reverse().toString())
            .collect(Collectors.joining(" "));

        System.out.println(result); // "olleh dlrow avaj"
    }
}
```

---

## ✅ 7. Find Duplicates in a List

### 🧩 Problem:

Given a list of integers, find all duplicate elements using streams.

### ✅ Solution:

```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindDuplicates {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 4, 5, 1, 6);

        Set<Integer> duplicates = numbers.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .filter(e -> e.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());

        System.out.println("Duplicates: " + duplicates);
    }
}
```

---

## ✅ 8. Check if Two Strings are Anagrams

### 🧩 Problem:

Given two strings, check if they are anagrams using Java Streams.

### ✅ Solution:

```java
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnagramChecker {
    public static void main(String[] args) {
        String s1 = "listen";
        String s2 = "silent";

        boolean isAnagram = s1.length() == s2.length() &&
            s1.chars().mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .equals(
            s2.chars().mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));

        System.out.println("Are anagrams: " + isAnagram);
    }
}
```

Here’s **Part 2** of globally-asked, Java Streams–based problems similar to:

```java
String.valueOf(...).chars().map(Character::getNumericValue)...
```

These problems continue with **numbers, strings, and character processing** — ideal for mid-to-senior Java interviews.

---

## ✅ 11. **Sum of Odd Digits in a Number**

```java
int sumOdd = String.valueOf(13579)
                   .chars()
                   .map(Character::getNumericValue)
                   .filter(n -> n % 2 != 0)
                   .sum();
// Output: 25
```

---

## ✅ 12. **Extract Only Even Digits**

```java
String evenDigits = String.valueOf(24813576)
                          .chars()
                          .mapToObj(c -> (char) c)
                          .filter(ch -> Character.getNumericValue(ch) % 2 == 0)
                          .map(String::valueOf)
                          .collect(Collectors.joining());
// Output: "2486"
```

---

## ✅ 13. **Remove All Zeros from a Number**

```java
String noZeros = String.valueOf(1023040)
                       .chars()
                       .filter(c -> c != '0')
                       .mapToObj(c -> String.valueOf((char) c))
                       .collect(Collectors.joining());
// Output: "1234"
```

---

## ✅ 14. **Sum of Squares of Digits**

```java
int sumSquares = String.valueOf(123)
                       .chars()
                       .map(Character::getNumericValue)
                       .map(n -> n * n)
                       .sum();
// Output: 14 (1² + 2² + 3² = 14)
```

---

## ✅ 15. **Find Repeated Digits in a Number**

```java
Set<Integer> duplicates = String.valueOf(1123581321)
    .chars()
    .map(Character::getNumericValue)
    .boxed()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
    .entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .map(Map.Entry::getKey)
    .collect(Collectors.toSet());
// Output: [1, 2, 3]
```

---

## ✅ 16. **Count Frequency of Each Digit**

```java
Map<Integer, Long> freq = String.valueOf(11223344)
    .chars()
    .map(Character::getNumericValue)
    .boxed()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
// Output: {1=2, 2=2, 3=2, 4=2}
```

---

## ✅ 17. **Multiply Even Digits, Sum Odd Digits**

```java
int number = 123456;
int evenProduct = String.valueOf(number).chars()
        .map(Character::getNumericValue)
        .filter(n -> n % 2 == 0)
        .reduce(1, (a, b) -> a * b); // 2 * 4 * 6 = 48

int oddSum = String.valueOf(number).chars()
        .map(Character::getNumericValue)
        .filter(n -> n % 2 != 0)
        .sum(); // 1 + 3 + 5 = 9
```

---

## ✅ 18. **Print Digits in Descending Order**

```java
String descending = String.valueOf(938271)
    .chars()
    .sorted((a, b) -> b - a)
    .mapToObj(c -> String.valueOf((char) c))
    .collect(Collectors.joining());
// Output: "987321"
```

---

## ✅ 19. **Check if Digits Are in Ascending Order**

```java
String num = "123456";
boolean isAscending = num.chars()
    .map(Character::getNumericValue)
    .boxed()
    .reduce((a, b) -> a <= b ? b : Integer.MIN_VALUE)
    .orElse(Integer.MIN_VALUE) != Integer.MIN_VALUE;
// Output: true
```

---

## ✅ 20. **Find Missing Digits (0–9) in a Number**

```java
Set<Integer> presentDigits = String.valueOf(1235678)
    .chars()
    .map(Character::getNumericValue)
    .boxed()
    .collect(Collectors.toSet());

List<Integer> missingDigits = IntStream.rangeClosed(0, 9)
    .filter(n -> !presentDigits.contains(n))
    .boxed()
    .collect(Collectors.toList());
// Output: [0, 4, 9]
```

---

Would you like next:

- Similar stream problems for **strings and words**?
- Advanced problems (e.g., validation, transformation, checksum)?
- Add **JUnit tests** for each of these?
- Put everything into **GitHub project** or **PDF guide**?

Let me know how you'd like to use these next.
