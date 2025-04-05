const express = require('express');
const router = express.Router();

const userRoutes = require("./user.routes");
const productRoutes = require("./product.routes");
const orderRoutes = require("./order.routes");

router.use("users", userRoutes);
router.use("products", productRoutes);
router.use("orders", orderRoutes);
module.exports = router;