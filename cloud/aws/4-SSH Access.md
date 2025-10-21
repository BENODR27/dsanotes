Excellent — **SSH access** is a must-know topic for the **AWS Certified Solutions Architect – Professional (SAP-C02)** exam because it’s central to **managing EC2 instances securely**, **troubleshooting connectivity**, and **designing bastion/jump-host architectures**.

Let’s go through **everything you need to know about SSH access in AWS**, from fundamentals to exam-level design considerations. 👇

---

## 🧭 1. What Is SSH Access?

**SSH (Secure Shell)** allows **secure, encrypted remote login** to Linux-based EC2 instances.
You use SSH to:

* Connect to instances for administration
* Transfer files securely (SCP/SFTP)
* Tunnel traffic (port forwarding)

AWS uses **public key authentication**, *not passwords*, for SSH access by default.

---

## 🔐 2. SSH Authentication in AWS

### 🔸 Step 1: Create an EC2 Key Pair

When launching an EC2 instance:

* You choose (or create) a **key pair**.
* AWS stores the **public key** in the instance’s metadata.
* You download the **private key (.pem)** file — this is used to connect.

Example:

```bash
aws ec2 create-key-pair --key-name MyKeyPair --query 'KeyMaterial' --output text > MyKeyPair.pem
chmod 400 MyKeyPair.pem
```

---

### 🔸 Step 2: Connect via SSH

Once the instance is running and has a public IP:

```bash
ssh -i MyKeyPair.pem ec2-user@<Public-IP>
```

Common usernames by OS:

| OS           | Default SSH User     |
| ------------ | -------------------- |
| Amazon Linux | `ec2-user`           |
| Ubuntu       | `ubuntu`             |
| RHEL         | `ec2-user`           |
| CentOS       | `centos`             |
| SUSE         | `ec2-user` or `root` |
| Debian       | `admin` or `root`    |

---

### 🔸 Step 3: Security Group Rules

Your **Security Group (SG)** must allow inbound SSH:

```
Type: SSH
Protocol: TCP
Port: 22
Source: <your IP> (e.g. 203.0.113.25/32)
```

**Exam Tip 💡:**
Never allow SSH from `0.0.0.0/0` (the whole internet). Use **your IP only** or **Systems Manager Session Manager** instead.

---

## 🌐 3. Network Layer Requirements

To SSH into an EC2 instance, you must have:

| Requirement                | Description                                         |
| -------------------------- | --------------------------------------------------- |
| **Public IP / Elastic IP** | Required if accessing from the Internet             |
| **Route Table**            | Subnet must route 0.0.0.0/0 to an Internet Gateway  |
| **Security Group**         | Allow inbound port 22 from your IP                  |
| **NACL (optional)**        | Inbound: 22, Outbound: Ephemeral ports (1024–65535) |
| **Key Pair**               | Must match the instance’s public key                |

If your instance is in a **private subnet**, you’ll need a **bastion host** or **Session Manager**.

---

## 🏰 4. Bastion Host (Jump Server)

A **bastion host** is a secure EC2 instance in a **public subnet** used to SSH into private instances.

### Architecture:

```
[Your Laptop]
   ↓ SSH (port 22)
[Bastion Host - Public Subnet]
   ↓ SSH (port 22, internal IP)
[Private EC2 Instance - Private Subnet]
```

**Security best practices:**

* Bastion has **restricted Security Group** (`your_ip → port 22`)
* Private instances allow **SSH only from the bastion SG**
* Enable **Session Manager** or **SSM Agent** to avoid direct SSH if possible
* Use **IAM roles**, not static credentials on bastion

---

### Example SG Configuration

| Security Group      | Rule           | Source     |
| ------------------- | -------------- | ---------- |
| Bastion SG          | Inbound TCP 22 | Your IP    |
| Private Instance SG | Inbound TCP 22 | Bastion SG |

---

## 🧩 5. Key Pair Management

* **Key pairs** are **Region-specific**
* Stored under **EC2 > Key Pairs**
* You cannot retrieve a private key from AWS after creation
* If lost → create a new key pair, and use **EC2 Instance Connect** or **Systems Manager** to add it manually

---

## ⚙️ 6. Alternate Connection Methods (Exam-Relevant)

AWS offers several **SSH alternatives** or **enhancements** to improve security and automation.

| Method                                  | Description                           | Benefits                                       |
| --------------------------------------- | ------------------------------------- | ---------------------------------------------- |
| **AWS Systems Manager Session Manager** | Connect without SSH or keys           | No port 22, IAM-controlled, logs to CloudWatch |
| **EC2 Instance Connect**                | Browser-based or CLI SSH              | Temporary SSH keys, IAM integration            |
| **SSM Run Command**                     | Run shell commands without logging in | Used for automation or compliance              |
| **AWS PrivateLink / VPN**               | Access via internal network           | Avoid public Internet exposure                 |
| **AWS Transfer Family**                 | SFTP over managed endpoint            | Managed service for file transfers             |

---

### Example: Connect Using Session Manager

Preconditions:

* Instance has **SSM Agent**
* Attached **IAM Role** with `AmazonSSMManagedInstanceCore`

Command:

```bash
aws ssm start-session --target <instance-id>
```

**Exam Tip 💡:**
For **secure, auditable, no-SSH-access architectures**, prefer **Session Manager** over bastion hosts.

---

## 🔒 7. Security Best Practices

| Best Practice                                      | Description                                        |
| -------------------------------------------------- | -------------------------------------------------- |
| 🔐 **Use least privilege**                         | Limit SSH to specific IPs (not 0.0.0.0/0)          |
| 🧱 **Use Security Groups, not NACLs**, for control | SGs are stateful and simpler                       |
| 🧩 **Rotate key pairs regularly**                  | Automate via Systems Manager or Lambda             |
| 💻 **Disable password authentication**             | Use key pairs only                                 |
| ☁️ **Use Session Manager / EC2 Instance Connect**  | Avoid SSH key sprawl                               |
| 🧍 **IAM roles over hardcoded credentials**        | No permanent secrets on EC2                        |
| 🧾 **Centralize logs**                             | Store SSM/Session Manager logs in CloudWatch or S3 |

---

## 🧠 8. Exam-Level Design Scenarios

### Scenario 1

> You need secure SSH access to EC2 instances in private subnets without exposing port 22 to the Internet.

✅ Use **Systems Manager Session Manager** (preferred)
✅ Or deploy a **bastion host** in a public subnet, with restricted inbound rules.

---

### Scenario 2

> An engineer lost their `.pem` key but must regain access.

✅ Use **EC2 Instance Connect** (for temporary access)
✅ Or **attach root volume** to another instance and update `~/.ssh/authorized_keys`.

---

### Scenario 3

> Compliance requires no inbound SSH traffic from the Internet.

✅ Use **Session Manager** exclusively
✅ Disable port 22 on all SGs
✅ Control access via **IAM roles and policies**

---

## 🧾 9. Troubleshooting SSH Access

| Issue                           | Likely Cause                                                  | Fix                                   |
| ------------------------------- | ------------------------------------------------------------- | ------------------------------------- |
| “Permission denied (publickey)” | Wrong key or user                                             | Check username, permissions on `.pem` |
| Timeout / no response           | SG or NACL blocking                                           | Open inbound 22 from your IP          |
| “Connection refused”            | SSH not running                                               | Restart SSHD service                  |
| Host unreachable                | Wrong route / no IGW                                          | Check route table, NAT, subnet config |
| Lost key pair                   | Recreate access using EC2 Instance Connect or Session Manager |                                       |

---

## ⚡ 10. Summary Cheat Sheet

| Concept                   | Key Points                        |
| ------------------------- | --------------------------------- |
| **Default Port**          | 22 (TCP)                          |
| **Auth Method**           | Public key (.pem)                 |
| **Security Group**        | Must allow inbound 22             |
| **Best Practice**         | Restrict IPs or use SSM           |
| **Private Subnet Access** | Via Bastion or Session Manager    |
| **Lost Key Recovery**     | Use EC2 Instance Connect or SSM   |
| **Logging**               | Use CloudWatch + SSM Session Logs |

---

Would you like me to include a **diagram of SSH access architectures** — showing:

* Public SSH access
* Bastion host model
* Session Manager (no SSH ports)
  That’s often used in exam prep guides and solution design questions.
