Excellent — let’s go over **Amazon CloudFront** in full professional-level depth, the way you’d need to understand it for **AWS Solutions Architect Professional** or enterprise deployments.

---

# 🌍 **Amazon CloudFront – Content Delivery Network (CDN)**

---

## 🚀 **Overview**

**Amazon CloudFront** is a **global Content Delivery Network (CDN)** service that securely delivers data, videos, applications, and APIs to users worldwide with **low latency** and **high transfer speeds**.

It acts as a **distributed cache layer** sitting in front of your origin (like **S3**, **EC2**, **ALB**, or an external HTTP server\*\*) to improve performance and reduce origin load.

---

## 🧱 **Core Architecture**

| Component          | Description                                                                                                        |
| ------------------ | ------------------------------------------------------------------------------------------------------------------ |
| **Origin**         | The original source of content — S3 bucket, Application Load Balancer, EC2, or external HTTP server.               |
| **Edge Locations** | Global PoPs (Points of Presence) where CloudFront caches content close to end-users.                               |
| **Distribution**   | A configuration that defines how CloudFront delivers content — includes origins, behaviors, cache rules, SSL, etc. |
| **Cache Behavior** | Rules defining how CloudFront caches specific paths, headers, or request patterns.                                 |
| **Viewer Request** | The request from an end user to CloudFront.                                                                        |
| **Origin Request** | The request from CloudFront to your origin (when content is not cached).                                           |

---

## ⚙️ **Step-by-Step: Creating a CloudFront Distribution**

---

### **Step 1: Choose Origin**

You can use:

- **S3 bucket** (for static websites or assets)
- **Application Load Balancer** (for dynamic APIs)
- **EC2 instance** (self-managed origin)
- **Custom HTTP server**

Example:

```
Origin Domain: myapp-bucket.s3.amazonaws.com
```

---

### **Step 2: Configure Cache Behaviors**

| Setting                    | Description                                         | Example                 |
| -------------------------- | --------------------------------------------------- | ----------------------- |
| **Path Pattern**           | Determines which requests this behavior applies to. | `/images/*`             |
| **Viewer Protocol Policy** | Redirect HTTP → HTTPS, or allow both.               | Redirect to HTTPS       |
| **Allowed HTTP Methods**   | GET/HEAD or include POST, PUT, DELETE, etc.         | GET, HEAD, OPTIONS      |
| **Caching**                | Based on TTL and headers.                           | Default TTL = 86400 sec |
| **Compression**            | Enable automatic GZIP/Brotli compression.           | On                      |

---

### **Step 3: Distribution Settings**

| Setting                             | Description                                                                   |
| ----------------------------------- | ----------------------------------------------------------------------------- |
| **Price Class**                     | Limits edge locations for cost control (e.g., “Use only US, Canada, Europe”). |
| **Alternate Domain Names (CNAMEs)** | Use your custom domain (e.g., `cdn.myapp.com`).                               |
| **SSL Certificate**                 | Attach an ACM certificate (must be in `us-east-1`).                           |
| **Default Root Object**             | Optional (e.g., `index.html` for static sites).                               |

---

### **Step 4: Restrict Access to Origin (S3 Example)**

To secure your S3 bucket so it can only be accessed via CloudFront:

#### **Option 1: Origin Access Identity (OAI)**

Legacy method (still widely used).

- Create **OAI**.
- Grant bucket policy access to that OAI only.

**Bucket Policy Example:**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowCloudFrontAccess",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::cloudfront:user/CloudFront Origin Access Identity EXAMPLEID"
      },
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::myapp-bucket/*"
    }
  ]
}
```

#### **Option 2: Origin Access Control (OAC)**

New recommended method — supports **signed requests** and **more security**.

- Create an **Origin Access Control (OAC)**.
- Associate it with the CloudFront distribution.
- Apply the generated **OAC policy** to your S3 bucket.

---

### **Step 5: Deploy Distribution**

- Click **Create Distribution**.
- Wait until the **Status = Deployed** (can take 10–15 minutes).
- You’ll get a **CloudFront domain** like:

```
d12345abcdef8.cloudfront.net
```

---

## 💻 **Connecting CloudFront with a Node.js / Spring Boot App**

You can front your **API endpoints** or **static assets** behind CloudFront.

Example setup:

```
User → CloudFront → ALB → EC2 (Node.js or Spring Boot App)
```

### **Node.js Example**

If your API is at `/api/*`, you can:

- Create a **cache behavior** `/api/*`
- Forward headers, cookies, and query strings
- Set TTL = 0 (so CloudFront doesn’t cache dynamic API responses)

---

## 🧠 **Key CloudFront Features**

| Feature                                | Description                                                             |
| -------------------------------------- | ----------------------------------------------------------------------- |
| **Edge Caching**                       | Reduces latency by caching static content near users.                   |
| **Field-Level Encryption**             | Encrypts sensitive data at the edge before sending to origin.           |
| **Signed URLs / Cookies**              | Restrict access to premium or private content.                          |
| **Geo Restriction**                    | Allow/deny content delivery by country.                                 |
| **Lambda@Edge / CloudFront Functions** | Run custom code at the edge for authentication, rewrites, headers, etc. |
| **Origin Groups**                      | Define failover origins for high availability.                          |
| **Real-Time Metrics**                  | Monitor via CloudWatch metrics or CloudFront reports.                   |

---

## 🔐 **Security Features**

- **HTTPS Support (TLS 1.2/1.3)**
- **AWS WAF Integration** – protect APIs/websites from SQLi, XSS, etc.
- **Origin Access Control (OAC)** – secure your S3 origins.
- **Custom Headers** – authenticate edge requests to origins.
- **IAM + CloudFront API access control**

---

## 📊 **Performance & Optimization**

| Technique              | Description                                                     |
| ---------------------- | --------------------------------------------------------------- |
| **Cache Invalidation** | Remove cached objects manually using invalidation paths (`/*`). |
| **Origin Shield**      | Adds an extra caching layer for multi-regional origins.         |
| **Compression**        | Enable GZIP/Brotli for faster downloads.                        |
| **TTL Tuning**         | Set `DefaultTTL`, `MinTTL`, `MaxTTL` for caching control.       |
| **HTTP/3 Support**     | Modern QUIC protocol for faster delivery.                       |

---

## 🧩 **Node.js Example – Signed URL**

For **private content delivery**, generate signed URLs:

```javascript
import { getSignedUrl } from "@aws-sdk/cloudfront-signer";

const url = getSignedUrl({
  url: "https://d12345abcdef8.cloudfront.net/secure/video.mp4",
  dateLessThan: new Date(Date.now() + 3600 * 1000), // 1 hour expiry
  privateKey: process.env.CLOUDFRONT_PRIVATE_KEY,
  keyPairId: process.env.CLOUDFRONT_KEY_PAIR_ID,
});

console.log("Signed URL:", url);
```

---

## 🧾 **Example Enterprise Use Cases**

| Use Case                    | Example                                                |
| --------------------------- | ------------------------------------------------------ |
| **Static Website Delivery** | S3 (Origin) + CloudFront (CDN) + ACM (HTTPS)           |
| **API Acceleration**        | CloudFront fronting ALB → EC2 / ECS / Lambda           |
| **Media Streaming**         | Video on demand using signed URLs                      |
| **Security Gateway**        | CloudFront + AWS WAF + Shield + OAC                    |
| **Global SaaS App**         | Multi-origin failover + edge caching for static assets |

---

## ✅ **Best Practices**

1. Always use **HTTPS-only** for viewers and origins.
2. Use **OAC (Origin Access Control)** for secure S3 access.
3. Use **short TTLs** for dynamic API content, long TTLs for static assets.
4. Automate **cache invalidations** via CI/CD (e.g., `aws cloudfront create-invalidation`).
5. Use **Origin Shield** for cost and latency optimization across regions.
6. Monitor via **CloudWatch metrics** (`4xxErrorRate`, `5xxErrorRate`, `BytesDownloaded`).
7. Enable **Access Logs** for debugging and analytics.

---

Would you like me to show you:

- 🧩 **Step-by-step setup in the AWS Console**, and
- 🧠 **Integration example (CloudFront + S3 + OAC + Node.js app)**

so you can see exactly how to deploy it end to end for a real-world application?
