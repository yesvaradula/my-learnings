# 01. Creating a GCP Account

**Goal:** go from nothing to a safe, billed-but-capped-by-habit GCP setup: Google account → billing account → first project → budget alert → `gcloud` CLI working.
**Time:** ~30 minutes. **Need:** a Google account, a phone for OTP, a debit/credit card (or the payment method your region accepts).

> Prices, credit amounts and trial length change. Treat the numbers here as "what it looked like when I wrote this" and confirm on the signup page.

---

## 1. Intuition first: what are you actually creating?

You don't create "a GCP account". You create **four separate things**, and confusing them is the most common beginner mistake:

| Thing | What it is | AWS / Azure equivalent |
|-------|-----------|------------------------|
| **Google identity** | Your login (`you@gmail.com` or Workspace/Cloud Identity). Proves who you are. | AWS root email / Entra ID user |
| **Billing account** | The payment method + invoices. Who pays. | AWS account's payer / Azure subscription billing |
| **Project** | The container for resources, APIs, IAM and quotas. What you build in. | AWS account / Azure subscription + resource group |
| **Resources** | VMs, buckets, databases, etc. live inside a project. | Same everywhere |

Key mental model: **a project is the unit of isolation, permissions, and cost tracking.** A billing account pays for one or many projects. Delete a project and everything in it goes with it (after a grace period).

```
Google identity (you)
        │  owns / administers
        ▼
Billing account  ──pays for──►  Project A (learning-sandbox)
                          └──►  Project B (client-demo-acme)
                                      │
                                      ▼
                          Compute Engine VM, GCS bucket, Cloud Run service ...
```

Real hierarchy at company scale: `Organization → Folders → Projects → Resources`. As an individual you won't have an Organization (that needs Google Workspace or Cloud Identity), and that is fine for learning.

---

## 2. Step by step

### Step 1: Use a Google account you control long-term
- Prefer a **dedicated learning Gmail** or your main personal one. Avoid your employer's Workspace account: admins can revoke it, and company policy may apply.
- Turn on **2-Step Verification** *before* you add a card. Use an authenticator app or passkey, not just SMS.

### Step 2: Open the console and start the free trial
1. Go to **https://console.cloud.google.com** and sign in.
2. Accept the terms. Click **"Try for free"** / **"Activate"** (the banner at the top).
3. Choose **country** (this decides currency, tax and payment options; hard to change later) and account type: **Individual** for learning, **Business** if you need GST/invoices in a company name.

### Step 3: Billing profile and card verification
1. Enter name, address, and **payment method**.
2. Google does a **small authorization/verification charge** to check the card is real. It is reverted. In India, the bank may ask for OTP / e-mandate approval; if the card fails, check that it is enabled for **international/online recurring** payments.
3. For Business accounts in India, add the **GSTIN** so invoices are tax-compliant.

### Step 4: Understand what the trial gives you
- **Free trial credits:** a fixed credit (historically **$300**) valid for a limited time (historically **90 days**). When credits or time run out, resources **stop**; you are **not charged** unless you manually **upgrade** to a paid account.
- **Always Free tier:** separate monthly free usage of some products (small `e2-micro` VM in selected regions, some Cloud Storage, Cloud Run requests, BigQuery queries, etc.). It survives after the trial, within limits.
- Trial restrictions: some things (e.g. GPUs, high quotas, Windows server licences) may be blocked until you upgrade.

> **Trial ≠ Free tier ≠ Upgrade.** Trial = temporary credits. Free tier = permanent small allowance. Upgrade = you now pay real money for anything above the free tier.

### Step 5: Create your first project properly
The signup usually gives you "My First Project". Don't live in it.
1. Top bar → project picker → **New Project**.
2. **Name:** `learning-sandbox` (human-friendly, editable).
3. **Project ID:** auto-generated and **globally unique and permanent**. Choose something sensible like `sai-learning-sandbox-01`. You can never rename it.
4. Link it to your billing account.

Convention that pays off later: one project per purpose (`learning-sandbox`, `fde-demo-rag`, `cert-practice`), not one giant project. Deleting a single-purpose project is a clean way to stop all spending.

### Step 6: Set a budget and alerts (do this today)
Billing → **Budgets & alerts** → **Create budget**.
- Scope: your billing account (or a single project).
- Amount: something small, e.g. **₹500 or $5**.
- Alerts at **50%, 90%, 100%**, plus email to yourself.

**Gotcha:** a budget **alerts**; it does **not stop** spending. Hard caps need automation (a Pub/Sub notification → Cloud Function that disables billing on the project). We'll build that in a later note.

### Step 7: Lock down the basics
- IAM → check that your user has **Owner** on the project (you will, as the creator).
- Don't create **service-account keys** (JSON files) unless you must; they are the classic leak. Prefer `gcloud auth` and short-lived credentials.
- Never commit anything from `~/.config/gcloud` or a key file into git.

### Step 8: Install and authenticate the `gcloud` CLI
1. Install the Google Cloud SDK for Windows: https://cloud.google.com/sdk/docs/install
2. Then:

```powershell
gcloud init                          # login in browser, pick project, default region
gcloud auth list                     # who am I?
gcloud config set project sai-learning-sandbox-01
gcloud config set compute/region asia-south1   # Mumbai; asia-south2 = Delhi
gcloud config list                   # confirm
gcloud projects list
```

Region tip: **`asia-south1` (Mumbai)** is closest from Hyderabad: lowest latency, and what Indian clients usually need for data-residency.

### Step 9: Cloud Shell (no install alternative)
The `>_` icon in the console opens **Cloud Shell**: a free, pre-authenticated Linux terminal with `gcloud`, `kubectl`, `terraform`, an editor, and 5 GB persistent home. Great for quick experiments.

---

## 3. Verify it worked (checklist)

- [ ] I can sign in at console.cloud.google.com and see my project.
- [ ] Billing account is linked to the project (Billing → My projects).
- [ ] A budget with email alerts exists.
- [ ] 2-Step Verification is on for the Google account.
- [ ] `gcloud auth list` shows my account; `gcloud config list` shows the right project + region.
- [ ] I can say, without looking, the difference between *trial*, *free tier* and *upgrade*.

---

## 4. Real-world gotchas

1. **Forgotten resources are the #1 bill.** A VM, load balancer, static IP, or Cloud SQL instance left running keeps charging. Habit: at the end of every session, `gcloud compute instances list` and delete what you don't need.
2. **Static external IPs cost money when unattached.** Easy to miss.
3. **Enabling an API ≠ paying**, but *using* it can. APIs are enabled per project (`gcloud services enable run.googleapis.com`).
4. **Project ID is forever.** Name it well the first time.
5. **Deleting a project** has a ~30-day recovery window, and billing stops for resources, but check the billing report to be sure.
6. **Region matters for price and latency.** `us-central1` is often cheapest; `asia-south1` is closest to you.
7. **Free trial can be used once per person/card.** Don't burn it on random clicking. Have a plan (see below).
8. **Account suspended?** Usually a payment verification or a suspected-abuse flag (crypto mining on stolen keys). Keep keys out of repos.

---

## 5. How this connects to AWS / Azure (cross-cloud cheat sheet)

| Concept | GCP | AWS | Azure |
|---------|-----|-----|-------|
| Isolation unit | Project | Account | Subscription (+ resource group) |
| Org hierarchy | Org → Folder → Project | Organization → OU → Account | Management group → Subscription |
| CLI | `gcloud` | `aws` | `az` |
| Browser terminal | Cloud Shell | CloudShell | Cloud Shell |
| Budget alerts | Budgets & alerts | AWS Budgets | Cost Management budgets |
| Free offering | $300 trial + Always Free | 12-month free tier + always free | $200 trial + free services |

**Why it matters for the FDE path:** on a client engagement you will usually be dropped into *their* org with a scoped project and a role. Knowing the project/billing/IAM model cold means you can ask "which project, which billing account, which role, which region?" in the first 10 minutes, and that is exactly the speed a Forward Deployed Engineer is judged on.

---

## 6. Plan for the trial credits (don't waste them)

Use the credits on things worth learning, in this order:
1. **Cloud Run:** deploy a Node.js container (ties to your full-stack background).
2. **Cloud Storage + CDN:** host a React build.
3. **Cloud SQL / Firestore:** a real backend for it.
4. **IAM + service accounts + Workload Identity:** the security model clients ask about.
5. **Vertex AI / Gemini API:** pairs with your Coding Ninjas RAG & agents classes.

---

## 7. Next notes
- `02_iam.md`: identities, roles, service accounts, least privilege.
- `03_compute.md`: Compute Engine vs Cloud Run vs GKE.
- `04_billing_guardrails.md`: hard budget cap via Pub/Sub + Cloud Function.

## Self-test (answer out loud)
1. Why can't you rename a project ID, and why does that make naming matter?
2. If the free credits run out, are you charged automatically? Why or why not?
3. Does a budget stop spending? What would?
4. You are handed a client's GCP project: what four things do you ask first?
