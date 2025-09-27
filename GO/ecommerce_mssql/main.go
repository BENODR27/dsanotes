package main

import (
    "ecommerce_mssql/config"
    "ecommerce_mssql/routes"
    "github.com/gin-gonic/gin"
)

func main() {
    config.Connect()
    r := gin.Default()
    routes.RegisterRoutes(r)
    r.Run(":8080")
}