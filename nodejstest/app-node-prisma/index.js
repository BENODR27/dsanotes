require("dotenv").config();
const express = require("express");
const app = express();
const port = process.env.PORT || 3000;
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

app.use(express.json());

// Routes will be imported later
const userRoutes = require("./routes/user.routes");
const productRoutes = require("./routes/product.routes");
const orderRoutes = require("./routes/order.routes");

app.use("/api/users", userRoutes);
app.use("/api/products", productRoutes);
app.use("/api/orders", orderRoutes);

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
