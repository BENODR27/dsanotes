Amazon CloudFront is a pay-as-you-go content delivery network (CDN) service that charges based on usage, including data transfer, HTTP/HTTPS requests, and additional features like Lambda@Edge. Below is a detailed breakdown of CloudFront pricing components:

---

## 📦 Data Transfer Out (From CloudFront to Viewers)

Charges apply when CloudFront delivers content to end-users:

- **First 10 TB per month**: $0.085 per GB
- **Next 40 TB per month**: $0.080 per GB
- **Next 100 TB per month**: $0.060 per GB
- **Over 150 TB per month**: Contact AWS for pricing

_Note: Data transfer from AWS origins (e.g., S3, EC2) to CloudFront is free._

---

## 🔁 HTTP/HTTPS Requests

Charges are based on the number of requests CloudFront handles:

- **Standard Requests**: $0.0075 per 10,000 requests
- **HTTPS Requests**: $0.01 per 10,000 requests

_Note: The first 20 million HTTPS requests per month are free._

---

## 🛠️ Lambda@Edge

For serverless compute functions at CloudFront edge locations:

- **Requests**: $0.60 per 1 million requests
- **Duration**: $0.00005001 per GB-second

_Note: No free tier is available for Lambda@Edge._

---

## 🧹 Invalidation Requests

To remove objects from CloudFront cache:

- **First 1,000 paths per month**: Free
- **Additional paths**: $0.005 per path

_Note: A path can be a single file or a wildcard pattern (e.g., `/images/_`).

---

## 🌍 Geo Restriction

CloudFront allows you to restrict content delivery to specific countries:

- **Allowlist**: Serve content only to selected countries
- **Blocklist**: Block content delivery to selected countries

_Note: Geo restriction is a feature of CloudFront and does not incur additional charges._

---

## 💡 Cost Optimization Tips

- **Use Price Classes**: Limit CloudFront to less expensive edge locations to reduce costs.
- **Optimize Cache Hit Ratio**: Configure cache policies to serve content from edge locations, minimizing origin fetches.
- **Monitor Usage**: Regularly review AWS billing and usage reports to identify cost-saving opportunities.

---

For a detailed cost estimate tailored to your usage, consider using the [AWS Pricing Calculator](https://aws.amazon.com/calculator/).
