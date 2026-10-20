# 02. A full app on one EC2 Windows instance, step by step

Class: Tue Oct 20, Binary Cloud. Same app as `01_single_instance_app.md` (UI + Python API + MySQL on one instance), built on **Windows Server** instead of Linux. Read 01 first for the intuition and the "one box to production" path; this note is the Windows walkthrough.

---

## 1. Intuition

Same three tiers, Windows tools:

```
Browser --HTTP :80--> [ EC2 Windows Server ]
                        |- IIS        serves the UI from C:\inetpub\wwwroot, forwards /api to the API
                        |- Python API waitress on 127.0.0.1:5000 (gunicorn does not run on Windows)
                        |- MySQL      Windows service, reachable only from inside the box
```

What really changes from Linux: how you get in (RDP instead of SSH), the web server (IIS), how a process stays alive (Windows service instead of `systemd`), and a second firewall on the machine itself.

---

## 2. Steps

### 2.1 Launch the instance
- EC2 -> Launch instance -> **Microsoft Windows Server** AMI (e.g. 2022 Base).
- Size: a micro instance is free-tier eligible but has 1 GB RAM, which makes Windows very slow; `t3.small` / `t3.medium` is smoother. Windows costs more per hour than Linux because the licence is included (verify on the live page).
- Key pair: create or pick one. On Windows you do not log in with the key; you use it to **decrypt the Administrator password**.

### 2.2 Security group
| Port | Purpose | Source |
|---|---|---|
| 3389 | RDP | **My IP** only |
| 80 / 443 | Web | Anywhere |
| 3306 | MySQL | **Do not add** |
| 5000 | API | **Do not add** (only IIS talks to it, on the same box) |

### 2.3 Connect with RDP
1. Wait a few minutes after launch (the password is not ready immediately).
2. Instance -> **Connect** -> **RDP client** -> **Get password** -> upload the `.pem` -> **Decrypt password**.
3. Download the `.rdp` file, or open Remote Desktop (`mstsc`) with the public DNS/IP. User: `Administrator`.
4. From a Mac: Microsoft's "Windows App" (formerly Remote Desktop).

### 2.4 IIS (web server) and the UI
```powershell
Install-WindowsFeature -Name Web-Server -IncludeManagementTools
```
Or Server Manager -> Add Roles and Features -> Web Server (IIS).
- Browse to `http://localhost` on the box: the IIS welcome page means it works.
- Put the UI files (index.html, JS, CSS) in **`C:\inetpub\wwwroot`** (the Default Web Site).

### 2.5 Python and the API
1. Install Python from python.org; tick **"Add python.exe to PATH"** in the installer.
2. Create the app folder and a virtual environment:
   ```powershell
   mkdir C:\app; cd C:\app
   python -m venv venv
   .\venv\Scripts\Activate.ps1      # if blocked: Set-ExecutionPolicy -Scope CurrentUser RemoteSigned
   pip install flask pymysql waitress
   ```
3. Run it on localhost only:
   ```powershell
   waitress-serve --listen=127.0.0.1:5000 app:app
   ```
   `gunicorn` is Unix-only; **waitress** is the usual production WSGI server on Windows. (The framework used in class may differ; the idea is the same.)

### 2.6 MySQL
1. Download and run the **MySQL Installer for Windows** (Server only is enough).
2. It installs MySQL as a **Windows service** (named like `MySQL80`) that starts with the machine.
3. Set the root password, then create a database and an app user with only the rights it needs.
4. MySQL listens on 3306; since the security group does not open 3306, it is reachable only from the box.

### 2.7 Wire IIS to the API (reverse proxy)
Plain IIS cannot forward requests. Install two free Microsoft modules:
1. **URL Rewrite**
2. **Application Request Routing (ARR)**, then in IIS Manager: server node -> Application Request Routing Cache -> Server Proxy Settings -> **Enable proxy**.

Then add a `web.config` in `C:\inetpub\wwwroot`:
```xml
<configuration>
  <system.webServer>
    <rewrite>
      <rules>
        <rule name="api" stopProcessing="true">
          <match url="^api/(.*)" />
          <action type="Rewrite" url="http://127.0.0.1:5000/{R:1}" />
        </rule>
      </rules>
    </rewrite>
  </system.webServer>
</configuration>
```
`/api/users` from the browser reaches the API as `/users` (the same prefix stripping as `proxy_pass .../` in nginx). UI and API share one origin, so no CORS.

(Alternative: run nginx for Windows instead of IIS with the same config as the Linux note. IIS is what enterprise Windows shops use, so it is worth knowing.)

### 2.8 Keep the API running: a Windows service
A PowerShell window running waitress dies when you log off. Wrap it in a service with **NSSM** (Non-Sucking Service Manager):
```powershell
nssm install MyApi "C:\app\venv\Scripts\waitress-serve.exe" --listen=127.0.0.1:5000 app:app
nssm set MyApi AppDirectory C:\app
nssm start MyApi
```
Check it in `services.msc`: startup type Automatic, so it comes back after a reboot.

### 2.9 Windows Defender Firewall
The machine has its own firewall in addition to the security group. Installing IIS normally enables the "World Wide Web Services (HTTP Traffic-In)" rule; if the site is not reachable from outside, check it or add one:
```powershell
New-NetFirewallRule -DisplayName "HTTP 80" -Direction Inbound -Protocol TCP -LocalPort 80 -Action Allow
```
Do **not** open 5000 or 3306 here either.

### 2.10 Test
- On the box: `http://localhost` (UI) and `http://localhost/api/...` (API through IIS).
- From your laptop: `http://<public-ip>`.

---

## 3. Gotchas (Windows-specific)

- **Two firewalls:** a port must be open in the security group **and** in Windows Defender Firewall. Debug order: is the service running and listening (`netstat -ano | findstr :80`) -> Windows firewall -> security group -> public IP.
- **gunicorn on Windows** fails to install/run; use waitress.
- **Execution policy** blocks `Activate.ps1` on a fresh server; `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`.
- **Downloads blocked on older images:** Internet Explorer Enhanced Security Configuration (Server Manager -> Local Server -> IE Enhanced Security Configuration -> Off for Administrators), or download with `Invoke-WebRequest`.
- **PATH not refreshed:** after installing Python or MySQL, open a new PowerShell window before using the commands.
- **RDP open to the world** (3389 from `0.0.0.0/0`) is one of the most attacked ports on the internet; keep it to My IP. Better still, AWS Systems Manager Fleet Manager / Session Manager can give access without opening 3389.
- **Cost:** Windows instances cost more per hour; stop or terminate after class. A stopped instance still bills for its EBS disk (Windows root disks are 30 GB by default).

---

## 4. Linux vs Windows, side by side

| Step | Linux (01) | Windows (02) |
|---|---|---|
| Get in | `ssh -i key.pem ubuntu@ip`, port 22 | RDP as Administrator, password decrypted with the key, port 3389 |
| Web server | `apt install nginx` | `Install-WindowsFeature Web-Server` (IIS) |
| UI folder | `/var/www/html` | `C:\inetpub\wwwroot` |
| Python server | gunicorn | waitress |
| Reverse proxy | `location /api/ { proxy_pass ... }` | URL Rewrite + ARR, rule in `web.config` |
| Keep API alive | `systemd` service | Windows service via NSSM |
| MySQL | `apt install mysql-server` | MySQL Installer, runs as a service |
| Firewalls | Security group (+ `ufw` if enabled) | Security group + Windows Defender Firewall |
| Check a port | `ss -tlnp` | `netstat -ano \| findstr :80` |

---

## 5. FDE angle

- Many enterprise clients run Windows estates: IIS, .NET, SQL Server, Active Directory. Walking in able to deploy on IIS and debug a Windows service is a real advantage on-site.
- The reverse-proxy idea is identical on both; only the syntax changes. Say it that way to a client: "same architecture, Windows tooling."
- A calm debug order (process -> OS firewall -> security group -> IP) is what makes a client trust you during an outage.

---

## 6. Self-test

1. You cannot log in with the `.pem` file directly on Windows. What is the key used for?
2. Why waitress instead of gunicorn?
3. Which two IIS modules are needed to forward `/api` to the Python process, and what does the rewrite rule do with the `/api/` prefix?
4. The site opens on `http://localhost` inside RDP but not from your laptop. List the checks in order.
5. The API stops when you log off. What is the fix?
6. Why should ports 5000 and 3306 stay closed in both firewalls?
