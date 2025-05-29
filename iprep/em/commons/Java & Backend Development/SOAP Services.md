Great — here's the **complete SOAP Banking project code** using **Spring Boot + Spring WS**, entirely in this chat.

---

## ✅ 1. `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>soap-banking</artifactId>
  <version>1.0.0</version>
  <packaging>jar</packaging>

  <properties>
    <java.version>17</java.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web-services</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>jakarta.xml.bind</groupId>
      <artifactId>jakarta.xml.bind-api</artifactId>
    </dependency>
    <dependency>
      <groupId>org.glassfish.jaxb</groupId>
      <artifactId>jaxb-runtime</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>jaxb2-maven-plugin</artifactId>
        <version>2.5.0</version>
        <executions>
          <execution>
            <id>xjc</id>
            <goals>
              <goal>xjc</goal>
            </goals>
            <configuration>
              <schemaDirectory>${project.basedir}/src/main/resources</schemaDirectory>
              <outputDirectory>${project.basedir}/src/main/java</outputDirectory>
              <clearOutputDir>false</clearOutputDir>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

---

## ✅ 2. `banking.xsd` (Place in `src/main/resources/`)

```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://example.com/banking"
           xmlns:tns="http://example.com/banking"
           elementFormDefault="qualified">

    <xs:element name="GetBalanceRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="accountNumber" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="GetBalanceResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="balance" type="xs:decimal"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="TransferRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="fromAccount" type="xs:string"/>
                <xs:element name="toAccount" type="xs:string"/>
                <xs:element name="amount" type="xs:decimal"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="TransferResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="status" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>
```

Run:

```bash
mvn clean install
```

This will generate JAXB classes from the XSD file.

---

## ✅ 3. `SoapBankingApplication.java`

```java
package com.example.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoapBankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoapBankingApplication.class, args);
    }
}
```

---

## ✅ 4. `BankAccount.java` (JPA Entity)

```java
package com.example.banking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class BankAccount {
    @Id
    private String accountNumber;
    private BigDecimal balance;

    public BankAccount() {}

    public BankAccount(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
```

---

## ✅ 5. `BankAccountRepository.java`

```java
package com.example.banking.repository;

import com.example.banking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
```

---

## ✅ 6. `BankService.java`

```java
package com.example.banking.service;

import com.example.banking.model.BankAccount;
import com.example.banking.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankService {
    @Autowired
    private BankAccountRepository repo;

    public BigDecimal getBalance(String accountNumber) {
        return repo.findById(accountNumber)
                .map(BankAccount::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    public String transfer(String from, String to, BigDecimal amount) {
        var fromAcc = repo.findById(from).orElseThrow();
        var toAcc = repo.findById(to).orElseThrow();

        if (fromAcc.getBalance().compareTo(amount) < 0) {
            return "Insufficient funds";
        }

        fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
        toAcc.setBalance(toAcc.getBalance().add(amount));

        repo.save(fromAcc);
        repo.save(toAcc);
        return "Success";
    }
}
```

---

## ✅ 7. `BankEndpoint.java`

```java
package com.example.banking.endpoint;

import com.example.banking.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.*;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import com.example.banking.*;

@Endpoint
public class BankEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/banking";

    @Autowired
    private BankService service;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetBalanceRequest")
    @ResponsePayload
    public GetBalanceResponse getBalance(@RequestPayload GetBalanceRequest request) {
        GetBalanceResponse response = new GetBalanceResponse();
        response.setBalance(service.getBalance(request.getAccountNumber()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TransferRequest")
    @ResponsePayload
    public TransferResponse transfer(@RequestPayload TransferRequest request) {
        TransferResponse response = new TransferResponse();
        response.setStatus(service.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount()));
        return response;
    }
}
```

---

## ✅ 8. `WebServiceConfig.java`

```java
package com.example.banking.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext ctx) {
        var servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(ctx);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "banking")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema schema) {
        var wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("BankingPort");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://example.com/banking");
        wsdl.setSchema(schema);
        return wsdl;
    }

    @Bean
    public XsdSchema bankingSchema() {
        return new SimpleXsdSchema(new org.springframework.core.io.ClassPathResource("banking.xsd"));
    }
}
```

---

## ✅ 9. `application.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create
```

---

## ✅ 10. Initial Test Data (`CommandLineRunner` inside `SoapBankingApplication.java`)

Add this in `main` class to prepopulate accounts:

```java
@Bean
CommandLineRunner initData(BankAccountRepository repo) {
    return args -> {
        repo.save(new BankAccount("A100", new BigDecimal("1000")));
        repo.save(new BankAccount("B200", new BigDecimal("500")));
    };
}
```

---

## 🧪 Test the Service

### Access WSDL:

```
http://localhost:8080/ws/banking.wsdl
```

### Sample SOAP Request:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:bank="http://example.com/banking">
   <soapenv:Header/>
   <soapenv:Body>
      <bank:GetBalanceRequest>
         <bank:accountNumber>A100</bank:accountNumber>
      </bank:GetBalanceRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

---

Here’s the complete integration of the Spring Boot SOAP Banking service with a ReactJS (TypeScript) frontend:

---

## ✅ Frontend: ReactJS with TypeScript

### Folder structure:

```
react-soap-banking/
├── src/
│   ├── api/soapClient.ts
│   ├── components/BalanceChecker.tsx
│   ├── components/TransferForm.tsx
│   ├── App.tsx
│   └── index.tsx
├── public/
│   └── index.html
├── package.json
└── tsconfig.json
```

---

### ✅ 1. `package.json`

```json
{
  "name": "react-soap-banking",
  "version": "1.0.0",
  "private": true,
  "dependencies": {
    "axios": "^1.6.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-scripts": "5.0.1",
    "typescript": "^5.2.0"
  },
  "scripts": {
    "start": "react-scripts start"
  }
}
```

---

### ✅ 2. `tsconfig.json`

```json
{
  "compilerOptions": {
    "target": "es5",
    "lib": ["dom", "dom.iterable", "esnext"],
    "jsx": "react",
    "module": "esnext",
    "moduleResolution": "node",
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "strict": true
  },
  "include": ["src"]
}
```

---

### ✅ 3. `src/api/soapClient.ts`

```ts
import axios from "axios";

const SOAP_URL = "http://localhost:8080/ws";

export const getBalance = async (accountNumber: string) => {
  const xml = `
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:bank="http://example.com/banking">
      <soapenv:Header/>
      <soapenv:Body>
        <bank:GetBalanceRequest>
          <bank:accountNumber>${accountNumber}</bank:accountNumber>
        </bank:GetBalanceRequest>
      </soapenv:Body>
    </soapenv:Envelope>`;

  const { data } = await axios.post(SOAP_URL, xml, {
    headers: { "Content-Type": "text/xml" },
  });

  const match = data.match(/<balance>([\d.]+)<\/balance>/);
  return match ? match[1] : "0";
};

export const transferFunds = async (
  from: string,
  to: string,
  amount: number
) => {
  const xml = `
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:bank="http://example.com/banking">
      <soapenv:Header/>
      <soapenv:Body>
        <bank:TransferRequest>
          <bank:fromAccount>${from}</bank:fromAccount>
          <bank:toAccount>${to}</bank:toAccount>
          <bank:amount>${amount}</bank:amount>
        </bank:TransferRequest>
      </soapenv:Body>
    </soapenv:Envelope>`;

  const { data } = await axios.post(SOAP_URL, xml, {
    headers: { "Content-Type": "text/xml" },
  });

  const match = data.match(/<status>([^<]+)<\/status>/);
  return match ? match[1] : "Unknown";
};
```

---

### ✅ 4. `src/components/BalanceChecker.tsx`

```tsx
import React, { useState } from "react";
import { getBalance } from "../api/soapClient";

export const BalanceChecker: React.FC = () => {
  const [account, setAccount] = useState("");
  const [balance, setBalance] = useState("");

  const checkBalance = async () => {
    const result = await getBalance(account);
    setBalance(result);
  };

  return (
    <div>
      <h2>Check Account Balance</h2>
      <input
        value={account}
        onChange={(e) => setAccount(e.target.value)}
        placeholder="Account Number"
      />
      <button onClick={checkBalance}>Check</button>
      {balance && <p>Balance: ₹{balance}</p>}
    </div>
  );
};
```

---

### ✅ 5. `src/components/TransferForm.tsx`

```tsx
import React, { useState } from "react";
import { transferFunds } from "../api/soapClient";

export const TransferForm: React.FC = () => {
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [amount, setAmount] = useState(0);
  const [status, setStatus] = useState("");

  const handleTransfer = async () => {
    const result = await transferFunds(from, to, amount);
    setStatus(result);
  };

  return (
    <div>
      <h2>Transfer Funds</h2>
      <input
        value={from}
        onChange={(e) => setFrom(e.target.value)}
        placeholder="From Account"
      />
      <input
        value={to}
        onChange={(e) => setTo(e.target.value)}
        placeholder="To Account"
      />
      <input
        type="number"
        value={amount}
        onChange={(e) => setAmount(Number(e.target.value))}
        placeholder="Amount"
      />
      <button onClick={handleTransfer}>Transfer</button>
      {status && <p>Status: {status}</p>}
    </div>
  );
};
```

---

### ✅ 6. `src/App.tsx`

```tsx
import React from "react";
import { BalanceChecker } from "./components/BalanceChecker";
import { TransferForm } from "./components/TransferForm";

const App: React.FC = () => (
  <div style={{ padding: "2rem", fontFamily: "Arial" }}>
    <h1>Banking App (SOAP)</h1>
    <BalanceChecker />
    <hr />
    <TransferForm />
  </div>
);

export default App;
```

---

### ✅ 7. `src/index.tsx`

```tsx
import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root")!);
root.render(<App />);
```

---

### ✅ 8. `public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>SOAP Banking App</title>
  </head>
  <body>
    <div id="root"></div>
  </body>
</html>
```

---

## ✅ Run Instructions

1. **Start Spring Boot backend** (on port `8080`)
2. **Start React frontend**

```bash
cd react-soap-banking
npm install
npm start
```

Open [http://localhost:3000](http://localhost:3000)

Let's Dockerize both the **Spring Boot SOAP Backend** and the **ReactJS (TypeScript) Frontend**.

---

## 🐳 Dockerizing Spring Boot SOAP Backend

### ✅ Step 1: Add `Dockerfile` to your Spring Boot project root

```Dockerfile
# Dockerfile for Spring Boot SOAP Backend
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/banking-soap-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

### ✅ Step 2: Add `docker-compose.yml` (optional but useful)

```yaml
version: "3.8"
services:
  soap-backend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
```

---

### ✅ Step 3: Build and run

```bash
# From backend project root
mvn clean package -DskipTests
docker build -t banking-soap-backend .
docker run -p 8080:8080 banking-soap-backend
```

Or if using `docker-compose`:

```bash
docker-compose up --build
```

---

## 🐳 Dockerizing ReactJS Frontend (TypeScript)

### ✅ Step 1: Add `Dockerfile` in the frontend project root

```Dockerfile
# Dockerfile for React Frontend
FROM node:18 as build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

### ✅ Step 2: Add `docker-compose.yml` to host both

If you're using both frontend and backend:

```yaml
version: "3.8"
services:
  soap-backend:
    build:
      context: ./springboot-backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"

  react-frontend:
    build:
      context: ./react-soap-banking
      dockerfile: Dockerfile
    ports:
      - "3000:80"
```

---

### ✅ Step 3: Build & Run both

```bash
docker-compose up --build
```

Visit:

- Backend WSDL: [http://localhost:8080/ws/banking.wsdl](http://localhost:8080/ws/banking.wsdl)
- Frontend App: [http://localhost:3000](http://localhost:3000)

---

