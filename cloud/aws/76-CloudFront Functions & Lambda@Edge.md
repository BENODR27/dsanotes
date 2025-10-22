Excellent — you’re now touching one of the **most advanced and powerful performance + customization topics** in AWS content delivery:
**CloudFront Functions vs. Lambda@Edge** 💡

Let’s break this down **clearly, deeply, and professionally** — the way enterprise AWS architects design **global edge logic** for scalable, low-latency apps.

---

## 🌍 Overview

Both **CloudFront Functions** and **Lambda@Edge** let you **run code at AWS edge locations** (close to your users).
They’re used to **customize content delivery, headers, routing, authentication, and caching** — without needing to modify your origin server or app backend.

But they differ in **capabilities, latency, runtime, and pricing.**

---

## ⚙️ CloudFront Architecture Context

```
User Request
   │
   ▼
CloudFront Edge Location
   │
   ├── CloudFront Function (viewer request/response)
   │
   ├── Lambda@Edge (viewer/origin request/response)
   │
   ▼
Origin (S3 / EC2 / ALB / API Gateway / etc.)
```

At the **edge location**, AWS executes your custom logic **before reaching your backend** — improving speed, security, and control.

---

## ⚡ CloudFront Functions

> **Lightweight, ultra-low latency JavaScript functions** that run **at CloudFront edge locations** — purpose-built for **simple, high-volume operations**.

---

### 🧩 Key Features

| Feature                   | Description                                                                                |
| ------------------------- | ------------------------------------------------------------------------------------------ |
| **Runtime**               | JavaScript (V8 engine, like Cloudflare Workers)                                            |
| **Latency**               | < 1 millisecond                                                                            |
| **Deployment**            | Instant across all edge locations                                                          |
| **Use Case**              | Lightweight HTTP header manipulation, redirects, URL rewrites, authentication, A/B testing |
| **Cost**                  | ~$0.10 per million invocations (very cheap)                                                |
| **Memory Limit**          | 2 MB                                                                                       |
| **Max Execution Time**    | 1 ms                                                                                       |
| **Event Types Supported** | Viewer Request, Viewer Response only                                                       |

---

### ⚙️ Common Use Cases

| Use Case                  | Example                                               |
| ------------------------- | ----------------------------------------------------- |
| **Header injection**      | Add security headers like `Strict-Transport-Security` |
| **URL rewrite/redirects** | Redirect `/home` → `/index.html`                      |
| **A/B testing**           | Route 10% users to new site                           |
| **Access control**        | Basic token validation before forwarding to origin    |

---

### 💻 Example – CloudFront Function

**Goal:** Redirect all users from HTTP → HTTPS

```javascript
function handler(event) {
  var request = event.request;
  if (
    request.headers["cloudfront-forwarded-proto"] &&
    request.headers["cloudfront-forwarded-proto"].value === "http"
  ) {
    return {
      statusCode: 301,
      statusDescription: "Moved Permanently",
      headers: {
        location: {
          value: `https://${request.headers.host.value}${request.uri}`,
        },
      },
    };
  }
  return request;
}
```

#### Deployment Steps

1. Go to **CloudFront Console → Functions → Create Function**
2. Paste code → **Save and Publish**
3. Attach to your distribution:
   _Behavior → Edit → Function associations → Viewer request_

---

## 🧠 Lambda@Edge

> **Full-featured AWS Lambda functions** that run in AWS edge locations **with access to the entire AWS SDK** and deeper integration with CloudFront requests/responses.

---

### 🧩 Key Features

| Feature                   | Description                                                            |
| ------------------------- | ---------------------------------------------------------------------- |
| **Runtime**               | Node.js / Python                                                       |
| **Latency**               | ~10–50 ms (slower than CloudFront Functions)                           |
| **Max Memory**            | 128–3008 MB                                                            |
| **Max Execution Time**    | 5–30 seconds                                                           |
| **Deployment**            | Global replication (~minutes to deploy)                                |
| **Use Case**              | Dynamic content personalization, auth, origin routing, request signing |
| **Event Types Supported** | Viewer Request, Viewer Response, Origin Request, Origin Response       |

---

### ⚙️ Common Use Cases

| Use Case                            | Description                                              |
| ----------------------------------- | -------------------------------------------------------- |
| **Authentication & JWT validation** | Validate signed cookies/tokens                           |
| **Dynamic origin routing**          | Route traffic to different backends based on header/path |
| **Response modification**           | Modify HTML or JSON payload                              |
| **SEO optimizations**               | Rewrite URLs or canonical tags                           |
| **A/B testing with state**          | Control per-user experiments                             |

---

### 💻 Example – Lambda@Edge Function (Node.js)

**Goal:** Add security headers on viewer response.

```javascript
"use strict";

exports.handler = async (event) => {
  const response = event.Records[0].cf.response;
  const headers = response.headers;

  headers["strict-transport-security"] = [
    {
      key: "Strict-Transport-Security",
      value: "max-age=63072000; includeSubdomains; preload",
    },
  ];
  headers["content-security-policy"] = [
    { key: "Content-Security-Policy", value: "default-src 'self'" },
  ];
  headers["x-content-type-options"] = [
    { key: "X-Content-Type-Options", value: "nosniff" },
  ];

  return response;
};
```

#### Deployment Steps

1. Go to **Lambda Console → Create Function**
2. Choose runtime: `Node.js 18.x`
3. Write handler as above
4. **Deploy to Lambda@Edge**:

   - Actions → **Deploy to Lambda@Edge**
   - Choose CloudFront distribution + event type (e.g., Viewer Response)
   - AWS replicates it globally (~minutes)

---

## 🧩 Comparison: CloudFront Functions vs Lambda@Edge

| Feature              | CloudFront Functions            | Lambda@Edge                                   |
| -------------------- | ------------------------------- | --------------------------------------------- |
| **Runtime**          | JavaScript (V8)                 | Node.js / Python                              |
| **Latency**          | < 1 ms                          | 10–50 ms                                      |
| **Cold Start**       | None                            | Possible                                      |
| **Max Duration**     | 1 ms                            | Up to 30 sec                                  |
| **Payload Size**     | Small                           | Larger allowed                                |
| **Events Supported** | Viewer Request, Viewer Response | Viewer/Origin Request, Viewer/Origin Response |
| **Access AWS SDK**   | ❌ No                           | ✅ Yes                                        |
| **Deployment Speed** | Instant                         | Takes minutes                                 |
| **Cost**             | Very low                        | Higher per GB-second                          |
| **Use Case**         | Header rewrite, redirect        | Auth, routing, dynamic responses              |

---

## 💼 Enterprise-Level Example: E-commerce Site

### Goal:

- Use CloudFront + S3 for frontend hosting
- Personalize user pages by region
- Enforce authentication via JWT

### Architecture:

```
Client Browser
   │
   ▼
CloudFront Distribution
   │
   ├── CloudFront Function (Viewer Request)
   │     → Redirect HTTP → HTTPS
   │     → Block outside India (Geo restriction)
   │
   ├── Lambda@Edge (Origin Request)
   │     → Validate JWT token
   │     → Add user ID header
   │
   ▼
S3 / API Gateway / ALB (Origin)
```

### Outcome:

✅ Instant redirects + security at edge
✅ Auth validation close to users
✅ No backend load until verified

---

## 💸 Pricing (as of 2025)

| Service                  | Cost                                  | Example                        |
| ------------------------ | ------------------------------------- | ------------------------------ |
| **CloudFront Functions** | $0.10 per million invocations         | 10M → $1.00                    |
| **Lambda@Edge**          | $0.60 per million + duration (GB-sec) | 10M → $6+ depending on runtime |
| **Data Transfer**        | Normal CloudFront rates apply         | Based on edge region           |

---

## 🧠 Best Practices for Professionals

✅ Use **CloudFront Functions** for:

- Lightweight transformations (headers, URLs, redirects)
- Ultra-low latency at global scale

✅ Use **Lambda@Edge** for:

- Heavy logic (auth, personalization, signed requests)
- When you need AWS SDK / external service access

✅ Combine both:

- CloudFront Function (simple edge rewrite)
- Lambda@Edge (complex origin logic)

✅ Always version Lambda@Edge before deploy (each version = immutable snapshot)

✅ Monitor:

- CloudWatch logs under `/aws/cloudfront/function/` or `/aws/lambda/us-east-1/`

---

## 🚀 Real Production Example

**Flow:**

1. CloudFront Function (viewer request) → Redirect HTTP → HTTPS
2. Lambda@Edge (origin request) → Validate JWT token + route to microservice
3. Origin (API Gateway / ECS) → Business logic
4. Lambda@Edge (viewer response) → Add headers + cache control

Result:

- Fast global edge performance
- Secure & scalable
- Minimal origin load

---

Would you like me to show you a **complete deployment example** that integrates:

- 🌐 CloudFront (with S3 origin)
- ⚡ CloudFront Function (redirect logic)
- 🔐 Lambda@Edge (JWT validation + dynamic routing)
- 📦 Infrastructure as Code (using AWS SAM or CDK)

That’s the **professional enterprise way** to build “Amazon.com-style” global edge delivery architecture.
