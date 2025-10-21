Perfect — you’re asking for **a full, professional, end-to-end setup** of **Amazon S3 Glacier Vault Lock**, including **step-by-step creation**, **policy locking**, and **how to use it with a Node.js app** for archive upload, retrieval, and verification.

Let’s go **step by step — AWS CLI + Node.js SDK (v3)** style — enterprise ready.

---

# 🧊 **Amazon S3 Glacier Vault Lock – Full Implementation Guide**

---

## 🔍 Overview

**Goal:**
✅ Create a **Glacier vault**,
✅ Apply a **Vault Lock policy** (WORM immutability),
✅ Upload archives from a **Node.js app**,
✅ Retrieve them when needed.

---

# 🧱 Step 1: Understanding the Components

| Component             | Description                                                               |
| --------------------- | ------------------------------------------------------------------------- |
| **Vault**             | Container for archives (like an S3 bucket for Glacier)                    |
| **Archive**           | Individual file or backup stored in a vault                               |
| **Vault Lock Policy** | JSON-based rule that enforces retention (e.g., can’t delete for 365 days) |
| **Vault Lock State**  | In Progress → Locked (after confirmation)                                 |

---

# ⚙️ Step 2: Create a Glacier Vault

### **Command**

```bash
aws glacier create-vault --account-id - --vault-name my-secure-vault
```

**Output:**

```json
{
  "location": "/123456789012/vaults/my-secure-vault"
}
```

---

# 🧾 Step 3: Create a Vault Lock Policy

Create a JSON file named **`vault-lock-policy.json`**.

### **vault-lock-policy.json**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PreventEarlyDeletion",
      "Effect": "Deny",
      "Principal": "*",
      "Action": "glacier:DeleteArchive",
      "Resource": "arn:aws:glacier:us-east-1:123456789012:vaults/my-secure-vault",
      "Condition": {
        "NumericLessThan": {
          "glacier:ArchiveAgeInDays": 365
        }
      }
    }
  ]
}
```

✅ Meaning: Any archive younger than 365 days cannot be deleted.

---

# 🔐 Step 4: Initiate Vault Lock (Test Phase)

### **Command**

```bash
aws glacier initiate-vault-lock \
  --account-id - \
  --vault-name my-secure-vault \
  --policy file://vault-lock-policy.json
```

**Output Example:**

```json
{
  "lockId": "abc12345-6789-xyz-0011-example"
}
```

👉 Vault Lock is now **“In Progress”**.
You can **test uploads/deletions** during this phase.

---

# 🧪 Step 5: Validate the Lock Policy (Optional)

Try to delete an archive (after upload) within 365 days — AWS should deny it.

Once confirmed, proceed to lock permanently.

---

# 🔒 Step 6: Complete the Vault Lock (Permanent)

### **Command**

```bash
aws glacier complete-vault-lock \
  --account-id - \
  --vault-name my-secure-vault \
  --lock-id abc12345-6789-xyz-0011-example
```

✅ The vault is now **immutable** — cannot delete or modify archives before retention expires.
Even **root users** and **AWS Support** cannot override this.

---

# 🧾 Step 7: Verify Lock Status

### **Command**

```bash
aws glacier describe-vault --account-id - --vault-name my-secure-vault
```

**Output Example:**

```json
{
  "VaultName": "my-secure-vault",
  "VaultLockPolicy": {
    "State": "Locked"
  }
}
```

---

# 💻 Step 8: Use Glacier with Node.js (AWS SDK v3)

Now, let’s integrate with a **Node.js app** to **upload, list, and retrieve archives**.

---

## 🪣 Install Dependencies

```bash
npm init -y
npm install @aws-sdk/client-glacier @aws-sdk/lib-storage dotenv
```

Create a `.env` file:

```
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=YOUR_ACCESS_KEY
AWS_SECRET_ACCESS_KEY=YOUR_SECRET_KEY
VAULT_NAME=my-secure-vault
```

---

## 📦 Node.js – Upload Archive to Glacier

```javascript
// upload.js
import { GlacierClient, UploadArchiveCommand } from "@aws-sdk/client-glacier";
import fs from "fs";
import dotenv from "dotenv";
dotenv.config();

const glacier = new GlacierClient({ region: process.env.AWS_REGION });

async function uploadArchive(filePath) {
  const fileStream = fs.createReadStream(filePath);
  const params = {
    vaultName: process.env.VAULT_NAME,
    body: fileStream,
    accountId: "-", // your AWS account ID, or "-" to use caller identity
  };

  try {
    const result = await glacier.send(new UploadArchiveCommand(params));
    console.log("✅ Archive uploaded successfully");
    console.log("Archive ID:", result.archiveId);
  } catch (err) {
    console.error("❌ Upload failed:", err);
  }
}

// Run the upload
uploadArchive("backup.zip");
```

✅ **This uploads a file to Glacier Vault.**
Each file returns a unique **Archive ID** — store it for future retrieval.

---

## 📋 List Vaults (Optional)

```javascript
import { GlacierClient, ListVaultsCommand } from "@aws-sdk/client-glacier";

const glacier = new GlacierClient({ region: "us-east-1" });

async function listVaults() {
  const data = await glacier.send(new ListVaultsCommand({ accountId: "-" }));
  console.log("Vaults:", data.VaultList);
}

listVaults();
```

---

## 📦 Retrieve Archive (Async Process)

Retrieving from Glacier is **asynchronous** — you initiate a job, then download it when ready.

### **Initiate Retrieval Job**

```javascript
import { GlacierClient, InitiateJobCommand } from "@aws-sdk/client-glacier";
import dotenv from "dotenv";
dotenv.config();

const glacier = new GlacierClient({ region: process.env.AWS_REGION });

async function retrieveArchive(archiveId) {
  const params = {
    accountId: "-",
    vaultName: process.env.VAULT_NAME,
    jobParameters: {
      Type: "archive-retrieval",
      ArchiveId: archiveId,
      Description: "Retrieve archive from Glacier",
      SNSTopic: "", // Optional: SNS topic for job completion notification
    },
  };

  const job = await glacier.send(new InitiateJobCommand(params));
  console.log("✅ Retrieval job started:", job.jobId);
}

retrieveArchive("YOUR_ARCHIVE_ID");
```

### **Check Job Status**

```javascript
import { GlacierClient, DescribeJobCommand } from "@aws-sdk/client-glacier";
const glacier = new GlacierClient({ region: process.env.AWS_REGION });

async function checkJobStatus(jobId) {
  const params = { accountId: "-", vaultName: process.env.VAULT_NAME, jobId };
  const result = await glacier.send(new DescribeJobCommand(params));
  console.log("Job Status:", result.StatusCode);
}

checkJobStatus("YOUR_JOB_ID");
```

### **Download Retrieved Archive**

Once status is `"Succeeded"`:

```javascript
import { GlacierClient, GetJobOutputCommand } from "@aws-sdk/client-glacier";
import fs from "fs";

const glacier = new GlacierClient({ region: "us-east-1" });

async function downloadArchive(jobId) {
  const params = { accountId: "-", vaultName: "my-secure-vault", jobId };
  const { body } = await glacier.send(new GetJobOutputCommand(params));
  const file = fs.createWriteStream("retrieved.zip");
  body.pipe(file);
  console.log("✅ Archive downloaded");
}

downloadArchive("YOUR_JOB_ID");
```

---

# 🧠 Important Notes

| Feature                  | Description                                |
| ------------------------ | ------------------------------------------ |
| **Vault Lock Policy**    | Immutable after locking                    |
| **Vault Lock State**     | In Progress → Locked                       |
| **Deletion Restriction** | Enforced by age condition (e.g., 365 days) |
| **Archive ID**           | Required for retrieval/deletion            |
| **Retrieval Time**       | 1–12 hours depending on retrieval tier     |
| **Integration**          | Node.js uses `@aws-sdk/client-glacier`     |
| **Cost Optimization**    | Use Glacier Deep Archive for older backups |

---

# ✅ Best Practices

1. Use **Vault Lock** for regulatory or compliance data (finance, legal, audit logs).
2. Store **Archive IDs** in DynamoDB or RDS for reference.
3. Use **SNS notifications** to trigger a Lambda when retrieval jobs complete.
4. Use **CloudTrail** to audit all Glacier operations.
5. Set retention policy carefully — **immutable means permanent**.

---

# 📊 Example Architecture

**Backup System (Node.js App)**
→ Uploads data to **Glacier Vault**
→ Vault protected by **Vault Lock Policy (365 days)**
→ CloudTrail logs access
→ Retrievals initiated only through admin workflow

---

Would you like me to generate a **diagram** showing this architecture —
`Node.js App → Glacier Vault (Vault Lock) → CloudTrail + SNS Notifications` —
so you can visualize how immutable backup and retrieval workflows fit together?
