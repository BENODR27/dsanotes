## **1. Linux VM Access – SSH**

**SSH (Secure Shell)** is the standard way to access Linux VMs.

### **Steps to Access:**

1. **During VM Creation:**

   - Choose **Authentication type**:

     - **SSH Public Key** (recommended)
     - **Password** (less secure)

   - Provide **username** and **SSH key** if using key-based authentication.

2. **Get VM Connection Info:**

   - Public IP or DNS name of the VM.
   - Username used during creation.

3. **Connect via SSH:**

```bash
ssh <username>@<public-ip>
```

- If using a private key:

```bash
ssh -i <path-to-private-key> <username>@<public-ip>
```

4. **Network Requirements:**

   - **NSG (Network Security Group)** must allow **Inbound TCP 22**.
   - VM must have a **public IP** (or use a jump/bastion host).

---

### **Optional: Azure Bastion for SSH**

- Azure Bastion allows **browser-based SSH access** to Linux VMs **without exposing public IPs**.
- Safer alternative to opening port 22.

---

## **2. Windows VM Access – WinRM / RDP**

Windows VMs can be accessed via **RDP** or **WinRM**:

### **RDP (Remote Desktop Protocol) – GUI Access**

1. **During VM Creation:**

   - Enable **RDP** access (default port 3389).
   - Set **username and password**.

2. **Connect via Remote Desktop:**

   - Use Windows “Remote Desktop Connection” or macOS **Microsoft Remote Desktop**.
   - Enter public IP / DNS, username, and password.

3. **Network Requirements:**

   - NSG must allow **Inbound TCP 3389**.
   - VM must have a public IP or use Azure Bastion.

---

### **WinRM (Windows Remote Management) – Command Line Access**

- WinRM uses **HTTP/HTTPS (ports 5985/5986)** to manage Windows machines remotely.
- **PowerShell Remoting Example:**

```powershell
# From your local machine
Enter-PSSession -ComputerName <vm-public-ip> -Credential <username>
```

- Requirements:

  - WinRM enabled on the VM (enabled by default in Azure Windows VMs)
  - Network Security Group allows ports **5985 (HTTP) / 5986 (HTTPS)**

---

## **3. Security Best Practices**

- **Avoid public IPs**: Use Azure Bastion or VPN for secure access.
- **Use key-based SSH** for Linux instead of passwords.
- **NSG rules**: Restrict inbound traffic to trusted IP ranges.
- **Just-in-time VM Access**: Open RDP/SSH ports only when needed.

---

### **Summary Table**

| VM OS   | Access Method | Port        | Authentication        | Notes                                        |
| ------- | ------------- | ----------- | --------------------- | -------------------------------------------- |
| Linux   | SSH           | 22          | Key-based or password | Can use Azure Bastion to avoid public IP     |
| Windows | RDP           | 3389        | Password              | GUI access, can use Azure Bastion            |
| Windows | WinRM         | 5985 / 5986 | Username + password   | CLI/PowerShell management, secure over HTTPS |

---
