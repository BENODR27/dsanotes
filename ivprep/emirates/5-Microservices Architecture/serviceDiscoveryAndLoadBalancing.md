Got it! You want a **professional-grade example** demonstrating **service discovery** and **load balancing** using **Eureka** and **Zuul** in a microservices architecture, with full code.

---

### Context

- **Eureka**: Netflix OSS service registry for service discovery.
- **Zuul**: Netflix OSS API Gateway and intelligent load balancer (routing, filtering).
- Microservices register with Eureka and Zuul routes requests to instances dynamically.

---

## Complete Professional Example with Spring Boot

### Solution Structure

| Module        | Description                                |
| ------------- | ------------------------------------------ |
| eureka-server | Eureka service registry                    |
| service-a     | Sample microservice registered with Eureka |
| service-b     | Another microservice                       |
| api-gateway   | Zuul API Gateway with Eureka integration   |

---

## 1. Eureka Server (Service Registry)

**`pom.xml`**

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>2022.0.3</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
</dependencies>
```

**`EurekaServerApplication.java`**

```java
package com.example.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(EurekaServerApplication.class, args);
  }
}
```

**`application.yml`**

```yaml
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false

spring:
  application:
    name: eureka-server
```

---

## 2. Sample Microservice A

**`pom.xml`**

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
</dependencies>
```

**`ServiceAApplication.java`**

```java
package com.example.servicea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServiceAApplication {
  public static void main(String[] args) {
    SpringApplication.run(ServiceAApplication.class, args);
  }
}
```

**`HelloController.java`**

```java
package com.example.servicea;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @GetMapping("/hello")
  public String hello() {
    return "Hello from Service A";
  }
}
```

**`application.yml`**

```yaml
server:
  port: 8081

spring:
  application:
    name: service-a

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

---

## 3. Sample Microservice B (Similar to A)

Change port to `8082`, name to `service-b` and message.

---

## 4. Zuul API Gateway

**`pom.xml`**

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
</dependencies>
```

**`ApiGatewayApplication.java`**

```java
package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ApiGatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }
}
```

**`application.yml`**

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

zuul:
  routes:
    service-a:
      path: /service-a/**
      service-id: service-a
    service-b:
      path: /service-b/**
      service-id: service-b
```

---

## How It Works

- Start Eureka server (`8761`).
- Start Service A (`8081`) and Service B (`8082`) — they register with Eureka.
- Start Zuul API Gateway (`8080`) — it discovers services via Eureka.
- Access:

  - `http://localhost:8080/service-a/hello` → Routed to Service A.
  - `http://localhost:8080/service-b/hello` → Routed to Service B.

Zuul load balances between multiple instances automatically if you run more instances of service A or B.

---

Absolutely! Ribbon is the default client-side load balancer used by Zuul (in the Spring Cloud Netflix stack). When Zuul routes to a service by `service-id` (Eureka service name), Ribbon automatically load balances among available instances.

---

# How to Add and Customize Ribbon Load Balancer Configurations in Zuul Gateway

---

## 1. Basic Setup (already in place)

If you follow the earlier example:

- Zuul routes to services using service IDs registered in Eureka.
- Ribbon picks up all instances of the service and load balances requests across them by default (round-robin strategy).

You **don't need to add any extra dependency** because:

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
```

**already brings Ribbon.**

---

## 2. Customize Ribbon Load Balancer Behavior

Ribbon can be configured via properties or Java config. The most common customizations:

- Load balancing rule (RoundRobin, Random, WeightedResponseTime, etc.)
- Retry mechanism
- Connect and read timeouts

---

## 3. Sample Ribbon Configurations in `application.yml` for Zuul Gateway

```yaml
# Zuul server port
server:
  port: 8080

spring:
  application:
    name: api-gateway

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

zuul:
  routes:
    service-a:
      path: /service-a/**
      service-id: service-a
    service-b:
      path: /service-b/**
      service-id: service-b

# Ribbon configuration for service-a
service-a:
  ribbon:
    # Load balancing rule (RoundRobinRule, RandomRule, WeightedResponseTimeRule, etc.)
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule

    # Connect and read timeout
    ConnectTimeout: 3000
    ReadTimeout: 5000

    # Retry settings
    MaxAutoRetries: 1
    MaxAutoRetriesNextServer: 1
    OkToRetryOnAllOperations: true

# Ribbon config for service-b (default RoundRobin)
service-b:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RoundRobinRule

    ConnectTimeout: 2000
    ReadTimeout: 3000
```

---

### Explanation:

- `NFLoadBalancerRuleClassName`: Fully qualified class name of Ribbon load balancing rule.
- `ConnectTimeout` and `ReadTimeout`: Milliseconds timeout for Ribbon client connections.
- Retry options control how Ribbon retries failed requests.

---

## 4. Ribbon Load Balancer Rule Options (some popular ones)

| Rule Class Name                                     | Description                                           |
| --------------------------------------------------- | ----------------------------------------------------- |
| `com.netflix.loadbalancer.RoundRobinRule`           | Requests distributed in round robin manner            |
| `com.netflix.loadbalancer.RandomRule`               | Requests routed randomly                              |
| `com.netflix.loadbalancer.WeightedResponseTimeRule` | Weighted by response time (fastest gets more traffic) |
| `com.netflix.loadbalancer.RetryRule`                | Retry with round robin if failure occurs              |
| `com.netflix.loadbalancer.BestAvailableRule`        | Chooses the server with least concurrent requests     |

---

## 5. Optional: Programmatic Ribbon Load Balancer Bean Configuration

If you want to override Ribbon’s default rule in Java config (for more complex scenarios), add this in your Zuul gateway app:

```java
package com.example.apigateway.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {

    @Bean
    public IRule ribbonRule() {
        // You can change to any Ribbon load balancer here
        return new RandomRule();
    }
}
```

> **Important:** If you create Ribbon config classes, you need to scope them properly. To only affect a specific service, use:

```java
@RibbonClient(name = "service-a", configuration = RibbonConfig.class)
```

---

## Summary

- Ribbon is enabled by default in Zuul with Eureka.
- Add properties under `<service-name>.ribbon.*` to customize load balancing.
- Change `NFLoadBalancerRuleClassName` to switch load balancing algorithms.
- Tune timeouts and retries for better resilience.
- Optional: define `IRule` beans for programmatic customization.

---

If you'd like, I can help generate a fully working sample repo or extend this with Spring Cloud Gateway examples (modern alternative to Zuul) or Kubernetes deployment with Ribbon in mind!
