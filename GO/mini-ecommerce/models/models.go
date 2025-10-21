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