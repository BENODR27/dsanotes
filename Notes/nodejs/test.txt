I'll set up a **professional-level testing** environment for your **Node.js eCommerce project** with **Jest, Supertest, and CI/CD integration**. The setup will include:

    - ** Unit Tests ** (for utility functions and business logic)
- ** Integration Tests ** (for API endpoints)
- ** Database Testing ** (for MySQL with TypeORM)
- ** Mocking Dependencies **
- ** CI / CD Automation with GitHub Actions **

    ---

# **📁 Project Structure **
    ```
ecommerce-app
│── src/
│   ├── app.js            # Express App
│   ├── server.js         # Server Entry
│   ├── database.js       # MySQL TypeORM Connection
│   ├── routes/
│   │   ├── auth.js       # Auth Routes
│   │   ├── products.js   # Product Routes
│   ├── controllers/
│   │   ├── authController.js
│   │   ├── productController.js
│   ├── models/
│   │   ├── User.js
│   │   ├── Product.js
│   ├── utils/
│   │   ├── hash.js       # Hash Utility Function
│── tests/
│   ├── auth.test.js      # Auth API Tests
│   ├── product.test.js   # Product API Tests
│   ├── utils.test.js     # Unit Tests
│── .github/workflows/test.yml # CI/CD GitHub Actions
│── jest.config.js
│── package.json
│── .gitignore
│── README.md
```

---

## ** 1️⃣ Initialize Node.js Project **
    Run:
```sh
mkdir ecommerce-app && cd ecommerce-app
npm init -y
```

---

## ** 2️⃣ Install Dependencies **
    ```sh
npm install express mysql2 typeorm bcrypt jsonwebtoken dotenv
npm install --save-dev jest supertest nodemon
```

---

## ** 3️⃣ Setup Database(`src/database.js`) **
    ```js
const { DataSource } = require("typeorm");
require("dotenv").config();

const AppDataSource = new DataSource({
  type: "mysql",
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
  username: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_NAME,
  entities: ["src/models/*.js"],
  synchronize: true,
  logging: false,
});

module.exports = AppDataSource;
```

---

## ** 4️⃣ Create Models **
### ** User Model(`src/models/User.js`) **
    ```js
const { EntitySchema } = require("typeorm");

module.exports = new EntitySchema({
  name: "User",
  tableName: "users",
  columns: {
    id: { primary: true, type: "int", generated: true },
    email: { type: "varchar", unique: true },
    password: { type: "varchar" },
  },
});
```

### ** Product Model(`src/models/Product.js`) **
    ```js
const { EntitySchema } = require("typeorm");

module.exports = new EntitySchema({
  name: "Product",
  tableName: "products",
  columns: {
    id: { primary: true, type: "int", generated: true },
    name: { type: "varchar" },
    price: { type: "float" },
  },
});
```

---

## ** 5️⃣ Create Express App(`src/app.js`) **
    ```js
const express = require("express");
const AppDataSource = require("./database");

const app = express();
app.use(express.json());

const authRoutes = require("./routes/auth");
const productRoutes = require("./routes/products");

app.use("/api/auth", authRoutes);
app.use("/api/products", productRoutes);

AppDataSource.initialize().then(() => console.log("Database Connected"));

module.exports = app;
```

---

## ** 6️⃣ Create Controllers **
### ** Auth Controller(`src/controllers/authController.js`) **
    ```js
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const AppDataSource = require("../database");
const User = require("../models/User");

exports.register = async (req, res) => {
  const userRepo = AppDataSource.getRepository(User);
  const { email, password } = req.body;

  const hashedPassword = await bcrypt.hash(password, 10);
  const user = userRepo.create({ email, password: hashedPassword });

  await userRepo.save(user);
  res.status(201).json({ message: "User registered" });
};

exports.login = async (req, res) => {
  const userRepo = AppDataSource.getRepository(User);
  const { email, password } = req.body;

  const user = await userRepo.findOneBy({ email });
  if (!user || !(await bcrypt.compare(password, user.password))) {
    return res.status(401).json({ message: "Invalid credentials" });
  }

  const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: "1h" });
  res.json({ token });
};
```

---

## ** 7️⃣ Create Routes **
### ** Auth Routes(`src/routes/auth.js`) **
    ```js
const express = require("express");
const { register, login } = require("../controllers/authController");

const router = express.Router();

router.post("/register", register);
router.post("/login", login);

module.exports = router;
```

---

## ** 8️⃣ Create Tests **
### ** Unit Test(`tests/utils.test.js`) **
    ```js
const bcrypt = require("bcrypt");

test("hashes a password correctly", async () => {
  const password = "secret";
  const hashed = await bcrypt.hash(password, 10);
  expect(await bcrypt.compare(password, hashed)).toBe(true);
});
```

### ** API Test(`tests/auth.test.js`) **
    ```js
const request = require("supertest");
const app = require("../src/app");

describe("Auth API Tests", () => {
  it("should register a new user", async () => {
    const res = await request(app)
      .post("/api/auth/register")
      .send({ email: "test@test.com", password: "123456" });

    expect(res.status).toBe(201);
    expect(res.body).toHaveProperty("message", "User registered");
  });

  it("should not login with wrong credentials", async () => {
    const res = await request(app)
      .post("/api/auth/login")
      .send({ email: "test@test.com", password: "wrongpass" });

    expect(res.status).toBe(401);
    expect(res.body).toHaveProperty("message", "Invalid credentials");
  });
});
```

---

## ** 9️⃣ Setup Jest(`jest.config.js`) **
    ```js
module.exports = {
  testEnvironment: "node",
  collectCoverage: true,
  coverageDirectory: "coverage",
};
```

---

## **🔟 Modify`package.json` **
    ```json
{
  "scripts": {
    "start": "node src/server.js",
    "test": "jest --coverage",
    "test:watch": "jest --watch"
  }
}
```

---

## ** 1️⃣1️⃣ Run Tests **
    ```sh
npm test
```

---

## ** 1️⃣2️⃣ CI / CD Integration with GitHub Actions **
    Create`.github/workflows/test.yml`:
```yml
name: Node.js CI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    services:
      mysql:
        image: mysql:5.7
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: test_db
        ports:
          - 3306:3306
        options: --health-cmd="mysqladmin ping" --health-interval=10s --health-timeout=5s --health-retries=3
    steps:
      - uses: actions/checkout@v3
      - name: Set up Node.js
        uses: actions/setup-node@v3
        with:
          node-version: 18
      - name: Install dependencies
        run: npm install
      - name: Run tests
        run: npm test
```

---

## **✅ Final Steps **
    1. ** Run tests locally **: `npm test`
2. ** Start server **: `npm start`
3. ** Check API on **: `http://localhost:5000/api/auth/register`
4. ** GitHub Actions will auto - run tests on push **

    ---

    This is a ** professional - level testing setup ** for your ** Node.js eCommerce project ** with ** Jest, Supertest, TypeORM, MySQL, and CI / CD integration **.Let me know if you need more features like ** mocking, Docker setup, or performance testing ** ! 🚀