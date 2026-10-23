# binary-cloud: Cloud notes (AWS / Azure / GCP)

Cloud course notes, one subfolder per provider. Class: Mon-Fri, 9:30-11:00 pm IST.

## Layout
- `aws/`: numbered notes, same format as `gcp/`.
- `gcp/`: numbered notes, `NN_topic.md`, each with intuition, steps, gotchas, cross-cloud table, FDE angle, self-test.
- `github/`: GitHub and CI/CD notes, same format (`01_github_actions.md`: beginner-to-solid guide with a practice ladder).

## Conventions
- Learning mode: intuition first, then architecture, then real-world gotchas; tie back to full stack + FDE goal.
- Prices/credits change: flag them "verify on the live page".
- Never commit keys, `gcloud` config or credentials.

## Progress
| Note | Topic | Status |
|------|-------|--------|
| aws/01_single_instance_app.md | UI + API + MySQL on one EC2 instance, Linux (Oct 19) and Windows (Oct 20) | Notes saved Oct 20 (from his class summary, joined Oct 20 class late); self-test open |
| aws/02_single_instance_windows.md | Same app on Windows Server: RDP, IIS + URL Rewrite/ARR, waitress, NSSM service, two firewalls | Notes saved Oct 20 (from his class summary); self-test open |
| github/01_github_actions.md | GitHub Actions from zero: vocabulary, triggers, jobs, secrets, OIDC deploys, security, debugging, practice ladder (Oct 23 class on GitHub and code maintenance; he is new to Actions) | Notes saved Oct 23; ladder and self-test open. Ladder task 2 (Java compile check) would flag MinimumSwaps.java and BitBasics.java (dry-run Oct 23: 82/84 compile) |
| gcp/01_account.md | Account, billing, project, budget, gcloud | Done Oct 15: project `learning-sandbox-511709`, budget Rs 500, `gcloud` OK, trial Rs 28,000 until Dec 24 2026 |

## Hands-on labs
- `aws/lab/gita-quotes/`: Gita Quotes app (Flask API + MySQL + static UI, config from env vars; routes smoke-tested locally Oct 21) with a README for both labs. **Linux lab: Sat Oct 24, 3-5:30 pm. Windows lab: Sun Oct 25, 3-5:30 pm** (after Coding Ninjas). Done when the page works from his laptop; terminate after each lab. Get the files onto Linux with `git clone` (keeps LF line endings for the systemd/nginx files).

## Planned
`02_iam.md`, `03_compute.md`, `04_billing_guardrails.md`.

## Open items
- Add backup codes and a second factor to the Google account.
- Delete the stopped Windows VM `ls-inst-win-server` + its disk when done (still bills for the disk).
- Decide on the UPI AutoPay mandate before the trial ends (revoke if not upgrading).
- Next: `02_iam.md`.
