Emirates, as a global airline and part of **Emirates Group IT**, uses a **diverse and highly specialized technology stack** to manage both **in-flight systems** and **ground-based enterprise systems**. Here’s a detailed breakdown of:

---

## ✈️ Operating Systems & Platforms Used in Emirates Flights

### 🛫 **In-Flight Systems (Cabin & Cockpit Tech)**

These are primarily embedded, real-time systems running on certified aviation-grade platforms.

| System                                        | OS Used                                                    | Purpose                                                                |
| --------------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------------------- |
| **IFE (In-Flight Entertainment)** – ICE       | **Embedded Linux**, **Android-based subsystems**, **RTOS** | Runs passenger entertainment, seat-back apps, and cabin crew interface |
| **Cabin Management System (CMS)**             | **VxWorks**, **QNX**, or **RTEMS**                         | Lighting, temperature, announcements, seat control                     |
| **Aircraft Data Communication & Monitoring**  | **Proprietary OS**, **RTOS**, or **ARINC 653**             | Flight data monitoring, telemetry, engine performance                  |
| **Wi-Fi & Connectivity (OnAir or Panasonic)** | **Linux-based systems** on custom aircraft routers         | In-flight Wi-Fi, satellite communication control                       |
| **Electronic Flight Bag (EFB)**               | **Windows 10**, **iPadOS**, **Android**                    | Pilot use: flight plans, manuals, checklists (on Surface Pro or iPad)  |

---

## 🧑‍💻 Programming Languages Used in Emirates Flight-Related Applications

### 💡 Embedded / Onboard Systems (Airplane Level)

* **C/C++** – For all real-time firmware in flight hardware
* **Ada** – Used in avionics due to safety certifications
* **Python** – For test harnesses, data validation, ML post-flight analytics
* **Java/Kotlin** – Android-based seat-back UI and IFE systems
* **JavaScript** – For browser-based passenger UI in newer IFE systems

---

## 🌐 Emirates Ground & Enterprise Systems (Connected to Flights)

### ✅ Emirates Group IT Stack (as per job description)

Used for enterprise apps, mobile/web check-in, crew management, and booking systems:

| Layer            | Technologies Used                                              |
| ---------------- | -------------------------------------------------------------- |
| **Backend**      | Java 11–17, Spring Boot, Python 3.7+, Node.js                  |
| **Frontend**     | React.js, Angular (legacy support), HTML5                      |
| **Databases**    | Oracle, SQL Server, PostgreSQL, Couchbase, ElasticSearch       |
| **APIs**         | REST, SOAP, OpenAPI Spec                                       |
| **Integration**  | Solace (Pub/Sub), SnapLogic, Informatica                       |
| **DevOps/Infra** | Docker, Kubernetes, OpenShift, Jenkins, SonarQube, AppDynamics |
| **Cloud**        | AWS, Azure, Google Cloud (hybrid)                              |

### Examples:

* **Booking System**: Java + Oracle + Spring Boot microservices
* **Check-in App**: React.js frontend + Java backend + Kafka/Redis for state
* **Crew Scheduling**: Node.js or Java, integrated with legacy ALCS and Oracle
* **Flight Tracker Dashboard**: Angular + Python APIs + ElasticSearch

---

## 🧠 Summary

| Environment       | OS Used                                      | Programming Languages                               |
| ----------------- | -------------------------------------------- | --------------------------------------------------- |
| In-Flight Systems | Embedded Linux, RTOS (VxWorks, QNX), Android | C, C++, Java, Kotlin, Ada                           |
| Cabin IFE         | Linux, Android                               | Java, HTML5, JavaScript                             |
| Ground Apps       | Windows, Linux (RHEL), macOS (for dev)       | Java, Python, JavaScript, Node.js, SQL              |
| Mobile Apps       | Android, iOS                                 | Kotlin, Swift, React Native, Flutter (in some apps) |

---

Would you like a visual architecture of one of these systems (e.g., In-Flight Entertainment or Booking Flow)? Or even a full sample project that mimics how Emirates might build one?
