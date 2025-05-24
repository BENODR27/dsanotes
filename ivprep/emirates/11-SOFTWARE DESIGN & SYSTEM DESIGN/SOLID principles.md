Here’s a **professional guide** to the **SOLID principles** and **software engineering principles** (DRY, KISS, YAGNI) with **real-world examples in TypeScript (React + Node.js style)**.

---

# 🧱 SOLID Principles + DRY, KISS, YAGNI

---

## ✅ S - Single Responsibility Principle (SRP)

**Each class/function should have only one reason to change.**

🔴 **Bad:**

```ts
class UserService {
  registerUser(user: User) {
    // save user
    // send email
  }
}
```

✅ **Good:**

```ts
class UserRepository {
  save(user: User) {
    /* DB logic */
  }
}

class EmailService {
  sendWelcomeEmail(email: string) {
    /* SMTP logic */
  }
}

class UserService {
  constructor(private repo: UserRepository, private mailer: EmailService) {}

  registerUser(user: User) {
    this.repo.save(user);
    this.mailer.sendWelcomeEmail(user.email);
  }
}
```

---

## ✅ O - Open/Closed Principle (OCP)

**Software entities should be open for extension but closed for modification.**

🔴 **Bad:**

```ts
class PaymentProcessor {
  pay(method: "credit" | "paypal") {
    if (method === "credit") {
      /* credit logic */
    } else if (method === "paypal") {
      /* paypal logic */
    }
  }
}
```

✅ **Good:**

```ts
interface PaymentMethod {
  pay(): void;
}

class CreditCard implements PaymentMethod {
  pay() {
    /* credit logic */
  }
}

class Paypal implements PaymentMethod {
  pay() {
    /* paypal logic */
  }
}

class PaymentProcessor {
  constructor(private method: PaymentMethod) {}
  process() {
    this.method.pay();
  }
}
```

---

## ✅ L - Liskov Substitution Principle (LSP)

**Derived classes must be substitutable for their base classes.**

🔴 **Bad:**

```ts
class Bird {
  fly() {}
}

class Ostrich extends Bird {
  fly() {
    throw new Error("Can't fly");
  } // Violates LSP
}
```

✅ **Good:**

```ts
interface Bird {
  move(): void;
}

class Sparrow implements Bird {
  move() {
    console.log("Flying");
  }
}

class Ostrich implements Bird {
  move() {
    console.log("Running");
  }
}
```

---

## ✅ I - Interface Segregation Principle (ISP)

**Clients should not be forced to depend on interfaces they don’t use.**

🔴 **Bad:**

```ts
interface Worker {
  work(): void;
  eat(): void;
}

class Robot implements Worker {
  work() {}
  eat() {} // Irrelevant
}
```

✅ **Good:**

```ts
interface Workable {
  work(): void;
}
interface Eatable {
  eat(): void;
}

class Human implements Workable, Eatable {
  work() {}
  eat() {}
}

class Robot implements Workable {
  work() {}
}
```

---

## ✅ D - Dependency Inversion Principle (DIP)

**High-level modules should not depend on low-level modules, but on abstractions.**

🔴 **Bad:**

```ts
class MySQLDatabase {
  connect() {}
}

class UserService {
  db = new MySQLDatabase(); // Tight coupling
}
```

✅ **Good:**

```ts
interface Database {
  connect(): void;
}

class MySQLDatabase implements Database {
  connect() {}
}

class UserService {
  constructor(private db: Database) {}
}
```

---

# 🧠 Other Principles

---

## ✅ DRY - Don't Repeat Yourself

**Avoid duplicating logic.**

🔴 **Bad:**

```ts
function getUserName(user: User) {
  return user.firstName + " " + user.lastName;
}

function getAuthorName(author: Author) {
  return author.firstName + " " + author.lastName;
}
```

✅ **Good:**

```ts
function getFullName(entity: { firstName: string; lastName: string }) {
  return `${entity.firstName} ${entity.lastName}`;
}
```

---

## ✅ KISS - Keep It Simple, Stupid

**Write code that’s simple and obvious.**

🔴 **Bad:**

```ts
function isEven(n: number) {
  return !!((n * 123456789) % 2 === 0); // Why?
}
```

✅ **Good:**

```ts
function isEven(n: number) {
  return n % 2 === 0;
}
```

---

## ✅ YAGNI - You Ain’t Gonna Need It

**Don’t implement something until it’s necessary.**

🔴 **Bad:**

```ts
class Analytics {
  enableTracking() {
    // Future: social media tracking?
  }
}
```

✅ **Good:**

```ts
class Analytics {
  // Add features only when needed
}
```

---

# 📁 Want a Downloadable Project?

Would you like a GitHub project that includes:

- All **SOLID principle** examples
- DRY/KISS/YAGNI examples
- React + Node backend folder structure
- TypeScript everywhere
- README for learning

Let me know and I’ll generate the ZIP or GitHub repo for you.
