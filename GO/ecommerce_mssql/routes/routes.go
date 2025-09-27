package routes

import (
    "ecommerce_mssql/controllers"
    "github.com/gin-gonic/gin"
)

func RegisterRoutes(r *gin.Engine) {
    r.GET("/products", controllers.GetProducts)
    r.POST("/products", controllers.CreateProduct)
}