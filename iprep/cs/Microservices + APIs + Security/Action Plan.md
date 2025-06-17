Here's a **step-by-step guide with full code** to create two **Spring Boot microservices** – `OrderService` and `InventoryService` – secured using **JWT** and routed via an **API Gateway** using **Spring Cloud Gateway**. The setup includes:

✅ Microservices
✅ Spring Cloud Gateway
✅ JWT authentication
✅ Centralized Auth Service

---

## 🏗️ Architecture Overview

```text
             +-------------------+
             |  API Gateway      |
             | (Spring Gateway)  |
             +-------------------+
                      |
        +-------------+--------------+
        |                            |
+---------------+          +------------------+
|  OrderService |          | InventoryService |
+---------------+          +------------------+
                      |
                +------------+
                | AuthService |
                +------------+
```

---

## 🔐 1. AuthService (JWT Provider)

### 📁 Structure

```
auth-service/
├── controller/
│   └── AuthController.java
├── config/
│   └── JwtUtil.java
└── application.yml
```

### ✅ `AuthController.java`

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil = new JwtUtil();

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        if ("user".equals(username) && "password".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return Map.of("token", token);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
```

### ✅ `JwtUtil.java`

```java
@Component
public class JwtUtil {
    private final String SECRET = "mysecurekeymysecurekeymysecurekey";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("auth-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
    }
}
```

### ✅ `application.yml`

```yaml
server:
  port: 8081
```

---

## 🌉 2. API Gateway

### 📁 Structure

```
gateway/
├── filter/
│   └── JwtAuthFilter.java
├── config/
│   └── RouteConfig.java
└── application.yml
```

### ✅ `JwtAuthFilter.java`

```java
@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    public JwtAuthFilter() {
        super(Config.class);
    }

    public static class Config {}

    private final String SECRET = "mysecurekeymysecurekeymysecurekey";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
            try {
                Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}
```

### ✅ `RouteConfig.java`

```java
@Configuration
public class RouteConfig {
    @Autowired
    JwtAuthFilter authFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service", r -> r.path("/order/**")
                        .filters(f -> f.filter(authFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://localhost:8082"))
                .route("inventory-service", r -> r.path("/inventory/**")
                        .filters(f -> f.filter(authFilter.apply(new JwtAuthFilter.Config())))
                        .uri("http://localhost:8083"))
                .build();
    }
}
```

### ✅ `application.yml`

```yaml
server:
  port: 8080
spring:
  cloud:
    gateway:
      default-filters:
        - AddResponseHeader=X-Powered-By, Gateway
```

---

## 📦 3. OrderService

### ✅ `OrderController.java`

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping
    public String getOrders() {
        return "Order List";
    }
}
```

### ✅ `application.yml`

```yaml
server:
  port: 8082
```

---

## 📦 4. InventoryService

### ✅ `InventoryController.java`

```java
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @GetMapping
    public String getInventory() {
        return "Inventory List";
    }
}
```

### ✅ `application.yml`

```yaml
server:
  port: 8083
```

---

## 🧪 Test Flow

1. **Start all services**: AuthService (`8081`), Gateway (`8080`), OrderService (`8082`), InventoryService (`8083`)

2. **Login**:

   ```bash
   curl -X POST http://localhost:8081/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"user", "password":"password"}'
   ```

   You get a JWT token.

3. **Access OrderService via Gateway**:

   ```bash
   curl http://localhost:8080/order \
     -H "Authorization: Bearer <TOKEN>"
   ```

4. **Access InventoryService via Gateway**:

   ```bash
   curl http://localhost:8080/inventory \
     -H "Authorization: Bearer <TOKEN>"
   ```

---

## 🧰 Dependencies to Add (`pom.xml`)

All services need:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Gateway additionally needs:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

AuthService needs:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

---

