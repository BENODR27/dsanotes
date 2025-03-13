// Example for app1.js, repeat similarly for app2.js and app3.js
const express = require('express');
const app = express();
const port = 4002;

app.get('/', (req, res) => {
    res.send(`Hello from App2 on port ${port}!`);
});

app.listen(port, () => {
    console.log(`App listening at http://localhost:${port}`);
});
