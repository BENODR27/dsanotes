Perfect — now you’re stepping into **advanced compute management** in Azure:
✅ **Custom VM Images** (for standardized environments)
✅ **Virtual Machine Scale Sets (VMSS)** (for auto-scaling workloads)

These are key skills for a **Senior Azure DevOps Engineer** to automate deployments, scaling, and resilience.

Let’s go deep 👇

---

# ⚙️ **1️⃣ Custom VM Images**

### 📘 **What Are Custom VM Images?**

A **Custom Image** is a reusable VM template you create from an existing configured VM.
Instead of installing tools (like Docker, Nginx, or agents) each time, you capture that configuration once and reuse it to create new VMs.

Think of it like a “**Golden Image**” — your standard OS + configurations.

---

## 🧩 **Why Use Custom Images**

✅ Consistency — every VM starts the same
✅ Faster provisioning — no need for post-install setup
✅ Easier automation — integrate into CI/CD or VMSS
✅ Useful for base environments (Dev, Test, Prod)

---

## 🧱 **Ways to Create Custom Images**

| Method                        | Description            | Use Case                     |
| ----------------------------- | ---------------------- | ---------------------------- |
| **Azure Portal**              | Manual capture         | Small environments           |
| **Azure CLI**                 | Automated capture      | DevOps automation            |
| **Packer**                    | Template-driven builds | Enterprise IaC               |
| **Azure Image Builder (AIB)** | Managed Packer service | Best practice for production |

---

## ⚙️ **A. Create Custom Image from Existing VM (CLI)**

### **Step 1: Prepare VM**

* Remove temporary files and credentials.
* Deprovision the VM:

  ```bash
  sudo waagent -deprovision+user
  ```
* Stop the VM:

  ```bash
  az vm deallocate -g rg-demo -n ubuntu-base
  ```

---

### **Step 2: Generalize the VM**

```bash
az vm generalize -g rg-demo -n ubuntu-base
```

---

### **Step 3: Capture Image**

```bash
az image create \
  --resource-group rg-demo \
  --name ubuntu-custom-image \
  --source ubuntu-base
```

---

### **Step 4: Create New VM from the Custom Image**

```bash
az vm create \
  --resource-group rg-demo \
  --name app-server-01 \
  --image ubuntu-custom-image \
  --admin-username azureuser \
  --generate-ssh-keys
```

---

## ⚙️ **B. Build Image with Packer (Recommended for DevOps)**

Example **Packer template (ubuntu.json):**

```json
{
  "builders": [{
    "type": "azure-arm",
    "client_id": "<service_principal_id>",
    "client_secret": "<service_principal_secret>",
    "tenant_id": "<tenant_id>",
    "subscription_id": "<subscription_id>",
    "managed_image_resource_group_name": "rg-demo",
    "managed_image_name": "packer-ubuntu-image",
    "os_type": "Linux",
    "image_publisher": "Canonical",
    "image_offer": "0001-com-ubuntu-server-jammy",
    "image_sku": "22_04-lts-gen2",
    "location": "East US",
    "vm_size": "Standard_B1s"
  }],
  "provisioners": [{
    "type": "shell",
    "inline": [
      "sudo apt update -y",
      "sudo apt install -y nginx docker.io"
    ]
  }]
}
```

Run:

```bash
packer build ubuntu.json
```

✅ Output: a **custom image** with Nginx + Docker preinstalled.

---

## 🧠 **Real DevOps Use Case**

* Standardize base image for app servers
* Include monitoring agents (Datadog, Prometheus)
* Pre-install Docker, Git, Node.js, etc.
* Use same image across environments (Dev/Test/Prod)
* Integrate with **Azure DevOps pipeline** (build → capture → deploy)

---

# ⚙️ **2️⃣ Virtual Machine Scale Sets (VMSS)**

### 📘 **What Is a Scale Set?**

A **VM Scale Set (VMSS)** is a **group of identical, load-balanced VMs** that automatically scale in/out based on demand.

---

## 🧩 **Benefits**

✅ Auto-scaling — adds/removes VMs automatically
✅ Integrated Load Balancer & Health probes
✅ Works with custom or marketplace images
✅ Rolling upgrades with zero downtime
✅ Perfect for stateless workloads (web apps, APIs)

---

## ⚙️ **Create a Basic VMSS (CLI)**

```bash
az vmss create \
  --resource-group rg-demo \
  --name web-scale-set \
  --image Ubuntu2204 \
  --upgrade-policy-mode automatic \
  --admin-username azureuser \
  --generate-ssh-keys \
  --instance-count 2 \
  --vm-sku Standard_B1s \
  --load-balancer "" \
  --public-ip-per-vm
```

This creates **2 identical Ubuntu VMs**, managed as a single scale set.

---

## ⚙️ **Add Auto-Scaling Rules**

Example: scale based on CPU usage

```bash
az monitor autoscale create \
  --resource-group rg-demo \
  --resource web-scale-set \
  --resource-type Microsoft.Compute/virtualMachineScaleSets \
  --name autoscale-rule \
  --min-count 2 \
  --max-count 5 \
  --count 2

az monitor autoscale rule create \
  --resource-group rg-demo \
  --autoscale-name autoscale-rule \
  --condition "Percentage CPU > 70 avg 5m" \
  --scale out 1

az monitor autoscale rule create \
  --resource-group rg-demo \
  --autoscale-name autoscale-rule \
  --condition "Percentage CPU < 30 avg 10m" \
  --scale in 1
```

---

## ⚙️ **Create VMSS with a Custom Image**

```bash
az vmss create \
  --resource-group rg-demo \
  --name web-scale-set \
  --image ubuntu-custom-image \
  --admin-username azureuser \
  --generate-ssh-keys \
  --instance-count 2 \
  --upgrade-policy-mode automatic
```

This deploys your **golden image** across multiple nodes automatically.

---

## ⚙️ **Update Scale Set Configuration**

Add more instances manually:

```bash
az vmss scale \
  --name web-scale-set \
  --resource-group rg-demo \
  --new-capacity 4
```

---

## 🧠 **Real DevOps Use Cases**

* Auto-scale web servers under high traffic
* Zero-downtime deployments using **rolling upgrades**
* Combine with **Application Gateway or Load Balancer**
* Blue-Green or Canary deployments
* Integration with **Terraform or Azure DevOps Pipelines**

---

## 🧱 **VMSS + Load Balancer Example Architecture**

```
          ┌────────────────────────────┐
          │        Azure LB            │
          │    (frontend IP + rules)   │
          └────────────┬───────────────┘
                       │
        ┌──────────────┴───────────────┐
        │         VM Scale Set         │
        │   VM1 (Zone 1)  VM2 (Zone 2) │
        │   VM3 (Zone 3)  VM4 (Zone 1) │
        └──────────────┬───────────────┘
                       │
              Azure Managed Disks
```

---

## ⚙️ **Terraform Example: VMSS using Custom Image**

```hcl
resource "azurerm_image" "custom" {
  name                = "ubuntu-custom"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  source_virtual_machine_id = azurerm_virtual_machine.base.id
}

resource "azurerm_linux_virtual_machine_scale_set" "vmss" {
  name                = "web-scale-set"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = "Standard_B1s"
  instances           = 2
  admin_username      = "azureuser"
  source_image_id     = azurerm_image.custom.id
  admin_ssh_key {
    username   = "azureuser"
    public_key = file("~/.ssh/id_rsa.pub")
  }
  upgrade_mode = "Rolling"
  automatic_instance_repair = true
}
```

---

# 🧭 **Quick Summary**

| Concept                       | Purpose                             | DevOps Use                           |
| ----------------------------- | ----------------------------------- | ------------------------------------ |
| **Custom Image**              | Pre-configured reusable VM template | Fast, consistent VM provisioning     |
| **VMSS**                      | Auto-scaling identical VMs          | Web/API servers, stateless workloads |
| **Azure Image Builder (AIB)** | Automated image creation pipeline   | Enterprise-grade custom images       |
| **Auto Scaling**              | Scale based on metrics              | Handle peak traffic cost-effectively |

---

ss