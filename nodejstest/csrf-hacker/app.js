const express = require("express");
const bodyParser = require("body-parser");

const app = express();

// Middleware for parsing JSON requests
app.use(bodyParser.json());
app.use(express.static("public")); // Serve HTML & client-side JS


// Start the server
const PORT = 8085;
app.listen(PORT, () => {
    console.log(`✅ Bank API running on http://localhost:${PORT}`);
});
