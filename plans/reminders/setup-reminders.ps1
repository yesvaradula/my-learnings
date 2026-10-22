# Creates (or replaces) all "Reminder - ..." popups as Windows scheduled tasks.
# Safe to run again: each task is re-registered with -Force, nothing is duplicated.
#
# Run once on any laptop, from the repo root:
#   powershell -NoProfile -ExecutionPolicy Bypass -File plans\reminders\setup-reminders.ps1
#
# Each task runs popup.ps1 from this folder, which picks a fresh line every time.
# Popup text lives in popup.ps1 (edit it freely); this file only holds the schedule.
# Re-run this script if the repo folder moves, since the tasks store popup.ps1's full path.
#
# Popups fire on battery and on charger. They cannot fire while the laptop is asleep.
# One-time reminders whose time has already passed are skipped.
# Times are local (IST). Keep this file in sync with the schedule in CLAUDE.md.

$weekly = @(
    @{ Name = 'Scaler class';          Kind = 'scaler';             At = '06:45'; Days = 'Monday', 'Wednesday', 'Friday' },
    @{ Name = 'Coding Ninjas class';   Kind = 'ninjas';             At = '10:45'; Days = 'Saturday', 'Sunday' },
    @{ Name = 'Binary Cloud class';    Kind = 'cloud';              At = '21:15'; Days = 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday' },
    @{ Name = 'Leave for office';      Kind = 'leave-office';       At = '09:15'; Days = 'Wednesday', 'Thursday' },
    @{ Name = 'Keka login (WFH)';      Kind = 'keka-login-wfh';     At = '10:55'; Days = 'Monday', 'Tuesday', 'Friday' },
    @{ Name = 'Keka logout (WFH)';     Kind = 'keka-logout-wfh';    At = '20:00'; Days = 'Monday', 'Tuesday', 'Friday' },
    @{ Name = 'Standup call';          Kind = 'standup';            At = '20:25'; Days = 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday' }
)

$once = @(
    @{ Name = 'Scaler Contest 2';      Kind = 'contest'; At = '2026-10-27 09:30' },
    @{ Name = 'Scaler Mock Interview'; Kind = 'mock';    At = '2026-10-28 22:30' }
)

# Reminders that were dropped from the schedule; removed if present.
# Keka on office days was dropped Oct 22: Keka reminders are for WFH days only.
$retired = @('Keka login (office)', 'Keka logout (office)')

$popup = Join-Path $PSScriptRoot 'popup.ps1'

# Default task settings refuse to start on battery (error 0x800710E0); allow it explicitly.
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries

function Register-Reminder($r, $trigger) {
    $action = New-ScheduledTaskAction -Execute 'powershell.exe' `
        -Argument "-NoProfile -WindowStyle Hidden -ExecutionPolicy Bypass -File `"$popup`" -Kind $($r.Kind)"
    Register-ScheduledTask -TaskName "Reminder - $($r.Name)" -Action $action -Trigger $trigger `
        -Settings $settings -Force | Out-Null
    Write-Host "OK    Reminder - $($r.Name)"
}

foreach ($name in $retired) {
    if (Get-ScheduledTask -TaskName "Reminder - $name" -ErrorAction SilentlyContinue) {
        Unregister-ScheduledTask -TaskName "Reminder - $name" -Confirm:$false
        Write-Host "DEL   Reminder - $name"
    }
}

foreach ($r in $weekly) {
    $trigger = New-ScheduledTaskTrigger -Weekly -WeeksInterval 1 -DaysOfWeek $r.Days -At $r.At
    Register-Reminder $r $trigger
}

foreach ($r in $once) {
    $at = [datetime]::ParseExact($r.At, 'yyyy-MM-dd HH:mm', $null)
    if ($at -le (Get-Date)) {
        Write-Host "SKIP  Reminder - $($r.Name) (already passed: $($r.At))"
        continue
    }
    Register-Reminder $r (New-ScheduledTaskTrigger -Once -At $at)
}
