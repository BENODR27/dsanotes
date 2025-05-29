Here is a complete, professional guide to **JPA (Java Persistence API)** and **Hibernate** with Spring Boot, covering all key concepts, annotations, real-world examples, and best practices.

---

## 🔷 What is JPA?

**JPA** is a **Java specification** for managing relational data in Java applications. It’s **just an API**, not an implementation.

### 🔷 What is Hibernate?

**Hibernate** is the **most popular implementation** of JPA.

---

## 🧱 Core JPA Concepts

| Concept       | Description                          |
| ------------- | ------------------------------------ |
| Entity        | Java class mapped to a DB table      |
| EntityManager | Interface for persistence operations |
| Repository    | Abstraction for CRUD and queries     |
| JPQL          | Java Persistence Query Language      |
| Transactions  | Unit of work for DB operations       |

---

## 📁 Project Structure Example

```
springboot-jpa-demo/
├── entity/Employee.java
├── repository/EmployeeRepository.java
├── controller/EmployeeController.java
├── service/EmployeeService.java
└── application.properties
```

---

## ✅ Entity Example (`Employee.java`)

```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String department;

    private double salary;

    // Getters and Setters
}
```

---

## ✅ Repository Example

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Custom finder method
    List<Employee> findByDepartment(String department);
}
```

---

## ✅ Service Layer

```java
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> findByDept(String dept) {
        return repository.findByDepartment(dept);
    }
}
```

---

## ✅ Controller Layer

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    public List<Employee> getAll() {
        return service.getAllEmployees();
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return service.saveEmployee(employee);
    }

    @GetMapping("/dept/{dept}")
    public List<Employee> byDept(@PathVariable String dept) {
        return service.findByDept(dept);
    }
}
```

---

## 🛠 application.properties (for H2/MySQL)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

---

## 🔑 JPA Annotations Overview

| Annotation                     | Purpose                                |
| ------------------------------ | -------------------------------------- |
| `@Entity`                      | Marks class as a persistent entity     |
| `@Table(name="...")`           | Optional, sets custom table name       |
| `@Id`                          | Primary key                            |
| `@GeneratedValue`              | Auto-generation of ID                  |
| `@Column`                      | Customizes column (nullable, length)   |
| `@OneToMany`, `@ManyToOne`     | For relationships                      |
| `@JoinColumn`                  | Specifies join column in relationships |
| `@Transient`                   | Excludes field from persistence        |
| `@Enumerated(EnumType.STRING)` | Persist enums as strings               |

---

## 🔄 Relationships

### ✅ OneToMany / ManyToOne

```java
// Department.java
@Entity
public class Department {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;
}

// Employee.java
@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

---

## 🔍 JPQL & Native Queries

### JPQL

```java
@Query("SELECT e FROM Employee e WHERE e.salary > :salary")
List<Employee> findEmployeesWithSalaryGreaterThan(@Param("salary") double salary);
```

### Native SQL

```java
@Query(value = "SELECT * FROM employees WHERE department = ?1", nativeQuery = true)
List<Employee> findByDeptNative(String dept);
```

---

## 🧪 JPA Testing Example

```java
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void testSaveAndFind() {
        Employee emp = new Employee();
        emp.setName("John");
        emp.setDepartment("IT");
        emp.setSalary(5000);

        repository.save(emp);

        List<Employee> result = repository.findByDepartment("IT");
        assertFalse(result.isEmpty());
    }
}
```

---

## ✅ JPA Lifecycle Callbacks

```java
@PrePersist
public void beforeSave() {
    System.out.println("Before insert");
}

@PostLoad
public void afterLoad() {
    System.out.println("After fetch");
}
```

---

## 🧠 Best Practices

| Practice                          | Description                                   |
| --------------------------------- | --------------------------------------------- |
| Use `JpaRepository`               | Simplifies basic CRUD and pagination          |
| Avoid bidirectional relationships | Unless needed, use unidirectional for clarity |
| Use `@Transactional` in service   | Ensures atomic operations                     |
| Avoid `fetch = FetchType.EAGER`   | Prefer `LAZY` for performance                 |
| Use DTOs for response bodies      | Don't expose entities directly via APIs       |

---

## 📦 Summary

| Concept         | Key Points                                      |
| --------------- | ----------------------------------------------- |
| JPA             | API for ORM in Java                             |
| Hibernate       | Most widely used JPA implementation             |
| Spring Data JPA | Simplifies CRUD, paging, and queries            |
| Entity          | Mapped to DB table with annotations             |
| Repository      | Interface to manage entities                    |
| JPQL            | Query language based on entity names            |
| Relationships   | `@OneToMany`, `@ManyToOne`, `@JoinColumn`, etc. |

---

Would you like:

- A full downloadable GitHub Spring Boot + JPA project?
- Advanced topics like:

  - Soft deletes
  - Auditing with `@CreatedDate`
  - Entity Graphs
  - Multitenancy

Let me know!
Excellent — here's a full professional-level JPA code and concept coverage with all possible use cases, examples, and explanations included in this chat. This will be exhaustive and production-ready.

---

# ✅ PART 1: JPA BASIC ENTITY MAPPING

## 1.1. Basic Entity

```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "salary")
    private Double salary;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @Lob
    private String resume;

    @Transient
    private int calculatedAge; // not persisted

    // Getters and Setters
}
```

---

## 1.2. Enum Mapping

```java
public enum Role {
    ADMIN, MANAGER, DEVELOPER
}
```

---

## 1.3. ID Generation Strategies

```java
@GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment (MySQL)
@GeneratedValue(strategy = GenerationType.SEQUENCE) // Oracle, PostgreSQL
@GeneratedValue(strategy = GenerationType.AUTO)     // lets provider choose
```

---

# ✅ PART 2: RELATIONSHIPS

## 2.1. One-to-One

```java
@Entity
public class User {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}

@Entity
public class Profile {
    @Id
    private Long id;
    private String bio;
}
```

---

## 2.2. One-to-Many / Many-to-One

```java
@Entity
public class Department {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;
}

@Entity
public class Employee {
    @Id
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
```

---

## 2.3. Many-to-Many

```java
@Entity
public class Student {
    @Id
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
      name = "student_course",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;
}

@Entity
public class Course {
    @Id
    private Long id;
    private String title;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
}
```

---

# ✅ PART 3: REPOSITORY LAYER

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByName(String name);
    List<Employee> findBySalaryBetween(double min, double max);

    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:keyword%")
    List<Employee> searchByName(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM employees WHERE salary > ?1", nativeQuery = true)
    List<Employee> findHighEarners(double salary);
}
```

---

# ✅ PART 4: CUSTOM QUERIES

### 4.1. JPQL

```java
@Query("SELECT e FROM Employee e WHERE e.role = :role")
List<Employee> findByRole(@Param("role") Role role);
```

### 4.2. Native SQL

```java
@Query(value = "SELECT * FROM employees WHERE department = ?1", nativeQuery = true)
List<Employee> findByDepartmentNative(String dept);
```

---

# ✅ PART 5: AUDITING

```java
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Auditable {
    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant modifiedDate;
}
```

Enable it in config:

```java
@Configuration
@EnableJpaAuditing
public class JpaConfig {}
```

---

# ✅ PART 6: ENTITY GRAPH (Performance Optimization)

```java
@Entity
@NamedEntityGraph(name = "Employee.detail", attributeNodes = @NamedAttributeNode("department"))
public class Employee { ... }

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(value = "Employee.detail", type = EntityGraph.EntityGraphType.LOAD)
    List<Employee> findAll();
}
```

---

# ✅ PART 7: SOFT DELETE

```java
@Entity
@SQLDelete(sql = "UPDATE employee SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Employee {
    ...
    private boolean deleted = false;
}
```

---

# ✅ PART 8: SPECIFICATIONS (Dynamic Queries)

```java
public class EmployeeSpecifications {
    public static Specification<Employee> hasDepartment(String dept) {
        return (root, query, builder) ->
            builder.equal(root.get("department"), dept);
    }
}
```

Use with:

```java
List<Employee> results = employeeRepository.findAll(EmployeeSpecifications.hasDepartment("IT"));
```

---

# ✅ PART 9: PAGING & SORTING

```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("salary").descending());
Page<Employee> employees = employeeRepository.findAll(pageable);
```

---

# ✅ PART 10: TRANSACTIONAL SUPPORT

```java
@Service
public class EmployeeService {

    @Transactional
    public void transferSalary(Long fromId, Long toId, double amount) {
        Employee from = repo.findById(fromId).orElseThrow();
        Employee to = repo.findById(toId).orElseThrow();

        from.setSalary(from.getSalary() - amount);
        to.setSalary(to.getSalary() + amount);
    }
}
```

---

# ✅ PART 11: TESTING

```java
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repo;

    @Test
    public void testSaveAndFind() {
        Employee e = new Employee();
        e.setName("John");
        e.setSalary(6000.0);
        repo.save(e);

        List<Employee> list = repo.findByName("John");
        assertEquals(1, list.size());
    }
}
```

---

# ✅ PART 12: ENTITY LIFECYCLE CALLBACKS

```java
@Entity
public class Employee {

    @PrePersist
    public void beforeSave() {
        System.out.println("Before insert");
    }

    @PostLoad
    public void afterLoad() {
        System.out.println("After fetch");
    }
}
```

---

# 🧠 Best Practices Summary

- ✅ Prefer `JpaRepository` over `CrudRepository`
- ✅ Use constructor injection
- ✅ Avoid exposing entities in controllers → use DTOs
- ✅ Use `@Transactional` in service layer, not repository
- ✅ Avoid EAGER fetching by default
- ✅ Use Entity Graphs instead of `JOIN FETCH` for performance
- ✅ Handle `Optional<T>` in findById properly
- ✅ Write integration tests with `@DataJpaTest`

---

Here’s a complete and **professional-grade list of all important annotations used in JPA & Hibernate** with clear **explanations and examples**, grouped by purpose — suitable for **interviews, projects, and production** use.

---

# ✅ 1. ENTITY DECLARATION

### `@Entity`

Marks a class as a JPA entity mapped to a table.

```java
@Entity
public class Employee {}
```

---

### `@Table(name = "employee_table")`

Defines the database table name for the entity.

```java
@Entity
@Table(name = "employee_table")
public class Employee {}
```

---

### `@MappedSuperclass`

Used to define a superclass whose fields are inherited by JPA entities but is not itself an entity.

```java
@MappedSuperclass
public class Auditable {
    @CreatedDate private LocalDateTime createdAt;
}
```

---

### `@Embeddable`

Marks a class as **embeddable** in an entity.

```java
@Embeddable
public class Address {
    private String city;
    private String state;
}
```

```java
@Entity
public class User {
    @Embedded
    private Address address;
}
```

---

# ✅ 2. PRIMARY KEY ANNOTATIONS

### `@Id`

Marks the field as the **primary key**.

```java
@Id
private Long id;
```

---

### `@GeneratedValue(strategy = ...)`

Auto-generates the primary key.

```java
@GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL
@GeneratedValue(strategy = GenerationType.SEQUENCE) // Oracle/PostgreSQL
@GeneratedValue(strategy = GenerationType.AUTO)     // Default
```

---

### `@SequenceGenerator` & `@TableGenerator`

Custom generators for primary key sequences.

```java
@SequenceGenerator(name = "emp_seq", sequenceName = "emp_sequence", allocationSize = 1)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
```

---

# ✅ 3. FIELD MAPPING

### `@Column(name = "emp_name", nullable = false, length = 100)`

Maps field to column with custom constraints.

```java
@Column(name = "emp_name", nullable = false)
private String name;
```

---

### `@Lob`

Used for large objects like long text or files.

```java
@Lob
private String description;
```

---

### `@Enumerated(EnumType.STRING)`

Stores enums as `String` (preferred) or `Ordinal`.

```java
@Enumerated(EnumType.STRING)
private Role role;
```

---

### `@Temporal`

Used for `java.util.Date` or `Calendar`.

```java
@Temporal(TemporalType.DATE)
private Date joiningDate;
```

---

### `@Transient`

Marks a field as **not to be persisted**.

```java
@Transient
private int tempCalculation;
```

---

# ✅ 4. RELATIONSHIPS

### `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`

```java
@OneToOne
@JoinColumn(name = "profile_id")
private Profile profile;
```

```java
@OneToMany(mappedBy = "department")
private List<Employee> employees;
```

```java
@ManyToOne
@JoinColumn(name = "dept_id")
private Department department;
```

```java
@ManyToMany
@JoinTable(name = "student_course",
   joinColumns = @JoinColumn(name = "student_id"),
   inverseJoinColumns = @JoinColumn(name = "course_id"))
private List<Course> courses;
```

---

### `@JoinColumn`

Specifies the **foreign key column**.

---

### `@JoinTable`

Defines the join table for many-to-many relationships.

---

# ✅ 5. LIFECYCLE EVENTS

### `@PrePersist`, `@PostPersist`, `@PreUpdate`, `@PostUpdate`, `@PreRemove`, `@PostRemove`, `@PostLoad`

```java
@PrePersist
public void beforeInsert() {
    this.createdAt = LocalDateTime.now();
}
```

---

# ✅ 6. ENTITY GRAPH (FETCHING OPTIMIZATION)

### `@NamedEntityGraph`, `@EntityGraph`

```java
@NamedEntityGraph(name = "Employee.withDepartment",
   attributeNodes = @NamedAttributeNode("department"))
@Entity
public class Employee {}

@EntityGraph(value = "Employee.withDepartment", type = EntityGraph.EntityGraphType.LOAD)
List<Employee> findAll();
```

---

# ✅ 7. AUDITING

### `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy`

Used with `@EntityListeners(AuditingEntityListener.class)`.

---

# ✅ 8. INHERITANCE

### `@Inheritance(strategy = InheritanceType.SINGLE_TABLE|TABLE_PER_CLASS|JOINED)`

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Payment {}
```

---

# ✅ 9. CUSTOM QUERY ANNOTATIONS

### `@Query`

Write JPQL or native SQL.

```java
@Query("SELECT e FROM Employee e WHERE e.name = :name")
List<Employee> findByName(@Param("name") String name);
```

---

### `@Param`

Binds method parameter to query parameter.

---

### `@Modifying`

Used with `@Query` for update/delete queries.

```java
@Modifying
@Query("DELETE FROM Employee e WHERE e.salary < :limit")
void deleteLowSalaries(@Param("limit") double limit);
```

---

# ✅ 10. SOFT DELETE

### `@SQLDelete`, `@Where`

```java
@SQLDelete(sql = "UPDATE employee SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
```

---

# ✅ 11. FETCHING STRATEGIES

### `fetch = FetchType.LAZY` or `FetchType.EAGER`

```java
@OneToMany(fetch = FetchType.LAZY)
```

---

# ✅ 12. JPA REPOSITORY BEHAVIOR

### `@Repository`

Marks the interface as a Spring Data repository.

---

# ✅ 13. SPRING DATA SPECIAL

### `@EnableJpaRepositories`

Enable JPA repositories in a Spring Boot config class.

```java
@EnableJpaRepositories(basePackages = "com.example.repo")
```

---

### `@EntityScan`

Scan for JPA entities if not in same package.

```java
@EntityScan(basePackages = "com.example.entity")
```

---

# ✅ 14. TRANSACTION SUPPORT

### `@Transactional`

Manages transactions at service or method level.

```java
@Transactional
public void save(Employee e) { ... }
```

---

# ✅ 15. PROJECTION & DTO MAPPING

### `@Value` or Constructor-based mapping in Spring

```java
public interface EmployeeProjection {
    String getName();
    Double getSalary();
}
```

---
