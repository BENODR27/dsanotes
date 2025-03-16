## **🔥 Using Axios in a Professional Node.js + Express.js Application ** 🚀

[Axios](https://axios-http.com/) is a **promise-based HTTP client** for making API requests in **Node.js and the browser**. It simplifies **sending HTTP requests**, **handling responses**, and **managing errors**.

    ---

# **📌 Install Axios **
```sh
npm install axios
```

    ```js
const axios = require("axios");
```

---

# **🚀 Professional Use Cases in Express.js **

## ** 1️⃣ Making External API Calls(`GET`, `POST`, `PUT`, `DELETE`) **
### ** Example: Fetching Data from an External API **
```js
const express = require("express");
const axios = require("axios");

const app = express();

app.get("/users", async (req, res) => {
  try {
    const response = await axios.get("https://jsonplaceholder.typicode.com/users");
    res.json(response.data);
  } catch (error) {
    console.error("Error fetching users:", error.message);
    res.status(500).json({ error: "Failed to fetch users" });
  }
});

app.listen(3000, () => console.log("Server running on port 3000"));
```
✅ ** Best For **: Fetching user details, product lists, or third - party API data.

---

## ** 2️⃣ Sending Data with a`POST` Request **
### ** Example: Creating a New Post **
    ```js
app.post("/create-post", async (req, res) => {
  try {
    const newPost = {
      title: "Hello World",
      body: "This is a test post",
      userId: 1,
    };

    const response = await axios.post("https://jsonplaceholder.typicode.com/posts", newPost);
    res.json(response.data);
  } catch (error) {
    console.error("Error creating post:", error.message);
    res.status(500).json({ error: "Failed to create post" });
  }
});
```
✅ ** Best For **: Sending form data, registering users, submitting payments.

---

## ** 3️⃣ Updating Data with a`PUT` Request **
### ** Example: Updating a User **
    ```js
app.put("/update-user/:id", async (req, res) => {
  try {
    const userId = req.params.id;
    const updatedUser = { name: "John Updated", email: "updated@example.com" };

    const response = await axios.put(`https://jsonplaceholder.typicode.com/users/${userId}`, updatedUser);
res.json(response.data);
  } catch (error) {
    console.error("Error updating user:", error.message);
    res.status(500).json({ error: "Failed to update user" });
}
});
```
✅ **Best For**: Updating user profiles, modifying order details, editing posts.

---

## **4️⃣ Deleting Data with a `DELETE` Request**
### **Example: Deleting a User**
```js
app.delete("/delete-user/:id", async (req, res) => {
    try {
        const userId = req.params.id;
        await axios.delete(`https://jsonplaceholder.typicode.com/users/${userId}`);
        res.json({ message: "User deleted successfully" });
    } catch (error) {
        console.error("Error deleting user:", error.message);
        res.status(500).json({ error: "Failed to delete user" });
    }
});
```
✅ **Best For**: Deleting users, canceling orders, removing comments.

---

## **5️⃣ Using Query Parameters & Headers**
### **Example: Fetch Users with Query Params**
```js
app.get("/search", async (req, res) => {
    try {
        const { name } = req.query;
        const response = await axios.get("https://jsonplaceholder.typicode.com/users", {
            params: { name },
            headers: { Authorization: "Bearer your_token_here" },
        });

        res.json(response.data);
    } catch (error) {
        console.error("Error searching users:", error.message);
        res.status(500).json({ error: "Search failed" });
    }
});
```
✅ **Best For**: Filtering users, searching products, paginating results.

---

## **6️⃣ Handling Errors & Retries**
### **Example: Implementing a Retry Mechanism**
```js
const axiosInstance = axios.create({
    baseURL: "https://jsonplaceholder.typicode.com",
    timeout: 5000, // 5 seconds timeout
    headers: { "Content-Type": "application/json" },
});

app.get("/retry-example", async (req, res) => {
    try {
        const response = await axiosInstance.get("/posts");
        res.json(response.data);
    } catch (error) {
        console.error("API request failed, retrying...", error.message);
        res.status(500).json({ error: "Request failed" });
    }
});
```
✅ **Best For**: Retrying failed API requests, handling timeouts.

---

## **7️⃣ Global Axios Configuration**
### **Example: Creating an Axios Instance**
```js
const axiosInstance = axios.create({
    baseURL: "https://jsonplaceholder.typicode.com",
    timeout: 5000, // 5 seconds timeout
    headers: { "Content-Type": "application/json" },
});

// Usage in Express route
app.get("/custom-instance", async (req, res) => {
    try {
        const response = await axiosInstance.get("/users");
        res.json(response.data);
    } catch (error) {
        console.error("Error:", error.message);
        res.status(500).json({ error: "Request failed" });
    }
});
```
✅ **Best For**: Setting API base URL, adding global headers, enforcing timeouts.

---

## **8️⃣ Axios Interceptors for Logging & Authentication**
### **Example: Logging Requests & Adding Auth Tokens**
```js
axios.interceptors.request.use(
    (config) => {
        console.log(`Request: ${config.method.toUpperCase()} ${config.url}`);
        config.headers.Authorization = `Bearer your_token_here`;
        return config;
    },
    (error) => Promise.reject(error)
);

// Logging Responses
axios.interceptors.response.use(
    (response) => {
        console.log("Response:", response.status);
        return response;
    },
    (error) => {
        console.error("Error Response:", error.message);
        return Promise.reject(error);
    }
);

// Example usage
app.get("/interceptors", async (req, res) => {
    try {
        const response = await axios.get("https://jsonplaceholder.typicode.com/users");
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ error: "Request failed" });
    }
});
```
✅ **Best For**: **Logging requests**, **attaching authentication tokens**, **handling global errors**.

---

# **🔥 Comparing Axios vs. Native Fetch**
| Feature            | Axios                 | Fetch API |
|-------------------|----------------------|-----------|
| **Promise-Based** | ✅ Yes                | ✅ Yes |
| **Automatic JSON Parsing** | ✅ Yes | ❌ No (must call `.json()`) |
| **Request/Response Interceptors** | ✅ Yes | ❌ No |
| **Timeout Handling** | ✅ Yes (`timeout` option) | ❌ No (requires `AbortController`) |
| **Global Defaults** | ✅ Yes (`axios.create()`) | ❌ No |
| **Automatic Headers** | ✅ Yes | ❌ No |
| **Error Handling** | ✅ Better (`catch (error.response)`) | ❌ Requires manual handling |

---

# **📌 Summary**
Axios **simplifies API calls** in a **professional Node.js + Express.js project**.

✅ **Easy API requests** (`GET`, `POST`, `PUT`, `DELETE`)  
✅ **Handles query parameters & headers**  
✅ **Global configuration & Axios instances**  
✅ **Interceptors for authentication & logging**  
✅ **Error handling & retry mechanisms**  

Would you like a **full eCommerce API project** using **Axios with testing and CI/CD**? 🚀