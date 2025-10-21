Perfect — let’s go deep into **Amazon CloudFront Geo Restriction** — an important feature for controlling **where** your content can be delivered globally.
This is a **key topic for AWS Certified Solutions Architect – Professional** and extremely useful in enterprise CDN setups (for compliance, licensing, and localization control).

---

# 🌍 **Amazon CloudFront – Geo Restriction (Geo Blocking)**

---

## 🎯 **What Is Geo Restriction?**

**Geo Restriction** (also called **Geoblocking**) lets you control **which countries** can access your CloudFront-distributed content.

You can:

* **Allowlist (whitelist)** certain countries
  → Only users from those countries can access the content.
* **Blocklist (blacklist)** certain countries
  → Users from those countries are denied access.

CloudFront uses **the viewer’s IP address** to determine the country using **GeoIP databases**.

---

## ⚙️ **Two Geo Restriction Methods**

| Method                                                           | Description                                                               | Use Case                                                          |
| ---------------------------------------------------------------- | ------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| **CloudFront Geo Restriction (built-in)**                        | Simple configuration at distribution level — blocks or allows countries.  | Licensing, legal compliance, export restrictions.                 |
| **Custom Geo Restriction (Lambda@Edge or CloudFront Functions)** | Fine-grained control — block at path-level, apply per file or rule logic. | Dynamic restrictions, region-based routing, API behavior changes. |

---

# 🧩 **Option 1: Built-in Geo Restriction**

---

## ✅ **Step-by-Step Setup (Console)**

1. Go to **AWS Management Console → CloudFront**
2. Choose your **distribution**
3. Click **Edit → Settings**
4. Scroll to **Restrictions → Geo restriction**
5. Choose **Type**:

   * `Whitelist` (only selected countries allowed)
   * `Blacklist` (block selected countries)
6. Select countries (e.g., block `CN`, `RU`, or `IR`)
7. Save changes
8. Wait for distribution to redeploy (~15 minutes)

---

### 💡 Example: Allow Only U.S. and Canada

| Setting   | Value      |
| --------- | ---------- |
| Type      | Whitelist  |
| Countries | `US`, `CA` |

✅ Only users from **United States** and **Canada** can access content.
All other viewers get an **HTTP 403 (Forbidden)** response.

---

### 💡 Example: Block China and Russia

| Setting   | Value      |
| --------- | ---------- |
| Type      | Blacklist  |
| Countries | `CN`, `RU` |

❌ Requests from **China** and **Russia** will be denied.

---

## ⚙️ **Geo Restriction via AWS CLI**

### **Block Certain Countries**

```bash
aws cloudfront update-distribution \
  --id E123ABCXYZ \
  --default-root-object index.html \
  --geo-restriction '{"RestrictionType":"blacklist","Items":["CN","RU"],"Quantity":2}'
```

---

# 🧠 **Option 2: Custom Geo Restriction Using Lambda@Edge**

If you want more flexibility — e.g., only block `/videos/*` for some regions —
use **Lambda@Edge** or **CloudFront Functions**.

---

## ✅ **Step-by-Step: Custom Logic Example**

### Step 1: Create a Lambda Function in `us-east-1`

Lambda@Edge **must be deployed in `us-east-1`** (N. Virginia).

### Step 2: Use This Node.js Code

```javascript
exports.handler = async (event) => {
  const request = event.Records[0].cf.request;
  const headers = request.headers;

  // CloudFront adds 'cloudfront-viewer-country' header automatically
  const country = headers['cloudfront-viewer-country']
    ? headers['cloudfront-viewer-country'][0].value
    : 'UNKNOWN';

  console.log(`Request from country: ${country}`);

  // Blocklist example
  const blockedCountries = ['CN', 'RU', 'IR'];
  if (blockedCountries.includes(country)) {
    return {
      status: '403',
      statusDescription: 'Forbidden',
      body: `Access denied for ${country}`,
      headers: {
        'content-type': [{ key: 'Content-Type', value: 'text/plain' }],
      },
    };
  }

  // Allow all other requests
  return request;
};
```

### Step 3: Associate Lambda@Edge

* Go to **CloudFront → Behaviors → Edit**
* Choose **Viewer Request** event
* Attach the Lambda function version
* Save and deploy (takes a few minutes to replicate globally)

✅ Now CloudFront will execute your code at the **edge** before forwarding to the origin.

---

# 📦 **Option 3: CloudFront Function (Lightweight Alternative)**

For ultra-low latency use cases, use **CloudFront Functions** (faster, cheaper than Lambda@Edge).

Example:

```javascript
function handler(event) {
    var request = event.request;
    var country = request.headers['cloudfront-viewer-country'].value;
    var blocked = ['CN', 'RU'];

    if (blocked.includes(country)) {
        return {
            statusCode: 403,
            statusDescription: 'Forbidden',
            headers: { 'content-type': { value: 'text/plain' } },
            body: 'Access denied from your region',
        };
    }
    return request;
}
```

Attach it to **Viewer Request** in your CloudFront behavior.

---

# 🧾 **Behavior of Geo Restriction**

| Scenario                     | Result                                   |
| ---------------------------- | ---------------------------------------- |
| Request from blocked country | 403 Forbidden                            |
| Request from allowed country | Content served normally                  |
| IP cannot be resolved        | Default = Allowed                        |
| Using VPN / Proxy            | Geo restriction applies based on exit IP |

---

# 🔐 **Security & Compliance Use Cases**

| Use Case                     | Description                                                        |
| ---------------------------- | ------------------------------------------------------------------ |
| **Licensing compliance**     | Restrict media access to certain regions (e.g., streaming rights). |
| **Regulatory compliance**    | Block users from embargoed countries.                              |
| **Corporate access control** | Serve internal tools to specific countries or regions only.        |
| **Tiered content access**    | Different content sets per region (with Lambda@Edge routing).      |

---

# ⚡ **Testing Geo Restriction**

You can simulate requests using a VPN or tools like:

```bash
curl -I https://d1234abcd.cloudfront.net/index.html \
  -H "CloudFront-Viewer-Country: CN"
```

Expected output:

```
HTTP/2 403
```

---

# 🧠 **Best Practices**

1. **Use built-in Geo Restriction** for simple country-based blocking.
2. **Use Lambda@Edge or CloudFront Functions** for custom, path-level logic.
3. **Monitor 403 errors** in CloudWatch to track blocked requests.
4. Combine with **WAF geographic match rules** for additional protection.
5. Keep **origin isolated** (e.g., private S3 with OAC) — CloudFront handles filtering.

---

# ✅ **Summary**

| Feature                              | Description                                         |
| ------------------------------------ | --------------------------------------------------- |
| **Built-in Geo Restriction**         | Allow/deny entire countries at distribution level   |
| **Custom (Lambda@Edge / Functions)** | Apply fine-grained rules                            |
| **Header Used**                      | `CloudFront-Viewer-Country`                         |
| **Response Code**                    | 403 (Forbidden)                                     |
| **Common Use Case**                  | Legal restrictions, licensing, content localization |

---

Would you like me to show you a **diagram of CloudFront Geo Restriction architecture**
— how the request flows from user → edge → restriction check → origin —
and how it differs between **built-in** vs **custom (Lambda@Edge)** methods?
