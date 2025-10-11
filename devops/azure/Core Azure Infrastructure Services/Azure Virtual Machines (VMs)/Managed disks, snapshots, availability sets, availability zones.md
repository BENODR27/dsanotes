Excellent 💪 — now you’re moving into **Azure VM resiliency and storage architecture**, which are **core DevOps and infrastructure concepts** every **Senior Azure DevOps Engineer** must know.

Let’s go step-by-step and make it **crystal clear** how to use and understand:

- **Managed Disks**
- **Snapshots**
- **Availability Sets**
- **Availability Zones**

---

# ⚙️ **1️⃣ Azure Managed Disks**

### 📘 **What are Managed Disks?**

Managed Disks are **Azure-managed storage volumes** used by Virtual Machines.
Azure automatically handles storage accounts, redundancy, and scaling — you just specify the **type**, **size**, and **performance**.

---

### 💾 **Disk Types**

| Type             | Description                      | Use Case                          |
| ---------------- | -------------------------------- | --------------------------------- |
| **Standard HDD** | Cheapest, basic storage          | Test/dev or low-traffic workloads |
| **Standard SSD** | Better performance, low cost     | Web servers, moderate apps        |
| **Premium SSD**  | High performance, low latency    | Production apps, databases        |
| **Ultra Disk**   | Extremely high IOPS & throughput | Mission-critical DBs              |

---

### ⚙️ **Attach Managed Disk to a VM (CLI)**

#### **1️⃣ Create a Managed Disk**

```bash
az disk create \
  --resource-group rg-demo \
  --name dataDisk1 \
  --size-gb 64 \
  --sku Premium_LRS \
  --query id -o tsv
```

#### **2️⃣ Attach Disk to VM**

```bash
az vm disk attach \
  --resource-group rg-demo \
  --vm-name ubuntu-devops-vm \
  --name dataDisk1
```

#### **3️⃣ Inside VM (format & mount)**

```bash
sudo fdisk -l
sudo mkfs -t ext4 /dev/sdc
sudo mkdir /data
sudo mount /dev/sdc /data
```

---

### 🧠 **Real DevOps Usage**

- Store **logs**, **backups**, or **app data**
- Use as **persistent volume** for Docker or Kubernetes
- Move disks between VMs during troubleshooting

---

# 📸 **2️⃣ Snapshots**

### 📘 **What are Snapshots?**

A **snapshot** is a **point-in-time backup** of a managed disk.
You can use it to **restore**, **clone**, or **replicate** disks/VMs.

---

### ⚙️ **Create Snapshot**

```bash
az snapshot create \
  --resource-group rg-demo \
  --name myVMDiskSnapshot \
  --source dataDisk1 \
  --sku Standard_LRS
```

---

### 🔄 **Restore from Snapshot**

```bash
az disk create \
  --resource-group rg-demo \
  --name restoredDisk \
  --source myVMDiskSnapshot
```

Then attach it to a new VM:

```bash
az vm disk attach \
  --resource-group rg-demo \
  --vm-name ubuntu-new-vm \
  --name restoredDisk
```

---

### 🧠 **Real DevOps Usage**

- Backup before patching or upgrades
- Rollback quickly if deployment fails
- Clone environments for testing

---

# 🧱 **3️⃣ Availability Sets**

### 📘 **What is an Availability Set?**

An **Availability Set** ensures your VMs are **spread across multiple fault domains (FD)** and **update domains (UD)**.

This provides **high availability within a single Azure region**.

---

### 🧩 **How It Works**

| Concept                | Description                                                                    |
| ---------------------- | ------------------------------------------------------------------------------ |
| **Fault Domain (FD)**  | Different physical racks/power units (protects from hardware failures)         |
| **Update Domain (UD)** | Logical groups Azure uses to apply OS updates (so not all VMs reboot together) |

---

### ⚙️ **Create Availability Set (CLI)**

```bash
az availability-set create \
  --resource-group rg-demo \
  --name app-avset \
  --platform-fault-domain-count 2 \
  --platform-update-domain-count 5 \
  --sku Aligned
```

---

### 🖥️ **Create VMs in the Availability Set**

```bash
az vm create \
  --resource-group rg-demo \
  --name web01 \
  --availability-set app-avset \
  --image Ubuntu2204 \
  --admin-username azureuser \
  --generate-ssh-keys

az vm create \
  --resource-group rg-demo \
  --name web02 \
  --availability-set app-avset \
  --image Ubuntu2204 \
  --admin-username azureuser \
  --generate-ssh-keys
```

Now Azure will place each VM in different fault/update domains.

---

### 🧠 **Real DevOps Usage**

- High availability for **web/app tier**
- Combine with **Load Balancer**
- Use in **HA cluster setups (2+ VMs)**

---

# 🌍 **4️⃣ Availability Zones**

### 📘 **What are Availability Zones?**

Availability Zones are **physically separate data centers within a region**.
They give you protection from **data center-level failures**.

> Each Azure region has **Zone 1**, **Zone 2**, **Zone 3**.

---

### ⚙️ **Deploy VMs in Availability Zones**

```bash
az vm create \
  --resource-group rg-demo \
  --name web01-zone1 \
  --zone 1 \
  --image Ubuntu2204 \
  --admin-username azureuser \
  --generate-ssh-keys

az vm create \
  --resource-group rg-demo \
  --name web02-zone2 \
  --zone 2 \
  --image Ubuntu2204 \
  --admin-username azureuser \
  --generate-ssh-keys
```

---

### 🧠 **Real DevOps Usage**

- Multi-zone deployments for **99.99% SLA**
- Geo-resilient web applications
- Combine with **Load Balancer** → distributes across zones
- Use **Zone-redundant storage (ZRS)** for data replication

---

# 🔄 **Availability Sets vs Availability Zones**

| Feature         | Availability Set       | Availability Zone          |
| --------------- | ---------------------- | -------------------------- |
| Scope           | Single datacenter      | Multiple datacenters       |
| Fault Isolation | Within a datacenter    | Across datacenters         |
| Redundancy Type | Logical                | Physical                   |
| SLA             | 99.95%                 | 99.99%                     |
| Cost            | No extra cost          | May increase network costs |
| Best For        | Basic HA within region | Mission-critical workloads |

---

# 🧠 **Real-World Example (DevOps Scenario)**

You’re deploying a production web app:

- You use **Availability Zones** for VMs (Zone 1 & Zone 2)
- Use **Managed Disks (Premium SSDs)** for each VM
- Automate **Snapshots** before patching or releases
- Group VMs into an **Availability Set** (if staying within one zone)
- Use an **Azure Load Balancer** in front

---

# 🔧 **Bicep Example (VM + Managed Disk + Availability Set)**

```bicep
param location string = resourceGroup().location
param adminUsername string = 'azureuser'
param sshPublicKey string

resource avset 'Microsoft.Compute/availabilitySets@2021-11-01' = {
  name: 'app-avset'
  location: location
  properties: {
    platformFaultDomainCount: 2
    platformUpdateDomainCount: 5
  }
}

resource vm 'Microsoft.Compute/virtualMachines@2021-11-01' = {
  name: 'web01'
  location: location
  properties: {
    availabilitySet: {
      id: avset.id
    }
    hardwareProfile: {
      vmSize: 'Standard_B1s'
    }
    osProfile: {
      computerName: 'web01'
      adminUsername: adminUsername
      linuxConfiguration: {
        disablePasswordAuthentication: true
        ssh: {
          publicKeys: [
            {
              path: '/home/${adminUsername}/.ssh/authorized_keys'
              keyData: sshPublicKey
            }
          ]
        }
      }
    }
    storageProfile: {
      imageReference: {
        publisher: 'Canonical'
        offer: '0001-com-ubuntu-server-jammy'
        sku: '22_04-lts-gen2'
        version: 'latest'
      }
      osDisk: {
        createOption: 'FromImage'
        managedDisk: {
          storageAccountType: 'Premium_LRS'
        }
      }
    }
  }
}
```

---

# 🧭 **Summary Cheat Sheet**

| Feature               | Purpose                   | CLI Example                  | Best Practice                         |
| --------------------- | ------------------------- | ---------------------------- | ------------------------------------- |
| **Managed Disk**      | Azure-managed VM disk     | `az disk create`             | Use Premium SSD for production        |
| **Snapshot**          | Point-in-time disk backup | `az snapshot create`         | Automate before updates               |
| **Availability Set**  | Logical redundancy        | `az availability-set create` | Use for 2+ VMs in same region         |
| **Availability Zone** | Physical redundancy       | `az vm create --zone 1`      | Use for critical production workloads |

---

