package config

import (
    "fmt"
    "gorm.io/driver/sqlserver"
    "gorm.io/gorm"
    "os"
)

var DB *gorm.DB

func Connect() {
    dsn := fmt.Sprintf("sqlserver://%s:%s@%s?database=%s",
        os.Getenv("DB_USER"),
        os.Getenv("DB_PASSWORD"),
        os.Getenv("DB_HOST"),
        os.Getenv("DB_NAME"),
    )

    db, err := gorm.Open(sqlserver.Open(dsn), &gorm.Config{})
    if err != nil {
        panic("Failed to connect to database")
    }

    DB = db
}