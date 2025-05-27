Here is a **complete professional workflow** for creating, testing, and deploying a Spring Boot backend project (e.g., an **eCommerce** application) with **JUnit + Mockito tests**, fully integrated into a **CI/CD pipeline**.

---

## ✅ PHASE 1: Project Initialization

### 1. **Create Spring Boot Project**

Use [Spring Initializr](https://start.spring.io/) with:

- **Dependencies**: Spring Web, Spring Data JPA, Spring Security, H2/PostgreSQL, Spring Boot Test, Lombok

```bash
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,security,h2,lombok \
  -d name=ecommerce-backend \
  -o ecommerce-backend.zip
unzip ecommerce-backend.zip
cd ecommerce-backend
```

### 2. **Basic Structure**

```bash
src/
 └── main/java/com/example/ecommerce
     ├── controller/
     ├── service/
     ├── model/
     ├── repository/
     └── EcommerceApplication.java

 └── test/java/com/example/ecommerce
     ├── controller/
     ├── service/
     └── repository/
```

---

## ✅ PHASE 2: Implement Business Logic

### Example Entity: `Product`

```java
@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private Double price;
    // Getters/Setters
}
```

### Example Repository

```java
public interface ProductRepository extends JpaRepository<Product, Long> {}
```

### Example Service

```java
@Service
public class ProductService {
    @Autowired private ProductRepository repo;

    public Product getProduct(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }
}
```

### Example Controller

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired private ProductService service;

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }
}
```

---

## ✅ PHASE 3: Write Unit & Integration Tests

### Unit Test – `ProductServiceTest.java`

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository repo;
    @InjectMocks private ProductService service;

    @Test
    void getProduct_shouldReturnProduct() {
        Product p = new Product(1L, "Phone", 699.0);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Product result = service.getProduct(1L);
        assertEquals("Phone", result.getName());
    }
}
```

### Controller Test – `ProductControllerTest.java`

```java
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ProductService service;

    @Test
    void shouldReturnProductJson() throws Exception {
        when(service.getProduct(1L)).thenReturn(new Product(1L, "Phone", 699.0));

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Phone"));
    }
}
```

---

## ✅ PHASE 4: Build & Run Tests

### Build and Run Tests Locally

```bash
./mvnw clean install
```

### Run Tests Only

```bash
./mvnw test
```

---

## ✅ PHASE 5: CI/CD Pipeline with GitHub Actions

### 1. Create `.github/workflows/ci.yml`

```yaml
name: Java CI Build & Test

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up Java
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Build with Maven
        run: ./mvnw clean install

      - name: Run Unit Tests
        run: ./mvnw test
```

---

## ✅ PHASE 6: Optional Additions

### Dockerize the App

```dockerfile
# Dockerfile
FROM openjdk:17
COPY target/ecommerce-backend.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build & Run Docker

```bash
./mvnw clean package -DskipTests
docker build -t ecommerce-backend .
docker run -p 8080:8080 ecommerce-backend
```

---

## ✅ PHASE 7: CI/CD + Docker (GitHub Actions)

Extend `.github/workflows/ci.yml` to include Docker build:

```yaml
- name: Build Docker Image
  run: |
    docker build -t ecommerce-backend .
```

---

## ✅ PHASE 8: Add Code Coverage (Optional)

### Use JaCoCo Plugin in `pom.xml`

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.8</version>
  <executions>
    <execution>
      <goals><goal>prepare-agent</goal></goals>
    </execution>
    <execution>
      <id>report</id>
      <phase>verify</phase>
      <goals><goal>report</goal></goals>
    </execution>
  </executions>
</plugin>
```

### Run with Coverage

```bash
./mvnw clean verify
```

Reports available at:

```
target/site/jacoco/index.html
```

---

Great! Below is a **comprehensive guide** with all possible professional-level Java testing patterns using `when(...)` from Mockito, with clear **instructions and real-world examples** relevant to enterprise applications like eCommerce.

---

## ✅ Mockito `when(...)` – Complete Catalog with Usage

---

### 🔹 1. Basic Return Value

#### ➤ Scenario:

Return a predefined object when a method is called.

```java
when(repo.findById(1L)).thenReturn(Optional.of(new Product(1L, "Phone", 699.0)));
```

#### ✔ Use this when:

You want to simulate the return of a method on a mock.

---

### 🔹 2. Chaining `.thenReturn(...)`

#### ➤ Scenario:

Return different values for multiple calls.

```java
when(cartService.getTotalItems())
    .thenReturn(1)
    .thenReturn(2)
    .thenReturn(3);
```

#### ✔ Use this when:

You want to simulate state change over time (e.g., shopping cart).

---

### 🔹 3. Throwing Exception with `.thenThrow(...)`

#### ➤ Scenario:

Simulate a method throwing an exception.

```java
when(userService.getUserById(999L))
    .thenThrow(new UserNotFoundException("User not found"));
```

#### ✔ Use this when:

You test error handling or fallback logic.

---

### 🔹 4. Using `any()` / `anyLong()` / `anyString()`

#### ➤ Scenario:

Ignore input values and always return a predefined result.

```java
when(paymentService.processPayment(any(PaymentRequest.class)))
    .thenReturn(new PaymentResponse("Success"));
```

#### ✔ Use this when:

Input parameters are irrelevant to the outcome.

---

### 🔹 5. Argument Matching with `eq()`

#### ➤ Scenario:

Return different responses based on specific values.

```java
when(discountService.applyDiscount(eq("PREMIUM")))
    .thenReturn(0.2);

when(discountService.applyDiscount(eq("BASIC")))
    .thenReturn(0.1);
```

---

### 🔹 6. Custom Argument Matching with `argThat()`

#### ➤ Scenario:

Match using complex logic.

```java
when(orderRepo.findByStatus(argThat(status -> status.contains("PENDING"))))
    .thenReturn(List.of(new Order()));
```

---

### 🔹 7. Void Method with `doNothing()`

#### ➤ Scenario:

You want to mock a `void` method.

```java
doNothing().when(emailService).sendInvoiceEmail(anyString(), any(Order.class));
```

---

### 🔹 8. Void Method with `doThrow()`

#### ➤ Scenario:

Simulate failure in a `void` method.

```java
doThrow(new RuntimeException("SMTP failure"))
    .when(emailService).sendInvoiceEmail(anyString(), any(Order.class));
```

---

### 🔹 9. Dynamic Behavior with `.thenAnswer()`

#### ➤ Scenario:

Use logic based on method input.

```java
when(orderService.getOrderById(anyLong()))
    .thenAnswer(invocation -> {
        Long id = invocation.getArgument(0);
        return new Order(id, "Item #" + id);
    });
```

---

### 🔹 10. Return `null` (not recommended, but possible)

```java
when(cacheService.get("nonExistentKey"))
    .thenReturn(null);
```

---

## 🧪 Example: ProductServiceTest with Various `when(...)`

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository repo;
    @InjectMocks private ProductService service;

    @Test
    void shouldReturnProduct() {
        Product phone = new Product(1L, "Phone", 699.0);
        when(repo.findById(1L)).thenReturn(Optional.of(phone));

        Product result = service. (1L);
        assertEquals("Phone", result.getName());
    }

    @Test
    void shouldThrowException() {
        when(repo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getProduct(2L));
    }

    @Test
    void shouldMatchArgumentWithCondition() {
        when(repo.findByName(argThat(name -> name.startsWith("Pro"))))
            .thenReturn(new Product(2L, "ProPhone", 999.0));

        Product result = service.getByName("ProModel");
        assertEquals("ProPhone", result.getName());
    }
}
```

---

Great! Below is a **comprehensive guide** with all possible professional-level Java testing patterns using `when(...)` from Mockito, with clear **instructions and real-world examples** relevant to enterprise applications like eCommerce.

---

## ✅ Mockito `when(...)` – Complete Catalog with Usage

---

### 🔹 1. Basic Return Value

#### ➤ Scenario:

Return a predefined object when a method is called.

```java
when(repo.findById(1L)).thenReturn(Optional.of(new Product(1L, "Phone", 699.0)));
```

#### ✔ Use this when:

You want to simulate the return of a method on a mock.

---

### 🔹 2. Chaining `.thenReturn(...)`

#### ➤ Scenario:

Return different values for multiple calls.

```java
when(cartService.getTotalItems())
    .thenReturn(1)
    .thenReturn(2)
    .thenReturn(3);
```

#### ✔ Use this when:

You want to simulate state change over time (e.g., shopping cart).

---

### 🔹 3. Throwing Exception with `.thenThrow(...)`

#### ➤ Scenario:

Simulate a method throwing an exception.

```java
when(userService.getUserById(999L))
    .thenThrow(new UserNotFoundException("User not found"));
```

#### ✔ Use this when:

You test error handling or fallback logic.

---

### 🔹 4. Using `any()` / `anyLong()` / `anyString()`

#### ➤ Scenario:

Ignore input values and always return a predefined result.

```java
when(paymentService.processPayment(any(PaymentRequest.class)))
    .thenReturn(new PaymentResponse("Success"));
```

#### ✔ Use this when:

Input parameters are irrelevant to the outcome.

---

### 🔹 5. Argument Matching with `eq()`

#### ➤ Scenario:

Return different responses based on specific values.

```java
when(discountService.applyDiscount(eq("PREMIUM")))
    .thenReturn(0.2);

when(discountService.applyDiscount(eq("BASIC")))
    .thenReturn(0.1);
```

---

### 🔹 6. Custom Argument Matching with `argThat()`

#### ➤ Scenario:

Match using complex logic.

```java
when(orderRepo.findByStatus(argThat(status -> status.contains("PENDING"))))
    .thenReturn(List.of(new Order()));
```

---

### 🔹 7. Void Method with `doNothing()`

#### ➤ Scenario:

You want to mock a `void` method.

```java
doNothing().when(emailService).sendInvoiceEmail(anyString(), any(Order.class));
```

---

### 🔹 8. Void Method with `doThrow()`

#### ➤ Scenario:

Simulate failure in a `void` method.

```java
doThrow(new RuntimeException("SMTP failure"))
    .when(emailService).sendInvoiceEmail(anyString(), any(Order.class));
```

---

### 🔹 9. Dynamic Behavior with `.thenAnswer()`

#### ➤ Scenario:

Use logic based on method input.

```java
when(orderService.getOrderById(anyLong()))
    .thenAnswer(invocation -> {
        Long id = invocation.getArgument(0);
        return new Order(id, "Item #" + id);
    });
```

---

### 🔹 10. Return `null` (not recommended, but possible)

```java
when(cacheService.get("nonExistentKey"))
    .thenReturn(null);
```

---

## 🧪 Example: ProductServiceTest with Various `when(...)`

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository repo;
    @InjectMocks private ProductService service;

    @Test
    void shouldReturnProduct() {
        Product phone = new Product(1L, "Phone", 699.0);
        when(repo.findById(1L)).thenReturn(Optional.of(phone));

        Product result = service.getProduct(1L);
        assertEquals("Phone", result.getName());
    }

    @Test
    void shouldThrowException() {
        when(repo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getProduct(2L));
    }

    @Test
    void shouldMatchArgumentWithCondition() {
        when(repo.findByName(argThat(name -> name.startsWith("Pro"))))
            .thenReturn(new Product(2L, "ProPhone", 999.0));

        Product result = service.getByName("ProModel");
        assertEquals("ProPhone", result.getName());
    }
}
```

---

Excellent. Here's a **comprehensive list of all professional Java testing functions and patterns** using **JUnit 5 + Mockito**, covering **all aspects** of unit and integration testing in real-world enterprise applications like **eCommerce**, **airline**, or **banking systems**.

---

## ✅ Professional Java Testing Functions Catalog

Each section includes:

- ✅ Function
- 🔹 Example
- 💡 When to Use It

---

### 🔹 1. `when(...)` – Stub Method Behavior

```java
when(repo.findById(1L)).thenReturn(Optional.of(product));
```

💡 Simulate method return values on mocks.

---

### 🔹 2. `thenReturn(...)` – Define Return Value

```java
when(service.getDiscount("PREMIUM")).thenReturn(0.2);
```

💡 Return constant or dynamic responses.

---

### 🔹 3. `thenThrow(...)` – Simulate Exceptions

```java
when(service.getUserById(99L)).thenThrow(new NotFoundException());
```

💡 Test exception-handling logic.

---

### 🔹 4. `thenAnswer(...)` – Return Dynamically Based on Input

```java
when(repo.findById(anyLong())).thenAnswer(inv -> {
    Long id = inv.getArgument(0);
    return Optional.of(new Product(id, "Item " + id));
});
```

💡 Return value based on input at runtime.

---

### 🔹 5. `doNothing().when(...)` – For `void` Methods

```java
doNothing().when(emailService).sendEmail(anyString(), any());
```

💡 Skip execution of `void` methods.

---

### 🔹 6. `doThrow().when(...)` – For `void` Methods with Exception

```java
doThrow(new MailException("SMTP error"))
    .when(emailService).sendEmail(anyString(), any());
```

💡 Test failure in `void` methods.

---

### 🔹 7. `verify(...)` – Verify Method Calls

```java
verify(repo).findById(1L);
```

💡 Confirm that certain methods were called.

---

### 🔹 8. `verify(..., times(N))` – Verify N Calls

```java
verify(service, times(2)).processPayment(any());
```

💡 Ensure method was called exact number of times.

---

### 🔹 9. `verifyNoInteractions(...)`

```java
verifyNoInteractions(paymentService);
```

💡 Assert that a mock was never used.

---

### 🔹 10. `verifyNoMoreInteractions(...)`

```java
verify(service).findById(1L);
verifyNoMoreInteractions(service);
```

💡 Ensure no extra calls happened.

---

### 🔹 11. `assertThrows(...)` – Exception Assertion

```java
assertThrows(OrderNotFoundException.class, () -> service.getOrder(-1L));
```

💡 Ensure method throws the correct exception.

---

### 🔹 12. `assertEquals(...)` / `assertNotEquals(...)`

```java
assertEquals("Phone", result.getName());
```

💡 Check if actual equals expected.

---

### 🔹 13. `assertTrue(...)` / `assertFalse(...)`

```java
assertTrue(product.isAvailable());
assertFalse(order.isDelivered());
```

💡 Boolean assertions.

---

### 🔹 14. `assertThat(...)` (Hamcrest/AssertJ)

```java
assertThat(result).isNotNull();
assertThat(result.getPrice()).isGreaterThan(100);
```

💡 Fluent assertions (more readable and powerful).

---

### 🔹 15. `ArgumentCaptor` – Capture Arguments

```java
ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
verify(repo).save(captor.capture());
assertEquals("Laptop", captor.getValue().getName());
```

💡 Validate internal argument passed to a mock.

---

### 🔹 16. `@Mock`, `@InjectMocks`, `@Spy`, `@Captor`

```java
@Mock private ProductRepository repo;
@InjectMocks private ProductService service;
```

💡 Use for injecting mocks into services.

---

### 🔹 17. `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`

```java
@WebMvcTest(ProductController.class)
@SpringBootTest
@DataJpaTest
```

💡 For testing controllers, full app context, or just repositories.

---

### 🔹 18. `MockMvc` – Controller Testing

```java
mockMvc.perform(get("/api/products/1"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.name").value("Phone"));
```

💡 Simulate HTTP requests and test controllers.

---

### 🔹 19. `TestRestTemplate` – Integration API Testing

```java
ResponseEntity<Product> response = restTemplate.getForEntity("/api/products/1", Product.class);
assertEquals(HttpStatus.OK, response.getStatusCode());
```

💡 Full integration tests with real HTTP requests.

---

### 🔹 20. `@Nested`, `@DisplayName`, `@ParameterizedTest`

#### Nested Structure:

```java
@Nested
@DisplayName("ProductService Tests")
class ProductServiceTests { ... }
```

#### Parameterized Test:

```java
@ParameterizedTest
@ValueSource(strings = {"PREMIUM", "BASIC"})
void testDiscountCodes(String code) {
    double discount = service.getDiscount(code);
    assertTrue(discount > 0);
}
```

💡 Structure your tests professionally.

---

## ✅ Professional Template

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository repo;
    @InjectMocks private ProductService service;

    @Test
    void testGetProduct_success() {
        Product p = new Product(1L, "Phone", 699.0);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Product result = service.getProduct(1L);
        assertNotNull(result);
        assertEquals("Phone", result.getName());
        verify(repo).findById(1L);
    }

    @Test
    void testGetProduct_failure() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> service.getProduct(99L));
    }
}
```

---

## ✅ Ready-to-Go Options

Would you like:

1. 📦 **Full Java Spring Boot project** with all test types implemented?
2. 📁 **All test cases for services, controllers, repositories, utilities, schedulers, and event handlers**?
3. 🚀 Add CI/CD pipeline for test execution and coverage reporting?
4. ⚙️ Integration with tools like **Mockito, JUnit, AssertJ, Jacoco, and REST-assured**?

Let me know what you want next, and I’ll provide the **complete professional setup** with code.
