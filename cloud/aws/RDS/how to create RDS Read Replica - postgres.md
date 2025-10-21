Perfect! Let’s go step by step on **creating an RDS Read Replica for PostgreSQL**. I’ll cover **both the AWS Management Console and CLI**, and note **exam-relevant best practices**.

---

## 🧭 1. Prerequisites

Before creating a read replica:

1. **Source Database** must be **PostgreSQL** in **RDS**.
2. **Automated backups must be enabled** on the source DB.

   * Replicas rely on the source’s backup and WAL (Write-Ahead Log) for replication.
3. Ensure **sufficient IAM permissions**:

   * `rds:CreateDBInstanceReadReplica`
   * `rds:DescribeDBInstances`
4. **VPC & security group** configured so replica can connect.
5. **Instance class** and storage type chosen based on expected load.

---

## ⚙️ 2. Creating a Read Replica via Console

### Step 1: Open RDS Console

* Navigate to **AWS Console → RDS → Databases**
* Select the **source PostgreSQL DB**.

### Step 2: Create Read Replica

* Click **Actions → Create read replica**
* Configure options:

  * **DB instance identifier** → e.g., `mydb-replica`
  * **Instance class** → e.g., `db.t3.medium`
  * **Storage type** → General Purpose (gp3), same as source or different
  * **Multi-AZ** (optional) → for HA
  * **VPC & subnet group** → must allow network connectivity
  * **Public accessibility** → Yes/No (depends on use case)

### Step 3: Configure Advanced Settings

* **Encryption** → can enable KMS encryption if required
* **Replication** → automatic from source
* **IAM role** → if using AWS service integrations
* **Tags** → optional, for cost allocation or management

### Step 4: Launch

* Click **Create read replica**
* AWS creates a **new DB instance** asynchronously replicating from the source.

---

## ⚡ 3. Creating a Read Replica via CLI

```bash
aws rds create-db-instance-read-replica \
    --db-instance-identifier mydb-replica \
    --source-db-instance-identifier mydb-primary \
    --db-instance-class db.t3.medium \
    --availability-zone us-east-1a \
    --publicly-accessible \
    --no-multi-az
```

**Optional parameters:**

* `--kms-key-id` → enable encryption
* `--tags` → resource tagging
* `--vpc-security-group-ids` → SG association
* `--storage-type` → gp3, io1, etc.

---

## 🧩 4. Cross-Region Read Replica

* Use `--source-region` parameter in CLI or **choose a different region in Console**
* Useful for:

  * Global read scaling
  * Disaster recovery

Example CLI:

```bash
aws rds create-db-instance-read-replica \
    --db-instance-identifier mydb-replica \
    --source-db-instance-identifier mydb-primary \
    --source-region us-east-1 \
    --db-instance-class db.t3.medium
```

---

## 🌐 5. Verification & Monitoring

1. **Replication Status:**

   * Check `Replication` → should show `replicating`
   * CloudWatch metric: `ReplicaLag` (seconds behind source)

2. **Connect to Replica:**

   ```bash
   psql -h mydb-replica-endpoint -U username -d dbname
   ```

   * **Read-only queries only**; writes will fail unless promoted.

3. **Promotion:**

   * If needed, promote replica to standalone:

     * Console → Actions → Promote
     * CLI → `aws rds promote-read-replica --db-instance-identifier mydb-replica`

---

## 🧠 6. Best Practices (Exam-Relevant)

| Practice                                       | Reason                                    |
| ---------------------------------------------- | ----------------------------------------- |
| Monitor **ReplicaLag**                         | Ensure read queries aren’t stale          |
| Use **Multi-AZ source DB**                     | High availability for writes              |
| Use **appropriate instance class for replica** | Scale reads without over-provisioning     |
| Enable **enhanced monitoring**                 | CloudWatch insights for performance       |
| Consider **cross-region replica**              | Disaster recovery or global users         |
| Replicas are **read-only**                     | Writes must go to primary unless promoted |

---

## 🧾 7. Quick Cheat Sheet

| Step | Action                                                 |
| ---- | ------------------------------------------------------ |
| 1    | Enable automated backups on source                     |
| 2    | Select source PostgreSQL instance                      |
| 3    | Choose “Create read replica”                           |
| 4    | Configure instance class, AZ, storage, networking      |
| 5    | Launch replica                                         |
| 6    | Monitor replication lag via CloudWatch                 |
| 7    | Use read replica for read queries or promote if needed |

---

I can also create a **diagram showing primary PostgreSQL → read replica (AZ & cross-region) with promotion path**, which is very handy for **SAP-C02 exam scenarios**.

Do you want me to make that visual?
