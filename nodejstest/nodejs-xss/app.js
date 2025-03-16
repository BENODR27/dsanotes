const express = require('express');
const bodyParser = require('body-parser');
const sanitizeHtml = require('sanitize-html');
const db = require('./database');

const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// 📝 Post a comment (SECURE: Sanitized input)
app.post('/comment', (req, res) => {
    const comment = sanitizeHtml(req.body.comment); // ✅ Removes harmful scripts

    db.run(`INSERT INTO comments (text) VALUES (?)`, [comment], (err) => {
        if (err) {
            return res.status(500).send("❌ Server error");
        }
        res.send("✅ Comment added! <a href='/comments'>View Comments</a>");
    });
});

// 🔍 Get all comments (SECURE: Displays sanitized output)
app.get('/comments', (req, res) => {
    db.all(`SELECT * FROM comments`, [], (err, rows) => {
        if (err) {
            return res.status(500).send("❌ Server error");
        }

        let page = '<h1>📜 Comments</h1>';
        rows.forEach(row => {
            page += `<p>${row.text}</p>`;  // ✅ Safe display
        });

        res.send(page);
    });
});

// Start server
app.listen(3000, () => {
    console.log('🚀 Server running at http://localhost:3000');
});
