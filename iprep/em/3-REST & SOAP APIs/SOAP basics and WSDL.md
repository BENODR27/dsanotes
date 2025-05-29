Here is a complete professional-grade example demonstrating SOAP web services with Spring Boot and Angular 19, including WSDL generation and client integration.

---

## 🔧 Project Overview

- **Backend**: Spring Boot exposing a SOAP Web Service (WSDL auto-generated).
- **Frontend**: Angular 19 consuming the SOAP service using `HttpClient`.
- **Service**: CountryInfoService to get country details (name, capital, currency).

---

## 🧱 1. Spring Boot SOAP Server

### ✅ `pom.xml` dependencies

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web-services</artifactId>
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
```

---

### ✅ Country Data XSD (resources/countries.xsd)

```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://example.com/countries"
           xmlns:tns="http://example.com/countries"
           elementFormDefault="qualified">

  <xs:element name="getCountryRequest">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="name" type="xs:string"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>

  <xs:element name="getCountryResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="country" type="tns:country"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>

  <xs:complexType name="country">
    <xs:sequence>
      <xs:element name="name" type="xs:string"/>
      <xs:element name="capital" type="xs:string"/>
      <xs:element name="currency" type="xs:string"/>
    </xs:sequence>
  </xs:complexType>

</xs:schema>
```

---

### ✅ Generate Java classes from XSD

Add plugin in `pom.xml`:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>jaxb2-maven-plugin</artifactId>
  <version>2.5.0</version>
  <executions>
    <execution>
      <goals><goal>xjc</goal></goals>
    </execution>
  </executions>
  <configuration>
    <schemaDirectory>src/main/resources</schemaDirectory>
    <outputDirectory>src/main/java</outputDirectory>
  </configuration>
</plugin>
```

Run:

```bash
mvn clean compile
```

---

### ✅ SOAP Endpoint Configuration

#### `WebServiceConfig.java`

```java
@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(context);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean(name = "countries")
  public DefaultWsdl11Definition countriesWsdl(XsdSchema countriesSchema) {
    DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
    wsdl.setPortTypeName("CountryPort");
    wsdl.setLocationUri("/ws");
    wsdl.setTargetNamespace("http://example.com/countries");
    wsdl.setSchema(countriesSchema);
    return wsdl;
  }

  @Bean
  public XsdSchema countriesSchema() {
    return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
  }
}
```

---

### ✅ SOAP Endpoint

```java
@Endpoint
public class CountryEndpoint {

  private static final String NAMESPACE_URI = "http://example.com/countries";

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
  @ResponsePayload
  public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
    Country country = new Country();
    country.setName("India");
    country.setCapital("New Delhi");
    country.setCurrency("INR");

    GetCountryResponse response = new GetCountryResponse();
    response.setCountry(country);
    return response;
  }
}
```

---

### ✅ Run the App

```bash
mvn spring-boot:run
```

Access WSDL:

```
http://localhost:8080/ws/countries.wsdl
```

---

## 🖥️ 2. Angular 19 Frontend

### ✅ Generate Angular project

```bash
ng new country-app --routing --style=scss
cd country-app
```

---

### ✅ Create Service to Call SOAP

#### Install XML parser

```bash
npm install xml2js
```

#### `soap.service.ts`

```ts
import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map } from "rxjs/operators";
import { parseString } from "xml2js";

@Injectable({ providedIn: "root" })
export class SoapService {
  private url = "/ws"; // proxy via Angular

  constructor(private http: HttpClient) {}

  getCountry(name: string) {
    const xml = `
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:con="http://example.com/countries">
        <soapenv:Header/>
        <soapenv:Body>
          <con:getCountryRequest>
            <con:name>${name}</con:name>
          </con:getCountryRequest>
        </soapenv:Body>
      </soapenv:Envelope>
    `;

    const headers = new HttpHeaders()
      .set("Content-Type", "text/xml")
      .set("Accept", "text/xml");

    return this.http
      .post(this.url, xml, { headers, responseType: "text" })
      .pipe(
        map((response) => {
          let result: any;
          parseString(
            response,
            { explicitArray: false },
            (err, res) => (result = res)
          );
          return result["SOAP-ENV:Envelope"]["SOAP-ENV:Body"][
            "ns2:getCountryResponse"
          ].country;
        })
      );
  }
}
```

---

### ✅ Display Result in Component

```ts
@Component({...})
export class CountryComponent {
  country: any;

  constructor(private soapService: SoapService) {}

  fetch() {
    this.soapService.getCountry('India').subscribe(data => this.country = data);
  }
}
```

---

## 🌐 Add Proxy Config for Angular

#### `proxy.conf.json`

```json
{
  "/ws": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```

Update `angular.json`:

```json
"serve": {
  "options": {
    "proxyConfig": "proxy.conf.json"
  }
}
```

Run Angular:

```bash
ng serve
```

---

## ✅ Output

- Angular UI fetches country info via SOAP request
- WSDL at `/ws/countries.wsdl`
- Auto-generated classes from `countries.xsd`

---

Would you like:

- Docker setup for this full stack?
- CI/CD with GitLab or Jenkins?
- Postman collection to test SOAP APIs?

Let me know!
Great! Here's a professional airline-related project idea that uses SOAP with Spring Boot (backend) and Angular 19 (frontend):

---

## ✈️ Airline Ticket Booking System – SOAP-Based

### 🧩 **Modules Covered**

- Flight search and booking
- Passenger management
- SOAP web service for booking
- Angular UI to consume the SOAP service
- Integration with WSDL for frontend communication

---

### 🧱 Project Architecture

```
Angular 19 (Client)
       |
       |  — (WSDL) —
       |
Spring Boot (SOAP Service)
       |
       ├── FlightService (search, reserve)
       └── PassengerService (add, update, fetch)
```

---

### 🔧 Technologies

- Backend: Spring Boot, Spring Web Services, JAXB, Maven
- Frontend: Angular 19, ngx-soap, RxJS
- Data: H2 (in-memory DB for demo), JPA/Hibernate

---

### 📦 Key Backend Files (Spring Boot)

#### 1. `FlightEndpoint.java` – SOAP controller

```java
@Endpoint
public class FlightEndpoint {
    private static final String NAMESPACE_URI = "http://airline.com/flights";

    @Autowired
    private FlightService flightService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SearchFlightRequest")
    @ResponsePayload
    public SearchFlightResponse searchFlights(@RequestPayload SearchFlightRequest request) {
        List<Flight> results = flightService.search(request.getSource(), request.getDestination());
        SearchFlightResponse response = new SearchFlightResponse();
        response.getFlights().addAll(results);
        return response;
    }
}
```

#### 2. `FlightService.java`

```java
@Service
public class FlightService {
    public List<Flight> search(String source, String destination) {
        // Search logic using JPA (demo)
        return flightRepository.findBySourceAndDestination(source, destination);
    }
}
```

#### 3. `flights.xsd` – WSDL schema

```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://airline.com/flights"
           xmlns:tns="http://airline.com/flights"
           elementFormDefault="qualified">

    <xs:element name="SearchFlightRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="source" type="xs:string"/>
                <xs:element name="destination" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="SearchFlightResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="flights" type="tns:Flight" maxOccurs="unbounded"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:complexType name="Flight">
        <xs:sequence>
            <xs:element name="flightNumber" type="xs:string"/>
            <xs:element name="source" type="xs:string"/>
            <xs:element name="destination" type="xs:string"/>
            <xs:element name="price" type="xs:decimal"/>
        </xs:sequence>
    </xs:complexType>
</xs:schema>
```

---

### 💻 Angular 19: Consuming SOAP

#### 1. Install ngx-soap

```bash
npm install ngx-soap
```

#### 2. Setup SOAP Client Service

```ts
@Injectable({ providedIn: "root" })
export class FlightService {
  private client: Client;

  constructor(private soap: NgxSoapService) {
    this.soap
      .createClient("http://localhost:8080/ws/flights.wsdl")
      .then((client) => (this.client = client));
  }

  searchFlights(source: string, destination: string): Observable<any> {
    return this.client.call("searchFlights", { source, destination });
  }
}
```

#### 3. Angular Component

```ts
@Component({
  selector: "app-search",
  templateUrl: "./search.component.html",
})
export class SearchComponent {
  flights = [];

  constructor(private flightService: FlightService) {}

  search() {
    this.flightService.searchFlights("DEL", "DXB").subscribe((res) => {
      this.flights = res.result.flights;
    });
  }
}
```

---

### 📜 Spring Configuration

#### `WebServiceConfig.java`

```java
@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "flights")
    public DefaultWsdl11Definition flightsWsdl(XsdSchema flightsSchema) {
        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("FlightPort");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://airline.com/flights");
        wsdl.setSchema(flightsSchema);
        return wsdl;
    }

    @Bean
    public XsdSchema flightsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("flights.xsd"));
    }
}
```

---

### 🧪 Sample WSDL URL

- Open your browser:

```
http://localhost:8080/ws/flights.wsdl
```

---

Great! Below is a full professional airline-related project demonstrating SOAP-based services using Spring Boot and Angular 19. This includes:

- A backend SOAP web service in Spring Boot
- A frontend Angular 19 app that consumes the SOAP service

---

## ✈️ Airline Booking SOAP Project Overview

**Use Case**: Search flight bookings by passenger name.

---

## 🛠 Backend – Spring Boot SOAP Web Service

### 1. Project Structure

```
airline-soap-backend/
├── src/main/java/com/example/airline/
│   ├── config/
│   │   └── WebServiceConfig.java
│   ├── endpoint/
│   │   └── BookingEndpoint.java
│   ├── model/
│   │   └── Booking.java
│   ├── service/
│   │   └── BookingService.java
│   └── AirlineSoapApplication.java
├── src/main/resources/
│   └── bookings.xsd
```

---

### 2. `bookings.xsd` (WSDL schema)

```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
           targetNamespace="http://example.com/airline"
           xmlns:tns="http://example.com/airline"
           elementFormDefault="qualified">

  <xs:element name="getBookingRequest">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="passengerName" type="xs:string"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>

  <xs:element name="getBookingResponse">
    <xs:complexType>
      <xs:sequence>
        <xs:element name="flight" type="xs:string"/>
        <xs:element name="destination" type="xs:string"/>
      </xs:sequence>
    </xs:complexType>
  </xs:element>

</xs:schema>
```

---

### 3. Generate Java classes from WSDL

Add the following plugin in `pom.xml`:

```xml
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
      <phase>generate-sources</phase>
      <configuration>
        <schemaDirectory>src/main/resources</schemaDirectory>
        <outputDirectory>${project.build.directory}/generated-sources/xjc</outputDirectory>
      </configuration>
    </execution>
  </executions>
</plugin>
```

Run:

```bash
mvn clean install
```

---

### 4. `WebServiceConfig.java`

```java
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext ctx) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(ctx);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "bookings")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema schema) {
        DefaultWsdl11Definition def = new DefaultWsdl11Definition();
        def.setPortTypeName("BookingPort");
        def.setLocationUri("/ws");
        def.setTargetNamespace("http://example.com/airline");
        def.setSchema(schema);
        return def;
    }

    @Bean
    public XsdSchema bookingsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("bookings.xsd"));
    }
}
```

---

### 5. `BookingService.java`

```java
@Service
public class BookingService {
    public Booking getBooking(String name) {
        return new Booking("AI202", "Dubai");
    }
}
```

---

### 6. `BookingEndpoint.java`

```java
@Endpoint
public class BookingEndpoint {

    private static final String NAMESPACE = "http://example.com/airline";
    private final BookingService service;

    public BookingEndpoint(BookingService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getBookingRequest")
    @ResponsePayload
    public GetBookingResponse getBooking(@RequestPayload GetBookingRequest request) {
        Booking booking = service.getBooking(request.getPassengerName());
        GetBookingResponse response = new GetBookingResponse();
        response.setFlight(booking.getFlight());
        response.setDestination(booking.getDestination());
        return response;
    }
}
```

---

## 🌐 Frontend – Angular 19 (Consumes SOAP via proxy)

### 1. Setup HTTP SOAP client in Angular

Install:

```bash
ng new airline-frontend
cd airline-frontend
npm install xml2js
```

---

### 2. `soap.service.ts`

```ts
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map } from "rxjs/operators";
import { parseString } from "xml2js";

@Injectable({ providedIn: "root" })
export class SoapService {
  constructor(private http: HttpClient) {}

  getBooking(name: string) {
    const body = `
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:air="http://example.com/airline">
         <soapenv:Header/>
         <soapenv:Body>
            <air:getBookingRequest>
               <air:passengerName>${name}</air:passengerName>
            </air:getBookingRequest>
         </soapenv:Body>
      </soapenv:Envelope>`;

    const headers = new HttpHeaders({ "Content-Type": "text/xml" });

    return this.http.post("/ws", body, { headers, responseType: "text" }).pipe(
      map((res) => {
        let parsed;
        parseString(res, { explicitArray: false }, (err, result) => {
          parsed = result;
        });
        return parsed["soapenv:Envelope"]["soapenv:Body"][
          "ns2:getBookingResponse"
        ];
      })
    );
  }
}
```

---

### 3. `booking.component.ts`

```ts
@Component({
  selector: "app-booking",
  template: `
    <input [(ngModel)]="name" placeholder="Passenger Name" />
    <button (click)="search()">Search</button>
    <div *ngIf="booking">
      Flight: {{ booking.flight }}, Destination: {{ booking.destination }}
    </div>
  `,
})
export class BookingComponent {
  name = "";
  booking: any;

  constructor(private soapService: SoapService) {}

  search() {
    this.soapService
      .getBooking(this.name)
      .subscribe((data) => (this.booking = data));
  }
}
```

---

### 4. Proxy Config (Angular → Spring Boot)

Create `proxy.conf.json`:

```json
{
  "/ws": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```

Add to `angular.json`:

```json
"serve": {
  "options": {
    "proxyConfig": "proxy.conf.json"
  }
}
```

---

## ✅ Run the Project

1. **Start Spring Boot SOAP server**

```bash
./mvnw spring-boot:run
```

2. **Start Angular frontend**

```bash
ng serve
```

---

## 🧠 Summary

| Component        | Technology         | Purpose                          |
| ---------------- | ------------------ | -------------------------------- |
| Backend          | Spring Boot + SOAP | Expose WSDL-based flight service |
| Frontend         | Angular 19         | Call SOAP and show booking info  |
| XML Bindings     | JAXB / XSD         | Define SOAP structure            |
| Client to Server | `HttpClient` + XML | Angular calling SOAP via proxy   |

Great question! Here's **how to test** the full SOAP-based airline project using both **Postman/SOAP UI** for backend testing and **Angular frontend** for end-to-end user testing.

---

## ✅ 1. Test the SOAP Backend (Spring Boot)

### 🔹 Method 1: Test with **Postman** or **SOAP UI**

#### A. Request

- **Endpoint URL**: `http://localhost:8080/ws`
- **Headers**:

  - `Content-Type: text/xml`

#### B. Body (XML Payload)

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:air="http://example.com/airline">
   <soapenv:Header/>
   <soapenv:Body>
      <air:getBookingRequest>
         <air:passengerName>John Doe</air:passengerName>
      </air:getBookingRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

#### C. Expected Response:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
   <soapenv:Body>
      <ns2:getBookingResponse xmlns:ns2="http://example.com/airline">
         <flight>AI202</flight>
         <destination>Dubai</destination>
      </ns2:getBookingResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## ✅ 2. Test the Angular Frontend (End-to-End)

### 🔹 Steps:

1. Open terminal and start Spring Boot backend:

   ```bash
   ./mvnw spring-boot:run
   ```

2. Start Angular frontend (ensure you have the proxy config):

   ```bash
   ng serve --proxy-config proxy.conf.json
   ```

3. Open the app in your browser:

   ```
   http://localhost:4200
   ```

4. In the UI:

   - Type a passenger name like `John Doe`.
   - Click the **Search** button.
   - You should see:

     ```
     Flight: AI202, Destination: Dubai
     ```

---

## 🧪 Optional: Unit Test the Backend Service Layer

### Example: `BookingServiceTest.java`

```java
@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Test
    public void testGetBooking() {
        Booking booking = bookingService.getBooking("John Doe");
        assertEquals("AI202", booking.getFlight());
        assertEquals("Dubai", booking.getDestination());
    }
}
```

Run with:

```bash
mvn test
```

---

## 🛠 Common Debug Tips

| Problem                        | Solution                                                              |
| ------------------------------ | --------------------------------------------------------------------- |
| No response / blank in Angular | Check if Angular proxy is set up and Spring Boot is running           |
| WSDL not showing               | Visit `http://localhost:8080/ws/bookings.wsdl`                        |
| SOAP error in Postman          | Make sure you use **Content-Type: text/xml** and proper XML structure |
| XML parser error in Angular    | Verify correct use of `xml2js` parsing                                |

---

Would you like me to include automated integration tests or Docker-based testing?
