# 01. A full app on one EC2 instance (Linux and Windows)

Class: Mon Oct 19 (Linux) and Tue Oct 20 (Windows), Binary Cloud. The tutor built a dynamic app (UI + API + database) on a single AWS EC2 instance, first on Linux, then on Windows.

---

## 1. Intuition

One virtual machine does every job a small web app needs:

```
Browser --HTTP :80--> [ EC2 instance ]
                        |- web server   serves the UI (HTML/JS/CSS) and forwards /api to the API
                        |- API (Python) business logic, talks to the database
                        |- MySQL        stores the data, reachable only from inside the box
```

It is the "hello world" of cloud architecture: the same three tiers you build every day as a full stack engineer (frontend, backend, database), just all living on one rented server instead of your laptop.

- **Good for:** learning, demos, prototypes, internal tools with a handful of users.
- **Not good for:** anything that must stay up or grow. One box is one point of failure.

---

## 2. Linux version (Oct 19)

Steps, with Ubuntu commands (on Amazon Linux 2023 use `dnf` instead of `apt`; it ships MariaDB rather than MySQL in the default repos).

1. **Launch the instance:** EC2 -> Launch instance -> Ubuntu AMI, `t2.micro` / `t3.micro` (free-tier eligible; verify on the live page), create or pick a **key pair** (`.pem`).
2. **Security group (the instance's firewall):**
   - `22` (SSH) from **My IP** only
   - `80` (HTTP), and `443` if using HTTPS, from anywhere
   - **not** `3306`: MySQL stays private
3. **Connect:** `ssh -i key.pem ubuntu@<public-ip>` (on Windows, `chmod` is not available; restrict the key file's permissions in its Properties if SSH complains).
4. **Web server:**
   ```bash
   sudo apt update
   sudo apt install -y nginx
   ```
   Put the UI files in `/var/www/html`.
5. **API (Python):**
   ```bash
   sudo apt install -y python3 python3-venv python3-pip
   python3 -m venv venv && source venv/bin/activate
   pip install flask pymysql gunicorn        # framework used in class may differ
   gunicorn -b 127.0.0.1:5000 app:app       # bind to localhost: only nginx talks to it
   ```
6. **Database:**
   ```bash
   sudo apt install -y mysql-server
   sudo mysql_secure_installation
   ```
   Create a database and an app user with only the rights it needs (not `root`).
7. **Wire nginx to the API** (`/etc/nginx/sites-available/default`):
   ```nginx
   server {
       listen 80;
       root /var/www/html;
       location /api/ {
           proxy_pass http://127.0.0.1:5000/;   # trailing "/" strips the /api/ prefix
       }
   }
   ```
   `sudo nginx -t && sudo systemctl reload nginx`
8. **Keep the API running** after logout: a `systemd` service for gunicorn (`Restart=always`), not a terminal left open.

**Reverse proxy:** nginx is the only thing the internet talks to. It serves static files itself and forwards API calls to the Python process on `127.0.0.1`. The UI and API share one origin (same host and port), so no CORS setup is needed.

---

## 3. Windows version (Oct 20)

Same app, same three tiers, different tools. Full step-by-step walkthrough: `02_single_instance_windows.md`.

1. **Launch:** a **Windows Server** AMI. Bigger and pricier than Linux: the Windows licence is in the hourly rate.
2. **Security group:** `3389` (RDP) from **My IP** only, `80`/`443` from anywhere, no `3306`.
3. **Connect:** EC2 -> instance -> Connect -> RDP client -> **Get password** (upload the private key to decrypt it). User: `Administrator`. Open with Remote Desktop (`mstsc`).
4. **Web server:** usually **IIS**, the native Windows web server:
   ```powershell
   Install-WindowsFeature -Name Web-Server -IncludeManagementTools
   ```
   (nginx also has a Windows build; IIS is what enterprise Windows shops use.)
5. **Python and MySQL:** installed from their Windows installers (or `winget` / `choco`).
6. **Two firewalls:** the AWS security group **and** Windows Defender Firewall on the machine. A port must be open in both. Example:
   ```powershell
   New-NetFirewallRule -DisplayName "HTTP 80" -Direction Inbound -Protocol TCP -LocalPort 80 -Action Allow
   ```

| | Linux | Windows |
|---|---|---|
| Remote access | SSH, port 22, key file | RDP, port 3389, password decrypted with the key |
| Web server | nginx | IIS (or nginx for Windows) |
| Installs | `apt` / `dnf` | Installers, `winget`, `choco`, Server Manager roles |
| Service manager | `systemd` | Windows Services (e.g. NSSM to wrap a Python app) |
| Firewalls | Security group (+ `ufw` if enabled) | Security group + Windows Defender Firewall |
| Cost | Lower | Higher (licence included) |

---

## 4. Gotchas

- **Never open 3306 (or any DB port) to `0.0.0.0/0`.** Bots scan for open MySQL ports within minutes.
- **SSH / RDP from "My IP" only.** Open RDP to the world is one of the most attacked ports on the internet.
- **"It works on the box but not from my browser":** check in order: the app listens on the right address (`0.0.0.0` vs `127.0.0.1`), the OS firewall, the security group, the public IP (it changes after a stop/start unless you use an Elastic IP).
- **Public IP changes** on every stop/start. Elastic IPs keep it, and AWS charges for public IPv4 addresses (verify on the live page).
- **Stop or terminate after class.** A stopped instance still bills for its EBS disk; terminate to stop all charges.
- **No secrets in code:** DB password in an environment variable or config file outside git (later: AWS Secrets Manager / SSM Parameter Store).
- **Data dies with the box** unless the EBS volume is kept or backed up (snapshots).

---

## 5. From one box to production (where this course will likely go)

| Problem with one box | Fix |
|---|---|
| DB and app compete for CPU/RAM; DB lost if the box dies | Move the DB to **RDS** (managed MySQL: backups, patching, Multi-AZ) |
| One app server = single point of failure, cannot scale | Make the app **stateless**, run several copies behind an **Application Load Balancer**, in an **Auto Scaling group** |
| Static UI served by the app server | **S3 + CloudFront** for the UI |
| Manual installs, snowflake server | Bake an **AMI**, or use user data scripts / containers / IaC (Terraform, CloudFormation) |

First thing to split out when traffic grows: usually the **database**, because it holds the state; once the app tier is stateless, it can scale horizontally.

---

## 6. Cross-cloud names

| Concept | AWS | GCP | Azure |
|---|---|---|---|
| Virtual machine | EC2 | Compute Engine | Virtual Machines |
| Instance firewall | Security group | VPC firewall rules | Network security group (NSG) |
| Managed MySQL | RDS | Cloud SQL | Azure Database for MySQL |
| Load balancer (HTTP) | Application Load Balancer | Cloud Load Balancing | Application Gateway |
| Auto scaling | Auto Scaling group | Managed instance group | VM Scale Sets |
| Machine image | AMI | Image | Managed image |

---

## 7. FDE angle

- Client environments vary: many enterprises are Windows-heavy (IIS, .NET, SQL Server, Active Directory). Being able to deploy and debug on both Linux and Windows on day one is a real advantage on-site.
- The single-box setup is often what a client already has. The FDE skill is to see its risks quickly (open ports, no backups, single point of failure) and propose the next step without breaking what works.
- Debug order under pressure: process -> OS firewall -> security group -> IP/DNS. Saying it out loud calms a client call.

---

## 8. Self-test

1. Why does the API bind to `127.0.0.1:5000` instead of `0.0.0.0:5000` when nginx is in front?
2. Which ports go in the security group for the Linux box, and from where? For the Windows box?
3. The Windows app works inside RDP but not from your laptop. Name the two firewalls to check.
4. Traffic grows 10x. What do you split out first, and why?
5. Why does the UI calling `/api/...` through nginx not need CORS?
6. What still costs money after you stop the instance?
