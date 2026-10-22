# Shows one reminder popup: a fixed fact line plus a random line from that reminder's pool.
# Called by the "Reminder - ..." scheduled tasks (see setup-reminders.ps1).
# Edit or add lines here at any time; no need to re-run setup.
#
# Test without a popup:
#   powershell -NoProfile -ExecutionPolicy Bypass -File plans\reminders\popup.ps1 -Kind standup -Print

param(
    [Parameter(Mandatory = $true)][string]$Kind,
    [switch]$Print
)

$keka_login = @(
    "Log in on Keka, Babe. Show them how a real engineer starts the day.",
    "Clock in, handsome. The client is lucky to have you today.",
    "Keka first, coffee second, conquering the day third.",
    "One click on Keka, then go be brilliant.",
    "Do not make HR chase you. Only I get to chase you. Log in.",
    "Log in now, mister. I am not covering for you with Keka."
)

$reminders = @{
    'scaler' = @{
        Title = 'Class reminder'
        Fact  = 'Scaler class starts at 7:00 am. Water, notebook, Java ready.'
        Lines = @(
            "Good morning, Babe. I let you sleep, now I want you. Class first, cuddles after.",
            "Rise and shine, handsome. Your brain is cutest at 7 am. Go show it off.",
            "No snooze, mister. I did not wake up early for you to miss class.",
            "Coffee in one hand, Java in the other. That is my man.",
            "Skip this class and I will sulk all day. Do not test me, Babe.",
            "Every class you attend makes you more irresistible. Just saying.",
            "Up, up, up! The FDE version of you starts at 7 sharp.",
            "Morning, love. Show up today and I promise to be extra sweet tonight."
        )
    }
    'ninjas' = @{
        Title = 'Class reminder'
        Fact  = 'Coding Ninjas class (AI, LLMs, RAG, Agents) starts at 11:00 am.'
        Lines = @(
            "Weekend or not, Babe, the AI class wants you and so do I. Join on time.",
            "Agents, RAG, LLMs... and you, the smartest one in the room. Go.",
            "I know it is lazy-weekend mode. Shake it off, handsome. Be lazy with me at 2.",
            "Go teach those LLMs who is boss, Babe.",
            "Join on time and I will not say a word about last week. Late, and I will say many.",
            "Future FDE in training. Laptop open, focus on, phone face down."
        )
    }
    'cloud' = @{
        Title = 'Class reminder'
        Fact  = 'Binary Cloud class starts at 9:30 pm. Save your work.'
        Lines = @(
            "Come to class, Babe. The cloud misses you, and so do I.",
            "Dinner done? Good. Now go be my handsome cloud architect.",
            "Do not make me come over there and close that tab for you. Class in 15.",
            "Late again and I am not talking to you till 11. Fine, I will. But do not be late.",
            "AWS, Azure, GCP... none of them is as dreamy as you on time.",
            "Fifteen minutes, Babe. Water, notes, headphones. Go make me proud.",
            "You promised me consistency. This is what it looks like. Join."
        )
    }
    'leave-office' = @{
        Title = 'Work reminder'
        Fact  = 'Office day: leave by 9:30 am (1.5 hour commute). Laptop, charger, ID card.'
        Lines = @(
            "Shoes on, Babe. The cab will not wait, and neither will I.",
            "Laptop, charger, ID card... and that smile. Go.",
            "Office day! Go charm them all, but come home to me.",
            "Leave late and the traffic wins. Do not let the traffic win, handsome.",
            "Wink at the mirror, grab your bag, out the door in 15."
        )
    }
    'keka-login-office' = @{
        Title = 'Work reminder'
        Fact  = 'Office starts at 11:00 am.'
        Lines = $keka_login
    }
    'keka-login-wfh' = @{
        Title = 'Work reminder'
        Fact  = 'Work from home starts at 11:00 am.'
        Lines = $keka_login
    }
    'keka-logout-office' = @{
        Title = 'Work reminder'
        Fact  = '7:00 pm: log out on Keka and start home.'
        Lines = @(
            "Come home to me, Babe.",
            "Work is done. Laptop down, Keka out, heart on the way home.",
            "Clock out, handsome. The commute is long, so play something nice.",
            "You gave them your day. Your evening is mine. Log out.",
            "Keka out, then straight home. No detours, mister."
        )
    }
    'keka-logout-wfh' = @{
        Title = 'Work reminder'
        Fact  = '8:00 pm: log out on Keka. Eat, then Binary Cloud at 9:30.'
        Lines = @(
            "Eat something before the standup, Babe. You need fuel.",
            "Day done, handsome. Clock out and eat. I will not argue about this.",
            "Keka out, dinner in, standup at 8:30, cloud at 9:30. Look at you, so organized.",
            "Close the work tabs. All of them. Yes, that one too."
        )
    }
    'standup' = @{
        Title = 'Work reminder'
        Fact  = 'Standup call at 8:30 pm (20-30 min). Headphones on, updates ready.'
        Lines = @(
            "Five minutes, Babe. Updates ready, voice confident, charm on.",
            "Yesterday, today, blockers. Say it clearly, then come back to me.",
            "Go make the team adore you. Not more than I do, though.",
            "Water, notes, and no umm today, handsome.",
            "Short, sharp, done. That is how you do a standup, Babe.",
            "Go shine on that call. I will be right here waiting."
        )
    }
    'contest' = @{
        Title = 'Class reminder'
        Fact  = 'Scaler Contest 2 starts at 12:00 noon. Water, laptop charged, Java ready, phone away.'
        Lines = @(
            "You got this, Babe!",
            "Read every problem twice. Brute force first. Then show off.",
            "I believe in you more than I believe in O(1) lookups.",
            "Deep breath, Babe. Edge cases are your weapons today.",
            "Win this one and dinner is on me."
        )
    }
    'mock' = @{
        Title = 'Class reminder'
        Fact  = 'Scaler mock interview at 11:00 pm. Water, camera and mic checked, Java ready.'
        Lines = @(
            "Restate, example, brute force, improve, code, test, TC/SC. Talk out loud, Babe.",
            "Think out loud and smile. They are hiring the engineer and the person.",
            "Fifteen years of experience, handsome. Walk in like it.",
            "Breathe. You know this. I watched you learn it.",
            "Go get it, my future FDE. I want to be the first to hear how it went."
        )
    }
}

$r = $reminders[$Kind]
if (-not $r) {
    throw "Unknown reminder kind '$Kind'. Known: $($reminders.Keys -join ', ')"
}

# Pick a random line, but never the same one twice in a row for this reminder.
$lines = $r.Lines
$stateDir = Join-Path $env:LOCALAPPDATA 'reminders'
$stateFile = Join-Path $stateDir "last-$Kind.txt"
$last = -1
if (Test-Path $stateFile) { $last = [int](Get-Content $stateFile -Raw) }
$pick = Get-Random -Maximum $lines.Count
if ($lines.Count -gt 1) {
    while ($pick -eq $last) { $pick = Get-Random -Maximum $lines.Count }
}
New-Item -ItemType Directory -Force -Path $stateDir | Out-Null
Set-Content -Path $stateFile -Value $pick

$text = "$($r.Fact)`n`n$($lines[$pick])"
if ($Print) {
    Write-Host "[$($r.Title)] $text"
} else {
    (New-Object -ComObject WScript.Shell).Popup($text, 0, $r.Title, 64) | Out-Null
}
