Here is a **comprehensive breakdown of Java 11–17 features**, including all major topics, types, and detailed descriptions you should know for the Emirates Group IT Senior Fullstack role.

---

## 🧠 Java 11–17 Features: Topics with Descriptions

---

### **1. var (Local Variable Type Inference) – Java 10+**

* **Description**: Allows developers to declare local variables without explicitly specifying the type.
* **Syntax**:

  ```java
  var list = new ArrayList<String>(); // infers ArrayList<String>
  ```
* **Use Case**: Cleaner syntax for simple, obvious assignments; improves readability.
* **Limitation**: Only for local variables (not fields, method params, or return types).

---

### **2. Records – Java 14 (Preview), Java 16 (Stable)**

* **Description**: A new way to create immutable data classes with less boilerplate.
* **Syntax**:

  ```java
  public record Person(String name, int age) {}
  ```
* **Use Case**: Ideal for DTOs, configuration holders, and data carriers.
* **Features**: Auto-generates constructor, `equals()`, `hashCode()`, and `toString()`.

---

### **3. Text Blocks – Java 13 (Preview), Java 15 (Stable)**

* **Description**: Allows multiline strings with better formatting and readability.
* **Syntax**:

  ```java
  String json = """
                {
                    "name": "Emirates",
                    "type": "Airline"
                }
                """;
  ```
* **Use Case**: HTML, SQL, JSON strings in Java code without `\n`, `+`, or ugly formatting.

---

### **4. Switch Expressions – Java 14 (Stable)**

* **Description**: Enhances `switch` to be used as an expression that returns a value.
* **Syntax**:

  ```java
  String result = switch (day) {
      case MONDAY, FRIDAY -> "Work";
      case SATURDAY, SUNDAY -> "Rest";
      default -> throw new IllegalArgumentException("Unknown day");
  };
  ```
* **Use Case**: Concise, readable decision logic without boilerplate `break` or variable assignment.

---

### **5. Pattern Matching for instanceof – Java 16**

* **Description**: Simplifies type checks and casts.
* **Syntax**:

  ```java
  if (obj instanceof String s) {
      System.out.println(s.toLowerCase());
  }
  ```
* **Use Case**: Cleaner code for `instanceof` checks, especially when casting is needed.

---

### **6. Sealed Classes – Java 15 (Preview), Java 17 (Stable)**

* **Description**: Restricts which classes can extend or implement a class/interface.
* **Syntax**:

  ```java
  public sealed class Shape permits Circle, Square {}
  public final class Circle extends Shape {}
  public final class Square extends Shape {}
  ```
* **Use Case**: Model finite, controlled hierarchies like ASTs, shapes, or command types.

---

### **7. Helpful NullPointerExceptions – Java 14**

* **Description**: JVM provides detailed messages indicating *which* variable was null.
* **Use Case**: Improves debugging experience.
* **Example**:

  ```text
  Cannot invoke "String.length()" because "str" is null
  ```

---

### **8. New String Methods – Java 11+**

* `String.isBlank()` – Checks if a string is empty or contains only whitespace.
* `String.lines()` – Returns a Stream of lines from the string.
* `String.strip()`, `stripLeading()`, `stripTrailing()` – Unicode-aware whitespace removal.
* `repeat(int count)` – Repeats the string `n` times.

---

### **9. Files & I/O Enhancements – Java 11+**

* `Files.readString()` / `Files.writeString()` – Simplified file I/O.

  ```java
  String content = Files.readString(Path.of("file.txt"));
  ```

---

### **10. HTTP Client API – Java 11**

* **Description**: Standardized HTTP client for HTTP/1.1 and HTTP/2.
* **Example**:

  ```java
  HttpClient client = HttpClient.newHttpClient();
  HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.example.com"))
      .build();
  HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
  ```
* **Use Case**: Replaces older `HttpURLConnection` or third-party clients.

---

### **11. Stream API Enhancements – Java 12+**

* `Collectors.teeing()` – Combines two collectors into one.
* `Stream.toList()` – Returns an immutable list from a stream (Java 16).
* `Predicate.not()` – Negate predicates more clearly in stream filters.

---

### **12. instanceof with Type Pattern Matching – Java 16**

* Already discussed above under "Pattern Matching" – reduces casting verbosity.

---

### **13. JEP 356: Enhanced Pseudo-Random Number Generators – Java 17**

* Provides new interfaces and implementations for PRNGs.
* Supports algorithms like LXM, SplitMix, Xoshiro, etc.

---

### **14. Foreign Function & Memory API (Preview in Java 17)**

* Replace JNI with cleaner native memory access.
* Relevant for performance-critical or integration-heavy backends.

---

### **15. Deprecations & Removals**

* **Applets**, **Security Manager**, and **RMI Activation** being phased out.
* Emphasis on modular Java (`module-info.java`), introduced in Java 9.

---

### 📌 Summary Table

| Feature                         | Java Version | Purpose                            |
| ------------------------------- | ------------ | ---------------------------------- |
| var                             | Java 10      | Type inference for local variables |
| Records                         | Java 14/16   | Compact, immutable data classes    |
| Text Blocks                     | Java 13/15   | Multi-line strings                 |
| Switch Expressions              | Java 14      | Return values from switch blocks   |
| Pattern Matching (`instanceof`) | Java 16      | Cleaner type checks and casts      |
| Sealed Classes                  | Java 15/17   | Controlled class hierarchies       |
| Helpful NullPointerExceptions   | Java 14      | Better debugging messages          |
| HTTP Client API                 | Java 11      | Built-in modern HTTP client        |
| String Enhancements             | Java 11+     | Common operations, unicode support |
| Stream Enhancements             | Java 12–16   | Advanced data processing           |
| PRNG API                        | Java 17      | Better random generators           |

---

Would you like a **PDF summary**, **flashcard quiz**, or **code snippets repo** for hands-on practice?
