Here's a **complete breakdown of Java 8 features** with **professional use cases** and **Java code examples** to help you master each one in depth:

---

### ✅ **Java 8 (2014) – The Most Transformative Release**

---

### 1. **Lambda Expressions**

#### 🔹 Use Case:

Simplify implementation of functional interfaces (e.g., event handlers, comparators, stream operations).

#### 🔹 Code Example:

```java
List<String> names = Arrays.asList("Tests", "Test2", "Java");
names.forEach(name -> System.out.println("Hello " + name));
```

#### 🔹 Professional Usage:

Used in enterprise code for clean collection processing, event-driven programming, and replacing anonymous inner classes in callbacks.

---

### 2. **Streams API**

#### 🔹 Use Case:

Functional-style operations for data processing pipelines: filtering, mapping, reducing.

#### 🔹 Code Example:

```java
List<String> names = Arrays.asList("Tom", "Bob", "Alice");
List<String> filtered = names.stream()
    .filter(name -> name.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
System.out.println(filtered); 
```

#### 🔹 Professional Usage:

Data transformation, aggregation, sorting, analytics over collections and DB result sets.

---

### 3. **Functional Interfaces** 

#### 🔹 Use Case:

Support for lambda expressions and functional-style code.

#### 🔹 Code Example:

```java
@FunctionalInterface
interface GreetingService {
    void greet(String message);
}

GreetingService service = msg -> System.out.println("Hello " + msg);
service.greet("World");
```

#### 🔹 Professional Usage:

Common in APIs like `Runnable`, `Callable`, `Comparator`, and custom business logic injections.

---

### 4. **Default and Static Methods in Interfaces**

#### 🔹 Use Case:

Add methods to interfaces without breaking existing implementations.

#### 🔹 Code Example:

```java
interface Vehicle {
    default void start() {
        System.out.println("Vehicle started");
    }

    static void stop() {
        System.out.println("Vehicle stopped");
    }
}
```

#### 🔹 Professional Usage:

Backward compatibility for API evolution (e.g., Java collections added `forEach`).

---

### 5. **Optional Class**

#### 🔹 Use Case:

Avoid null checks and `NullPointerException`. Encourage better error handling.

#### 🔹 Code Example:

```java
Optional<String> optionalName = Optional.of("Tests");
optionalName.ifPresent(name -> System.out.println(name.toUpperCase()));
```

#### 🔹 Professional Usage:

Used for method return types to clearly indicate possible absence of value (e.g., `findById()` in repositories).

---

### 6. **New Date and Time API (`java.time`)**

#### 🔹 Use Case:

Thread-safe and immutable date/time manipulation, replacing `Date` and `Calendar`.

#### 🔹 Code Example:

```java
LocalDate date = LocalDate.now();
LocalDate nextWeek = date.plusWeeks(1);
System.out.println("Today: " + date + " | Next Week: " + nextWeek);
```

#### 🔹 Professional Usage:

Time zone conversion, date arithmetic, scheduling systems, expiry tracking.

---

### 7. **Nashorn JavaScript Engine**

> ⚠️ Deprecated in Java 15+

#### 🔹 Use Case:

Run embedded JavaScript within Java apps (e.g., templating, DSL execution).

#### 🔹 Code Example:

```java
ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
engine.eval("print('Hello from JS')");
```

#### 🔹 Professional Usage:

Used in CMS systems or rule engines needing dynamic scripting (now replaced by GraalVM in modern setups).

```java
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class GraalVMExample {
    public static void main(String[] args) {
        // Create a GraalVM context for JavaScript
        try (Context context = Context.create("js")) {
            // JavaScript code to execute
            String script = "const greeting = 'Hello from GraalVM!'; greeting.toUpperCase();";

            // Evaluate the JavaScript code
            Value result = context.eval("js", script);

            // Print the result
            System.out.println(result.asString()); // Output: HELLO FROM GRAALVM!
        }
    }
}
```
---

### 8. **Parallel Arrays with `parallelSort()`**

#### 🔹 Use Case:

High-performance sorting of large arrays using multiple CPU cores.

#### 🔹 Code Example:

```java
int[] numbers = { 9, 3, 7, 1, 5 };
Arrays.parallelSort(numbers);
System.out.println(Arrays.toString(numbers)); // Output: [1, 3, 5, 7, 9]
```

#### 🔹 Professional Usage:

Optimizing data-heavy applications (financial analytics, big data preprocessing).

---

### 9. **Base64 Encoding/Decoding**

#### 🔹 Use Case:

Secure encoding of data for transmission (JWT, OAuth tokens, media uploads).

#### 🔹 Code Example:

```java
String original = "secret123";
String encoded = Base64.getEncoder().encodeToString(original.getBytes());
String decoded = new String(Base64.getDecoder().decode(encoded));
System.out.println(encoded); // c2VjcmV0MTIz
System.out.println(decoded); // secret123
```

#### 🔹 Professional Usage:

Common in API authentication, file encoding (e.g., images over REST), and cryptography.

---

Here is a **complete list of Java 8 features** including **major**, **minor**, and **hidden enhancements**, with **detailed descriptions**, **professional use cases**, and **examples** wherever applicable. These go beyond the core features you typically see mentioned.

---

## ✅ Complete Feature Set – **Java 8 (2014)**

---

### 1. **Lambda Expressions**

_(Already Covered Above)_

---

### 2. **Functional Interfaces & `@FunctionalInterface` Annotation**

#### 🔹 What is a Functional Interface?

A **functional interface** is an interface that contains exactly one abstract method. It can have any number of default or static methods but must have only one abstract method to qualify as a functional interface. Functional interfaces are the foundation of **lambda expressions** in Java.

#### 🔹 Key Characteristics:

- Contains exactly **one abstract method**.
- Can include **default** and **static methods**.
- Automatically treated as a functional interface if it meets the criteria, but you can explicitly annotate it with `@FunctionalInterface`.

#### 🔹 `@FunctionalInterface` Annotation:

The `@FunctionalInterface` annotation is used to indicate that an interface is intended to be a functional interface. It ensures that the interface adheres to the functional interface contract during compile time.

#### 🔹 Code Example:

```java
@FunctionalInterface
interface GreetingService {
    void greet(String message);

    // Default method
    default void sayHello() {
        System.out.println("Hello!");
    }

    // Static method
    static void sayGoodbye() {
        System.out.println("Goodbye!");
    }
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        GreetingService service = message -> System.out.println("Greeting: " + message);
        service.greet("Welcome to Java 8!");
        service.sayHello();
        GreetingService.sayGoodbye();
    }
}
```

#### 🔹 Use Case:

Functional interfaces are widely used in:

- **Lambda expressions**: Simplify the implementation of single-method interfaces.
- **Stream API**: Functional interfaces like `Predicate`, `Function`, `Consumer`, and `Supplier` are used extensively.
- **Event handling**: Replace anonymous inner classes with concise lambda expressions.

#### 🔹 Common Functional Interfaces in `java.util.function` Package:

| Interface       | Abstract Method Signature          | Description                                      |
|------------------|------------------------------------|--------------------------------------------------|
| `Predicate<T>`   | `boolean test(T t)`               | Represents a condition (e.g., filtering).       |
| `Function<T,R>`  | `R apply(T t)`                    | Transforms input of type `T` to output of type `R`. |
| `Consumer<T>`    | `void accept(T t)`                | Performs an action on the given input.          |
| `Supplier<T>`    | `T get()`                         | Supplies a value without taking any input.      |
| `BiFunction<T,U,R>` | `R apply(T t, U u)`            | Takes two inputs and produces a result.         |

Functional interfaces enable **functional programming** in Java, making code more concise, readable, and expressive.

_(Already Covered Above)_

---

### 3. **Streams API**

#### 🔹 What is Streams?

Streams in Java 8 are a new abstraction introduced to process sequences of elements in a functional style. They allow you to perform operations such as filtering, mapping, and reducing on collections or arrays in a declarative and concise manner. Streams are not data structures but pipelines of computations that operate on data.

#### 🔹 Key Characteristics:

- **Lazy Evaluation**: Operations on streams are not executed until a terminal operation is invoked.
- **Functional Programming**: Supports functional-style operations like `map`, `filter`, and `reduce`.
- **Parallel Processing**: Streams can be processed in parallel to leverage multi-core processors.
- **Immutable**: Streams do not modify the underlying data source.

#### 🔹 Use Case:

Functional-style operations for data processing pipelines: filtering, mapping, reducing.

#### 🔹 Code Example:

```java
List<String> names = Arrays.asList("Tom", "Bob", "Alice");
List<String> filtered = names.stream()
    .filter(name -> name.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
System.out.println(filtered); // Output: [ALICE]
```

#### 🔹 Professional Usage:

### Streams in Enterprise Applications with Examples

Streams are widely used in enterprise applications for:

- **Data Transformation and Mapping**: Transforming data from one form to another using operations like `map()` and `flatMap()`.  
    **Example**:
    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    List<Integer> nameLengths = names.stream()
            .map(String::length)
            .collect(Collectors.toList());
    System.out.println(nameLengths); // [5, 3, 7]
    ```

- **Filtering Data**: Using `filter()` to extract elements that meet specific criteria.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> evenNumbers = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
    System.out.println(evenNumbers); // [2, 4]
    ```

- **Sorting**: Sorting collections using `sorted()` for natural or custom order.  
    **Example**:
    ```java
    List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
    List<String> sortedNames = names.stream()
            .sorted()
            .collect(Collectors.toList());
    System.out.println(sortedNames); // [Alice, Bob, Charlie]
    ```

### **Aggregation and Reduction**

#### 🔹 Description:

Aggregation and reduction are operations that combine multiple elements of a stream into a single result.
 These operations are commonly used for tasks like summing, averaging, finding the maximum or minimum, and concatenating strings.

#### 🔹 Example: Summing Numbers

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream().reduce(0, Integer::sum);
System.out.println(sum); // Output: 15
```

#### 🔹 Example: Finding Maximum

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int max = numbers.stream().reduce(Integer.MIN_VALUE, Integer::max);
System.out.println(max); // Output: 5
```

#### 🔹 Example: Concatenating Strings

```java
List<String> words = Arrays.asList("Java", "is", "fun");
String sentence = words.stream().reduce("", (a, b) -> a + " " + b).trim();
System.out.println(sentence); // Output: Java is fun
```

#### 🔹 Use Case:

- Summing up numerical data (e.g., calculating total sales).
- Finding the maximum or minimum value in a dataset (e.g., highest score in a game).
- Concatenating strings for reporting or logging purposes.
- Performing custom aggregations in data processing pipelines.
- Simplifying complex calculations in functional programming workflows.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    int sum = numbers.stream()
            .reduce(0, Integer::sum);
    System.out.println(sum); // 15
    ```

- **Grouping and Partitioning**: Grouping data by attributes using `Collectors.groupingBy()`.  
    **Example**:
    ```java
    List<String> items = Arrays.asList("apple", "banana", "apricot", "blueberry");
    Map<Character, List<String>> grouped = items.stream()
            .collect(Collectors.groupingBy(item -> item.charAt(0)));
    System.out.println(grouped); // {a=[apple, apricot], b=[banana, blueberry]}
    ```

- **Joining Data**: Concatenating elements into a single string using `Collectors.joining()`.  
    **Example**:
    ```java
    List<String> words = Arrays.asList("Java", "is", "fun");
    String sentence = words.stream()
            .collect(Collectors.joining(" "));
    System.out.println(sentence); // Java is fun
    ```

- **Parallel Processing**: Leveraging `parallelStream()` for concurrent processing.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    int sum = numbers.parallelStream()
            .reduce(0, Integer::sum);
    System.out.println(sum); // 15
    ```

- **Pagination**: Using `skip()` and `limit()` to implement pagination.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    List<Integer> page = numbers.stream()
            .skip(3)
            .limit(3)
            .collect(Collectors.toList());
    System.out.println(page); // [4, 5, 6]
    ```

- **Deduplication**: Removing duplicate elements using `distinct()`.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 4);
    List<Integer> uniqueNumbers = numbers.stream()
            .distinct()
            .collect(Collectors.toList());
    System.out.println(uniqueNumbers); // [1, 2, 3, 4]
    ```

- **Debugging and Logging**: Using `peek()` to inspect elements during processing.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3);
    numbers.stream()
            .peek(n -> System.out.println("Processing: " + n))
            .map(n -> n * 2)
            .forEach(System.out::println);
    ```

- **Data Collection**: Collecting processed data into collections like `List`, `Set`, or `Map`.  
    **Example**:
    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    Set<String> nameSet = names.stream()
            .collect(Collectors.toSet());
    System.out.println(nameSet); // [Alice, Bob, Charlie]
    ```

- **Statistical Analysis**: Using `Collectors.summarizingInt()` to calculate statistics.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    IntSummaryStatistics stats = numbers.stream()
            .collect(Collectors.summarizingInt(Integer::intValue));
    System.out.println(stats); // IntSummaryStatistics{count=5, sum=15, min=1, average=3.000000, max=5}
    ```

- **Custom Reductions**: Using `reduce()` for custom aggregation logic.  
    **Example**:
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
    System.out.println(product); // 120
    ```

- **Event Processing**: Processing streams of events in real-time applications.  
    **Example**:
    ```java
    List<String> events = Arrays.asList("event1", "event2", "event3");
    events.stream()
            .filter(event -> event.startsWith("event"))
            .forEach(System.out::println);
    ```

- **File and I/O Operations**: Reading and processing files line by line.  
    **Example**:
    ```java
    try (Stream<String> lines = Files.lines(Paths.get("data.txt"))) {
            lines.filter(line -> line.contains("error"))
                    .forEach(System.out::println);
    } catch (IOException e) {
            e.printStackTrace();
    }
    ```

- **Database Result Set Processing**: Transforming and aggregating database results.  
    **Example**:
    ```java
    List<String> results = Arrays.asList("row1", "row2", "row3");
    List<String> processedResults = results.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    System.out.println(processedResults); // [ROW1, ROW2, ROW3]
    ```

- **Pipeline Processing**: Building complex data processing pipelines for analytics or ETL.  
    **Example**:
    ```java
    List<String> data = Arrays.asList("apple", "banana", "cherry");
    List<String> processedData = data.stream()
            .filter(item -> item.startsWith("a"))
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    System.out.println(processedData); // [APPLE]
    ```

- **Asynchronous Workflows**: Combining streams with `CompletableFuture`.  
    **Example**:
    ```java
    CompletableFuture.supplyAsync(() -> "Data")
            .thenApply(String::toUpperCase)
            .thenAccept(System.out::println); // DATA
    ```

Streams provide a declarative and functional approach to data processing, making them a powerful tool for simplifying complex workflows in enterprise applications.

- Data transformation and aggregation.
- Sorting and filtering collections.
- Performing analytics over collections or database result sets.
- Simplifying complex data processing pipelines.

--- 

_(Already Covered Above)_

---

### 4. **Default and Static Methods in Interfaces**

_(Already Covered Above)_

---

### 5. **Optional Class**

_(Already Covered Above)_

---

### 6. **New Date and Time API (`java.time`)**

_(Already Covered Above)_

Additional Classes:

### **Period, Duration, and ChronoUnit for Calculating Differences**

#### 🔹 Description:

Java 8 introduced `Period`, `Duration`, and `ChronoUnit` in the `java.time` package to calculate differences between dates and times.

---

#### **1. Period**

- Represents a date-based amount of time (e.g., years, months, days).
- Used for calculating differences between two `LocalDate` objects.

**Example:**

```java
LocalDate startDate = LocalDate.of(2020, 1, 1);
LocalDate endDate = LocalDate.of(2023, 6, 1);

Period period = Period.between(startDate, endDate);
System.out.println("Years: " + period.getYears());
System.out.println("Months: " + period.getMonths());
System.out.println("Days: " + period.getDays());
// Output: Years: 3, Months: 5, Days: 0
```

**Use Case:**
- Calculating age or time elapsed between two dates.

---

#### **2. Duration**

- Represents a time-based amount of time (e.g., hours, minutes, seconds).
- Used for calculating differences between two `LocalTime`, `LocalDateTime`, or `Instant` objects.

**Example:**

```java
LocalTime startTime = LocalTime.of(10, 30);
LocalTime endTime = LocalTime.of(12, 45);

Duration duration = Duration.between(startTime, endTime);
System.out.println("Hours: " + duration.toHours());
System.out.println("Minutes: " + duration.toMinutes());
// Output: Hours: 2, Minutes: 135
```

**Use Case:**
- Measuring elapsed time in hours, minutes, or seconds.

---

#### **3. ChronoUnit**

- Provides a more granular way to calculate differences between temporal objects.
- Supports units like `DAYS`, `HOURS`, `MINUTES`, `SECONDS`, etc.

**Example:**

```java
LocalDate startDate = LocalDate.of(2020, 1, 1);
LocalDate endDate = LocalDate.of(2023, 6, 1);

long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);

System.out.println("Days: " + daysBetween);
System.out.println("Months: " + monthsBetween);
// Output: Days: 1247, Months: 41
```

**Use Case:**
- Calculating differences in specific units (e.g., days, months, years).

---

### **DateTimeFormatter for Parsing/Formatting**

#### 🔹 Description:

`DateTimeFormatter` is used to parse and format date-time objects in Java 8. It provides predefined formats and allows custom patterns.

---

#### **1. Predefined Formats**

- `DateTimeFormatter.ISO_LOCAL_DATE`: Formats as `yyyy-MM-dd`.
- `DateTimeFormatter.ISO_LOCAL_DATE_TIME`: Formats as `yyyy-MM-dd'T'HH:mm:ss`.

**Example:**

```java
LocalDate date = LocalDate.now();
String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
System.out.println(formattedDate); // e.g., 2023-06-01
```

**Use Case:**
- Quickly format dates using standard ISO formats.

---

#### **2. Custom Patterns**

- Define custom patterns using symbols like `yyyy`, `MM`, `dd`, `HH`, `mm`, `ss`.

**Example:**

```java
LocalDateTime dateTime = LocalDateTime.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

String formattedDateTime = dateTime.format(formatter);
System.out.println(formattedDateTime); // e.g., 01-06-2023 14:30:45
```

**Use Case:**
- Formatting dates and times for user-friendly display or specific business requirements.

---

#### **3. Parsing Strings to Dates**

- Convert a string into a `LocalDate`, `LocalTime`, or `LocalDateTime` using `DateTimeFormatter`.

**Example:**

```java
String dateStr = "01-06-2023";
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

LocalDate parsedDate = LocalDate.parse(dateStr, formatter);
System.out.println(parsedDate); // 2023-06-01
```

**Use Case:**
- Parsing user input or data from external sources into date-time objects.

---

#### **4. Locale-Specific Formatting**

- Format dates and times based on a specific locale.

**Example:**

```java
LocalDate date = LocalDate.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.US);

String formattedDate = date.format(formatter);
System.out.println(formattedDate); // e.g., Thursday, June 01, 2023
```

**Use Case:**
- Displaying dates in a locale-specific format for internationalization.

---

These tools (`Period`, `Duration`, `ChronoUnit`, and `DateTimeFormatter`) provide powerful and flexible ways to handle date-time calculations and formatting in Java 8.

```java
LocalDate dob = LocalDate.of(1995, 5, 23);
Period age = Period.between(dob, LocalDate.now());
System.out.println("Age: " + age.getYears());
```

---

### 7. **Nashorn JavaScript Engine**

_(Already Covered Above)_

---

### 8. **Parallel Array Sorting**

_(Already Covered Above)_

---

### 9. **Base64 Encoding and Decoding API**

_(Already Covered Above)_

---

### 10. **Collectors API in Streams**

#### 🔹 Use Case:

Used with streams for grouping, partitioning, summarizing.

#### 🔹 Example:

```java
List<String> list = Arrays.asList("apple", "banana", "apricot");
Map<Character, List<String>> grouped = list.stream()
    .collect(Collectors.groupingBy(s -> s.charAt(0)));
System.out.println(grouped); // {a=[apple, apricot], b=[banana]}
```

---

### 11. **Repeatable Annotations**

#### 🔹 Description:

You can now apply the same annotation multiple times on a declaration.

#### 🔹 Example:

```java
@Schedule(day = "Monday")
@Schedule(day = "Tuesday")
public class Meeting {}

@Repeatable(Schedules.class)
@interface Schedule {
    String day();
}

@interface Schedules {
    Schedule[] value();
}
```
```

#### 🔹 Use Case:

Defining multiple metadata entries for tools, frameworks (e.g., scheduling, security roles).

---

### 12. **Type Annotations**

#### 🔹 Description:

Annotations can now be placed wherever a type is used.

#### 🔹 Example:

```java
@NonNull String message;
List<@NonNull String> messages;
```

> Requires external tools like Checker Framework to enforce.

#### 🔹 Use Case:

Enhances code analysis tools and null-safety verification in enterprise code.

---

### 13. **Method References**

#### 🔹 Description:

Shorthand for lambda expressions that just call an existing method.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("A", "B", "C");
names.forEach(System.out::println);
```

Types:

- Static: `Class::staticMethod`
- Instance: `obj::instanceMethod`
- Constructor: `Class::new`

---

### 14. **CompletableFuture API**

#### 🔹 Description:

Advanced asynchronous programming support with better chaining and composition.

#### 🔹 Example:

```java
CompletableFuture.supplyAsync(() -> "Data")
    .thenApply(String::toUpperCase)
    .thenAccept(System.out::println);
```

#### 🔹 Use Case:

Used for async workflows (non-blocking APIs, microservices), replacing `Future`.

---

### 15. **Java.util.function Package**

#### 🔹 Description:

Contains functional interfaces for use with lambdas and streams:

- `Function<T,R>`
- `Predicate<T>`
- `Consumer<T>`
- `Supplier<T>`

#### 🔹 Example:

```java
Predicate<String> isEmpty = String::isEmpty;
System.out.println(isEmpty.test("")); // true
```

---

### 16. **ForEach in Iterable**

#### 🔹 Description:

`Iterable` interface now includes `forEach()` default method.

#### 🔹 Example:

```java
List<String> list = List.of("Java", "8");
list.forEach(System.out::println);
```

---

### 17. **StringJoiner Class**

#### 🔹 Description:

Build delimited strings easily.

#### 🔹 Example:

```java
StringJoiner joiner = new StringJoiner(", ", "[", "]");
joiner.add("Java").add("8");
System.out.println(joiner); // [Java, 8]
```

---

### 18. **New Math Methods**

- `Math.addExact()`, `subtractExact()`, `multiplyExact()`, `floorDiv()`, `floorMod()`

#### 🔹 Use Case:

Prevent overflow silently going undetected in math ops.

#### 🔹 Example:

```java
int result = Math.addExact(10, 20); // throws ArithmeticException if overflow
```

---

### 19. **Concurrent Additions: `LongAdder`, `DoubleAdder`**

#### 🔹 Use Case:

High-throughput counters in concurrent environments.

#### 🔹 Example:

```java
LongAdder counter = new LongAdder();
counter.increment();
System.out.println(counter.intValue());
```

---

### 20. **Enhanced Security APIs**

- Improved TLS/SSL protocols.
- Support for stronger encryption algorithms.

---

### 21. **New Files & IO Enhancements (`java.nio.file`)**

- `Files.list()`, `Files.lines()`, `Files.find()` support Stream API.

```java
Files.lines(Paths.get("data.txt")).forEach(System.out::println);
```

---

### 22. **Metaspace Replaces PermGen**

#### 🔹 Description:

JVM memory management improvement — `PermGen` removed and replaced by `Metaspace`.

#### 🔹 Use Case:

Reduces `OutOfMemoryError: PermGen space`. Managed dynamically.

---

### 23. **Reflection Enhancements: `Parameter.getName()`**

```java
Method method = MyClass.class.getMethod("myMethod", String.class);
for (Parameter p : method.getParameters()) {
    System.out.println(p.getName()); // If compiled with -parameters
}
```

Here are **more Java 8 features** that you may find important, along with their **descriptions**, **professional use cases**, and **examples**. I'll continue from where we left off to ensure no feature is missed.

---

### 24. **`MethodHandles` API Enhancements**

#### 🔹 Description:

Introduces efficient method invocation by using `MethodHandles` which replaces reflection in many use cases for better performance.

#### 🔹 Example:

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle handle = lookup.findVirtual(String.class, "toUpperCase", MethodType.methodType(String.class));
String result = (String) handle.invokeExact("java");
System.out.println(result); // JAVA
```

#### 🔹 Use Case:

High-performance dynamic method invocation for critical systems or frameworks (e.g., low-latency applications).

---

### 25. **Enhanced `@Override` Annotation (for interface default methods)**

#### 🔹 Description:

You can now use `@Override` with methods implemented in interfaces, not just superclasses.

#### 🔹 Example:

```java
interface Animal {
    default void sound() {
        System.out.println("Generic animal sound");
    }
}

class Dog implements Animal {
    @Override
    public void sound() {
        System.out.println("Bark");
    }
}
```

#### 🔹 Use Case:

Clearer intent in code, especially for interfaces with default methods.

---

### 26. **`@FunctionalInterface` Annotation**

#### 🔹 Description:

Indicates that an interface is intended to have exactly one abstract method. Provides compile-time checking.

#### 🔹 Example:

```java
@FunctionalInterface
interface MyFunctionalInterface {
    void execute();
}
```

#### 🔹 Use Case:

Used in APIs that require functional interfaces, ensuring they follow the functional programming principles.

---

### 27. **`Spliterator` Interface**

#### 🔹 Description:

Provides a mechanism for traversing and partitioning elements of a collection in parallel, improving collection processing.

#### 🔹 Example:

```java
List<String> list = Arrays.asList("a", "b", "c", "d");
Spliterator<String> spliterator = list.spliterator();
spliterator.forEachRemaining(System.out::println);
```

#### 🔹 Use Case:

Efficient parallel traversal of large data structures (e.g., in distributed processing systems).

---

### 28. **`java.util.concurrent` Enhancements**

#### 🔹 **`StampedLock`**

`StampedLock` is a new type of lock introduced in Java 8 that provides an alternative to traditional read-write locks. It allows for optimistic reads, which can improve performance in scenarios where reads are more frequent than writes.

#### 🔹 Key Features:

- **Optimistic Reads**: Allows threads to read data without blocking, as long as no write occurs during the read.
- **Read and Write Locks**: Supports traditional read and write locks for scenarios requiring stricter synchronization.
- **Stamp-Based Validation**: Uses a stamp (long value) to validate whether the lock state has changed during an optimistic read.

#### 🔹 Code Example:

```java
import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {
    private double x, y;
    private final StampedLock lock = new StampedLock();

    // Write method
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    // Optimistic read method
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x, currentY = y;

        // Validate the stamp to ensure no write occurred
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

#### 🔹 Use Case:

- Optimizing performance in applications with frequent reads and infrequent writes, such as caching systems or analytics dashboards.

---

#### 🔹 **`CompletableFuture` Integration with `ExecutorService`**

`CompletableFuture` can be integrated with `ExecutorService` to handle asynchronous computations more effectively. By providing a custom executor, you can control the thread pool used for executing tasks.

#### 🔹 Code Example:

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureWithExecutor {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture.supplyAsync(() -> {
            System.out.println("Task running in: " + Thread.currentThread().getName());
            return "Result";
        }, executor).thenAccept(result -> {
            System.out.println("Processed result: " + result);
        });

        executor.shutdown();
    }
}
```

#### 🔹 Use Case:

- Managing thread pools for asynchronous tasks in high-performance applications, such as web servers or microservices.

These enhancements in `java.util.concurrent` provide powerful tools for building scalable and efficient concurrent applications.

---

### 29. **`Collectors` Utility Class Enhancements**

#### 🔹 Description:

Java 8 enhanced the `Collectors` class with more useful static methods, such as:

- `joining()`
- `groupingBy()`
- `partitioningBy()`
- `counting()`
- `summarizingInt()`

#### 🔹 Example:

```java
List<String> names = Arrays.asList("John", "Jane", "Jack");
String result = names.stream()
    .collect(Collectors.joining(", "));
System.out.println(result); // John, Jane, Jack
```

#### 🔹 Use Case:

Group, summarize, and join collections efficiently in business logic or reporting tasks.

---

### 30. **`Optional` Enhancements**

#### 🔹 Description:

New methods were added to the `Optional` class to make it more powerful:

- `ifPresentOrElse()`
- `or()`
- `stream()`

#### 🔹 Example:

```java
Optional<String> optional = Optional.of("Tests");
optional.ifPresentOrElse(
    System.out::println,
    () -> System.out.println("No value present")
);
```

#### 🔹 Use Case:

Used in business applications to avoid null checks and streamline control flows, especially for method return types.

---

### 31. **`Predicate` Enhancements**

#### 🔹 Description:

`Predicate` interface was enhanced with default methods like `and()`, `or()`, and `negate()` to combine conditions.

#### 🔹 Example:

```java
Predicate<Integer> isEven = num -> num % 2 == 0;
Predicate<Integer> isGreaterThan10 = num -> num > 10;

System.out.println(isEven.and(isGreaterThan10).test(12)); // true
```

#### 🔹 Use Case:

Common in filters for collection processing, validation logic, and conditional business rules.

---

### 32. **Method References in Collections**

#### 🔹 Description:

Java 8 allows using method references in collection operations like `forEach`, `map`, `filter`, etc.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Tom", "Jerry", "Spike");
names.forEach(System.out::println);
```

#### 🔹 Use Case:

Clean and concise iteration over collections, common in data pipelines, UI frameworks, and event-driven systems.

---

### 33. **`try-with-resources` Enhancements**

#### 🔹 Description:

Java 8 improved the `try-with-resources` statement to automatically close resources that implement `AutoCloseable` (like `Closeable`).

#### 🔹 Example:

```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    System.out.println(reader.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
```

#### 🔹 Use Case:

Helps manage resources like database connections, file handlers, network sockets, and streams without manually closing them.

---

### 34. **Lambda Expressions in `Comparator`**

#### 🔹 Description:

`Comparator` interface is enhanced to support lambda expressions, reducing verbosity and simplifying sorting operations.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Tom", "Jerry", "Alice");
names.sort((a, b) -> a.compareTo(b)); // Sorting using lambda
System.out.println(names);
```

#### 🔹 Use Case:

Commonly used in sorting collections, databases (sorting result sets), and APIs for easy configuration of sorting logic.

---

### 35. **`@SuppressWarnings` Enhancements**

#### 🔹 Description:

Introduced to suppress warnings on specific methods/parameters related to lambda expressions.

---

### 36. **String Buffer and String Builder Enhancements**

#### 🔹 Description:

Java 8 introduced some minor optimizations and methods to `StringBuffer` and `StringBuilder` for performance improvements.

#### 🔹 Example:

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
System.out.println(sb.toString()); // Hello World
```

#### 🔹 Use Case:

Used in scenarios where string concatenation occurs frequently in performance-sensitive code.

---

### 37. **`@SafeVarargs` Annotation**

#### 🔹 Description:

This annotation is used to suppress warnings related to varargs in a method that is guaranteed to not introduce unsafe operations.

#### 🔹 Example:

```java
@SafeVarargs
public static <T> void printItems(T... items) {
    for (T item : items) {
        System.out.println(item);
    }
}
```

---

### 38. **Enhanced `String` Methods**

#### 🔹 Description:

Several new methods were added to the `String` class:

- `String.join()`
- `String.lines()`
- `String.strip()`
- `String.stripLeading()`
- `String.stripTrailing()`

#### 🔹 Example:

```java
String result = String.join(",", "a", "b", "c");
System.out.println(result); // a,b,c
```

---

### 39. **Improved Exception Handling**

#### 🔹 Description:

Java 8 introduced better exception handling mechanisms when using lambdas, like `LambdaConversionException`.

---

Here are **additional features and topics** from **Java 8**, continuing where we left off, to cover everything comprehensively:

---

### 40. **`default` Methods in Interfaces (Expanded)**

#### 🔹 Description:

Java 8 introduced the ability to define methods in interfaces with a `default` implementation. This allows new methods to be added to interfaces without breaking backward compatibility.

#### 🔹 Example:

```java
interface Animal {
    default void sound() {
        System.out.println("Generic animal sound");
    }
}

class Dog implements Animal {
    // Override the default sound method
    public void sound() {
        System.out.println("Bark");
    }
}
```

#### 🔹 Use Case:

Used extensively in APIs like `Collections` to allow the addition of new methods while maintaining backward compatibility with older versions of interfaces.

---

### 41. **`Stream.reduce()`**

#### 🔹 Description:

The `reduce()` method in the `Stream` API is used to perform a reduction on the elements of the stream using an associative accumulation function and returns an `Optional`.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream().reduce(0, (a, b) -> a + b); // Sum of numbers
System.out.println(sum); // 15
```

#### 🔹 Use Case:

Used in scenarios where a single result (e.g., sum, average, concatenation) needs to be calculated from a stream of data.

---

### 42. **`Stream.forEachOrdered()`**

#### 🔹 Description:

In contrast to `forEach()`, `forEachOrdered()` guarantees the processing order when using parallel streams.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("John", "Jane", "Tom");
names.parallelStream().forEachOrdered(System.out::println);
```

#### 🔹 Use Case:

Useful when order matters in parallel stream processing (e.g., displaying results in a UI in order of input).

---

### 43. **`AtomicReference` API (Improved)**

#### 🔹 Description:

Java 8 improved `AtomicReference` to support better thread safety and more efficient manipulation of reference variables.

#### 🔹 Example:

```java
AtomicReference<String> ref = new AtomicReference<>("initial");
ref.updateAndGet(s -> s + " updated");
System.out.println(ref.get()); // initial updated
```

#### 🔹 Use Case:

Helps in managing thread-safe, mutable objects, especially in concurrent applications.

---

### 44. **`Stream.peek()`**

#### 🔹 Description:

This method allows you to inspect the elements of the stream as they pass through without modifying the stream itself. It is mainly used for debugging.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream()
       .peek(n -> System.out.println("Processing: " + n))
       .map(n -> n * 2)
       .forEach(System.out::println);
```

#### 🔹 Use Case:

Can be used for logging or debugging purposes in stream pipelines.

---

### 45. **`Optional.orElseThrow()`**

#### 🔹 Description:

This method allows you to throw an exception when an `Optional` is empty, replacing the need for `ifPresent` and manually checking for values.

#### 🔹 Example:

```java
Optional<String> name = Optional.ofNullable(null);
String result = name.orElseThrow(() -> new IllegalArgumentException("Value is missing"));
```

#### 🔹 Use Case:

Simplifies error handling when dealing with missing values in business logic.

---

### 46. **`Supplier<T>` Interface Enhancements**

#### 🔹 Description:

The `Supplier<T>` interface in Java 8 can now be used in many scenarios to lazily generate values, such as with the `Optional` class and `Stream` operations.

#### 🔹 Example:

```java
Supplier<String> supplier = () -> "Hello, World!";
System.out.println(supplier.get()); // Hello, World!
```

#### 🔹 Use Case:

Used for deferred execution and lazy initialization, such as in caching mechanisms or deferred computations.

---

### 47. **`Stream.collect()` Enhancements**

#### 🔹 Description:

In addition to the standard collectors like `toList()`, `toSet()`, Java 8 introduces new built-in collectors like `groupingBy()`, `partitioningBy()`, and `joining()`.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
String result = names.stream().collect(Collectors.joining(", "));
System.out.println(result); // Alice, Bob, Charlie
```

#### 🔹 Use Case:

Used in data transformations, reporting, and collection aggregation scenarios.

---

### 48. **`new HashMap()` with `computeIfAbsent()`**

#### 🔹 Description:

`computeIfAbsent()` method in `Map` allows for efficient computation of a value if it's absent in the map, reducing the need for explicit null checks.

#### 🔹 Example:

```java
Map<String, Integer> map = new HashMap<>();
map.computeIfAbsent("a", key -> key.length());
System.out.println(map.get("a")); // 1
```

#### 🔹 Use Case:

Efficiently populate or initialize maps in caching and mapping operations.

---

### 49. **New `Default` Methods in `Iterable` Interface**

#### 🔹 Description:

Java 8 adds the `forEach()` method to the `Iterable` interface, enabling iteration through collections using lambda expressions.

#### 🔹 Example:

```java
List<String> list = Arrays.asList("Apple", "Banana", "Cherry");
list.forEach(System.out::println);
```

#### 🔹 Use Case:

Simplifies collection iteration with lambda expressions, improving readability and reducing boilerplate.

---

### 50. **`Files` Class Enhancements**

#### 🔹 Description:

New utility methods were added to the `java.nio.file.Files` class to enhance file operations, such as `copy()`, `move()`, `delete()`, and more.

#### 🔹 Example:

```java
Path source = Paths.get("source.txt");
Path destination = Paths.get("destination.txt");
Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
```

#### 🔹 Use Case:

Essential for efficient file handling in I/O-intensive applications.

---

### 51. **`Comparator.reverseOrder()` and `Comparator.naturalOrder()`**

#### 🔹 Description:

Java 8 provides two static methods for natural order and reverse order comparison in `Comparator`.

#### 🔹 Example:

```java
List<Integer> list = Arrays.asList(3, 2, 1);
list.sort(Comparator.naturalOrder());
System.out.println(list); // [1, 2, 3]

list.sort(Comparator.reverseOrder());
System.out.println(list); // [3, 2, 1]
```

#### 🔹 Use Case:

Useful in sorting and ordering collections in both ascending and descending orders.

---

### 52. **`Stream.sorted()`**

#### 🔹 Description:

The `Stream.sorted()` method allows sorting elements of a stream either in natural order or using a custom comparator.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Bob", "Alice", "Charlie");
names.stream()
     .sorted()
     .forEach(System.out::println);
```

#### 🔹 Use Case:

Sorting operations, especially when combined with stream processing in business logic and reporting.

---

### 53. **`Stream.distinct()`**

#### 🔹 Description:

The `distinct()` method removes duplicate elements from the stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 4);
numbers.stream()
       .distinct()
       .forEach(System.out::println); // 1, 2, 3, 4
```

#### 🔹 Use Case:

Used in data cleanup and filtering tasks, especially when handling user data or processing large datasets.

---

### 54. **`Stream.flatMap()`**

#### 🔹 Description:

The `flatMap()` method allows you to flatten a stream of collections into a single stream.

#### 🔹 Example:

```java
List<List<String>> listOfLists = Arrays.asList(
    Arrays.asList("A", "B"),
    Arrays.asList("C", "D")
);
listOfLists.stream()
           .flatMap(Collection::stream)
           .forEach(System.out::println); // A, B, C, D
```

#### 🔹 Use Case:

Common in scenarios where collections need to be flattened (e.g., processing a list of records or documents).

---

Here are **additional features and topics** from **Java 8** that weren't covered yet:

---

### 55. **`Stream` and Parallel Streams**

#### 🔹 Description:

Java 8 introduced parallel streams, which allow you to easily process data in parallel. This can lead to performance improvements for large datasets by utilizing multiple CPU cores.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.parallelStream()
                 .mapToInt(Integer::intValue)
                 .sum();
System.out.println(sum); // 15
```

#### 🔹 Use Case:

Ideal for processing large collections in parallel, such as statistical analysis, large-scale data transformations, etc.

---

### 56. **`Stream.findAny()`**

#### 🔹 Description:

Returns an arbitrary element from the stream. It is especially useful when working with parallel streams, as it doesn't guarantee any specific order.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("John", "Jane", "Tom");
names.stream()
     .findAny()
     .ifPresent(System.out::println); // Could be any name
```

#### 🔹 Use Case:

Used when the order of elements doesn't matter and you need a quick result, particularly in parallel processing scenarios.

---

### 57. **`Stream.allMatch()`, `Stream.anyMatch()`, `Stream.noneMatch()`**

#### 🔹 Description:

These methods are used to test whether all, any, or none of the elements in the stream satisfy the provided predicate.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0);  // False
boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);  // True
boolean noneEven = numbers.stream().noneMatch(n -> n % 2 == 0); // False
```

#### 🔹 Use Case:

Commonly used in filtering scenarios, such as validating user data or checking conditions over a dataset.

---

### 58. **`Stream.count()`**

#### 🔹 Description:

Returns the count of elements in the stream.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
long count = names.stream().count();
System.out.println(count); // 3
```

#### 🔹 Use Case:

Used when you need to quickly determine the number of items in a collection or stream, commonly used in pagination or UI data display.

---

### 59. **`Stream.limit()`**

#### 🔹 Description:

This method returns a stream consisting of the first `n` elements of the original stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream().limit(3).forEach(System.out::println); // 1, 2, 3
```

#### 🔹 Use Case:

Useful when you only need a subset of data, for example in pagination or when handling large data sets but only interested in the top N results.

---

### 60. **`Stream.skip()`**

#### 🔹 Description:

Skips the first `n` elements in the stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream().skip(2).forEach(System.out::println); // 3, 4, 5
```

#### 🔹 Use Case:

Used when you want to skip a certain number of elements from the start, e.g., when working with paging or range-based data.

---

### 61. **`Stream.flatMapToInt()` and Other `flatMapTo` Methods**

#### 🔹 Description:

`flatMapToInt()`, `flatMapToDouble()`, and `flatMapToLong()` are variations of `flatMap()` for primitive data types.

#### 🔹 Example:

```java
List<List<Integer>> listOfLists = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5, 6)
);
listOfLists.stream()
           .flatMapToInt(list -> list.stream().mapToInt(Integer::intValue))
           .forEach(System.out::println);
```

#### 🔹 Use Case:

Used for transforming nested collections into a single stream of primitive types (int, long, double) for more efficient processing.

---

### 62. **`Stream` API for Grouping**

#### 🔹 Description:

Java 8 introduced several grouping operations using the `Collectors.groupingBy()` method, allowing you to group elements based on a classifier function.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Alice", "Bob", "Tom", "Anna", "Charlie");
Map<Integer, List<String>> groupedByLength = names.stream()
    .collect(Collectors.groupingBy(String::length));
System.out.println(groupedByLength);
```

#### 🔹 Use Case:

Helpful in business applications like categorizing products, customers, or orders by certain attributes.

---

### 63. **`Stream` API for Partitioning**

#### 🔹 Description:

`Collectors.partitioningBy()` partitions the elements of a stream into two groups based on a predicate.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
System.out.println(partitioned);
```

#### 🔹 Use Case:

Used in scenarios where you need to split data into two categories, such as filtering even and odd numbers.

---

### 64. **`Stream` API for Joining Strings**

#### 🔹 Description:

Java 8 introduced the `Collectors.joining()` method to concatenate stream elements into a single string.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Hello", "World");
String sentence = words.stream().collect(Collectors.joining(" ", "[", "]"));
System.out.println(sentence); // [Hello World]
```

#### 🔹 Use Case:

Used for string concatenation in reports, messages, or any scenario where a combined string output is required.

---

### 65. **`Stream` API for Mapping**

#### 🔹 Description:

The `map()` function transforms the elements of a stream by applying a function.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("john", "paul", "george", "ringo");
List<String> capitalizedNames = names.stream()
    .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
    .collect(Collectors.toList());
System.out.println(capitalizedNames); // [John, Paul, George, Ringo]
```

#### 🔹 Use Case:

Commonly used to modify or transform data, like applying a function to elements before processing or output.

---

### 66. **`Stream` API for Collecting to Set**

#### 🔹 Description:

Java 8 allows you to collect stream elements into a set using `Collectors.toSet()`.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 1);
Set<Integer> uniqueNumbers = numbers.stream().collect(Collectors.toSet());
System.out.println(uniqueNumbers); // [1, 2, 3]
```

#### 🔹 Use Case:

Useful for eliminating duplicates or aggregating elements into a set for further processing.

---

### 67. **`Stream` API for Summarizing Statistics**

#### 🔹 Description:

`Collectors.summarizingInt()`, `Collectors.summarizingDouble()`, and `Collectors.summarizingLong()` provide summary statistics (count, sum, average, min, max) for streams of numerical data.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));
System.out.println(stats); // IntSummaryStatistics{count=5, sum=15, min=1, average=3.000000, max=5}
```

#### 🔹 Use Case:

Used when you need to calculate statistics (e.g., in financial calculations, performance analysis).

---

### 68. **`Stream` API for Mapping to a Collection**

#### 🔹 Description:

The `collect()` method can also be used to map stream elements into a new collection (List, Set, Map).

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
Set<String> wordSet = words.stream().collect(Collectors.toSet());
System.out.println(wordSet); // [banana, cherry, apple]
```

#### 🔹 Use Case:

Used in cases where you need to collect elements into a specific collection type (List, Set, Map) for further processing.

---

These are **more advanced and subtle Java 8 features** that provide enhanced power and flexibility for developers. Each of these can help optimize performance, simplify code, and implement efficient functional programming practices.

Would you like to proceed with **Java 9 and beyond**, or focus on specific topics within Java 8?
Here are **additional Java 8 features** that are important for understanding the full scope of this release:

---

### 69. **`CompletableFuture`**

#### 🔹 Description:

`CompletableFuture` allows asynchronous programming by providing methods that allow you to write non-blocking code and handle results or exceptions as they become available.

#### 🔹 Example:

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
    .thenApplyAsync(result -> result * 2)
    .thenApplyAsync(result -> result + 5);
future.thenAccept(System.out::println); // Output will be 25
```

#### 🔹 Use Case:

Used in scenarios where tasks need to run in the background, such as API calls, parallel computations, or handling multiple tasks concurrently in non-blocking ways.

---

### 70. **`Method References`**

#### 🔹 Description:

Method references provide a shorthand for writing lambda expressions that call a method. It improves code readability and is an alternative to lambdas.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
words.forEach(System.out::println); // Equivalent to words.forEach(word -> System.out.println(word));
```

#### 🔹 Use Case:

Used when a lambda expression is simply calling an existing method. It makes code concise and readable.

---

### 71. **`@FunctionalInterface` Annotation**

#### 🔹 Description:

This annotation indicates that an interface is intended to be a functional interface (i.e., it has exactly one abstract method), which makes it suitable for lambda expressions.

#### 🔹 Example:

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    void performAction();
}
```

#### 🔹 Use Case:

Functional interfaces are used to pass behavior as parameters to methods, especially when working with `Stream`, `Optional`, and other functional features.

---

### 72. **`Collectors.toMap()`**

#### 🔹 Description:

`Collectors.toMap()` is used to collect stream elements into a map, where you specify how to derive keys and values from the stream elements.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
Map<Integer, String> map = words.stream()
    .collect(Collectors.toMap(String::length, Function.identity()));
System.out.println(map); // {5=apple, 6=banana, 5=cherry}
```

#### 🔹 Use Case:

Useful for situations where you want to create a Map from a stream of objects, such as mapping identifiers to values or grouping data by some characteristic.

---

### 73. **`Collectors.groupingBy()` with Multiple Criteria**

#### 🔹 Description:

`groupingBy()` allows you to group elements in a stream by multiple criteria. You can pass multiple classifiers to group by different properties.

#### 🔹 Example:

```java
List<Employee> employees = Arrays.asList(
    new Employee("John", 30, "Sales"),
    new Employee("Jane", 40, "HR"),
    new Employee("Tom", 35, "Sales")
);

Map<String, Map<Integer, List<Employee>>> grouped = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment,
                                   Collectors.groupingBy(Employee::getAge)));
System.out.println(grouped);
```

#### 🔹 Use Case:

Used in business analytics or grouping complex data structures based on more than one criterion, e.g., grouping employees by department and age.

---

### 74. **`Collectors.toCollection()`**

#### 🔹 Description:

`toCollection()` allows you to collect the elements of a stream into a custom collection, such as a `TreeSet` or `LinkedList`.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
Set<String> wordSet = words.stream()
    .collect(Collectors.toCollection(TreeSet::new));
System.out.println(wordSet); // Sorted set: [apple, banana, cherry]
```

#### 🔹 Use Case:

Useful when you need to collect elements into a specific type of collection that’s not the default (`List` or `Set`).

---

### 75. **`Stream.collect()`**

#### 🔹 Description:

The `collect()` method is a terminal operation that transforms the elements of the stream into a different form, usually a collection like `List`, `Set`, or `Map`.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
List<String> filteredWords = words.stream()
    .filter(word -> word.length() > 5)
    .collect(Collectors.toList());
System.out.println(filteredWords); // [banana, cherry]
```

#### 🔹 Use Case:

Used when you need to collect filtered or transformed data into a specific collection (e.g., `List`, `Set`).

---

### 76. **`Stream` API for Reducing**

#### 🔹 Description:

The `reduce()` method is used to perform a reduction on the elements of the stream using an associative accumulation function and returns an `Optional`.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b); // 15
System.out.println(sum);
```

#### 🔹 Use Case:

Useful for accumulating or summarizing data, such as calculating the sum, maximum, minimum, or concatenating strings.

---

### 77. **`Stream` API for `forEachOrdered()`**

#### 🔹 Description:

`forEachOrdered()` guarantees that the order of elements is preserved even when the stream is processed in parallel.

#### 🔹 Example:

```java
List<String> names = Arrays.asList("Tom", "Jerry", "Mickey");
names.parallelStream()
     .forEachOrdered(System.out::println); // Prints in the original order
```

#### 🔹 Use Case:

Used when maintaining the original order of elements in a parallel stream is important.

---

### 78. **`Default` and `Static Methods in Interfaces`**

#### 🔹 Description:

Java 8 allows interfaces to have default methods (with implementation) and static methods. This improves the ability to add new methods to interfaces without breaking existing implementations.

#### 🔹 Example:

```java
interface MyInterface {
    default void sayHello() {
        System.out.println("Hello");
    }

    static void staticMethod() {
        System.out.println("Static method");
    }
}

public class Test {
    public static void main(String[] args) {
        MyInterface.staticMethod(); // Static method
    }
}
```

#### 🔹 Use Case:

Used to add new methods to interfaces in libraries without breaking backward compatibility.

---

### 79. **`java.util.Optional` Class**

#### 🔹 Description:

The `Optional` class helps in preventing `NullPointerException` by wrapping values that may or may not be `null`.

#### 🔹 Example:

```java
Optional<String> optionalName = Optional.of("John");
optionalName.ifPresent(name -> System.out.println(name)); // John
```

#### 🔹 Use Case:

Widely used in situations where values may be `null`, and you want to explicitly handle those cases (e.g., method returns, API responses).

---

### 80. **`Interface` Inheritance with Default Methods**

#### 🔹 Description:

Interfaces in Java 8 can now inherit default methods, making it easier to extend interfaces while maintaining backward compatibility.

#### 🔹 Example:

```java
interface A {
    default void sayHello() {
        System.out.println("Hello from A");
    }
}

interface B extends A {
    default void sayHello() {
        System.out.println("Hello from B");
    }
}

public class Test implements B {
    public static void main(String[] args) {
        new Test().sayHello(); // Output: Hello from B
    }
}
```

#### 🔹 Use Case:

Used when multiple interfaces provide common behavior that can be shared across implementations.

---

### 81. **`Method Parameter Reflection`**

#### 🔹 Description:

Java 8 introduced `MethodParameter` for reflection-based manipulation of method parameters (e.g., to read annotations from method parameters).

#### 🔹 Example:

```java
Method method = MyClass.class.getMethod("myMethod", String.class);
MethodParameter[] params = method.getParameters();
```

#### 🔹 Use Case:

Used in frameworks like Spring to handle annotations in method parameters (e.g., `@RequestParam` in REST controllers).

---

### 82. **`@Repeatable` Annotations**

#### 🔹 Description:

Java 8 allows annotations to be repeated on the same element, instead of being limited to one instance of an annotation.

#### 🔹 Example:

```java
@Repeatable(MyAnnotations.class)
public @interface MyAnnotation {
    String value();
}

public @interface MyAnnotations {
    MyAnnotation[] value();
}
```

#### 🔹 Use Case:

Used in scenarios where multiple instances of the same annotation are needed (e.g., security policies, logging annotations).

---

Here are **additional Java 8 features** that should complete your understanding of this version:

---

### 83. **`Stream` API with `map()` and `flatMap()`**

#### 🔹 Description:

The `map()` function transforms each element of the stream, while `flatMap()` flattens nested streams into a single stream.

#### 🔹 Example:

```java
// map() - transforms each string to uppercase
List<String> words = Arrays.asList("apple", "banana", "cherry");
List<String> uppercasedWords = words.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
System.out.println(uppercasedWords); // [APPLE, BANANA, CHERRY]

// flatMap() - flattening a list of lists into a single list
List<List<Integer>> lists = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5, 6)
);
List<Integer> flatList = lists.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
System.out.println(flatList); // [1, 2, 3, 4, 5, 6]
```

#### 🔹 Use Case:

- `map()` is often used to transform data, such as modifying the elements of a collection.
- `flatMap()` is useful when you have nested collections and want to flatten them into a single stream.

---

### 84. **`Stream` API with `peek()`**

#### 🔹 Description:

`peek()` is an intermediate operation that allows you to inspect elements as they flow through the stream. It is often used for debugging purposes.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
List<String> modifiedWords = words.stream()
    .peek(word -> System.out.println("Processing: " + word))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

#### 🔹 Use Case:

`peek()` is primarily used for debugging or logging elements as they are processed in the stream pipeline. Avoid side effects in production code.

---

### 85. **`Stream` API with `reduce()` for Custom Reduction**

#### 🔹 Description:

The `reduce()` method can also be used for custom reductions such as concatenating strings, multiplying elements, or performing other accumulations.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
String sentence = words.stream()
    .reduce((word1, word2) -> word1 + " " + word2)
    .orElse("No words");
System.out.println(sentence); // Java is fun
```

#### 🔹 Use Case:

Commonly used when you need to perform accumulations that require custom logic, like summing or concatenating values.

---

### 86. **`Stream` API with `skip()` and `limit()`**

#### 🔹 Description:

`skip()` skips a given number of elements, while `limit()` restricts the number of elements in the stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
List<Integer> limitedNumbers = numbers.stream()
    .skip(3) // Skips the first 3 elements
    .limit(4) // Limits the stream to the next 4 elements
    .collect(Collectors.toList());
System.out.println(limitedNumbers); // [4, 5, 6, 7]
```

#### 🔹 Use Case:

`skip()` is useful when you want to skip the first few elements in a stream (e.g., paging), and `limit()` is used for restricting the number of elements.

---

### 87. **`Stream` API for `anyMatch()`, `allMatch()`, `noneMatch()`**

#### 🔹 Description:

These methods are terminal operations that check if any, all, or none of the elements in the stream satisfy a given condition.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(2, 4, 6, 8, 10);

// anyMatch() checks if any element satisfies the condition
boolean hasOdd = numbers.stream().anyMatch(n -> n % 2 != 0); // false

// allMatch() checks if all elements satisfy the condition
boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0); // true

// noneMatch() checks if no element satisfies the condition
boolean noOdd = numbers.stream().noneMatch(n -> n % 2 != 0); // true
```

#### 🔹 Use Case:

These methods are helpful for performing validations or checks without needing to process all elements.

---

### 88. **`Collectors.joining()`**

#### 🔹 Description:

`joining()` is a special collector used for concatenating elements of a stream into a single `String`.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
String sentence = words.stream()
    .collect(Collectors.joining(" ")); // Joins words with space
System.out.println(sentence); // Java is fun
```

#### 🔹 Use Case:

Used to join stream elements into a single string, often with a delimiter, prefix, or suffix (e.g., creating a CSV line or forming a sentence).

---

### 89. **`Collectors.partitioningBy()`**

#### 🔹 Description:

`partitioningBy()` is a special collector used to partition the elements of a stream into two groups based on a predicate.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
System.out.println(partitioned); // {false=[1, 3, 5], true=[2, 4, 6]}
```

#### 🔹 Use Case:

Used when you need to split data into two groups, such as even vs odd numbers or valid vs invalid inputs.

---

### 90. **`Collectors.counting()`**

#### 🔹 Description:

`counting()` is a special collector used to count the number of elements in a stream.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry", "apple");
long count = words.stream()
    .collect(Collectors.counting());
System.out.println(count); // 4
```

#### 🔹 Use Case:

Used when you need to know how many elements are in a stream, often as part of an aggregation process.

---

### 91. **`Collectors.summarizingInt()`**

#### 🔹 Description:

`summarizingInt()` is a special collector that returns a `IntSummaryStatistics` object, which contains information such as the count, sum, average, and max of elements in a stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));
System.out.println(stats); // IntSummaryStatistics{count=5, sum=15, min=1, average=3.000000, max=5}
```

#### 🔹 Use Case:

Useful for collecting statistics (e.g., sum, average, etc.) about a set of numbers.

---

### 92. **`Collectors.mapping()`**

#### 🔹 Description:

`mapping()` is a special collector that applies a function to each element before collecting them into a result (e.g., collecting `String` elements into their lengths).

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
Set<Integer> lengths = words.stream()
    .collect(Collectors.mapping(String::length, Collectors.toSet()));
System.out.println(lengths); // [5, 6]
```

#### 🔹 Use Case:

Used when you want to apply a transformation (like mapping to another type) before collecting the stream into a result.

---

### 93. **`Collectors.toSet()`**

#### 🔹 Description:

`toSet()` collects the stream elements into a `Set`, ensuring uniqueness of elements.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry", "apple");
Set<String> uniqueWords = words.stream()
    .collect(Collectors.toSet());
System.out.println(uniqueWords); // [banana, cherry, apple]
```

#### 🔹 Use Case:

Used to collect elements into a set when you want to ensure uniqueness in the collection.

---

### 94. **`Stream` API with `distinct()`**

#### 🔹 Description:

`distinct()` removes duplicates from the stream and returns a new stream with only unique elements.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "apple", "cherry");
List<String> uniqueWords = words.stream()
    .distinct()
    .collect(Collectors.toList());
System.out.println(uniqueWords); // [apple, banana, cherry]
```

#### 🔹 Use Case:

Used to remove duplicate elements from a stream.

---

### 95. **`Stream` API with `filter()`**

#### 🔹 Description:

`filter()` is an intermediate operation that filters elements based on a condition, returning a new stream with elements that match the condition.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evenNumbers = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
System.out.println(evenNumbers); // [2, 4, 6]
```

#### 🔹 Use Case:

Used when you need to filter elements based on specific conditions (e.g., finding even numbers).

---

Here are even more **Java 8 features** that continue to enhance its functionality:

---

### 96. **`Stream` API with `findFirst()`**

#### 🔹 Description:

`findFirst()` returns the first element in the stream that matches the condition, or `Optional.empty()` if the stream is empty.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Optional<Integer> firstEven = numbers.stream()
    .filter(n -> n % 2 == 0)
    .findFirst();
System.out.println(firstEven.orElse(-1)); // 2
```

#### 🔹 Use Case:

Used when you want to retrieve the first element that matches a given condition (e.g., the first even number).

---

### 97. **`Stream` API with `findAny()`**

#### 🔹 Description:

`findAny()` returns any element from the stream that matches the condition. The choice of the element is not guaranteed, making it more efficient for parallel streams.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Optional<Integer> anyEven = numbers.stream()
    .filter(n -> n % 2 == 0)
    .findAny();
System.out.println(anyEven.orElse(-1)); // 2 (in this case, but could be any even number)
```

#### 🔹 Use Case:

Used when you need to retrieve any matching element, typically in a parallel stream.

---

### 98. **`CompletableFuture` for Asynchronous Programming**

#### 🔹 Description:

Introduced in Java 8, `CompletableFuture` is a powerful class for handling asynchronous programming and working with future values.

#### 🔹 Example:

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
    .thenApplyAsync(value -> value * 2);

future.thenAccept(result -> System.out.println("Result: " + result)); // Result: 20
```

#### 🔹 Use Case:

Used to write non-blocking, asynchronous code (e.g., when making HTTP requests or performing I/O operations in parallel).

---

### 99. **`Predicate` Interface (Java 8)**

#### 🔹 Description:

`Predicate` is a functional interface used for testing conditions. It's typically used in filtering and matching elements in collections.

#### 🔹 Example:

```java
Predicate<Integer> isEven = num -> num % 2 == 0;
System.out.println(isEven.test(4)); // true
```

#### 🔹 Use Case:

Commonly used for filtering collections, validating input, or conditions in `Stream` operations.

---

### 100. **`Consumer` Interface (Java 8)**

#### 🔹 Description:

`Consumer` is a functional interface used for accepting input and performing some action without returning any result. It is often used for operations like iteration or modifying elements.

#### 🔹 Example:

```java
Consumer<String> print = s -> System.out.println(s);
print.accept("Hello, Java 8!");
```

#### 🔹 Use Case:

Used for operations that consume input but don't produce a result (e.g., printing, logging, or modifying collections).

---

### 101. **`Supplier` Interface (Java 8)**

#### 🔹 Description:

`Supplier` is a functional interface used for supplying a result without taking any input. It is often used for lazy evaluations or generating values.

#### 🔹 Example:

```java
Supplier<Double> randomValue = () -> Math.random();
System.out.println(randomValue.get()); // Random number between 0 and 1
```

#### 🔹 Use Case:

Common in factories or situations where a value needs to be generated lazily (e.g., creating random numbers or values).

---

### 102. **`Function` Interface (Java 8)**

#### 🔹 Description:

`Function` is a functional interface that represents a function that accepts one argument and produces a result. It's commonly used for transformations or mappings.

#### 🔹 Example:

```java
Function<String, Integer> stringLength = s -> s.length();
System.out.println(stringLength.apply("Hello")); // 5
```

#### 🔹 Use Case:

Used for transformations or mappings in `Stream` operations (e.g., converting a `String` to an `Integer`).

---

### 103. **`Method References` (Java 8)**

#### 🔹 Description:

Method references provide a way to refer directly to a method without executing it. They are a shorthand for lambda expressions.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
words.forEach(System.out::println); // Same as words.forEach(word -> System.out.println(word));
```

#### 🔹 Use Case:

Makes the code more concise, especially when referring to existing methods.

---

### 104. **`Type Annotations` (Java 8)**

#### 🔹 Description:

Java 8 allows annotations to be applied to type declarations, giving developers more flexibility in specifying behavior during the compile-time checking.

#### 🔹 Example:

```java
List<@NonNull String> list = new ArrayList<>();
```

#### 🔹 Use Case:

Useful for tools that perform static analysis, such as nullability checks and type validation.

---

### 105. **`JavaTime` API Enhancements**

#### 🔹 Description:

Java 8 introduced the `java.time` package, which provided a more modern, comprehensive, and immutable set of date and time APIs, including `Instant`, `Duration`, `Period`, `LocalDate`, `LocalTime`, and `LocalDateTime`.

#### 🔹 Example:

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plusDays(1);
System.out.println(tomorrow); // 2023-06-12
```

#### 🔹 Use Case:

Used for better handling of date-time operations without the pitfalls of `Date` and `Calendar`.

---

### 106. **`Optional` Class Enhancements**

#### 🔹 Description:

The `Optional` class in Java 8 helps avoid `NullPointerExceptions` by wrapping values that could be `null` and providing safer methods for dealing with them.

#### 🔹 Example:

```java
Optional<String> value = Optional.ofNullable("Java 8");
System.out.println(value.orElse("Default Value")); // Java 8
```

#### 🔹 Use Case:

Used to represent potentially `null` values and avoid manual null checks in your code.

---

### 107. **`Streams` with `collect()` and `groupingBy()`**

#### 🔹 Description:

`groupingBy()` is a collector that groups the elements of the stream according to a classifier function. It returns a `Map` where the key is the classifier and the value is a list of elements.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry", "apricot", "blueberry");
Map<Character, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(word -> word.charAt(0)));
System.out.println(grouped); // {a=[apple, apricot], b=[banana, blueberry], c=[cherry]}
```

#### 🔹 Use Case:

Used when you need to group elements based on a certain attribute (e.g., categorizing words by their first letter).

---

### 108. **`Collectors.toMap()`**

#### 🔹 Description:

`toMap()` is a collector that collects stream elements into a `Map`.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
Map<Integer, String> wordMap = words.stream()
    .collect(Collectors.toMap(String::length, word -> word));
System.out.println(wordMap); // {5=apple, 6=banana, 6=cherry}
```

#### 🔹 Use Case:

Used when you need to convert a stream into a `Map` (e.g., mapping word lengths to their corresponding words).

---

### 109. **`Collectors.teeing()`**

#### 🔹 Description:

Introduced in Java 12, but still notable in the context of modern Java, `teeing()` allows you to perform multiple downstream operations on the same stream and merge their results.

#### 🔹 Example:

```java
// Example for Java 12 or later
var result = stream.collect(Collectors.teeing(
    Collectors.counting(),
    Collectors.summingInt(String::length),
    (count, length) -> "Count: " + count + ", Length sum: " + length));
```

#### 🔹 Use Case:

Used for aggregating data from a stream in more complex ways.

---

### 110. **`Stream` API with `forEachOrdered()`**

#### 🔹 Description:

`forEachOrdered()` ensures that the elements are processed in encounter order, particularly useful in parallel streams.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
words.parallelStream().forEachOrdered(System.out::println);
```

#### 🔹 Use Case:

Ensures ordering when processing a stream in parallel.

---

Here are additional **Java 8 features** and their usage examples:

---

### 111. **`Stream` API with `distinct()`**

#### 🔹 Description:

The `distinct()` method removes duplicate elements from the stream, ensuring that only unique elements are retained.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
numbers.stream().distinct().forEach(System.out::println); // 1, 2, 3, 4, 5
```

#### 🔹 Use Case:

Useful when you need to remove duplicates from a collection or stream of data.

---

### 112. **`Stream` API with `skip()`**

#### 🔹 Description:

The `skip(n)` method skips the first `n` elements of a stream and returns a stream with the remaining elements.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream().skip(2).forEach(System.out::println); // 3, 4, 5
```

#### 🔹 Use Case:

Used when you want to skip a certain number of elements (e.g., pagination scenarios).

---

### 113. **`Stream` API with `limit()`**

#### 🔹 Description:

The `limit(n)` method limits the stream to the first `n` elements, making it useful for working with sublists.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream().limit(3).forEach(System.out::println); // 1, 2, 3
```

#### 🔹 Use Case:

Used when you want to restrict the number of elements processed in a stream.

---

### 114. **`Stream` API with `sorted()`**

#### 🔹 Description:

The `sorted()` method sorts the elements in the stream based on their natural order or a provided comparator.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(5, 3, 1, 4, 2);
numbers.stream().sorted().forEach(System.out::println); // 1, 2, 3, 4, 5
```

#### 🔹 Use Case:

Commonly used when you need to sort elements before processing them.

---

### 115. **`Stream` API with `mapToInt()`, `mapToDouble()`, and `mapToLong()`**

#### 🔹 Description:

These methods allow you to convert elements to primitive types, optimizing memory and performance.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("1", "2", "3");
words.stream().mapToInt(Integer::parseInt).forEach(System.out::println); // 1, 2, 3
```

#### 🔹 Use Case:

Used when you need to perform operations on primitive types directly, improving performance by avoiding autoboxing.

---

### 116. **`Stream` API with `flatMap()`**

#### 🔹 Description:

The `flatMap()` method is used to flatten nested collections or arrays into a single stream.

#### 🔹 Example:

```java
List<List<String>> listOfLists = Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("c", "d"));
listOfLists.stream().flatMap(List::stream).forEach(System.out::println); // a, b, c, d
```

#### 🔹 Use Case:

Used when you have nested collections (like lists of lists) and want to flatten them into a single stream.

---

### 117. **`Stream` API with `reduce()`**

#### 🔹 Description:

The `reduce()` method performs a reduction on the elements of the stream using an associative accumulation function, returning an `Optional` result.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream().reduce(0, Integer::sum);
System.out.println(sum); // 15
```

#### 🔹 Use Case:

Used for performing aggregations such as summing, multiplying, or concatenating elements in a stream.

---

### 118. **`Stream` API with `peek()`**

#### 🔹 Description:

The `peek()` method allows you to examine the elements in the stream as they pass through a certain stage, without altering the stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream()
       .peek(System.out::println) // Prints each element
       .map(n -> n * 2)
       .forEach(System.out::println); // Prints transformed values
```

#### 🔹 Use Case:

Useful for debugging or logging intermediate values in a stream pipeline without modifying the stream's elements.

---

### 119. **`Collectors.toList()`**

#### 🔹 Description:

The `toList()` collector collects elements from a stream into a `List`.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
List<String> collected = words.stream().collect(Collectors.toList());
System.out.println(collected); // [Java, is, fun]
```

#### 🔹 Use Case:

Commonly used when you need to collect elements into a `List` after performing transformations on a stream.

---

### 120. **`Collectors.joining()`**

#### 🔹 Description:

The `joining()` collector concatenates elements of a stream into a single string with optional delimiters, prefix, and suffix.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
String sentence = words.stream().collect(Collectors.joining(" ", "[", "]"));
System.out.println(sentence); // [Java is fun]
```

#### 🔹 Use Case:

Used for joining strings from a collection into a single string, useful for formatting output.

---

### 121. **`Collectors.mapping()`**

#### 🔹 Description:

The `mapping()` collector performs a transformation on the stream elements before collecting them into a collection, such as a list or set.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("java", "is", "fun");
List<Integer> lengths = words.stream()
                              .collect(Collectors.mapping(String::length, Collectors.toList()));
System.out.println(lengths); // [4, 2, 3]
```

#### 🔹 Use Case:

Used when you need to transform stream elements before performing collection operations.

---

### 122. **`Collectors.counting()`**

#### 🔹 Description:

The `counting()` collector counts the number of elements in a stream.

#### 🔹 Example:

```java
List<String> words = Arrays.asList("Java", "is", "fun");
long count = words.stream().collect(Collectors.counting());
System.out.println(count); // 3
```

#### 🔹 Use Case:

Used when you need to count the number of elements in a collection or stream.

---

### 123. **`Collectors.summingInt()`**

#### 🔹 Description:

The `summimgInt()` collector sums the integer values of elements in a stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream().collect(Collectors.summingInt(Integer::intValue));
System.out.println(sum); // 15
```

#### 🔹 Use Case:

Used for calculating sums of numeric values in a collection.

---

### 124. **`Collectors.averagingInt()`**

#### 🔹 Description:

The `averagingInt()` collector calculates the average of the integer values in a stream.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
double average = numbers.stream().collect(Collectors.averagingInt(Integer::intValue));
System.out.println(average); // 3.0
```

#### 🔹 Use Case:

Used when you need to calculate the average value of a set of numbers.

---

### 125. **`Collectors.reducing()`**

#### 🔹 Description:

The `reducing()` collector performs a reduction on the stream elements using a binary operator.

#### 🔹 Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int product = numbers.stream().collect(Collectors.reducing(1, (a, b) -> a * b));
System.out.println(product); // 120
```

#### 🔹 Use Case:

Used for performing custom reductions, like calculating the product or maximum of stream elements.

---

These additional **Java 8 features** give you more tools for functional programming and stream processing. Would you like to explore Java 9 or any other version next?
