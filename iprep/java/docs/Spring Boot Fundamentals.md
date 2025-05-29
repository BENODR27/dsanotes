Sure! Here's a **professional-level breakdown** of **Spring Boot Fundamentals**, focusing on:

1. ✅ **Core Annotations**
2. ✅ **Starter Projects (Dependencies)**
3. ✅ **Auto-Configuration (Autoconfig)**

---

## 🔹 1. **Spring Boot Annotations (Core)**

Spring Boot simplifies Spring development using **annotations** that automate behavior.

### a. `@SpringBootApplication`

```java
@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

🔹 _Combination of:_

- `@Configuration` – Java-based config
- `@EnableAutoConfiguration` – Enables autoconfig
- `@ComponentScan` – Scans `@Component`, `@Service`, etc.

---

### b. `@RestController`

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
```

🔹 Combines `@Controller` + `@ResponseBody` for REST APIs.

---

### c. `@Autowired`

```java
@Service
public class MyService {
    public String serve() { return "Serving..."; }
}

@RestController
public class MyController {
    @Autowired
    private MyService service;
}
```

🔹 Injects beans automatically (by type).

---

### d. `@Component`, `@Service`, `@Repository`, `@Controller`

```java
@Component       // Generic component
@Service         // Business logic
@Repository      // DAO layer (exception translation)
@Controller      // MVC controller
```

🔹 All are Spring-managed beans, discovered via `@ComponentScan`.

---

### e. `@Configuration` and `@Bean`

```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```

🔹 Java-based config (replaces XML).

---

### f. `@Value` and `@ConfigurationProperties`

```java
@Value("${server.port}")
private int port;
```

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private int timeout;
}
```

🔹 Load properties from `application.yml` or `application.properties`.

---

## 🔹 2. **Spring Boot Starter Projects**

Spring Boot uses **starter dependencies** to simplify dependency management.

### Example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

| Starter Name                     | Purpose                                |
| -------------------------------- | -------------------------------------- |
| `spring-boot-starter-web`        | Web + REST APIs (Tomcat, Jackson, MVC) |
| `spring-boot-starter-data-jpa`   | Spring Data JPA + Hibernate            |
| `spring-boot-starter-security`   | Spring Security                        |
| `spring-boot-starter-test`       | JUnit, Mockito, AssertJ                |
| `spring-boot-starter-thymeleaf`  | Thymeleaf template engine              |
| `spring-boot-starter-validation` | Bean Validation (Hibernate Validator)  |

🔹 **Starters include** all common dependencies as a single unit, reducing manual configuration.

---

## 🔹 3. **Auto-Configuration (`@EnableAutoConfiguration`)**

Spring Boot automatically configures components based on:

- Classpath contents
- Application properties
- Beans declared

### Example:

If `spring-boot-starter-data-jpa` is on the classpath and `application.properties` includes:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
```

➡️ Spring Boot **auto-configures**:

- `DataSource`
- `EntityManagerFactory`
- `TransactionManager`

---

### 🔍 How It Works Internally

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
```

- Spring Boot scans `META-INF/spring.factories` or `spring/org.springframework.boot.autoconfigure.EnableAutoConfiguration` to load default configurations.

---

## ✅ Summary

| Concept            | Purpose                                                    |
| ------------------ | ---------------------------------------------------------- |
| Annotations        | Declarative style to define components, config, and wiring |
| Starter Projects   | Pre-bundled dependencies for common features               |
| Auto-Configuration | Smart default setup based on the classpath & properties    |

---

Absolutely! Here's a comprehensive **Spring Boot Starter Project** that integrates:

- ✅ **RESTful API** using `@RestController`
- ✅ **Database Integration** with Spring Data JPA and MySQL
- ✅ **Exception Handling** using `@ControllerAdvice`
- ✅ **Auto-Configuration** via Spring Boot starters

This project serves as a solid foundation for building scalable and maintainable Spring Boot applications.

---

## 🚀 Project Overview

We'll develop a **Book Management API** with full CRUD capabilities.

### 🔧 Technologies Used

- **Spring Boot 3.x**
- **Spring Web**
- **Spring Data JPA**
- **MySQL Database**
- **Maven Build Tool**
- **Java 17**

---

## 📁 Project Structure

```
book-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com.example.bookapi/
│       │       ├── BookApiApplication.java
│       │       ├── controller/
│       │       │   └── BookController.java
│       │       ├── entity/
│       │       │   └── Book.java
│       │       ├── exception/
│       │       │   ├── GlobalExceptionHandler.java
│       │       │   └── ResourceNotFoundException.java
│       │       ├── repository/
│       │       │   └── BookRepository.java
│       │       └── service/
│       │           ├── BookService.java
│       │           └── BookServiceImpl.java
│       └── resources/
│           ├── application.properties
│           └── data.sql
├── pom.xml
```

---

## 🛠️ Step-by-Step Implementation

### 1. **Initialize the Project**

Use [Spring Initializr](https://start.spring.io/) with the following settings:

- **Project**: Maven
- **Language**: Java
- **Spring Boot**: 3.x
- **Group**: `com.example`
- **Artifact**: `book-api`
- **Dependencies**:

  - Spring Web
  - Spring Data JPA
  - MySQL Driver

---

### 2. **Configure `application.properties`**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookdb
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

---

### 3. **Define the Entity**

```java
package com.example.bookapi.entity;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    // Constructors, Getters, Setters
}
```

---

### 4. **Create the Repository**

```java
package com.example.bookapi.repository;

import com.example.bookapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
```

---

### 5. **Implement the Service Layer**

```java
package com.example.bookapi.service;

import com.example.bookapi.entity.Book;
import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
}
```

```java
package com.example.bookapi.service;

import com.example.bookapi.entity.Book;
import com.example.bookapi.exception.ResourceNotFoundException;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
```

---

### 6. **Develop the Controller**

```java
package com.example.bookapi.controller;

import com.example.bookapi.entity.Book;
import com.example.bookapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
       this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
       return ResponseEntity.ok(bookService.saveBook(book));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
       return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
       return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
       return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
       bookService.deleteBook(id);
       return ResponseEntity.noContent().build();
    }
}
```

---

### 7. **Handle Exceptions Globally**

```java
package com.example.bookapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

```java
package com.example.bookapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Additional exception handlers can be added here
}
```

---

## 🧪 Testing the API

You can test the endpoints using tools like **Postman** or **cURL**:

- **Create a Book**:

  ```bash
  POST /api/books
  {
    "title": "Effective Java",
    "author": "Joshua Bloch"
  }
  ```

- **Get All Books**:

  ```bash
  GET /api/books
  ```

- **Get Book by ID**:

  ```bash
  GET /api/books/{id}
  ```

- **Update Book**:

  ```bash
  PUT /api/books/{id}
  {
    "title": "Effective Java - 3rd Edition",
    "author": "Joshua Bloch"
  }
  ```

- **Delete Book**:

  ```bash
  DELETE /api/books/{id}
  ```

---

## 📦 Ready-to-Use Starter Projects

For further reference and to explore more advanced features, consider checking out these GitHub repositories:

1. **Spring Boot 3 REST API Example with CRUD Operations**
   A comprehensive example demonstrating CRUD operations using Spring Boot 3.
   🔗 [GitHub Repository](https://github.com/bezkoder/spring-boot-3-rest-api-example)

2. **Spring Boot REST API Starter Template**
   A starter template for building RESTful APIs with Spring Boot, including database integration.
   🔗 [GitHub Repository](https://github.com/bekaku/java-spring-boot-starter)

3. **Spring Boot REST API Starter Project**
   A simple starter project for creating RESTful APIs using Spring Boot, with database and mail server integration.
   🔗 [GitHub Repository](https://github.com/Monogramm/spring-rest-api-starter)

---

Would you like assistance in setting up this project in your local development environment or deploying it to a cloud platform like AWS or Heroku?
