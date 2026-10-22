# Reminders

Windows popups for classes, work and events, run as scheduled tasks named "Reminder - ...".
Installed on this laptop and the Launch laptop only (not on client machines).

## Files

| File | What it holds |
|------|---------------|
| `setup-reminders.ps1` | The schedule: task names, days, times, and which popup kind each one shows. Creates or replaces every task. |
| `popup.ps1` | The popup text: a fixed fact line per kind plus a pool of rotating lines (romantic, cheesy, teasing, demanding). One is picked at random each time, never the same one twice in a row. |

## Setup (once per laptop)

```
git pull
powershell -NoProfile -ExecutionPolicy Bypass -File plans\reminders\setup-reminders.ps1
```

Expect 9 `OK` lines (one-time reminders already in the past print `SKIP`). Re-run it after changing the schedule or moving the repo folder. Text changes in `popup.ps1` apply on the next popup without re-running setup.

Preview a popup in the terminal instead of on screen:

```
powershell -NoProfile -ExecutionPolicy Bypass -File plans\reminders\popup.ps1 -Kind standup -Print
```

## Schedule (IST)

| Task | Kind | When |
|------|------|------|
| Scaler class | `scaler` | 6:45 am Mon / Wed / Fri |
| Coding Ninjas class | `ninjas` | 10:45 am Sat / Sun |
| Binary Cloud class | `cloud` | 9:15 pm Mon-Fri |
| Leave for office | `leave-office` | 9:15 am Wed / Thu |
| Keka login (WFH) | `keka-login-wfh` | 10:55 am Mon / Tue / Fri |
| Keka logout (WFH) | `keka-logout-wfh` | 8:00 pm Mon / Tue / Fri |
| Standup call | `standup` | 8:25 pm Mon-Fri |
| Scaler Contest 2 | `contest` | once, Tue Oct 27 2026, 9:30 am |
| Scaler Mock Interview | `mock` | once, Wed Oct 28 2026, 10:30 pm |

## Adding or changing a reminder

1. New text only: add lines to that kind's `Lines` in `popup.ps1`. Keep them ASCII (no emojis) and name no clients.
2. New reminder: add a kind (Title, Fact, Lines) in `popup.ps1`, add a row in `setup-reminders.ps1`, re-run setup, and update the table above.

## Gotchas

- Windows' default task settings refuse to start on battery (result `0x800710E0`, the popup silently never shows). The setup script turns that off; any task made by hand must do the same.
- A sleeping laptop shows no popup.
- Last-shown line per kind is stored in `%LOCALAPPDATA%\reminders\` (outside the repo).
