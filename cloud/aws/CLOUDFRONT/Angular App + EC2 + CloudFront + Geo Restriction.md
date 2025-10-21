Perfect — you want a **complete AWS deployment setup** for an **Angular 19 app** on **Ubuntu EC2**, served via **CloudFront**, with **Geo Restriction to allow only India**. I’ll give you a **step-by-step, production-ready guide**, including **EC2 setup, Angular build, Nginx, S3/CloudFront integration, and Geo Restriction**.

---

# **Full AWS Setup: Angular App + EC2 + CloudFront + Geo Restriction**

---

## **Step 0: Prerequisites**

- AWS Account with admin privileges
- Angular 19 application ready (`ng build`)
- Node.js and npm installed locally for Angular build
- Basic knowledge of AWS CLI / Console

---

# **Step 1: Launch EC2 Instance (Ubuntu)**

1. **EC2 Console → Launch Instance**

   - AMI: Ubuntu 22.04 LTS
   - Instance type: `t3.medium` (or higher)
   - Key pair: Create/download `.pem` file
   - Security Group:

     - Allow SSH (22) from your IP
     - Allow HTTP (80) from anywhere
     - Allow HTTPS (443) from anywhere

2. **Connect via SSH**

```bash
ssh -i "my-key.pem" ubuntu@<EC2_PUBLIC_IP>
```

---

# **Step 2: Install Node.js, Nginx, and PM2 (Optional)**

```bash
# Update
sudo apt update && sudo apt upgrade -y

# Node.js 20.x
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs build-essential

# Verify
node -v
npm -v

# Nginx
sudo apt install -y nginx
sudo systemctl enable nginx
sudo systemctl start nginx

# PM2 (optional, for server-side Node apps)
sudo npm install -g pm2
```

---

# **Step 3: Build Angular App**

On your local machine:

```bash
ng build --configuration production
```

- This generates a `dist/<your-app-name>` folder containing **static files** (`index.html`, JS, CSS).

---

# **Step 4: Deploy Angular to EC2**

1. **Upload files to EC2** (using `scp` or `rsync`):

```bash
scp -i "my-key.pem" -r dist/<app-name>/* ubuntu@<EC2_PUBLIC_IP>:/home/ubuntu/angular-app
```

2. **Move to Nginx root**

```bash
sudo mv /home/ubuntu/angular-app/* /var/www/html/
```

3. **Set permissions**

```bash
sudo chown -R www-data:www-data /var/www/html
```

4. **Configure Nginx**

```bash
sudo nano /etc/nginx/sites-available/default
```

Replace the server block:

```nginx
server {
    listen 80;
    server_name _;

    root /var/www/html;
    index index.html;

    location / {
        try_files $uri /index.html;
    }
}
```

5. **Test and reload Nginx**

```bash
sudo nginx -t
sudo systemctl restart nginx
```

✅ Your Angular app is now accessible via `http://<EC2_PUBLIC_IP>`.

---

# **Step 5: Create CloudFront Distribution**

1. **CloudFront Console → Create Distribution**

   - Origin Domain: `<EC2_PUBLIC_IP>` (or use a public DNS record)
   - Origin Protocol Policy: `HTTP Only` or `HTTPS Only` (if SSL)
   - Viewer Protocol Policy: `Redirect HTTP to HTTPS`

2. **Default Cache Behavior**

   - Allowed HTTP Methods: GET, HEAD (for static Angular app)
   - Cache Policy: CachingOptimized
   - Compress Objects Automatically: Enabled

3. **Distribution Settings**

   - Alternate Domain Names (CNAME): e.g., `www.myapp.com`
   - SSL Certificate: Use ACM (recommended)
   - Default Root Object: `index.html`

---

# **Step 6: Configure Geo Restriction (Allow Only India)**

1. In **CloudFront Distribution → Edit → Restrictions → Geo restriction**
2. **Restriction Type**: `Whitelist`
3. **Countries**: Select **India**
4. Save changes and wait for distribution deployment (~15 min)

✅ Only users from India will access the Angular app.
Others receive **403 Forbidden**.

---

# **Step 7: Update DNS (Optional)**

- Point your domain to CloudFront distribution using **Route 53** or any DNS provider.
- Example:

```
CNAME: www.myapp.com → d1234abcdef.cloudfront.net
```

---

# **Step 8: Verify Setup**

1. Visit your domain / CloudFront URL from **India** → App loads ✅
2. Test from VPN (outside India) → Should return **403 Forbidden** ✅
3. CloudFront metrics available in **CloudWatch** → Monitor hits, errors.

---

# **Step 9: Optional Enhancements**

- **HTTPS**: Use ACM certificate for custom domain.
- **Origin Shield**: Enable to reduce latency & origin load.
- **WAF**: Protect CloudFront with IP or country-based rules.
- **S3 + CloudFront Option**: Host Angular static files in **S3 bucket** + CloudFront (more scalable than EC2 for static sites).

---

# **Architecture Overview**

```
User (India)
   │
   ▼
CloudFront Distribution
   │ Geo Restriction (India only)
   ▼
Origin: EC2 Ubuntu (Nginx) serving Angular app
```

✅ This is a **secure, production-ready setup** for enterprise/global deployment.

---

I can create a **diagram showing the entire flow** including:

- EC2 origin
- CloudFront caching
- Geo restriction whitelist (India)
- Optional Route 53 domain + SSL

This will make it **visually clear for deployment and exam prep**.

Do you want me to create that diagram?
