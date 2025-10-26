Excellent — you want to **build a full professional URL shortener** in Go, the kind of project that showcases senior-level engineering practices.

Let’s do this step-by-step:
We’ll create a **production-ready, modular, testable, containerized Go URL Shortener API** using:

* **Gin** (web framework)
* **GORM** + **PostgreSQL** (persistence)
* **Redis** (caching for fast lookups)
* **Docker + docker-compose** (deployment)
* **Clean architecture structure** (service, repository, handler)
* **Unit tests**
* **Swagger docs**

---

## 🧱 1. Project Structure

```bash
url-shortener/
├── cmd/
│   └── server/
│       └── main.go
├── internal/
│   ├── config/
│   │   └── config.go
│   ├── handler/
│   │   └── url_handler.go
│   ├── repository/
│   │   └── url_repository.go
│   ├── service/
│   │   └── url_service.go
│   └── model/
│       └── url.go
├── pkg/
│   ├── logger/
│   │   └── logger.go
│   └── shortener/
│       └── shortener.go
├── go.mod
├── go.sum
├── Dockerfile
└── docker-compose.yml
```

---

## ⚙️ 2. Initialize Go Module

```bash
go mod init github.com/yourname/url-shortener
go get github.com/gin-gonic/gin gorm.io/gorm gorm.io/driver/postgres github.com/go-redis/redis/v8 github.com/rs/zerolog
```

---

## 🧩 3. Configuration (`internal/config/config.go`)

```go
package config

import (
    "fmt"
    "os"
)

type Config struct {
    DBHost string
    DBPort string
    DBUser string
    DBPass string
    DBName string
    RedisAddr string
    ServerPort string
}

func LoadConfig() *Config {
    return &Config{
        DBHost: getEnv("DB_HOST", "localhost"),
        DBPort: getEnv("DB_PORT", "5432"),
        DBUser: getEnv("DB_USER", "postgres"),
        DBPass: getEnv("DB_PASS", "postgres"),
        DBName: getEnv("DB_NAME", "shortener"),
        RedisAddr: getEnv("REDIS_ADDR", "localhost:6379"),
        ServerPort: getEnv("PORT", "8080"),
    }
}

func (c *Config) DSN() string {
    return fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
        c.DBHost, c.DBPort, c.DBUser, c.DBPass, c.DBName)
}

func getEnv(key, fallback string) string {
    if v := os.Getenv(key); v != "" {
        return v
    }
    return fallback
}
```

---

## 🧩 4. Model (`internal/model/url.go`)

```go
package model

import "time"

type URL struct {
    ID        uint      `gorm:"primaryKey"`
    ShortCode string    `gorm:"uniqueIndex;not null"`
    Original  string    `gorm:"not null"`
    CreatedAt time.Time
}
```

---

## 🧩 5. Repository Layer (`internal/repository/url_repository.go`)

```go
package repository

import (
    "context"
    "errors"
    "github.com/yourname/url-shortener/internal/model"
    "gorm.io/gorm"
)

type URLRepository interface {
    Save(ctx context.Context, url *model.URL) error
    FindByCode(ctx context.Context, code string) (*model.URL, error)
}

type urlRepository struct {
    db *gorm.DB
}

func NewURLRepository(db *gorm.DB) URLRepository {
    return &urlRepository{db: db}
}

func (r *urlRepository) Save(ctx context.Context, url *model.URL) error {
    return r.db.WithContext(ctx).Create(url).Error
}

func (r *urlRepository) FindByCode(ctx context.Context, code string) (*model.URL, error) {
    var url model.URL
    if err := r.db.WithContext(ctx).Where("short_code = ?", code).First(&url).Error; err != nil {
        if errors.Is(err, gorm.ErrRecordNotFound) {
            return nil, nil
        }
        return nil, err
    }
    return &url, nil
}
```

---

## 🧩 6. Service Layer (`internal/service/url_service.go`)

```go
package service

import (
    "context"
    "errors"
    "github.com/yourname/url-shortener/internal/model"
    "github.com/yourname/url-shortener/internal/repository"
    "github.com/yourname/url-shortener/pkg/shortener"
    "github.com/go-redis/redis/v8"
)

type URLService interface {
    Shorten(ctx context.Context, original string) (string, error)
    Resolve(ctx context.Context, code string) (string, error)
}

type urlService struct {
    repo  repository.URLRepository
    cache *redis.Client
}

func NewURLService(repo repository.URLRepository, cache *redis.Client) URLService {
    return &urlService{repo: repo, cache: cache}
}

func (s *urlService) Shorten(ctx context.Context, original string) (string, error) {
    code := shortener.Generate(8)
    url := &model.URL{ShortCode: code, Original: original}
    if err := s.repo.Save(ctx, url); err != nil {
        return "", err
    }
    s.cache.Set(ctx, code, original, 0)
    return code, nil
}

func (s *urlService) Resolve(ctx context.Context, code string) (string, error) {
    if val, err := s.cache.Get(ctx, code).Result(); err == nil {
        return val, nil
    }
    url, err := s.repo.FindByCode(ctx, code)
    if err != nil || url == nil {
        return "", errors.New("not found")
    }
    s.cache.Set(ctx, code, url.Original, 0)
    return url.Original, nil
}
```

---

## 🧩 7. Shortener Utility (`pkg/shortener/shortener.go`)

```go
package shortener

import (
    "math/rand"
    "time"
)

const charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

func Generate(length int) string {
    rand.Seed(time.Now().UnixNano())
    b := make([]byte, length)
    for i := range b {
        b[i] = charset[rand.Intn(len(charset))]
    }
    return string(b)
}
```

---

## 🧩 8. Handler Layer (`internal/handler/url_handler.go`)

```go
package handler

import (
    "context"
    "net/http"
    "github.com/gin-gonic/gin"
    "github.com/yourname/url-shortener/internal/service"
)

type URLHandler struct {
    service service.URLService
}

func NewURLHandler(service service.URLService) *URLHandler {
    return &URLHandler{service: service}
}

func (h *URLHandler) Shorten(c *gin.Context) {
    var req struct{ URL string `json:"url" binding:"required,url"` }
    if err := c.ShouldBindJSON(&req); err != nil {
        c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
        return
    }

    code, err := h.service.Shorten(context.Background(), req.URL)
    if err != nil {
        c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
        return
    }
    c.JSON(http.StatusOK, gin.H{"short_url": c.Request.Host + "/" + code})
}

func (h *URLHandler) Resolve(c *gin.Context) {
    code := c.Param("code")
    original, err := h.service.Resolve(context.Background(), code)
    if err != nil {
        c.JSON(http.StatusNotFound, gin.H{"error": "URL not found"})
        return
    }
    c.Redirect(http.StatusFound, original)
}
```

---

## 🧩 9. Main Entrypoint (`cmd/server/main.go`)

```go
package main

import (
    "context"
    "fmt"
    "github.com/gin-gonic/gin"
    "github.com/go-redis/redis/v8"
    "gorm.io/driver/postgres"
    "gorm.io/gorm"

    "github.com/yourname/url-shortener/internal/config"
    "github.com/yourname/url-shortener/internal/handler"
    "github.com/yourname/url-shortener/internal/model"
    "github.com/yourname/url-shortener/internal/repository"
    "github.com/yourname/url-shortener/internal/service"
)

func main() {
    cfg := config.LoadConfig()

    db, err := gorm.Open(postgres.Open(cfg.DSN()), &gorm.Config{})
    if err != nil {
        panic(err)
    }
    db.AutoMigrate(&model.URL{})

    cache := redis.NewClient(&redis.Options{Addr: cfg.RedisAddr})
    if err := cache.Ping(context.Background()).Err(); err != nil {
        panic(err)
    }

    repo := repository.NewURLRepository(db)
    svc := service.NewURLService(repo, cache)
    h := handler.NewURLHandler(svc)

    r := gin.Default()
    r.POST("/shorten", h.Shorten)
    r.GET("/:code", h.Resolve)

    r.Run(fmt.Sprintf(":%s", cfg.ServerPort))
}
```

---

## 🧩 10. Docker Setup

### `Dockerfile`

```Dockerfile
FROM golang:1.23 AS builder
WORKDIR /app
COPY . .
RUN go mod download && go build -o url-shortener ./cmd/server

FROM gcr.io/distroless/base-debian12
WORKDIR /
COPY --from=builder /app/url-shortener .
EXPOSE 8080
CMD ["./url-shortener"]
```

### `docker-compose.yml`

```yaml
version: "3.9"
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=db
      - DB_USER=postgres
      - DB_PASS=postgres
      - DB_NAME=shortener
      - REDIS_ADDR=redis:6379
    depends_on:
      - db
      - redis
  db:
    image: postgres:15
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: shortener
    ports:
      - "5432:5432"
  redis:
    image: redis:7
    ports:
      - "6379:6379"
```

Run everything:

```bash
docker-compose up --build
```

---

## ✅ Test API

**Shorten URL**

```bash
curl -X POST http://localhost:8080/shorten \
  -H "Content-Type: application/json" \
  -d '{"url": "https://google.com"}'
```

➡️ Response:

```json
{"short_url":"localhost:8080/aB3kLxZ1"}
```

**Redirect**

```
http://localhost:8080/aB3kLxZ1
```

➡️ Redirects to [https://google.com](https://google.com)

---

## 🧠 Bonus Ideas (for senior polish)

* Add JWT authentication
* Add Swagger (with `swaggo/gin-swagger`)
* Add Prometheus metrics (`/metrics` endpoint)
* Add rate limiting (middleware)
* Deploy to K8s

---

Would you like me to generate this entire app as a **ready-to-run GitHub project zip** (with Docker setup, tests, and Swagger preconfigured)?
That way, you can just unzip and run `docker-compose up`.




Perfect — you’re thinking like a **senior engineer** 👏

Let’s add proper **environment-based configuration** so your app behaves differently in **development**, **staging**, and **production** — including logging, database, and caching settings.

---

## 🧭 Goal

We’ll make your Go URL shortener automatically detect the environment (`development`, `staging`, `production`) using an environment variable like:

```bash
APP_ENV=development
```

Then we’ll:

* Use **different configs** per environment
* Enable **verbose logging & debugging** only in dev
* Allow local DB/Redis for dev, and cloud configs for prod
* Run cleanly in Docker

---

## 🧩 1. Update Configuration (`internal/config/config.go`)

```go
package config

import (
    "fmt"
    "os"
)

type Config struct {
    Env        string
    DBHost     string
    DBPort     string
    DBUser     string
    DBPass     string
    DBName     string
    RedisAddr  string
    ServerPort string
    Debug      bool
}

func LoadConfig() *Config {
    env := getEnv("APP_ENV", "development")

    cfg := &Config{
        Env:        env,
        DBHost:     getEnv("DB_HOST", "localhost"),
        DBPort:     getEnv("DB_PORT", "5432"),
        DBUser:     getEnv("DB_USER", "postgres"),
        DBPass:     getEnv("DB_PASS", "postgres"),
        DBName:     getEnv("DB_NAME", "shortener"),
        RedisAddr:  getEnv("REDIS_ADDR", "localhost:6379"),
        ServerPort: getEnv("PORT", "8080"),
    }

    // Environment-specific overrides
    switch env {
    case "development":
        cfg.Debug = true
    case "staging", "production":
        cfg.Debug = false
    }

    return cfg
}

func (c *Config) DSN() string {
    return fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
        c.DBHost, c.DBPort, c.DBUser, c.DBPass, c.DBName)
}

func getEnv(key, fallback string) string {
    if v := os.Getenv(key); v != "" {
        return v
    }
    return fallback
}
```

---

## 🧩 2. Enable Conditional Logging (`pkg/logger/logger.go`)

```go
package logger

import (
    "os"
    "github.com/rs/zerolog"
)

func New(debug bool) zerolog.Logger {
    level := zerolog.InfoLevel
    if debug {
        level = zerolog.DebugLevel
    }

    return zerolog.New(os.Stdout).
        Level(level).
        With().
        Timestamp().
        Logger()
}
```

---

## 🧩 3. Use Environment in `main.go`

Modify your `cmd/server/main.go`:

```go
package main

import (
    "context"
    "fmt"
    "github.com/gin-gonic/gin"
    "github.com/go-redis/redis/v8"
    "gorm.io/driver/postgres"
    "gorm.io/gorm"

    "github.com/yourname/url-shortener/internal/config"
    "github.com/yourname/url-shortener/internal/handler"
    "github.com/yourname/url-shortener/internal/model"
    "github.com/yourname/url-shortener/internal/repository"
    "github.com/yourname/url-shortener/internal/service"
    "github.com/yourname/url-shortener/pkg/logger"
)

func main() {
    cfg := config.LoadConfig()
    log := logger.New(cfg.Debug)

    log.Info().Str("env", cfg.Env).Msg("Starting URL Shortener")

    db, err := gorm.Open(postgres.Open(cfg.DSN()), &gorm.Config{})
    if err != nil {
        log.Fatal().Err(err).Msg("Database connection failed")
    }
    db.AutoMigrate(&model.URL{})

    cache := redis.NewClient(&redis.Options{Addr: cfg.RedisAddr})
    if err := cache.Ping(context.Background()).Err(); err != nil {
        log.Fatal().Err(err).Msg("Redis connection failed")
    }

    repo := repository.NewURLRepository(db)
    svc := service.NewURLService(repo, cache)
    h := handler.NewURLHandler(svc)

    if cfg.Env == "production" {
        gin.SetMode(gin.ReleaseMode)
    }

    r := gin.Default()
    r.POST("/shorten", h.Shorten)
    r.GET("/:code", h.Resolve)

    log.Info().Str("port", cfg.ServerPort).Msg("Server running")
    r.Run(fmt.Sprintf(":%s", cfg.ServerPort))
}
```

---

## 🧩 4. Add `.env` File (for local dev)

You can use `direnv` or a `.env` file loader such as [godotenv](https://github.com/joho/godotenv).

Create `.env` file:

```bash
APP_ENV=development
DB_HOST=localhost
DB_USER=postgres
DB_PASS=postgres
DB_NAME=shortener
REDIS_ADDR=localhost:6379
PORT=8080
```

Then, in your `main.go`, add this at the top before loading config (optional):

```go
import "github.com/joho/godotenv"

func init() {
    _ = godotenv.Load() // load .env if exists
}
```

---

## 🐳 5. Docker Compose Update for Environments

Update `docker-compose.yml`:

```yaml
version: "3.9"

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - APP_ENV=development
      - DB_HOST=db
      - DB_USER=postgres
      - DB_PASS=postgres
      - DB_NAME=shortener
      - REDIS_ADDR=redis:6379
    depends_on:
      - db
      - redis

  db:
    image: postgres:15
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: shortener
    ports:
      - "5432:5432"

  redis:
    image: redis:7
    ports:
      - "6379:6379"
```

When deploying to production (e.g. AWS ECS, Kubernetes), simply change:

```yaml
APP_ENV=production
```

---

## 🧠 Summary

| Env         | Debug | Logging | DB               | Redis       |
| ----------- | ----- | ------- | ---------------- | ----------- |
| development | true  | verbose | local            | local       |
| staging     | false | info    | staging instance | remote      |
| production  | false | info    | cloud DB         | cloud cache |

---

Would you like me to extend this setup with **automatic environment-specific configuration files** (e.g. `config.dev.yaml`, `config.prod.yaml`) — which is more enterprise-style and lets ops override without recompiling?
