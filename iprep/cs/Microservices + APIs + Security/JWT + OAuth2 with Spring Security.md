Here is a **complete Spring Boot project** using **Spring Security with JWT and OAuth2**. The code provides a basic setup for:

- OAuth2 login (e.g., Google)
- JWT-based authentication
- Secure REST endpoints

---

### 📁 Project Structure

```
src/
└── main/
    ├── java/
    │   └── com/example/securityjwt/
    │       ├── config/
    │       │   ├── JwtAuthenticationFilter.java
    │       │   ├── JwtUtils.java
    │       │   ├── SecurityConfig.java
    │       ├── controller/
    │       │   └── ApiController.java
    │       ├── model/
    │       │   └── UserResponse.java
    │       ├── SecurityJwtApplication.java
    └── resources/
        └── application.yml
```

---

### 1️⃣ `SecurityJwtApplication.java`

```java
package com.example.securityjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class, args);
    }
}
```

---

### 2️⃣ `application.yml`

```yaml
server:
  port: 8080

spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET
            scope:
              - email
              - profile
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://www.googleapis.com/oauth2/v3/userinfo
            user-name-attribute: sub
```

---

### 3️⃣ `SecurityConfig.java`

```java
package com.example.securityjwt.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login()
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) ->
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())));
        return http.build();
    }
}
```

---

### 4️⃣ `JwtAuthenticationFilter.java`

```java
package com.example.securityjwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils = new JwtUtils();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtUtils.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authToken = new UsernamePasswordAuthenticationToken(userEmail, null, null);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
```

---

### 5️⃣ `JwtUtils.java`

```java
package com.example.securityjwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    private static final String SECRET = "mysecretkeymysecretkeymysecretkeymysecretkey";
    private static final long EXPIRATION = 86400000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }
}
```

---

### 6️⃣ `ApiController.java`

```java
package com.example.securityjwt.controller;

import com.example.securityjwt.config.JwtUtils;
import com.example.securityjwt.model.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final JwtUtils jwtUtils = new JwtUtils();

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public API - no auth needed.";
    }

    @GetMapping("/token")
    public String getToken(Authentication authentication) {
        return jwtUtils.generateToken(authentication.getName());
    }

    @GetMapping("/secure")
    public UserResponse secureEndpoint(Authentication authentication) {
        return new UserResponse(authentication.getName(), "You have access to the secure API.");
    }
}
```

---

### 7️⃣ `UserResponse.java`

```java
package com.example.securityjwt.model;

public record UserResponse(String username, String message) {}
```

---

### 🔑 How it works

- `/api/public` → open endpoint
- `/api/token` → generate JWT for current user (OAuth2 login required)
- `/api/secure` → requires valid JWT (`Authorization: Bearer <token>`)

---
