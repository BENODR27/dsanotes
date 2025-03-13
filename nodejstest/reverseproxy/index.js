const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Reverse proxy routes
app.use('/app1', createProxyMiddleware({ target: 'http://localhost:4001', changeOrigin: true }));
app.use('/app2', createProxyMiddleware({ target: 'http://localhost:4002', changeOrigin: true }));

app.listen(3008, () => console.log('(Proxy) running on port 3008'));
