## **1. Azure AD Roles**

**Definition:**
An **Azure AD role** defines a set of **permissions** to perform specific tasks in Azure AD or Microsoft 365.

**Key points:**

- Roles **control access to directory resources**, like users, groups, apps, and devices.
- Azure AD has **built-in roles** and also allows **custom roles**.
- Roles can be **assigned at directory level**, not necessarily per subscription.

**Common built-in Azure AD roles:**

| Role                                 | Permissions / Use Case                                                                          |
| ------------------------------------ | ----------------------------------------------------------------------------------------------- |
| **Global Administrator**             | Full access to all Azure AD and Microsoft 365 services. Can manage roles, users, subscriptions. |
| **User Administrator**               | Can manage users and groups, reset passwords, assign some roles.                                |
| **Application Administrator**        | Can register/manage applications in Azure AD.                                                   |
| **Security Administrator**           | Manage security settings, review audit logs, and manage security-related tasks.                 |
| **Conditional Access Administrator** | Manage Conditional Access policies.                                                             |
| **Billing Administrator**            | Manage subscriptions and billing info.                                                          |

**Custom Roles:**

- You can create roles with **specific permissions** using the **least privilege principle**.
- Example: A custom role that can only reset passwords but cannot delete users.

---

## **2. Role Assignments**

**Definition:**
A **role assignment** links a **user, group, or service principal** to a **role** in a **specific scope**.

**Key points:**

- Assignment = **Who** + **Role** + **Scope**
- Scope can be:

  - **Directory level** (applies to all Azure AD resources)
  - **Administrative unit** (subset of directory objects)
  - For Azure RBAC (different from AAD roles), scope can be **subscription, resource group, or resource**.

**Example:**

- User Alice → Assigned **User Administrator** → Scope: Entire directory
- Group DevOps → Assigned **Application Administrator** → Scope: Specific admin unit

**Important:**

- A user can have **multiple role assignments**.
- **Effective permissions** = combination of all roles assigned.

---

### **Azure AD Roles vs Azure RBAC Roles**

| Feature    | Azure AD Roles                                      | Azure RBAC Roles                                      |
| ---------- | --------------------------------------------------- | ----------------------------------------------------- |
| Purpose    | Manage **Azure AD resources** (users, apps, groups) | Manage **Azure resources** (VMs, storage, network)    |
| Scope      | Directory / Admin units                             | Subscription / Resource group / Resource              |
| Examples   | Global Admin, Security Admin                        | Owner, Contributor, Reader                            |
| Assignment | Users, groups, service principals                   | Users, groups, service principals, managed identities |

---

### **Summary**

1. **Azure AD Roles** → Define **permissions** to manage Azure AD resources.
2. **Role Assignments** → Assign a role to a **principal** at a **specific scope**.
3. Always follow **least privilege**: assign only what’s needed.
