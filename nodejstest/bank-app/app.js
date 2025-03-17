const express = require("express");
const sqlite3 = require("sqlite3").verbose();
const bodyParser = require("body-parser");

const app = express();
const db = new sqlite3.Database("./banking.db");

// Middleware for parsing JSON requests
app.use(bodyParser.json());
app.use(express.static("public")); // Serve HTML & client-side JS

// Create accounts table if not exists
db.run(`
  CREATE TABLE IF NOT EXISTS accounts (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    balance INTEGER NOT NULL
  )
`);

// Create the debit_history table
db.run(`
  CREATE TABLE IF NOT EXISTS debit_history (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
  )
`);

// Route to create a new account
app.post("/create-account", (req, res) => {
    const { name, initial_balance } = req.body;

    if (!name || !initial_balance || initial_balance <= 0) {
        return res.status(400).send("⚠️ Invalid account creation request");
    }

    db.run("INSERT INTO accounts (name, balance) VALUES (?, ?)", [name, initial_balance], function (err) {
        if (err) {
            return res.status(500).send("❌ Server error");
        }

        res.json({
            message: "✅ Account created successfully",
            account_id: this.lastID,
            initial_balance
        });
    });
});

// Route to view balance for a specific account
app.get("/balance/:account_id", (req, res) => {
    const accountId = req.params.account_id;

    db.get("SELECT * FROM accounts WHERE account_id = ?", [accountId], (err, row) => {
        if (err) {
            return res.status(500).send("❌ Server error");
        }

        if (!row) {
            return res.status(404).send("⚠️ Account not found");
        }

        res.json({
            account_id: row.account_id,
            name: row.name,
            balance: row.balance
        });
    });
});

// Route to debit from an account and store transaction in debit_history
app.post("/debit", (req, res) => {
    const { account_id, amount } = req.body;

    if (!account_id || !amount || amount <= 0) {
        return res.status(400).send("⚠️ Invalid transaction request");
    }

    // Retrieve current balance
    db.get("SELECT * FROM accounts WHERE account_id = ?", [account_id], (err, row) => {
        if (err) {
            return res.status(500).send("❌ Server error");
        }

        if (!row) {
            return res.status(404).send("⚠️ Account not found");
        }

        // Check if balance is sufficient
        if (row.balance < amount) {
            return res.status(400).send("⚠️ Insufficient balance");
        }

        // Deduct the amount from the balance
        const newBalance = row.balance - amount;

        // Update the account balance
        db.run("UPDATE accounts SET balance = ? WHERE account_id = ?", [newBalance, account_id], (err) => {
            if (err) {
                return res.status(500).send("❌ Server error");
            }

            // Store the debit transaction in the debit_history table
            db.run("INSERT INTO debit_history (account_id, amount) VALUES (?, ?)", [account_id, amount], (err) => {
                if (err) {
                    return res.status(500).send("❌ Server error");
                }

                // Respond with success message and new balance
                res.json({
                    message: "✅ Debit transaction successful",
                    new_balance: newBalance
                });
            });
        });
    });
});

// Route to get debit history of a specific account
app.get("/debit-history/:account_id", (req, res) => {
    const accountId = req.params.account_id;

    db.all("SELECT * FROM debit_history WHERE account_id = ?", [accountId], (err, rows) => {
        if (err) {
            return res.status(500).send("❌ Server error");
        }

        if (rows.length === 0) {
            return res.status(404).send("⚠️ No debit history found for this account");
        }

        // Prepare response with all debit transactions
        let history = "<h1>Debit History</h1>";
        rows.forEach(row => {
            history += `
                <p>Transaction ID: ${row.transaction_id} | Amount: ${row.amount} | Date: ${row.timestamp}</p>
            `;
        });

        res.send(history);
    });
});

// Start the server
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`✅ Bank API running on http://localhost:${PORT}`);
});
