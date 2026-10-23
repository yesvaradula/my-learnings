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

Every popup fires 5 minutes before its event.

| Task | Kind | When |
|------|------|------|
| Scaler class | `scaler` | 6:55 am Mon / Wed / Fri |
| Coding Ninjas class | `ninjas` | 10:55 am Sat / Sun |
| Binary Cloud class | `cloud` | 9:25 pm Mon-Fri |
| Leave for office | `leave-office` | 9:25 am Wed / Thu |
| Keka login (WFH) | `keka-login-wfh` | 10:55 am Mon / Tue / Fri |
| Keka logout (WFH) | `keka-logout-wfh` | 7:55 pm Mon / Tue / Fri |
| Projector hours (enter the week's hours) | `projector` | 7:25 pm Fri, before the Keka logout |
| Standup call | `standup` | 8:25 pm Mon-Fri |
| Scaler Contest 2 | `contest` | once, Tue Oct 27 2026, 9:55 am |
| Scaler Mock Interview | `mock` | once, Wed Oct 28 2026, 10:55 pm |

## Adding or changing a reminder

1. New text only: add lines to that kind's `Lines` in `popup.ps1`. Keep them ASCII (no emojis) and name no clients.
2. New reminder: add a kind (Title, Fact, Lines) in `popup.ps1`, add a row in `setup-reminders.ps1`, re-run setup, and update the table above.

## Gotchas

- Windows' default task settings refuse to start on battery (result `0x800710E0`, the popup silently never shows). The setup script turns that off; any task made by hand must do the same.
- Morning reminders (Scaler, Coding Ninjas, Leave for office, Keka login, Contest) have `Wake = $true`: they wake a sleeping laptop. Setup also allows wake timers on battery in the power plan (off by default). On a Modern Standby laptop the screen may stay dark; the popup waits there until the lid opens. Evening reminders do not wake the laptop.
- Missed runs (Oct 23: lid closed 4:24-8:37 pm, the 7:55 Keka logout and 8:25 standup popups never showed): every task has `StartWhenAvailable`, so a run missed during sleep starts as soon as the laptop wakes. Each task passes its time as `-Due`; `popup.ps1` adds a "Late popup" line when it is more than 3 minutes late, and stays silent when it is over 60 minutes late. A laptop with the lid closed still cannot be relied on, so keep a phone alarm for anything that must not be missed.
- Last-shown line per kind is stored in `%LOCALAPPDATA%\reminders\` (outside the repo).
