const sqlite3 = require('sqlite3').verbose();

// Connect to SQLite database (creates if it doesn't exist)
const db = new sqlite3.Database('./comments.db', (err) => {
    if (err) {
        console.error('❌ Database connection error:', err);
    } else {
        console.log('✅ Connected to SQLite database');
    }
});

// Create comments table
db.serialize(() => {
    db.run(`CREATE TABLE IF NOT EXISTS comments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    text TEXT NOT NULL
  )`);
});

module.exports = db;
