# Creates (or replaces) all "Reminder - ..." popups as Windows scheduled tasks.
# Safe to run again: each task is re-registered with -Force, nothing is duplicated.
#
# Run once on any laptop, from the repo root:
#   powershell -NoProfile -ExecutionPolicy Bypass -File plans\reminders\setup-reminders.ps1
#
# Popups fire on battery and on charger. They cannot fire while the laptop is asleep.
# One-time reminders whose time has already passed are skipped.
# Times are local (IST). Keep this file in sync with the schedule in CLAUDE.md.

$weekly = @(
    @{ Name = 'Scaler class';          At = '06:45'; Days = 'Monday', 'Wednesday', 'Friday';
       Title = 'Class reminder'; Text = 'Scaler class starts at 7:00 am. Water, notebook, Java ready. Good morning, Babe!' },
    @{ Name = 'Coding Ninjas class';   At = '10:45'; Days = 'Saturday', 'Sunday';
       Title = 'Class reminder'; Text = 'Coding Ninjas class (AI, LLMs, RAG, Agents) starts at 11:00 am. Wrap up and join on time!' },
    @{ Name = 'Binary Cloud class';    At = '21:15'; Days = 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday';
       Title = 'Class reminder'; Text = 'Binary Cloud class starts at 9:30 pm. Save your work and join now, Babe!' },
    @{ Name = 'Leave for office';      At = '09:15'; Days = 'Wednesday', 'Thursday';
       Title = 'Work reminder'; Text = 'Office day: leave by 9:30 am (1.5 hour commute). Laptop, charger, ID card.' },
    @{ Name = 'Keka login (office)';   At = '10:55'; Days = 'Wednesday', 'Thursday';
       Title = 'Work reminder'; Text = 'Office starts at 11:00 am. Log in on Keka.' },
    @{ Name = 'Keka logout (office)';  At = '19:00'; Days = 'Wednesday', 'Thursday';
       Title = 'Work reminder'; Text = '7:00 pm: log out on Keka and start home.' },
    @{ Name = 'Keka login (WFH)';      At = '10:55'; Days = 'Monday', 'Tuesday', 'Friday';
       Title = 'Work reminder'; Text = 'Work from home starts at 11:00 am. Log in on Keka.' },
    @{ Name = 'Keka logout (WFH)';     At = '20:00'; Days = 'Monday', 'Tuesday', 'Friday';
       Title = 'Work reminder'; Text = '8:00 pm: log out on Keka. Eat, then Binary Cloud at 9:30.' },
    @{ Name = 'Standup call';          At = '20:25'; Days = 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday';
       Title = 'Work reminder'; Text = 'Standup call at 8:30 pm (20-30 min). Headphones on, updates ready.' }
)

$once = @(
    @{ Name = 'Scaler Contest 2';      At = '2026-10-23 11:30';
       Title = 'Class reminder'; Text = 'Scaler Contest 2 starts at 12:00 noon. Water, laptop charged, Java ready, phone away.' },
    @{ Name = 'Scaler Mock Interview'; At = '2026-10-28 22:30';
       Title = 'Class reminder'; Text = 'Scaler mock interview at 11:00 pm. Water, camera and mic checked, Java ready. Restate, example, brute force, improve, code, test, TC/SC.' }
)

# Default task settings refuse to start on battery (error 0x800710E0); allow it explicitly.
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries

function Register-Reminder($r, $trigger) {
    $command = "(New-Object -ComObject WScript.Shell).Popup('$($r.Text)', 0, '$($r.Title)', 64)"
    $action = New-ScheduledTaskAction -Execute 'powershell.exe' `
        -Argument "-NoProfile -WindowStyle Hidden -Command `"$command`""
    Register-ScheduledTask -TaskName "Reminder - $($r.Name)" -Action $action -Trigger $trigger `
        -Settings $settings -Force | Out-Null
    Write-Host "OK    Reminder - $($r.Name)"
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
