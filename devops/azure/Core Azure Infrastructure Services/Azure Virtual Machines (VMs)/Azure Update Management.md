Excellent 👏 — **Azure Update Management** is one of those critical DevOps and cloud operations tools that ensures **your servers (Linux + Windows)** are **secure, compliant, and up to date** with the latest patches — without manual intervention.

Let’s go through it **step-by-step** — from **concept to setup**, with **CLI, Portal, Automation Account, and real-world use cases** (exactly how senior DevOps engineers use it in production).

---

# 🧭 **What Is Azure Update Management?**

**Azure Update Management (AUM)** is a **service under Azure Automation** that lets you:

✅ Manage **Windows & Linux updates** across **Azure, on-prem, and hybrid VMs**
✅ Schedule **patch deployments** with reboot options
✅ Get **compliance reports** on update status
✅ Integrate with **Azure Arc** to manage non-Azure servers

---

## ⚙️ **Architecture Overview**

```
          ┌──────────────────────────────────────┐
          │          Azure Automation             │
          │     + Log Analytics Workspace         │
          └──────────────────┬───────────────────┘
                             │
                             │
          ┌──────────────────┴───────────────────┐
          │     Update Management Solution       │
          └──────────────────┬───────────────────┘
                             │
       ┌─────────────────────┼──────────────────────┐
       │                     │                      │
 ┌──────────────┐     ┌──────────────┐      ┌──────────────┐
 │ Azure VMs    │     │ Hybrid VMs   │      │ Arc VMs       │
 │ (Windows/Linux)│   │ On-premises │      │ AWS/GCP VMs   │
 └──────────────┘     └──────────────┘      └──────────────┘
```

---

# 🧩 **Core Components**

| Component                      | Description                                      |
| ------------------------------ | ------------------------------------------------ |
| **Azure Automation Account**   | Hosts Update Management, schedules, and runbooks |
| **Log Analytics Workspace**    | Stores update compliance data and logs           |
| **Update Management Solution** | Service that performs assessment and patching    |
| **Hybrid Runbook Worker**      | For on-prem or Arc-connected machines            |
| **Azure Arc**                  | Connects non-Azure VMs for unified management    |

---

# ⚙️ **1️⃣ Prerequisites**

- An **Azure Automation Account**
- A **Log Analytics Workspace**
- VMs (Azure, Arc, or on-prem) with **Monitoring Agent (AMA or MMA)** installed
- Appropriate **RBAC permissions** (Automation Contributor / Reader)

---

# 🧱 **2️⃣ Setup Azure Update Management (Portal)**

### ✅ **Step-by-Step**

1. Go to **Azure Portal → Automation Account → Update Management**
2. Click **Enable**
3. Choose a **Log Analytics Workspace**
4. Link the workspace to the **Automation Account**
5. Add VMs:

   - Azure VMs (auto-discovered)
   - On-prem or Arc VMs (via agent)

6. Create an **Update Deployment Schedule**:

   - OS type (Windows/Linux)
   - Schedule (time, recurrence)
   - Maintenance window
   - Pre/post scripts
   - Reboot options

---

# 💻 **3️⃣ Using Azure CLI**

```bash
# Create Resource Group
az group create -n rg-update -l eastus

# Create Log Analytics Workspace
az monitor log-analytics workspace create \
  -g rg-update \
  -n update-law

# Create Automation Account
az automation account create \
  -g rg-update \
  -n update-auto \
  --location eastus

# Link Automation Account and Log Analytics
az resource link create \
  --target $(az monitor log-analytics workspace show -g rg-update -n update-law --query id -o tsv) \
  --name "AutomationLink" \
  --properties "{ 'sourceId': '$(az automation account show -g rg-update -n update-auto --query id -o tsv)' }"
```

> 💡 Note: CLI doesn’t yet create update deployments directly — you use PowerShell or Portal for scheduling.

---

# ⚙️ **4️⃣ PowerShell Setup (Automation Script)**

```powershell
# Connect to Azure
Connect-AzAccount

# Variables
$rg = "rg-update"
$automationAccount = "update-auto"
$workspaceName = "update-law"

# Get Automation Account and Workspace
$autoAccount = Get-AzAutomationAccount -Name $automationAccount -ResourceGroupName $rg
$workspace = Get-AzOperationalInsightsWorkspace -Name $workspaceName -ResourceGroupName $rg

# Link Workspace to Automation Account
New-AzAutomationLinkWorkspace -ResourceGroupName $rg -AutomationAccountName $automationAccount -WorkspaceName $workspaceName
```

---

# 🧰 **5️⃣ Create Update Deployment (Portal)**

### Windows Example

1. **Azure Portal → Update Management → Schedule update deployment**
2. Select:

   - Machines (VMs or groups)
   - OS type: Windows
   - Update classifications (Security, Critical, etc.)
   - Schedule: date, time, recurrence
   - Maintenance window (e.g., 120 minutes)
   - Reboot option: `Always`, `If required`, `Never`

3. **Create**

### Linux Example

Similar steps, except:

- Update classifications: `Critical`, `Other updates`
- Uses native package managers (apt/yum)

---

# 🔍 **6️⃣ Monitor Update Compliance**

In **Azure Portal → Automation Account → Update Management**, you’ll see:

| Metric                | Description                      |
| --------------------- | -------------------------------- |
| **Missing Updates**   | Number of updates not installed  |
| **Compliance %**      | Percentage of updated systems    |
| **Deployment Status** | In progress / succeeded / failed |
| **Job Output Logs**   | Details of each patch operation  |

---

# 🧠 **7️⃣ Real-Time Scenarios (DevOps Use Cases)**

| Use Case                             | Description                                                    |
| ------------------------------------ | -------------------------------------------------------------- |
| **Patch Management Automation**      | Schedule OS patches during maintenance windows                 |
| **Zero-Downtime Blue/Green Updates** | Patch half of VMs at a time (using dynamic groups or tags)     |
| **Hybrid Cloud Update Strategy**     | Use Azure Arc to patch on-prem + AWS VMs                       |
| **Compliance Reporting**             | Export update status to Power BI or Sentinel                   |
| **Pipeline Integration**             | Trigger patch jobs in Azure DevOps/YAML pipelines via REST API |

---

# ⚙️ **8️⃣ Advanced Features**

| Feature                           | Description                                                        |
| --------------------------------- | ------------------------------------------------------------------ |
| **Dynamic Groups**                | Automatically target VMs using tags or queries                     |
| **Pre/Post Scripts**              | Run automation runbooks before/after updates (e.g., stop services) |
| **Non-Azure Machine Support**     | Patch any connected system via Arc                                 |
| **Reports & Alerts**              | Use Log Analytics queries + Alerts                                 |
| **Integration with Azure Policy** | Enforce that VMs are onboarded to Update Management                |

---

# 💾 **9️⃣ KQL Queries for Log Analytics**

```kql
# List of non-compliant VMs
UpdateSummary
| where OSType == "Windows"
| where MissingCriticalUpdatesCount > 0
| project Computer, MissingCriticalUpdatesCount, LastScanTime

# Update Compliance Over Time
UpdateSummary
| summarize avg(CompliancePercentage) by bin(TimeGenerated, 1d)
```

---

# 🔒 **10️⃣ Security & Governance Best Practices**

| Practice                                                     | Why It Matters                        |
| ------------------------------------------------------------ | ------------------------------------- |
| Use **Azure Policy** to enforce Update Management on all VMs | Ensures compliance                    |
| Use **Managed Identity** for Automation Account              | No credentials exposure               |
| Store logs in **Log Analytics + Sentinel**                   | Centralized monitoring                |
| Use **Role-based Access Control (RBAC)**                     | Limit patch scheduling rights         |
| Schedule during off-peak hours                               | Reduce impact on production workloads |

---

# ✅ **Summary**

| Feature           | Description                                       |
| ----------------- | ------------------------------------------------- |
| **Service Name**  | Azure Update Management                           |
| **Purpose**       | Automate patching across Azure & hybrid VMs       |
| **Dependencies**  | Automation Account + Log Analytics                |
| **Supported OS**  | Windows & Linux                                   |
| **Integration**   | Azure Arc, PowerShell, REST API, DevOps Pipelines |
| **Best Practice** | Dynamic groups + pre/post automation scripts      |

---

# 🚀 **Real-World Example: Full Automation Workflow**

1. **Terraform** deploys:

   - Resource Group
   - Log Analytics Workspace
   - Automation Account
   - Azure VMs

2. **Azure Policy** ensures all new VMs are onboarded to Update Management.

3. **Automation Account** runs a **pre-patch runbook** (backup snapshots, drain traffic).

4. **Azure Update Deployment** runs at midnight on weekends.

5. **Post-runbook** restarts services and re-registers the instance in load balancer.

6. **Log Analytics** dashboards + alerts monitor patch compliance.

---
