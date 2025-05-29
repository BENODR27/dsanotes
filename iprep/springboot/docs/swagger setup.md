To use **`springdoc-openapi-ui`** with **Spring Boot**, follow this step-by-step guide to generate interactive API documentation (Swagger UI):

---

## ✅ What is `springdoc-openapi-ui`?

`springdoc-openapi-ui` automatically generates **OpenAPI 3 (Swagger)** documentation from your Spring Boot REST API endpoints and exposes it via a browser UI.

---

## 🔧 Step-by-Step Integration

### **Step 1: Add Dependency (Maven)**

In your `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.7.0</version> <!-- Use the latest version -->
</dependency>
```

> ✅ For **Spring Boot 3**, use `springdoc-openapi-starter-webmvc-ui`.

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

---

### **Step 2: Start Your Spring Boot App**

After adding the dependency, start your application.

Springdoc will automatically scan all your `@RestController` endpoints.

---

### **Step 3: Access Swagger UI**

- Open browser and go to:

  ```
  http://localhost:8080/swagger-ui.html
  ```

  or

  ```
  http://localhost:8080/swagger-ui/index.html
  ```

- You’ll see all your REST endpoints with schema, models, and test tools.

---

### **Step 4: (Optional) Customize OpenAPI Info**

Create a config class to customize API documentation metadata:

```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankingAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Banking App API")
                    .description("RESTful API for International Money Transfers")
                    .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("com.bankingapp.controller")
                .build();
    }
}
```

---

### **Step 5: (Optional) Secure with JWT or Bearer Auth**

Add security schema if you’re using JWT:

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearer-key",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
}
```

---

## 🧪 Sample Endpoint for Demo

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(List.of("Alice", "Bob", "Charlie"));
    }
}
```

Now this endpoint will show in Swagger UI!

---

Would you like me to generate a working `springdoc-openapi-ui` config file for your project or help with JWT secured endpoints in Swagger?
