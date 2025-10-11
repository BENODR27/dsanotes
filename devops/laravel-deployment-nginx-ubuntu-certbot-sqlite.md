Absolutely! I’ll give you a **step-by-step guide** to deploy a **Laravel app on an Ubuntu server using Nginx, Python Certbot for HTTPS, and SQLite** from scratch. I’ll assume you’re starting with a fresh Ubuntu server (22.04 LTS or similar).

---

## **Step 0: Connect to your server**

```bash
ssh user@your_server_ip
```

---

## **Step 1: Update the system**

```bash
sudo apt update && sudo apt upgrade -y
```

---

## **Step 2: Install required packages**

Install PHP, Nginx, and other dependencies:

```bash
sudo apt install nginx php-fpm php-cli php-mbstring php-bcmath php-sqlite3 php-xml composer unzip git curl -y
```

**Explanation**:

- `php-fpm` → PHP processor for Nginx
- `php-mbstring` → Required for Laravel string handling
- `php-bcmath` → Math operations in Laravel
- `php-sqlite3` → SQLite support

---

## **Step 3: Install Python and Certbot**

```bash
sudo apt install python3-certbot-nginx -y
```

**Explanation:** Certbot will handle SSL certificates for Nginx.

---

## **Step 4: Clone your Laravel project**

Move to your web directory:

```bash
cd /var/www/
sudo git clone <YOUR_LARAVEL_REPO_URL> myapp
cd myapp
```

---

## **Step 5: Set up Laravel**

1. **Install PHP dependencies**:

```bash
composer install --optimize-autoloader --no-dev
```

2. **Set permissions**:

```bash
sudo chown -R www-data:www-data /var/www/myapp
sudo chmod -R 775 /var/www/myapp/storage
sudo chmod -R 775 /var/www/myapp/bootstrap/cache
```

3. **Set environment variables**:

```bash
cp .env.example .env
php artisan key:generate
```

**Edit `.env` for SQLite:**

```env
DB_CONNECTION=sqlite
DB_DATABASE=/var/www/myapp/database/database.sqlite
```

4. **Create SQLite file**:

```bash
touch database/database.sqlite
```

---

## **Step 6: Configure Nginx**

Create a new Nginx site configuration:

```bash
sudo nano /etc/nginx/sites-available/myapp
```

Paste:

```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;

    root /var/www/myapp/public;
    index index.php index.html index.htm;

    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }

    location ~ \.php$ {
        include snippets/fastcgi-php.conf;
        fastcgi_pass unix:/var/run/php/php8.2-fpm.sock;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include fastcgi_params;
    }

    location ~ /\.ht {
        deny all;
    }
}
```

Enable the site:

```bash
sudo ln -s /etc/nginx/sites-available/myapp /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

---

## **Step 7: Setup SSL with Certbot**

```bash
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com
```

Follow the interactive prompts to generate SSL. Certbot will automatically configure HTTPS in Nginx.

Test SSL:

```bash
sudo systemctl reload nginx
```

---

## **Step 8: Laravel optimization**

```bash
php artisan config:cache
php artisan route:cache
php artisan view:cache
```

---

## **Step 9: Configure Supervisor (optional, if using queues)**

If you plan to use Laravel queues:

```bash
sudo apt install supervisor -y
sudo nano /etc/supervisor/conf.d/laravel-worker.conf
```

Paste:

```ini
[program:laravel-worker]
process_name=%(program_name)s_%(process_num)02d
command=php /var/www/myapp/artisan queue:work --sleep=3 --tries=3
autostart=true
autorestart=true
user=www-data
numprocs=1
redirect_stderr=true
stdout_logfile=/var/www/myapp/storage/logs/worker.log
```

Enable:

```bash
sudo supervisorctl reread
sudo supervisorctl update
sudo supervisorctl start laravel-worker:*
```

---

## ✅ **Step 10: Test your app**

Open your browser:

```
https://yourdomain.com
```

If everything is configured correctly, your Laravel app should load with SSL enabled, using SQLite as the database.

---

### **Optional Optimizations**

- Set up **UFW firewall**:

```bash
sudo ufw allow 'Nginx Full'
sudo ufw enable
```

- Enable **auto-renewal for Certbot** (it’s automatic by default):

```bash
sudo certbot renew --dry-run
```

---

