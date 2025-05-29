Creating a professional RESTful API using Java and Spring Boot involves adhering to REST principles while leveraging Spring’s annotations and components. Below is a full breakdown with clean, production-ready code examples:

---

## ✅ RESTful API: Key Concepts

| Concept          | Description                                                               |
| ---------------- | ------------------------------------------------------------------------- |
| **Resource**     | Entity (e.g., `/users`, `/products`)                                      |
| **HTTP Methods** | `GET`, `POST`, `PUT`, `DELETE`, `PATCH`                                   |
| **Status Codes** | 200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, etc. |
| **Stateless**    | Each request contains all information required                            |
| **JSON**         | Common format for request/response payloads                               |

---

## 📦 Example Project: User Management API

```
user-api/
├── controller/UserController.java
├── model/User.java
├── repository/UserRepository.java
├── service/UserService.java
├── exception/GlobalExceptionHandler.java
├── dto/UserDTO.java
├── mapper/UserMapper.java
└── UserApiApplication.java
```

---

## 1. `User.java` (Entity)

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String role;
}
```

---

## 2. `UserRepository.java`

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
```

---

## 3. `UserDTO.java` (Data Transfer Object)

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String role;
}
```

---

## 4. `UserMapper.java`

```java
@Component
public class UserMapper {
    public User toEntity(UserDTO dto) {
        return new User(null, dto.getName(), dto.getEmail(), dto.getRole());
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getName(), user.getEmail(), user.getRole());
    }
}
```

---

## 5. `UserService.java`

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public User updateUser(Long id, User updated) {
        User existing = getUserById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setRole(updated.getRole());
        return repository.save(existing);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
```

---

## 6. `UserController.java`

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAllUsers().stream().map(mapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserDTO dto) {
        User created = service.createUser(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserDTO dto) {
        User updated = service.updateUser(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 7. `GlobalExceptionHandler.java`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
```

---

## 8. `ResourceNotFoundException.java`

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

## 9. `application.yml`

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.H2Dialect
```

---

## ✅ Example REST Calls

| Method   | Endpoint          | Description    |
| -------- | ----------------- | -------------- |
| `GET`    | `/api/users`      | Get all users  |
| `GET`    | `/api/users/{id}` | Get user by ID |
| `POST`   | `/api/users`      | Create user    |
| `PUT`    | `/api/users/{id}` | Update user    |
| `DELETE` | `/api/users/{id}` | Delete user    |

---

## 🧪 Testing Tools

- **Postman** or **cURL** for API testing.
- **Spring Boot Test** for unit/integration tests.
- **Swagger UI** can be added using:

  ```xml
  <dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.15</version>
  </dependency>
  ```

---
