Absolutely! I can create a professional, scalable mini e-commerce backend in **Go** using an ORM, **SQLite for development**, and **MySQL for production**. We'll use **GORM**, the most popular ORM in Go, which supports both SQLite and MySQL seamlessly. I’ll include a **RESTful API**, basic **user, product, and order models**, and environment-based database configuration.

Here’s a full example of a professional structure:

---

### **Project Structure**

```
mini-ecommerce/
├── main.go
├── go.mod
├── config/
│   └── config.go
├── models/
│   └── models.go
├── routes/
│   └── routes.go
├── controllers/
│   └── controllers.go
├── database/
│   └── database.go
└── .env
```

---

### **1. go.mod**

```go
module mini-ecommerce

go 1.21

require (
    github.com/gin-gonic/gin v1.9.3
    gorm.io/driver/mysql v1.4.7
    gorm.io/driver/sqlite v1.5.1
    gorm.io/gorm v1.26.3
    github.com/joho/godotenv v1.7.1
)
```

---

### **2. .env**

```env
APP_ENV=development
DB_DSN_DEV=sqlite.db
DB_USER=root
DB_PASSWORD=yourpassword
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=mini_ecommerce
```

---

### **3. config/config.go**

```go
package config

import (
    "os"
)

type Config struct {
    AppEnv      string
    DBDSNDev    string
    DBUser      string
    DBPassword  string
    DBHost      string
    DBPort      string
    DBName      string
}

func LoadConfig() Config {
    return Config{
        AppEnv:     os.Getenv("APP_ENV"),
        DBDSNDev:   os.Getenv("DB_DSN_DEV"),
        DBUser:     os.Getenv("DB_USER"),
        DBPassword: os.Getenv("DB_PASSWORD"),
        DBHost:     os.Getenv("DB_HOST"),
        DBPort:     os.Getenv("DB_PORT"),
        DBName:     os.Getenv("DB_NAME"),
    }
}
```

---

### **4. database/database.go**

```go
package database

import (
    "fmt"
    "log"
    "mini-ecommerce/config"
    "mini-ecommerce/models"
    "gorm.io/driver/mysql"
    "gorm.io/driver/sqlite"
    "gorm.io/gorm"
)

var DB *gorm.DB

func ConnectDB(cfg config.Config) {
    var err error

    if cfg.AppEnv == "production" {
        dsn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True&loc=Local",
            cfg.DBUser, cfg.DBPassword, cfg.DBHost, cfg.DBPort, cfg.DBName)
        DB, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
    } else {
        DB, err = gorm.Open(sqlite.Open(cfg.DBDSNDev), &gorm.Config{})
    }

    if err != nil {
        log.Fatalf("Failed to connect database: %v", err)
    }

    // Auto migrate models
    DB.AutoMigrate(&models.User{}, &models.Product{}, &models.Order{})
}
```

---

### **5. models/models.go**

```go
package models

import "gorm.io/gorm"

// User model
type User struct {
    gorm.Model
    Name     string `json:"name"`
    Email    string `json:"email" gorm:"unique"`
    Password string `json:"password"`
    Orders   []Order
}

// Product model
type Product struct {
    gorm.Model
    Name        string  `json:"name"`
    Description string  `json:"description"`
    Price       float64 `json:"price"`
    Stock       int     `json:"stock"`
}

// Order model
type Order struct {
    gorm.Model
    UserID    uint      `json:"user_id"`
    ProductID uint      `json:"product_id"`
    Quantity  int       `json:"quantity"`
    Status    string    `json:"status"`
}
```

---

### **6. controllers/controllers.go**

```go
package controllers

import (
    "mini-ecommerce/database"
    "mini-ecommerce/models"
    "net/http"
    "strconv"

    "github.com/gin-gonic/gin"
)

// CreateProduct adds a new product
func CreateProduct(c *gin.Context) {
    var product models.Product
    if err := c.ShouldBindJSON(&product); err != nil {
        c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    database.DB.Create(&product)
    c.JSON(http.StatusOK, product)
}

// ListProducts lists all products
func ListProducts(c *gin.Context) {
    var products []models.Product
    database.DB.Find(&products)
    c.JSON(http.StatusOK, products)
}

// CreateOrder adds a new order
func CreateOrder(c *gin.Context) {
    var order models.Order
    if err := c.ShouldBindJSON(&order); err != nil {
        c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    database.DB.Create(&order)
    c.JSON(http.StatusOK, order)
}

// ListOrders lists all orders
func ListOrders(c *gin.Context) {
    var orders []models.Order
    database.DB.Preload("User").Preload("Product").Find(&orders)
    c.JSON(http.StatusOK, orders)
}

// GetOrder retrieves a single order
func GetOrder(c *gin.Context) {
    id := c.Param("id")
    var order models.Order
    if err := database.DB.Preload("User").Preload("Product").First(&order, id).Error; err != nil {
        c.JSON(http.StatusNotFound, gin.H{"error": "Order not found"})
        return
    }
    c.JSON(http.StatusOK, order)
}
```

---

### **7. routes/routes.go**

```go
package routes

import (
    "mini-ecommerce/controllers"

    "github.com/gin-gonic/gin"
)

func SetupRoutes(r *gin.Engine) {
    r.GET("/products", controllers.ListProducts)
    r.POST("/products", controllers.CreateProduct)

    r.GET("/orders", controllers.ListOrders)
    r.GET("/orders/:id", controllers.GetOrder)
    r.POST("/orders", controllers.CreateOrder)
}
```

---

### **8. main.go**

```go
package main

import (
    "mini-ecommerce/config"
    "mini-ecommerce/database"
    "mini-ecommerce/routes"
    "log"
    "os"

    "github.com/gin-gonic/gin"
    "github.com/joho/godotenv"
)

func main() {
    // Load .env
    if err := godotenv.Load(); err != nil {
        log.Println("No .env file found, using system environment variables")
    }

    cfg := config.LoadConfig()
    database.ConnectDB(cfg)

    r := gin.Default()
    routes.SetupRoutes(r)

    port := os.Getenv("PORT")
    if port == "" {
        port = "8080"
    }

    r.Run(":" + port)
}
```

---

✅ **Features included:**

* Environment-based DB config (SQLite for dev, MySQL for prod)
* GORM ORM with AutoMigrate
* REST API for Products & Orders
* Scalable folder structure
* Easy to extend with authentication, categories, or payment later

---

Here’s a **clear step-by-step guide** to run your mini e-commerce backend in **development** (SQLite) and **production** (MySQL) modes.

---

## **1. Prerequisites**

* Install Go (≥ 1.21)
* Install MySQL for production (optional for dev)
* Install `git` if cloning
* Install Gin & GORM (already in `go.mod`)

---

## **2. Setup .env**

We already have `.env`:

```env
APP_ENV=development
DB_DSN_DEV=sqlite.db
DB_USER=root
DB_PASSWORD=yourpassword
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=mini_ecommerce
```

* **For dev:** `APP_ENV=development` → uses SQLite (`sqlite.db`)
* **For prod:** `APP_ENV=production` → uses MySQL (`mini_ecommerce` database)

---

## **3. Initialize Go Module**

```bash
go mod tidy
```

This installs all dependencies (`gin`, `gorm`, etc.).

---

## **4. Run Development (SQLite)**

1. Make sure `.env` has `APP_ENV=development`
2. Run:

```bash
go run main.go
```

* The server will run on `http://localhost:8080`
* SQLite file `sqlite.db` will be created automatically in your project folder
* Test APIs:

```bash
curl http://localhost:8080/products
```

---

## **5. Run Production (MySQL)**

1. Create MySQL database:

```sql
CREATE DATABASE mini_ecommerce;
```

2. Update `.env`:

```env
APP_ENV=production
DB_USER=root
DB_PASSWORD=yourpassword
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=mini_ecommerce
```

3. Run server:

```bash
go run main.go
```

* GORM will connect to MySQL and automatically migrate the tables
* Server runs on `http://localhost:8080`

---

## **6. Optional: Build Executable**

```bash
# For dev
go build -o mini-ecommerce-dev main.go
./mini-ecommerce-dev

# For prod
go build -o mini-ecommerce-prod main.go
./mini-ecommerce-prod
```

---

### **7. Switching Environments**

* Just change `APP_ENV` in `.env`:

| Environment | APP_ENV     | DB Used                  |
| ----------- | ----------- | ------------------------ |
| Development | development | SQLite (`sqlite.db`)     |
| Production  | production  | MySQL (`mini_ecommerce`) |

---

