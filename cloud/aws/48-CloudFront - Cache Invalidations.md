Perfect — let’s dive into **CloudFront Cache Invalidation**, a critical feature for controlling content freshness in a CDN. This is essential for enterprise setups and production-grade deployments.

---

# 🌐 **Amazon CloudFront – Cache Invalidation**

---

## **What Is Cache Invalidation?**

- CloudFront caches your content at **edge locations** to reduce latency and origin load.
- When content at the origin changes (like a new Angular build), **CloudFront still serves the old cached version** until it expires.
- **Cache Invalidation** lets you **manually remove specific objects** or paths from the cache **before TTL expires**, forcing CloudFront to fetch the latest version from your origin.

---

## **Key Concepts**

| Concept                  | Description                                                                 |
| ------------------------ | --------------------------------------------------------------------------- |
| **Object Path**          | The file or path you want to invalidate, e.g., `/index.html`                |
| **Wildcard**             | `*` can be used to invalidate multiple objects, e.g., `/assets/*`           |
| **Invalidation Request** | An API call or console operation to remove objects from edge caches         |
| **TTL (Time-to-Live)**   | If you don’t invalidate, CloudFront serves cached content until TTL expires |

---

## **Step 1: Using the AWS Management Console**

1. Go to **CloudFront → Distributions → Select your distribution**
2. Click **Invalidations → Create Invalidation**
3. Enter the **object paths** to invalidate, e.g.:

   - Single file: `/index.html`
   - Multiple files: `/assets/*`

4. Click **Invalidate** → Status changes to `InProgress`
5. Wait for **invalidations to complete** (usually a few minutes)

---

## **Step 2: Using AWS CLI**

### **Single File Invalidation**

```bash
aws cloudfront create-invalidation \
  --distribution-id E123ABCXYZ \
  --paths "/index.html"
```

### **Wildcard Invalidation**

```bash
aws cloudfront create-invalidation \
  --distribution-id E123ABCXYZ \
  --paths "/assets/*"
```

**Output Example:**

```json
{
  "Invalidation": {
    "Id": "I1234567890EXAMPLE",
    "Status": "InProgress",
    "CreateTime": "2025-10-21T12:34:56Z"
  }
}
```

✅ Check status:

```bash
aws cloudfront get-invalidation \
  --distribution-id E123ABCXYZ \
  --id I1234567890EXAMPLE
```

---

## **Step 3: Using AWS SDK (Node.js Example)**

```javascript
import {
  CloudFrontClient,
  CreateInvalidationCommand,
} from "@aws-sdk/client-cloudfront";

const client = new CloudFrontClient({ region: "us-east-1" });

async function invalidateCache(distributionId, paths) {
  const params = {
    DistributionId: distributionId,
    InvalidationBatch: {
      CallerReference: `${Date.now()}`,
      Paths: {
        Quantity: paths.length,
        Items: paths,
      },
    },
  };

  const command = new CreateInvalidationCommand(params);
  const result = await client.send(command);
  console.log("Invalidation created:", result.Invalidation.Id);
}

// Example usage
invalidateCache("E123ABCXYZ", ["/index.html", "/assets/*"]);
```

---

## **Step 4: Best Practices**

1. **Minimize Wildcard Invalidations**

   - Wildcards invalidate **all matching objects**, which may increase cost and processing time.

2. **Use Versioned Filenames**

   - Angular apps often include **hash in filenames** (e.g., `main.abc123.js`) → reduces the need for invalidations.

3. **Automate Invalidations in CI/CD**

   - Trigger invalidation automatically after deploying a new build.

4. **Monitor Invalidation Usage**

   - First 1,000 invalidation paths per month are free; after that, **$0.005 per path**.

5. **Combine with TTL/Caching Strategy**

   - Adjust **Cache-Control headers** at origin for dynamic content to reduce frequent invalidations.

---

## ✅ **Summary**

- CloudFront caches content at the edge; invalidations **force refresh before TTL**.
- Can be done via **Console, CLI, SDK, or CI/CD automation**.
- Use **versioned assets** to minimize invalidations and optimize cost.
- Supports **wildcards** for batch invalidations.

---

I can also create a **visual workflow diagram** showing:

**User → CloudFront Edge → Cached Content → Invalidation Trigger → Updated Content from Origin**,

which is extremely useful for enterprise setup and exam prep.

Do you want me to create that diagram?
