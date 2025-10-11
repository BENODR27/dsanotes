Got it! Let’s go **step by step** to create a **DNS zone in Azure** and connect a **third-party domain** (like GoDaddy, Namecheap, etc.) to it.

---

## **Step 1: Create a DNS Zone in Azure**

1. Log in to the **Azure Portal** → [https://portal.azure.com](https://portal.azure.com)
2. Search for **DNS zones** → Click **+ Create**
3. **Fill in details**:

   - **Subscription**: Select your subscription
   - **Resource Group**: Use existing or create new
   - **Name**: Enter your domain name (e.g., `example.com`)
   - **Location**: Select any region (DNS zones are global)

4. Click **Review + Create**, then **Create**

---

## **Step 2: Add DNS Records in Azure**

1. Go to your **DNS zone** in Azure
2. Click **+ Record set**

   - **Type**: Usually `A` for root domain, `CNAME` for subdomain
   - **Name**: e.g., `@` for root or `www` for subdomain
   - **TTL**: 3600 (default)
   - **IP Address**: Enter your Azure VM public IP (for `A` record)
   - Save

**Example Records for Node.js app on Azure VM**:

| Type  | Name | Value (IP)    |
| ----- | ---- | ------------- |
| A     | @    | 40.112.123.45 |
| CNAME | www  | @             |

---

## **Step 3: Get Azure DNS Name Servers**

1. In your **DNS zone**, go to **Overview**
2. Copy all the **Name Servers (NS)**. It will look like:

```
ns1-01.azure-dns.com
ns2-01.azure-dns.net
ns3-01.azure-dns.org
ns4-01.azure-dns.info
```

---

## **Step 4: Update Name Servers at Your Domain Registrar**

1. Log in to your **domain registrar** (GoDaddy, Namecheap, etc.)
2. Find **DNS settings / Name Servers**
3. Replace existing name servers with the **Azure NS servers** you copied
4. Save changes

> ⚠️ DNS propagation may take up to **24–48 hours**, though usually faster.

---

## **Step 5: Verify DNS Propagation**

You can use online tools like:

- [https://dnschecker.org](https://dnschecker.org)
- [https://www.whatsmydns.net](https://www.whatsmydns.net)

Check if your domain resolves to the **Azure VM IP** correctly.

---

## **Step 6: (Optional) Add Subdomains**

For example, if you want `api.example.com`:

1. In Azure DNS → **+ Record set**
2. Type: `A`
3. Name: `api`
4. IP: your VM or another IP

---

### ✅ Result:

- Your **third-party domain** now points to your **Azure VM** via Azure DNS.
- You can manage all DNS records directly in Azure Portal.

---

