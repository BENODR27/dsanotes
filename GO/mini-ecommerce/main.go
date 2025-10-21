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