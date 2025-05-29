Here’s a **complete guide to set up Swagger (OpenAPI)** in a **Node.js application** using **Express**:

---

## ✅ Goal

Enable interactive API documentation using **Swagger UI** in your Node.js Express app.

---

## 🔧 Step-by-Step Setup

### **Step 1: Install Required Packages**

Run this in your project root:

```bash
npm install swagger-ui-express swagger-jsdoc
```

---

### **Step 2: Create Swagger Configuration**

In the root of your project (or `config/`), create a file named `swagger.js`:

```js
// swagger.js
const swaggerJSDoc = require("swagger-jsdoc");

const swaggerDefinition = {
  openapi: "3.0.0",
  info: {
    title: "Banking App API",
    version: "1.0.0",
    description: "RESTful API for International Money Transfers",
  },
  servers: [
    {
      url: "http://localhost:3000",
      description: "Development server",
    },
  ],
};

const options = {
  swaggerDefinition,
  apis: ["./routes/*.js"], // Path to the API docs (adjust based on your structure)
};

const swaggerSpec = swaggerJSDoc(options);
module.exports = swaggerSpec;
```

---

### **Step 3: Integrate with Express**

In your `app.js` or `server.js`:

```js
const express = require("express");
const swaggerUi = require("swagger-ui-express");
const swaggerSpec = require("./swagger");

const app = express();
app.use(express.json());

// Swagger UI route
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerSpec));

// Example route
app.get("/api/hello", (req, res) => {
  res.send({ message: "Hello World" });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
```

---

### **Step 4: Document Your Endpoints**

In your `routes/*.js` files or directly in `app.js`, use JSDoc comments:

```js
/**
 * @swagger
 * /api/hello:
 *   get:
 *     summary: Say Hello
 *     description: Returns a simple hello world message.
 *     responses:
 *       200:
 *         description: Successful response
 */
app.get("/api/hello", (req, res) => {
  res.send({ message: "Hello World" });
});
```

---

### **Step 5: Run Your App**

```bash
node app.js
```

Open browser at:

```
http://localhost:3000/api-docs
```

🎉 You now have interactive Swagger documentation!

---

## 🛡️ Optional: Add JWT Auth to Swagger

Modify `swaggerDefinition` in `swagger.js`:

```js
components: {
  securitySchemes: {
    bearerAuth: {
      type: 'http',
      scheme: 'bearer',
      bearerFormat: 'JWT',
    }
  }
},
security: [{
  bearerAuth: []
}]
```

---

