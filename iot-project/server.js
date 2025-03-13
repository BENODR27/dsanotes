const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

// Serve static files from the 'public' directory
app.use(express.static('public'));

io.on('connection', (socket) => {
    console.log('Client connected: ' + socket.id);

    // Listen for sensor data from the client
    socket.on('sensorData', (data) => {
        console.log('Received sensor data:', data);
        // Optionally process and store data in a database
        // Broadcast data to all connected clients (e.g., for dashboard updates)
        io.emit('updateDashboard', data);
    });

    socket.on('disconnect', () => {
        console.log('Client disconnected: ' + socket.id);
    });
});

const PORT = 3010;
server.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
