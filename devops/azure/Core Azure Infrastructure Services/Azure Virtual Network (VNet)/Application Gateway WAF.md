Absolutely! Let’s go **step by step** to create an **Azure Application Gateway with WAF (Web Application Firewall)** and then integrate it with your web app. I’ll include both **setup and integration**.

---

## **Step 1: Choose the WAF Tier**

- In Azure, Application Gateway has two tiers:

  - **Standard** → Layer 7 load balancing only
  - **WAF** → Layer 7 + Web Application Firewall

> For web application protection, select **WAF (Prevention or Detection mode)**.

---

## **Step 2: Create Application Gateway WAF**

1. Go to **Azure Portal → Create a Resource → Networking → Application Gateway**.
2. Fill in **Basics**:

   - **Subscription & Resource Group**
   - **Name** of the Gateway
   - **Region**
   - **Tier**: WAF v2 (recommended)
   - **Autoscaling** or fixed capacity

3. Click **Next: Frontend IP Configuration**.

---

## **Step 3: Configure Frontend IP**

- **Public** (internet-facing) or **Private** (internal).
- For **Public**, assign a public IP address.
- Click **Next: Backend Pools**.

---

## **Step 4: Configure Backend Pool**

- **Backend pool** = servers hosting your app (VMs, VMSS, or App Service).
- Click **Add backend pool**:

  - Name your pool
  - Add **targets** (IP addresses, App Service, VMs)

> If your app is in **App Service**, choose App Service as backend.

---

## **Step 5: Configure Listeners**

- **Listener** listens for incoming traffic.
- Click **Add Listener**:

  - Name the listener
  - Protocol: HTTP or HTTPS
  - Port: 80 or 443
  - For HTTPS, upload **SSL certificate (.pfx)** for SSL termination

---

## **Step 6: Configure Routing Rules**

- Routing rules map listener → backend pool.
- **Path-based routing**:

  - `/api/*` → API backend pool
  - `/images/*` → image backend pool

- **Host-based routing**:

  - `app1.domain.com` → pool1
  - `app2.domain.com` → pool2

> Click **Next: Web Application Firewall**.

---

## **Step 7: Enable WAF**

- **Enable WAF** during setup or in settings later.
- **Mode Options**:

  - **Detection** → Logs threats but doesn’t block
  - **Prevention** → Blocks attacks (recommended for production)

- **Rule Set**:

  - Choose OWASP 3.2 (latest)
  - Optionally, add **custom WAF rules** (IP restrictions, rate limiting, geo-filtering)

---

## **Step 8: Configure Health Probes**

- Health probes monitor backend status; only healthy backends receive traffic.
- Default probe exists, but you can customize:

  - Protocol: HTTP/HTTPS
  - Path: `/health` (or your app’s health endpoint)
  - Interval & unhealthy threshold

---

## **Step 9: Review + Create**

- Review all settings and click **Create**.
- Wait for deployment (usually ~10–15 minutes).

---

## **Step 10: Integrate with Your Web App**

### **Option 1: Backend is Azure App Service**

1. In **Backend Pool**, select your App Service.
2. Application Gateway will automatically route traffic to your App Service.
3. Ensure your **App Service allows traffic from AG**:

   - Add AG **subnet** to **Access Restrictions** if using private endpoints.

### **Option 2: Backend is VM / VMSS**

1. Add VM private IPs to backend pool.
2. Ensure **NSG (Network Security Group)** allows traffic from AG subnet.
3. Test by accessing **AG frontend IP / DNS** → traffic should reach the app.

---

## **Step 11: Optional Advanced Config**

- **SSL Termination & End-to-End Encryption**: Terminate SSL at AG and forward HTTPS to backend.
- **Rewrite Rules**: Modify URLs or headers, e.g., `/app1/* → /`
- **Session Affinity**: Sticky sessions for apps needing same-user routing.
- **Diagnostics Logs**: Enable WAF logs in **Log Analytics** or **Storage** for monitoring attacks.

---

## **Step 12: Test**

1. Access **Application Gateway frontend IP / DNS**.
2. Check routing works correctly (paths and hosts).
3. If in **WAF Prevention mode**, test with a simulated attack or OWASP tool to confirm it blocks malicious requests.

---
