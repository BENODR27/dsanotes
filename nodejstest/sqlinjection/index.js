const express = require('express');
const db = require('./database');

const app = express();

// Route: Get product by ID (❌ VULNERABLE TO SQL INJECTION)
app.get('/product/vulnerable/:id', (req, res) => {
    const productId = req.params.id;
    const query = `SELECT * FROM products WHERE id = ${productId};`; // ❌ Bad practice!

    db.all(query, (err, result) => {
        if (err) {
            return res.status(500).send("Server error");
        }
        if (result.length === 0) {
            return res.status(404).send("Product not found");
        }
        res.json(result);
    });
});

// Route: Get product by ID (✅ SECURE WITH PARAMETERIZED QUERY)
app.get('/product/secure/:id', (req, res) => {
    const productId = req.params.id;
    const query = `SELECT * FROM products WHERE id = ?`; // ✅ Safe query

    db.all(query, [productId], (err, result) => {
        if (err) {
            return res.status(500).send("Server error");
        }
        if (result.length === 0) {
            return res.status(404).send("Product not found");
        }
        res.json(result);
    });
});

// Start the server
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
