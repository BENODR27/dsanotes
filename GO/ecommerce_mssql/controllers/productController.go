package controllers

import (
    "ecommerce_mssql/config"
    "ecommerce_mssql/models"
    "github.com/gin-gonic/gin"
    "net/http"
)

func GetProducts(c *gin.Context) {
    var products []models.Product
    config.DB.Find(&products)
    c.JSON(http.StatusOK, products)
}

func CreateProduct(c *gin.Context) {
    var product models.Product
    if err := c.ShouldBindJSON(&product); err != nil {
        c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }
    config.DB.Create(&product)
    c.JSON(http.StatusOK, product)
}