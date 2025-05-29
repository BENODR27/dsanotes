Here are professional-level examples of key design patterns (Singleton, Factory, Strategy, Observer, Builder) implemented with Java and Spring Boot:

---

## ✅ 1. **Singleton Pattern**

Ensures only one instance is created. Spring beans are singleton by default.

### `SingletonService.java`

```java
@Service
public class SingletonService {
    public String getMessage() {
        return "I am a Singleton Bean!";
    }
}
```

### Usage in Controller

```java
@RestController
@RequestMapping("/singleton")
public class SingletonController {

    private final SingletonService singletonService;

    @Autowired
    public SingletonController(SingletonService singletonService) {
        this.singletonService = singletonService;
    }

    @GetMapping
    public String getMessage() {
        return singletonService.getMessage();
    }
}
```

---

## ✅ 2. **Factory Pattern**

Creates objects without exposing the creation logic.

### `Notification.java`

```java
public interface Notification {
    String notifyUser();
}
```

### Concrete Implementations

```java
public class EmailNotification implements Notification {
    public String notifyUser() {
        return "Sending Email Notification";
    }
}

public class SMSNotification implements Notification {
    public String notifyUser() {
        return "Sending SMS Notification";
    }
}
```

### Factory Class

```java
@Component
public class NotificationFactory {

    public Notification getNotification(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SMSNotification();
            default -> throw new IllegalArgumentException("Unknown type");
        };
    }
}
```

### Usage in Controller

```java
@RestController
@RequestMapping("/factory")
public class FactoryController {

    @Autowired
    private NotificationFactory factory;

    @GetMapping("/{type}")
    public String send(@PathVariable String type) {
        Notification notification = factory.getNotification(type);
        return notification.notifyUser();
    }
}
```

---

## ✅ 3. **Strategy Pattern**

Encapsulates algorithms and makes them interchangeable.

### `PaymentStrategy.java`

```java
public interface PaymentStrategy {
    String pay(double amount);
}
```

### Implementations

```java
public class CreditCardPayment implements PaymentStrategy {
    public String pay(double amount) {
        return "Paid $" + amount + " using Credit Card.";
    }
}

public class PayPalPayment implements PaymentStrategy {
    public String pay(double amount) {
        return "Paid $" + amount + " using PayPal.";
    }
}
```

### Context Class

```java
@Component
public class PaymentContext {
    private PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public String executePayment(double amount) {
        return strategy.pay(amount);
    }
}
```

### Usage in Controller

```java
@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private PaymentContext paymentContext;

    @GetMapping("/pay")
    public String pay(@RequestParam String method, @RequestParam double amount) {
        if (method.equalsIgnoreCase("paypal")) {
            paymentContext.setPaymentStrategy(new PayPalPayment());
        } else {
            paymentContext.setPaymentStrategy(new CreditCardPayment());
        }
        return paymentContext.executePayment(amount);
    }
}
```

---

## ✅ 4. **Observer Pattern**

Used for event-driven systems.

### `Observer.java`

```java
public interface Observer {
    void update(String message);
}
```

### Concrete Observers

```java
public class EmailObserver implements Observer {
    public void update(String message) {
        System.out.println("Email received: " + message);
    }
}

public class SmsObserver implements Observer {
    public void update(String message) {
        System.out.println("SMS received: " + message);
    }
}
```

### Subject

```java
@Component
public class NotificationSubject {
    private final List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(String message) {
        observers.forEach(o -> o.update(message));
    }
}
```

### Controller

```java
@RestController
@RequestMapping("/observer")
public class ObserverController {

    @Autowired
    private NotificationSubject subject;

    @PostConstruct
    public void init() {
        subject.subscribe(new EmailObserver());
        subject.subscribe(new SmsObserver());
    }

    @GetMapping("/notify")
    public String notifyAll(@RequestParam String msg) {
        subject.notifyAllObservers(msg);
        return "Notification sent";
    }
}
```

---

## ✅ 5. **Builder Pattern**

Useful for constructing complex objects.

### `User.java`

```java
public class User {
    private final String username;
    private final String email;
    private final int age;

    private User(UserBuilder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.age = builder.age;
    }

    public static class UserBuilder {
        private String username;
        private String email;
        private int age;

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    // Getters omitted for brevity
}
```

### Usage in Controller

```java
@RestController
@RequestMapping("/builder")
public class BuilderController {

    @GetMapping
    public User buildUser() {
        return new User.UserBuilder()
                .setUsername("john_doe")
                .setEmail("john@example.com")
                .setAge(30)
                .build();
    }
}
```

---

Here’s a **professional-level explanation of the key differences** between the core design patterns you've asked about, with **clear purpose, usage, and analogy** to help you differentiate them in interviews or real-world Spring Boot projects:

---

### ✅ 1. **Singleton Pattern**

| Aspect         | Details                                                                                       |
| -------------- | --------------------------------------------------------------------------------------------- |
| **Purpose**    | Ensures **only one instance** of a class exists and provides a **global access point** to it. |
| **Use Case**   | Configuration manager, logging, cache manager, DB connection manager.                         |
| **Spring Tip** | By default, Spring beans are Singleton-scoped.                                                |
| **Analogy**    | Like a **president** of a country – one at a time, globally known.                            |

---

### ✅ 2. **Factory Pattern**

| Aspect         | Details                                                                                                                          |
| -------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| **Purpose**    | Hides object creation logic by creating objects **based on input or conditions**.                                                |
| **Use Case**   | Choosing notification service: SMS, Email, WhatsApp dynamically.                                                                 |
| **Spring Tip** | Use `@Component` Factory class; avoid tight coupling.                                                                            |
| **Analogy**    | Like a **restaurant kitchen**: You place an order, and the chef decides what ingredients and tools are needed behind the scenes. |

---

### ✅ 3. **Strategy Pattern**

| Aspect         | Details                                                                                              |
| -------------- | ---------------------------------------------------------------------------------------------------- |
| **Purpose**    | Defines a **family of algorithms**, encapsulates them, and makes them **interchangeable**.           |
| **Use Case**   | Payment methods (Card, PayPal, Wallet), sorting algorithms, compression.                             |
| **Spring Tip** | Inject strategy using context or configuration.                                                      |
| **Analogy**    | Like **choosing a travel method**: flight, train, or car—all go to the same destination differently. |

---

### ✅ 4. **Observer Pattern**

| Aspect         | Details                                                                                             |
| -------------- | --------------------------------------------------------------------------------------------------- |
| **Purpose**    | **One-to-many dependency** between objects: when one changes state, all its observers are notified. |
| **Use Case**   | Event-driven systems, stock price changes, notification systems.                                    |
| **Spring Tip** | Use Spring’s built-in `ApplicationEventPublisher` or custom observer registration.                  |
| **Analogy**    | Like **subscribers to a YouTube channel** – if a video is uploaded, all subscribers get notified.   |

---

### ✅ 5. **Builder Pattern**

| Aspect         | Details                                                                                             |
| -------------- | --------------------------------------------------------------------------------------------------- |
| **Purpose**    | Used to build **complex objects step-by-step** without needing a constructor with many parameters.  |
| **Use Case**   | Creating objects with optional fields (e.g., User registration with optional profile picture, bio). |
| **Spring Tip** | Combine with Lombok `@Builder` or manually implement for fine control.                              |
| **Analogy**    | Like **ordering a custom-made pizza** – choose size, crust, toppings step by step.                  |

---

### 🔁 Summary Table

| Pattern   | Key Idea                        | Example Scenario                     | Benefit                           |
| --------- | ------------------------------- | ------------------------------------ | --------------------------------- |
| Singleton | Single instance globally        | Config loader                        | Saves memory, ensures consistency |
| Factory   | Centralized object creation     | Create Email or SMS notification     | Hides instantiation logic         |
| Strategy  | Switchable algorithms/behaviors | Payment gateway                      | Promotes Open/Closed Principle    |
| Observer  | One-to-many notification        | Notify subscribers when data changes | Decoupled, event-driven           |
| Builder   | Step-by-step object creation    | Construct User with optional fields  | Avoids telescoping constructors   |

---

Here’s a comprehensive list of **professional-level interview questions and answers** for the most common design patterns used in Java and Spring Boot: **Singleton, Factory, Strategy, Observer, Builder**, etc.

---

## ✅ **1. Singleton Pattern**

### Q1: What is the Singleton Pattern?

**A:** Singleton ensures that a class has only one instance and provides a global point of access to it. In Spring, beans are singleton-scoped by default.

---

### Q2: How is Singleton implemented in Java?

**A:**

```java
public class Singleton {
    private static final Singleton instance = new Singleton();
    private Singleton() {}
    public static Singleton getInstance() {
        return instance;
    }
}
```

---

### Q3: How does Spring Boot handle Singleton Pattern?

**A:** Spring creates beans as singletons by default. Declaring a class with `@Service`, `@Component`, or `@Bean` ensures it will have one instance in the container.

---

### Q4: What are the problems with traditional Singleton?

**A:**

- Thread-safety in multithreaded environments (unless handled explicitly).
- Difficulties in unit testing due to global state.

---

### Q5: How to make a Singleton thread-safe?

**A:**

```java
public class ThreadSafeSingleton {
    private static volatile ThreadSafeSingleton instance;
    private ThreadSafeSingleton() {}
    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null)
                    instance = new ThreadSafeSingleton();
            }
        }
        return instance;
    }
}
```

---

## ✅ **2. Factory Pattern**

### Q1: What is the Factory Pattern?

**A:** The Factory Pattern provides an interface for creating objects in a superclass, but allows subclasses to alter the type of objects that will be created.

---

### Q2: Why use Factory Pattern in Spring Boot?

**A:** To encapsulate object creation logic and decouple the client from specific classes (e.g., dynamically choosing a payment processor or notification type).

---

### Q3: How is Factory Pattern implemented in Spring Boot?

**A:**

```java
@Component
public class NotificationFactory {
    public Notification getNotification(String type) {
        if ("email".equalsIgnoreCase(type)) return new EmailNotification();
        if ("sms".equalsIgnoreCase(type)) return new SmsNotification();
        throw new IllegalArgumentException("Invalid type");
    }
}
```

---

### Q4: What’s the difference between Factory and Abstract Factory?

**A:**

- Factory creates one object type.
- Abstract Factory groups multiple factories to produce families of related objects.

---

## ✅ **3. Strategy Pattern**

### Q1: What is the Strategy Pattern?

**A:** Strategy Pattern enables selecting an algorithm's behavior at runtime by defining a family of algorithms and encapsulating them.

---

### Q2: How is Strategy Pattern useful in Spring Boot?

**A:** It helps in building interchangeable business logic components, e.g., different tax calculation or payment processing strategies.

---

### Q3: How do you implement Strategy Pattern using Spring’s DI?

**A:** Create interfaces for strategies, implement them as `@Component`, and inject using `@Qualifier` or context class.

---

### Q4: Real-world Spring Boot use case?

**A:** Dynamic file compression strategy: ZIP, TAR, GZIP based on user input.

---

## ✅ **4. Observer Pattern**

### Q1: What is the Observer Pattern?

**A:** A one-to-many dependency: when the subject changes, all observers are notified automatically.

---

### Q2: How is it applied in Spring Boot?

**A:** Using `ApplicationEventPublisher` and `@EventListener`.

---

### Q3: Example in Spring Boot?

**A:**

```java
@Component
public class MyPublisher {
    @Autowired ApplicationEventPublisher publisher;
    public void publish(String msg) {
        publisher.publishEvent(new MyCustomEvent(this, msg));
    }
}
```

```java
@Component
public class MyListener {
    @EventListener
    public void handleEvent(MyCustomEvent event) {
        System.out.println("Received: " + event.getMessage());
    }
}
```

---

### Q4: Difference between Observer and Event-driven pattern?

**A:** Event-driven is an architectural style. Observer is a design pattern implementing event-based communication within a module.

---

## ✅ **5. Builder Pattern**

### Q1: What is the Builder Pattern?

**A:** A creational pattern that constructs complex objects step-by-step, allowing setting optional parameters and avoiding telescoping constructors.

---

### Q2: How to use Builder Pattern with Lombok in Spring Boot?

**A:**

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String email;
    private int age;
}
```

Usage:

```java
User user = User.builder().name("John").email("john@example.com").age(30).build();
```

---

### Q3: Real-world scenario?

**A:** Creating complex request objects like search filters, user profiles, or DTOs with optional fields.

---

## ✅ **Cross-Pattern Questions**

### Q: How to choose between Factory and Strategy?

**A:**

- **Factory** is used for **object creation**.
- **Strategy** is used for **behavior selection**.

---

### Q: Can you combine design patterns in a real system?

**A:** Yes. For example, a payment module might use:

- **Factory** to instantiate the payment type.
- **Strategy** to choose algorithm.
- **Singleton** for a shared cache.
- **Builder** to build request DTOs.
- **Observer** to trigger notifications post-payment.

---

