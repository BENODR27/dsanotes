Here is a professional and complete guide to **Dependency Injection (DI)** in **Java with Spring Boot**, including real-world usage, design principles, and code examples.

---

## ✅ What is Dependency Injection?

**Dependency Injection** is a design pattern in which an object’s dependencies are **provided by an external entity (e.g., the framework)** rather than the object creating them itself.

### 🔍 Why use DI?

- Promotes **loose coupling**
- Improves **testability** (e.g., via mock injection)
- Follows **SOLID principles** (especially the Dependency Inversion Principle)

---

## 🧱 DI in Spring Boot

Spring Boot provides DI via:

1. **Constructor Injection** ✅ recommended
2. **Field Injection**
3. **Setter Injection**

---

## ✅ Example Project Structure

```
di-demo/
├── controller/BookController.java
├── service/BookService.java
├── repository/BookRepository.java
└── DiDemoApplication.java
```

---

### 1. `BookRepository.java`

```java
@Repository
public class BookRepository {
    public String getBook() {
        return "Clean Code by Robert C. Martin";
    }
}
```

---

### 2. `BookService.java`

```java
@Service
public class BookService {

    private final BookRepository repository;

    // ✅ Constructor Injection (Recommended)
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public String fetchBook() {
        return repository.getBook();
    }
}
```

---

### 3. `BookController.java`

```java
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    @Autowired  // Spring can omit this if there's only one constructor
    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> getBook() {
        return ResponseEntity.ok(service.fetchBook());
    }
}
```

---

## ✅ Field Injection (⚠️ Not recommended for testing)

```java
@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public String fetchBook() {
        return repository.getBook();
    }
}
```

---

## ✅ Setter Injection

```java
@Service
public class BookService {

    private BookRepository repository;

    @Autowired
    public void setRepository(BookRepository repository) {
        this.repository = repository;
    }

    public String fetchBook() {
        return repository.getBook();
    }
}
```

---

## 🔁 DI in Tests (Using Mocks)

```java
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository repository;

    @InjectMocks
    BookService service;

    @Test
    void testFetchBook() {
        when(repository.getBook()).thenReturn("Test Driven Development");

        String book = service.fetchBook();
        assertEquals("Test Driven Development", book);
    }
}
```

---

## 📌 DI Best Practices

| Practice                                                      | Description                                        |
| ------------------------------------------------------------- | -------------------------------------------------- |
| ✅ Prefer constructor injection                               | Easier to test and makes dependencies explicit     |
| ✅ Use `@Component`, `@Service`, `@Repository`, `@Controller` | Register classes with Spring context               |
| ❌ Avoid field injection                                      | Hard to mock and test                              |
| ✅ Use profiles or qualifiers when multiple beans exist       | Helps Spring decide which implementation to inject |

---

## 🧠 Advanced DI Concepts

### 🔹 `@Qualifier` for multiple beans

```java
@Bean
public BookRepository repo1() { ... }

@Bean
public BookRepository repo2() { ... }

@Service
public class BookService {
    public BookService(@Qualifier("repo1") BookRepository repo) {
        this.repository = repo;
    }
}
```

---

### 🔹 `@Primary` for preferred bean

```java
@Primary
@Bean
public BookRepository defaultRepo() { ... }
```

---

### 🔹 `@Profile` for environment-specific beans

```java
@Profile("dev")
@Bean
public BookRepository devRepo() { ... }

@Profile("prod")
@Bean
public BookRepository prodRepo() { ... }
```

---

### 🔹 `@Lazy` for on-demand injection

```java
@Autowired
@Lazy
private HeavyService service;
```

---

## 📦 Summary

| Feature                        | Description                                   |
| ------------------------------ | --------------------------------------------- |
| `@Autowired`                   | Main annotation to inject dependencies        |
| Constructor Injection          | Preferred, clear and testable                 |
| Field Injection                | Less testable, not recommended                |
| Setter Injection               | Useful for optional dependencies              |
| `@Component`, `@Service`, etc. | Register classes in Spring                    |
| Testing                        | Use `@Mock` and `@InjectMocks` for unit tests |

---

Here’s a **professional-level guide to all key Spring Dependency Injection annotations**, including `@Qualifier`, `@Autowired`, `@Component`, and others—complete with usage examples and best practices.

---

## 🔑 1. `@Autowired`

**Purpose**: Injects dependencies automatically by type (constructor, field, or setter).

### ✅ Constructor Injection (Recommended)

```java
@Service
public class OrderService {
    private final PaymentService paymentService;

    @Autowired // Optional in Spring 4.3+
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

### ⚠️ Field Injection (Not recommended for testing)

```java
@Autowired
private PaymentService paymentService;
```

---

## 🔑 2. `@Qualifier`

**Purpose**: Resolves **ambiguity** when multiple beans of the same type exist.

### 🧱 Example:

```java
@Component("paypalService")
public class PayPalService implements PaymentService {}

@Component("stripeService")
public class StripeService implements PaymentService {}
```

### ✅ Usage in Service:

```java
@Service
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(@Qualifier("paypalService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

---

## 🔑 3. `@Primary`

**Purpose**: Marks one bean as the **default choice** when multiple candidates exist.

```java
@Primary
@Component
public class StripeService implements PaymentService {}
```

### 🧠 Combined with `@Qualifier`

If you want more control:

```java
@Qualifier("paypalService")
```

Overrides `@Primary`.

---

## 🔑 4. `@Component`

**Purpose**: Generic stereotype for any Spring-managed class.

```java
@Component
public class EmailUtility {}
```

➡️ Automatically picked up by component scanning.

---

## 🔑 5. `@Service`, `@Repository`, `@Controller`

All are specializations of `@Component` and add **semantic meaning**:

| Annotation    | Used for                      | Benefits                                     |
| ------------- | ----------------------------- | -------------------------------------------- |
| `@Service`    | Business logic classes        | Logs service layer exceptions                |
| `@Repository` | DAO layer interacting with DB | Wraps DB exceptions in `DataAccessException` |
| `@Controller` | MVC Controller layer          | Used for web endpoint mappings               |

---

## 🔑 6. `@Bean`

**Purpose**: Declares a Spring bean in a `@Configuration` class (manual DI).

```java
@Configuration
public class AppConfig {

    @Bean
    public PaymentService paymentService() {
        return new StripeService();
    }
}
```

---

## 🔑 7. `@Configuration`

Marks a class that declares `@Bean` methods and is processed by Spring to generate bean definitions.

```java
@Configuration
public class AppConfig {
    @Bean
    public EmailService emailService() {
        return new EmailService();
    }
}
```

---

## 🔑 8. `@Scope`

Defines the **bean scope** (singleton, prototype, request, session, etc.).

```java
@Component
@Scope("prototype")
public class ReportGenerator {}
```

Common scopes:

- `singleton` (default)
- `prototype`
- `request` (Web only)
- `session` (Web only)

---

## 🔑 9. `@Profile`

Used to define environment-specific beans (e.g., `dev`, `test`, `prod`).

```java
@Profile("dev")
@Bean
public DataSource devDataSource() {
    return new H2DataSource();
}
```

---

## 🔑 10. `@Value`

Injects values from properties or environment.

```java
@Value("${app.email.sender}")
private String senderEmail;
```

---

## 🔑 11. `@Lazy`

Injects a bean **only when needed**, not at application startup.

```java
@Autowired
@Lazy
private HeavyComponent heavyComponent;
```

---

## 📌 Summary Table

| Annotation       | Description                              |
| ---------------- | ---------------------------------------- |
| `@Autowired`     | Inject dependencies by type              |
| `@Qualifier`     | Resolve multiple beans of same type      |
| `@Primary`       | Make a bean the default among candidates |
| `@Component`     | Generic component to be DI-managed       |
| `@Service`       | Business logic layer                     |
| `@Repository`    | Data access layer                        |
| `@Controller`    | Web/MVC layer                            |
| `@Bean`          | Manual bean registration                 |
| `@Configuration` | Declares `@Bean` methods                 |
| `@Scope`         | Defines bean lifecycle scope             |
| `@Profile`       | Load bean based on active profile        |
| `@Value`         | Inject config/property value             |
| `@Lazy`          | Inject lazily on first use               |

---

Would you like:

- A downloadable PDF cheat sheet for these annotations?
- A code project demonstrating all these in use?

Let me know your preferred next step.
