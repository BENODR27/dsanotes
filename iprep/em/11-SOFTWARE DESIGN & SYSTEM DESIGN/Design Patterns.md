Here’s a **professional-grade guide** to core **Design Patterns** with real-world, TypeScript-based examples. These patterns are widely used in both **frontend (React)** and **backend (Node.js/NestJS)** development.

---

# 🎯 1. Singleton Pattern

**Ensure a class has only one instance and provide a global access point.**

✅ **Example: Config Service**

```ts
class ConfigService {
  private static instance: ConfigService;
  private constructor(private config = { env: "dev" }) {}

  static getInstance(): ConfigService {
    if (!ConfigService.instance) {
      ConfigService.instance = new ConfigService();
    }
    return ConfigService.instance;
  }

  get(key: string): string {
    return this.config[key];
  }
}

const config1 = ConfigService.getInstance();
const config2 = ConfigService.getInstance();
console.log(config1 === config2); // true
```

---

# 🏭 2. Factory Pattern

**Define an interface for creating an object, but let subclasses decide which class to instantiate.**

✅ **Example: Notification Factory**

```ts
interface Notification {
  send(message: string): void;
}

class EmailNotification implements Notification {
  send(msg: string) {
    console.log("Email:", msg);
  }
}

class SMSNotification implements Notification {
  send(msg: string) {
    console.log("SMS:", msg);
  }
}

class NotificationFactory {
  static create(type: "email" | "sms"): Notification {
    if (type === "email") return new EmailNotification();
    return new SMSNotification();
  }
}

const notifier = NotificationFactory.create("email");
notifier.send("Welcome!");
```

---

# 👁 3. Observer Pattern

**An object (subject) maintains a list of dependents (observers) and notifies them of any state changes.**

✅ **Example: React-style State System**

```ts
type Observer = (data: any) => void;

class Subject {
  private observers: Observer[] = [];

  subscribe(observer: Observer) {
    this.observers.push(observer);
  }

  notify(data: any) {
    this.observers.forEach((obs) => obs(data));
  }
}

const subject = new Subject();
subject.subscribe((data) => console.log("Observer A:", data));
subject.subscribe((data) => console.log("Observer B:", data));

subject.notify("State updated!");
```

---

# 🧠 4. Strategy Pattern

**Define a family of algorithms, encapsulate each one, and make them interchangeable.**

✅ **Example: Payment Strategies**

```ts
interface PaymentStrategy {
  pay(amount: number): void;
}

class CreditCardPayment implements PaymentStrategy {
  pay(amount: number) {
    console.log(`Paid ${amount} using Credit Card.`);
  }
}

class PayPalPayment implements PaymentStrategy {
  pay(amount: number) {
    console.log(`Paid ${amount} using PayPal.`);
  }
}

class CheckoutContext {
  constructor(private strategy: PaymentStrategy) {}
  checkout(amount: number) {
    this.strategy.pay(amount);
  }
}

const cart = new CheckoutContext(new PayPalPayment());
cart.checkout(100);
```

---

# 🧩 5. Adapter Pattern

**Allows the interface of an existing class to be used as another interface.**

✅ **Example: Legacy API Adapter**

```ts
class OldLogger {
  log(message: string) {
    console.log("Legacy:", message);
  }
}

interface Logger {
  info(msg: string): void;
}

class LoggerAdapter implements Logger {
  constructor(private legacy: OldLogger) {}
  info(msg: string) {
    this.legacy.log(msg);
  }
}

const logger: Logger = new LoggerAdapter(new OldLogger());
logger.info("This works!");
```

---

# 🧰 6. Decorator Pattern

**Add responsibilities to objects dynamically.**

✅ **Example: Logging Decorator**

```ts
function LogExecution(
  target: any,
  prop: string,
  descriptor: PropertyDescriptor
) {
  const original = descriptor.value;
  descriptor.value = function (...args: any[]) {
    console.log(`Calling ${prop} with`, args);
    return original.apply(this, args);
  };
}

class Calculator {
  @LogExecution
  add(a: number, b: number) {
    return a + b;
  }
}

const calc = new Calculator();
calc.add(2, 3); // Logs before running
```

---

# 📦 Want All in One Project?

Would you like me to generate a **full TypeScript project** with:

- All major design patterns above (in organized folders)
- React and Node examples (frontend/backend)
- README with real-world usage
- GitHub or ZIP download

👉 Say "Yes, create full repo" and I’ll prepare the download for you.
