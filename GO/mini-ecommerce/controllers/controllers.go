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