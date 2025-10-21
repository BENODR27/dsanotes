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