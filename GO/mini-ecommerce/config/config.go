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