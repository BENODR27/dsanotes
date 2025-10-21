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