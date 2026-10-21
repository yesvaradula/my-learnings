# Lab: Gita Quotes on one EC2 instance (Linux, then Windows)

Hands-on practice for notes `01_single_instance_app.md` (Linux) and `02_single_instance_windows.md` (Windows). The app is ready; the lab is the deployment.

```
Browser --:80--> web server --/api/*--> Python API (127.0.0.1:5000) --> MySQL (localhost:3306)
                  |  nginx (Linux) / IIS (Windows)
                  '-- serves static/index.html
```

| File | What it is |
|---|---|
| `app.py` | Flask API: `GET /api/health`, `GET /api/quotes`, `POST /api/quotes`. DB settings from env vars (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`). |
| `static/index.html` | The UI: list and add quotes; shows API + DB status at the top. |
| `schema.sql` | Creates database `gita`, user `gita_app` (local only, SELECT/INSERT only), table `quotes`, two seed rows. **Change the password** before running. |
| `requirements.txt` | flask, pymysql, plus gunicorn on Linux or waitress on Windows (picked automatically). |
| `deploy/nginx-gita.conf` | Linux nginx site. |
| `deploy/gita-api.service` | Linux systemd unit for gunicorn. |
| `deploy/web.config` | Windows IIS rewrite rule (needs URL Rewrite + ARR). |

All API routes start with `/api`, so the web server forwards `/api/...` **unchanged** (no prefix stripping, unlike the example in note 01).

**Getting the files onto the server:** push this repo to GitHub and `git clone` on the instance, or copy the folder with `scp -i key.pem -r gita-quotes ubuntu@<ip>:~` (Linux) / paste through the RDP session (Windows).

---

## Lab 1: Linux (Ubuntu), Sat Oct 24, ~2.5 hours

**Done when:** `http://<public-ip>` from your laptop shows the quotes page with "API: ok, DB: ok", and a new quote survives a page refresh.

1. **Launch:** EC2, Ubuntu LTS AMI, free-tier size (verify on the live page), new key pair. Security group: SSH 22 from **My IP**, HTTP 80 from anywhere. Nothing else.
2. **Connect:** `ssh -i key.pem ubuntu@<public-ip>`
3. **Install:**
   ```bash
   sudo apt update
   sudo apt install -y nginx mysql-server python3-venv
   ```
4. **Database:**
   ```bash
   # edit the password in schema.sql first
   sudo mysql < ~/gita-quotes/schema.sql
   sudo mysql -e "SELECT * FROM gita.quotes;"      # 2 seed rows
   ```
5. **API:**
   ```bash
   sudo mkdir -p /opt/gita-quotes && sudo chown ubuntu /opt/gita-quotes
   cp -r ~/gita-quotes/* /opt/gita-quotes/
   cd /opt/gita-quotes
   python3 -m venv venv
   venv/bin/pip install -r requirements.txt
   echo "DB_PASSWORD=<your password>" | sudo tee /etc/gita-api.env
   sudo chmod 600 /etc/gita-api.env
   sudo cp deploy/gita-api.service /etc/systemd/system/
   sudo systemctl daemon-reload
   sudo systemctl enable --now gita-api
   curl http://127.0.0.1:5000/api/health            # {"db":"ok","status":"ok"}
   ```
6. **UI + proxy:**
   ```bash
   sudo mkdir -p /var/www/gita && sudo cp static/index.html /var/www/gita/
   sudo cp deploy/nginx-gita.conf /etc/nginx/sites-available/gita
   sudo ln -s /etc/nginx/sites-available/gita /etc/nginx/sites-enabled/gita
   sudo rm /etc/nginx/sites-enabled/default
   sudo nginx -t && sudo systemctl reload nginx
   curl http://localhost/api/quotes                 # JSON through nginx
   ```
7. **From your laptop:** open `http://<public-ip>`, add a quote, refresh.
8. **Break it on purpose (the real learning):**
   - `sudo systemctl stop gita-api` -> refresh: what does the page say? (`502 Bad Gateway` from nginx). Start it again.
   - `sudo systemctl stop mysql` -> what does `/api/health` say? Start it again.
   - `sudo reboot`, reconnect: does everything come back on its own? (It should: both services are enabled.)
9. **Clean up:** terminate the instance. Check that the EBS volume is gone too.

---

## Lab 2: Windows Server, Sun Oct 25, ~2.5 hours

**Done when:** the same page works from your laptop, served by IIS, with the API running as a Windows service.

1. **Launch:** Windows Server 2022 Base AMI, `t3.small` or bigger (micro is very slow), same key pair. Security group: RDP 3389 from **My IP**, HTTP 80 from anywhere.
2. **Connect:** Connect -> RDP client -> Get password (upload the key) -> RDP as `Administrator`.
3. **IIS:** `Install-WindowsFeature -Name Web-Server -IncludeManagementTools` (PowerShell as Administrator). `http://localhost` shows the IIS page.
4. **Downloads** (Edge on the server): Python 3 (tick "Add python.exe to PATH"), MySQL Installer (Server only), IIS **URL Rewrite** and **Application Request Routing**, NSSM.
5. **Database:** run the MySQL installer, set a root password, then in a new PowerShell:
   ```powershell
   # edit the password in schema.sql first
   Get-Content C:\gita-quotes\schema.sql | mysql -u root -p
   ```
   (Add `C:\Program Files\MySQL\MySQL Server 8.x\bin` to PATH if `mysql` is not found. PowerShell has no `<` redirection, hence `Get-Content ... |`.)
6. **API:**
   ```powershell
   cd C:\gita-quotes
   python -m venv venv
   .\venv\Scripts\pip install -r requirements.txt
   $env:DB_PASSWORD = "<your password>"
   .\venv\Scripts\waitress-serve --listen=127.0.0.1:5000 app:app     # try it, then Ctrl+C
   ```
   Then as a service with NSSM:
   ```powershell
   nssm install GitaApi "C:\gita-quotes\venv\Scripts\waitress-serve.exe" --listen=127.0.0.1:5000 app:app
   nssm set GitaApi AppDirectory C:\gita-quotes
   nssm set GitaApi AppEnvironmentExtra DB_PASSWORD=<your password>
   nssm start GitaApi
   Invoke-RestMethod http://127.0.0.1:5000/api/health
   ```
7. **UI + proxy:** in IIS Manager, server node -> Application Request Routing Cache -> Server Proxy Settings -> tick **Enable proxy** -> Apply. Then:
   ```powershell
   Copy-Item C:\gita-quotes\static\index.html C:\inetpub\wwwroot\
   Copy-Item C:\gita-quotes\deploy\web.config C:\inetpub\wwwroot\
   Invoke-RestMethod http://localhost/api/quotes
   ```
8. **From your laptop:** `http://<public-ip>`. If it fails, check in order: `GitaApi` running (`services.msc`), Windows Defender Firewall rule for port 80, security group, public IP.
9. **Break it on purpose:** stop the `GitaApi` service and refresh (IIS returns a 502-type error); stop `MySQL80`; reboot and check both services come back (startup type Automatic).
10. **Clean up:** terminate. Windows costs more per hour; do not leave it stopped with a disk either.

---

## Stretch goals (only if both labs are done)
- HTTPS with a free certificate (Linux: certbot + a domain).
- Move MySQL to **RDS** and point `DB_HOST` at it: the app does not change, only an env var. That is the first step from "one box" to production.
- Put the steps in an EC2 **user data** script so a new instance builds itself on boot.

## Interview / FDE lines from this lab
- *"I keep config in environment variables, so the same build runs on Linux, Windows or against RDS by changing settings, not code."*
- *"The API binds to localhost and only the web server is public; the database port is never opened."*
- *"When a page fails I check process, OS firewall, security group, then IP, in that order."*
