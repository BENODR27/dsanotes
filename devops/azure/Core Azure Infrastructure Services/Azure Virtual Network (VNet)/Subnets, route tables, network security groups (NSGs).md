Perfect — now we’re diving into **Azure networking fundamentals**, which are **critical for a Senior Azure DevOps Engineer**, especially when designing secure, scalable, and highly available infrastructures. Let’s break it down **step by step** for **VNets, Subnets, Route Tables, and NSGs**, including **real-world usage and IaC examples**.

---

# 🧱 **Azure Virtual Network (VNet)**

### 🔹 **What is a VNet?**

- A **logical representation of a network in Azure**.
- Enables **VMs and resources to communicate privately**.
- Acts like your on-prem **LAN in the cloud**, fully isolated unless connected via **VPN/ExpressRoute** or **VNet Peering**.

---

### 🔹 **Key Features**

- **IP address space**: Define a private range (CIDR) e.g., `10.0.0.0/16`.
- **Subnets**: Logical segmentation within a VNet.
- **Network Security**: Control traffic using **NSGs**.
- **Routing**: Customize traffic flow with **route tables**.
- **Connectivity**: VNet peering, VPN, ExpressRoute.

---

# ⚙️ **1️⃣ Subnets**

### 🔹 **What Are Subnets?**

- Subdivision of a VNet’s IP address space.
- Organize **resources by function** (Web, App, DB).
- Each subnet can have **its own NSGs and route tables**.

---

### 🔹 **Example**

VNet: `10.0.0.0/16`

| Subnet Name   | Address Range | Purpose                                             |
| ------------- | ------------- | --------------------------------------------------- |
| `10.0.1.0/24` | WebSubnet     | Frontend VMs / App Gateway                          |
| `10.0.2.0/24` | AppSubnet     | Application servers                                 |
| `10.0.3.0/24` | DBSubnet      | Databases / Storage                                 |
| `10.0.4.0/24` | GatewaySubnet | VPN / Bastion hosts (must be named `GatewaySubnet`) |

> **Note**: Azure reserves the first 4 and last IPs of each subnet.

---

### 🔹 **CLI Example: Create VNet + Subnets**

```bash
# Create VNet
az network vnet create \
  --name myVNet \
  --resource-group rg-network \
  --address-prefix 10.0.0.0/16 \
  --location eastus

# Create Subnets
az network vnet subnet create \
  --vnet-name myVNet \
  --resource-group rg-network \
  --name WebSubnet \
  --address-prefix 10.0.1.0/24

az network vnet subnet create \
  --vnet-name myVNet \
  --resource-group rg-network \
  --name AppSubnet \
  --address-prefix 10.0.2.0/24
```

---

# ⚙️ **2️⃣ Network Security Groups (NSGs)**

### 🔹 **What Are NSGs?**

- NSGs **filter inbound/outbound traffic** to subnets or VMs.
- Rules are **stateful** (return traffic is allowed automatically).
- Each rule has **priority (100–4096)**; lowest wins.

---

### 🔹 **Example NSG Rules**

| Priority | Direction | Protocol | Source | Destination | Port | Action |
| -------- | --------- | -------- | ------ | ----------- | ---- | ------ |
| 100      | Inbound   | TCP      | Any    | 10.0.1.0/24 | 80   | Allow  |
| 110      | Inbound   | TCP      | Any    | 10.0.1.0/24 | 443  | Allow  |
| 200      | Inbound   | TCP      | Any    | 10.0.0.0/16 | 3389 | Deny   |
| 1000     | Outbound  | Any      | Any    | Any         | \*   | Allow  |

---

### 🔹 **CLI Example: Create NSG and Rules**

```bash
# Create NSG
az network nsg create \
  --resource-group rg-network \
  --name WebNSG

# Create Inbound Rule (Allow HTTP)
az network nsg rule create \
  --resource-group rg-network \
  --nsg-name WebNSG \
  --name Allow-HTTP \
  --priority 100 \
  --direction Inbound \
  --access Allow \
  --protocol Tcp \
  --destination-port-ranges 80

# Associate NSG to Subnet
az network vnet subnet update \
  --vnet-name myVNet \
  --name WebSubnet \
  --resource-group rg-network \
  --network-security-group WebNSG
```

---

# ⚙️ **3️⃣ Route Tables**

### 🔹 **What Are Route Tables?**

- Control **traffic routing inside or outside the VNet**.
- Can override **default system routes**.
- Associated to **subnets**.

---

### 🔹 **Example**

- Default system routes already exist for:

  - Local VNet communication
  - Internet traffic (0.0.0.0/0)

- Custom route example: Direct `10.0.3.0/24` traffic through **Network Virtual Appliance (NVA)**:

```bash
az network route-table create \
  --name AppRouteTable \
  --resource-group rg-network

az network route-table route create \
  --name RouteToNVA \
  --resource-group rg-network \
  --route-table-name AppRouteTable \
  --address-prefix 10.0.3.0/24 \
  --next-hop-type VirtualAppliance \
  --next-hop-ip-address 10.0.2.4

# Associate route table to AppSubnet
az network vnet subnet update \
  --vnet-name myVNet \
  --name AppSubnet \
  --resource-group rg-network \
  --route-table AppRouteTable
```

---

# 🧠 **4️⃣ Real-World DevOps Use Cases**

| Scenario                | Implementation                                                      |
| ----------------------- | ------------------------------------------------------------------- |
| Secure Web/App/DB Tiers | Separate subnets for Web/App/DB with NSGs per subnet                |
| Bastion Host            | Dedicated subnet `AzureBastionSubnet` + NSG allows only 443 inbound |
| Hybrid Connectivity     | Custom route table to direct traffic through VPN/ExpressRoute       |
| Compliance              | NSGs enforce least privilege access for VMs/services                |
| VMSS Security           | Associate NSG to VMSS subnet instead of each VM individually        |

---

# ⚙️ **5️⃣ Bicep Example: VNet + Subnet + NSG + Route Table**

```bicep
param location string = resourceGroup().location

resource vnet 'Microsoft.Network/virtualNetworks@2023-05-01' = {
  name: 'myVNet'
  location: location
  properties: {
    addressSpace: { addressPrefixes: ['10.0.0.0/16'] }
    subnets: [
      {
        name: 'WebSubnet'
        properties: { addressPrefix: '10.0.1.0/24' }
      }
      {
        name: 'AppSubnet'
        properties: { addressPrefix: '10.0.2.0/24' }
      }
    ]
  }
}

resource nsg 'Microsoft.Network/networkSecurityGroups@2023-05-01' = {
  name: 'WebNSG'
  location: location
  properties: {
    securityRules: [
      {
        name: 'AllowHTTP'
        properties: {
          priority: 100
          direction: 'Inbound'
          access: 'Allow'
          protocol: 'Tcp'
          sourcePortRange: '*'
          destinationPortRange: '80'
          sourceAddressPrefix: '*'
          destinationAddressPrefix: '*'
        }
      }
    ]
  }
}

resource subnetNsg 'Microsoft.Network/virtualNetworks/subnets@2023-05-01' = {
  parent: vnet
  name: 'WebSubnet'
  properties: {
    addressPrefix: '10.0.1.0/24'
    networkSecurityGroup: {
      id: nsg.id
    }
  }
}
```

---

# ✅ **Summary Cheat Sheet**

| Concept         | Purpose                  | DevOps Use Case                      |
| --------------- | ------------------------ | ------------------------------------ |
| **VNet**        | Isolated Azure network   | Multi-tier apps, hybrid connectivity |
| **Subnet**      | Segmentation within VNet | Web/App/DB tiers, Bastion subnet     |
| **NSG**         | Traffic filtering        | Firewall rules, security compliance  |
| **Route Table** | Control traffic routing  | Custom paths, NVA, VPN, ExpressRoute |

---

