Great! Here's how to create **systemd service files** to run **Apache Kafka** and **Zookeeper** as system services on an Ubuntu server. This will allow you to start/stop Kafka using `systemctl`.

---

## 🧱 PART 1: Create Zookeeper systemd Service

### 1. Create the file

```bash
sudo nano /etc/systemd/system/zookeeper.service
```

### 2. Paste the following:

```ini
[Unit]
Description=Apache Zookeeper Server
After=network.target

[Service]
Type=simple
User=kafka
ExecStart=/home/kafka/kafka/kafka_2.13-3.6.0/bin/zookeeper-server-start.sh /home/kafka/kafka/kafka_2.13-3.6.0/config/zookeeper.properties
ExecStop=/home/kafka/kafka/kafka_2.13-3.6.0/bin/zookeeper-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```

---

## 🧱 PART 2: Create Kafka systemd Service

### 1. Create the file

```bash
sudo nano /etc/systemd/system/kafka.service
```

### 2. Paste the following:

```ini
[Unit]
Description=Apache Kafka Server
Requires=zookeeper.service
After=zookeeper.service

[Service]
Type=simple
User=kafka
ExecStart=/home/kafka/kafka/kafka_2.13-3.6.0/bin/kafka-server-start.sh /home/kafka/kafka/kafka_2.13-3.6.0/config/server.properties
ExecStop=/home/kafka/kafka/kafka_2.13-3.6.0/bin/kafka-server-stop.sh
Restart=on-abnormal

[Install]
WantedBy=multi-user.target
```

> 🔁 **Update the paths** `/home/kafka/kafka/kafka_2.13-3.6.0/` if your Kafka is in a different location.

---

## ✅ Reload & Start Services

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable zookeeper
sudo systemctl enable kafka
sudo systemctl start zookeeper
sudo systemctl start kafka
```

---

## 🔍 Check Status

```bash
sudo systemctl status zookeeper
sudo systemctl status kafka
```

You should see both services running.

---

## 🛑 Stop When Needed

```bash
sudo systemctl stop kafka
sudo systemctl stop zookeeper
```

---

Here’s a **step-by-step guide** to self-deploy **Apache Kafka on an Ubuntu server** and integrate it with a **Spring Boot application**. This includes installation, configuration, testing, and Spring Boot integration.

---

## 🧱 PART 1: Install and Run Kafka on Ubuntu

### ✅ Prerequisites

* Ubuntu 20.04/22.04
* Java 11 or higher
* 2 GB+ RAM
* Open port 9092 (Kafka) and 2181 (Zookeeper)

---

### 1. Install Java

```bash
sudo apt update
sudo apt install openjdk-11-jdk -y
java -version
```

---

### 2. Create Kafka User and Directory

```bash
sudo adduser kafka
sudo su - kafka
mkdir -p ~/kafka
cd ~/kafka
```

---

### 3. Download and Extract Kafka

```bash
wget https://downloads.apache.org/kafka/3.6.0/kafka_2.13-3.6.0.tgz
tar -xvzf kafka_2.13-3.6.0.tgz
cd kafka_2.13-3.6.0
```

---

### 4. Start Zookeeper

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

📝 *Leave this running or use `tmux`/`screen` to background.*

---

### 5. Start Kafka Server (new terminal or background)

```bash
bin/kafka-server-start.sh config/server.properties
```

---

### ✅ Kafka is now running on:

* **Zookeeper**: `localhost:2181`
* **Kafka Broker**: `localhost:9092`

---

### 6. Create a Test Topic (optional)

```bash
bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

---

## 🧱 PART 2: Spring Boot Kafka Integration

### 1. Add Kafka Dependency

```xml
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
</dependency>
```

---

### 2. application.yml

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: my-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

---

### 3. DTO Class

```java
public class BookingEvent {
    private String user;
    private String flightId;
    private double amount;
    // Getters, Setters, Constructor
}
```

---

### 4. Kafka Producer

```java
@Service
public class BookingProducer {

    @Autowired
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void sendEvent(BookingEvent event) {
        kafkaTemplate.send("booking-events", event);
    }
}
```

---

### 5. Kafka Consumer

```java
@Service
public class BookingConsumer {

    @KafkaListener(topics = "booking-events", groupId = "my-group")
    public void consume(BookingEvent event) {
        System.out.println("🎯 Event Received: " + event.getUser());
    }
}
```

---

### 6. Triggering Event (optional controller)

```java
@RestController
@RequestMapping("/api/book")
public class BookingController {

    @Autowired
    private BookingProducer producer;

    @PostMapping
    public ResponseEntity<String> book(@RequestBody BookingEvent event) {
        producer.sendEvent(event);
        return ResponseEntity.ok("Booking event sent");
    }
}
```

---

## 🧪 Test the Flow

1. Start Kafka/Zookeeper
2. Run your Spring Boot app
3. POST a request:

```bash
curl -X POST http://localhost:8080/api/book \
-H "Content-Type: application/json" \
-d '{"user":"john@airline.com", "flightId":"EK202", "amount":500}'
```

🖥 Console:

```
🎯 Event Received: john@airline.com
```

---

## 🛠️ Optional Enhancements

| Feature              | How                         |
| -------------------- | --------------------------- |
| Run Kafka as service | Systemd service file        |
| Use Docker Compose   | Faster setup                |
| Add Monitoring       | JMX, Prometheus Exporter    |
| Use Cluster Setup    | Run multiple brokers for HA |

---

