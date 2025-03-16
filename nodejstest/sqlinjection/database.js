const sqlite3 = require('sqlite3').verbose();

// Connect to SQLite database (or create it if it doesn't exist)
const db = new sqlite3.Database('./ecommerce.db', (err) => {
    if (err) {
        console.error('Error connecting to SQLite:', err);
    } else {
        console.log('Connected to SQLite database');
    }
});

// Create products table
db.serialize(() => {
    db.run(`CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price REAL
  )`);

    // Insert sample products
    db.run(`INSERT INTO products (name, description, price) VALUES 
    ('Product 1', 'Description for product 1', 10.99),
    ('Product 2', 'Description for product 2', 15.99),
    ('Product 3', 'Description for product 3', 7.99)`);
});

module.exports = db;
