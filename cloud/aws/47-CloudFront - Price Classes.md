Perfect — let’s break down **CloudFront Price Classes**, which is essential for **controlling costs** when deploying a CDN globally.

---

# 🌍 **CloudFront Price Classes**

**Price Classes** let you **limit which edge locations** serve your content, helping reduce costs while still delivering good performance to your target audience.

---

## **How Price Classes Work**

- CloudFront has **edge locations worldwide**.
- Using **Price Classes**, you choose which regions are allowed to serve your content.
- Requests from excluded regions are **routed to the nearest included edge location**, potentially increasing latency but reducing cost.

---

## **Available Price Classes**

| Price Class         | Edge Locations Included               | Typical Use Case                                              |
| ------------------- | ------------------------------------- | ------------------------------------------------------------- |
| **Price Class 100** | US, Canada, Europe                    | Lowest cost; suitable if users are primarily in these regions |
| **Price Class 200** | US, Canada, Europe, Asia, Middle East | Mid-level cost; balanced for wider coverage                   |
| **Price Class All** | All edge locations globally           | Highest cost; best for global audience with lowest latency    |

---

### **Price Class Impact Example**

- Serving **1 TB data** monthly from **All Locations** costs **$0.085/GB** for first 10 TB.
- Using **Price Class 100**, you only pay for edge locations in US, Canada, Europe → lower effective cost.
- Users outside these regions are routed to the nearest included edge, which may slightly increase latency.

---

## **Step-by-Step: Setting Price Class in CloudFront**

1. Go to **CloudFront Console → Distributions**
2. Select your **distribution → Edit**
3. Scroll to **Price Class**
4. Select desired class:

   - Price Class 100
   - Price Class 200
   - Price Class All

5. Save changes → wait for **distribution deployment (~15 min)**

---

## **Best Practices**

1. **Match Price Class to User Base**

   - If 90% of users are in India, choose **Price Class 200** or include **Asia** regions.

2. **Combine with Geo Restriction**

   - Restrict delivery to your main audience region to reduce waste and cost.

3. **Monitor Metrics**

   - Use **CloudFront usage reports** and **CloudWatch** to track bandwidth and optimize cost.

4. **Use Price Class for Static Content**

   - Static assets like images, CSS, JS are ideal candidates for Price Class optimization.

---

✅ **Summary:**
Price Classes let you **control CloudFront costs** by limiting which edge locations serve your content while still providing good performance for your primary users.

---

If you want, I can create a **visual map of CloudFront Price Classes** showing **which regions are included in 100, 200, and All**, which is extremely handy for **architecture diagrams and exam prep**.

Do you want me to do that?
