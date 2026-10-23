# Shows one reminder popup: a fixed fact line plus a random line from that reminder's pool.
# Called by the "Reminder - ..." scheduled tasks (see setup-reminders.ps1).
# Edit or add lines here at any time; no need to re-run setup.
#
# Test without a popup:
#   powershell -NoProfile -ExecutionPolicy Bypass -File plans\reminders\popup.ps1 -Kind standup -Print

param(
    [Parameter(Mandatory = $true)][string]$Kind,
    # Scheduled time, 'HH:mm' (weekly) or 'yyyy-MM-dd HH:mm' (one-time). Lets a run that was
    # delayed by sleep say it is late, or skip itself when it is too stale to be useful.
    [string]$Due,
    [switch]$Print
)

$lateNote = ''
if ($Due) {
    $format = if ($Due.Length -gt 5) { 'yyyy-MM-dd HH:mm' } else { 'HH:mm' }
    $dueAt = [datetime]::ParseExact($Due, $format, $null)
    $lateBy = ((Get-Date) - $dueAt).TotalMinutes
    if ($lateBy -gt 60) { exit }
    if ($lateBy -gt 3) {
        $lateNote = "Late popup: this was due at $($dueAt.ToString('h:mm tt')) but the laptop was asleep.`n`n"
    }
}

$keka_login = @(
    "Log in on Keka, Babe. Show them how a real engineer starts the day.",
    "Clock in, handsome. The client is lucky to have you today.",
    "Keka first, coffee second, conquering the day third.",
    "One click on Keka, then go be brilliant.",
    "Do not make HR chase you. Only I get to chase you. Log in.",
    "Log in now, mister. I am not covering for you with Keka.",
    "Morning, superstar. Keka is waiting, and so is a day full of wins.",
    "Log in like you mean it, Babe. Then go make your manager look smart.",
    "Clock in on time and I will brag about you to everyone. Clock in late and I will brag less.",
    "Keka login: two seconds. Being my favourite engineer: all day long.",
    "Deep breath, big smile, log in. You were built for days like this.",
    "No commute today, Babe. Use the saved time to be brilliant, not to nap.",
    "Pyjamas are fine. Slacking is not. Log in, handsome.",
    "Home office, real work. Close the fridge and open Keka.",
    "Working from home means I get to see you focus all day. Do not disappoint me."
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
            "Morning, love. Show up today and I promise to be extra sweet tonight.",
            "Blanket off, Babe. I know it is warm in there, but your future is warmer.",
            "The early bird gets the offer letter. Up you get, handsome.",
            "Five minutes to class. Splash some water, find your notebook, find your fire.",
            "You are not a snooze-button man. You are a Forward Deployed Engineer in the making.",
            "I would kiss you awake if I could. Consider this popup the next best thing."
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
            "Future FDE in training. Laptop open, focus on, phone face down.",
            "Brunch can wait, Babe. Agents first, pancakes after.",
            "Three hours of AI, then the whole afternoon is ours. Deal?",
            "The smartest man I know is about to get smarter. I love watching that.",
            "If an AI agent can show up on time, so can you, handsome."
        )
    }
    'cloud' = @{
        Title = 'Class reminder'
        Fact  = 'Binary Cloud class starts at 9:30 pm. Save your work.'
        Lines = @(
            "Come to class, Babe. The cloud misses you, and so do I.",
            "Dinner done? Good. Now go be my handsome cloud architect.",
            "Do not make me come over there and close that tab for you. Class in 5.",
            "Late again and I am not talking to you till 11. Fine, I will. But do not be late.",
            "AWS, Azure, GCP... none of them is as dreamy as you on time.",
            "Five minutes, Babe. Water, notes, headphones. Go make me proud.",
            "You promised me consistency. This is what it looks like. Join.",
            "One more class between you and cloud mastery. Do not keep the cloud waiting, Babe.",
            "Put the phone down. Yes, even my messages. Class first.",
            "I fell for a man who finishes what he starts. Go finish this course.",
            "Scrolling is not studying, mister. Close it and join.",
            "Grab some water, sit up straight, and look handsome on that camera.",
            "Tonight's topic is lucky to have you as a student. Go."
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
            "Wink at the mirror, grab your bag, out the door in 5.",
            "Did you eat breakfast? Do not lie to me. Eat something, then go.",
            "Wallet, keys, phone, charger. Check twice, Babe. I know you.",
            "Use the commute well: a podcast, a revision note, or thinking about me.",
            "Go look sharp at the office, handsome. Not too sharp, I get jealous."
        )
    }
    'keka-login-wfh' = @{
        Title = 'Work reminder'
        Fact  = 'Work from home starts at 11:00 am.'
        Lines = $keka_login
    }
    'keka-logout-wfh' = @{
        Title = 'Work reminder'
        Fact  = '8:00 pm: log out on Keka. Eat, then Binary Cloud at 9:30.'
        Lines = @(
            "Eat something before the standup, Babe. You need fuel.",
            "Day done, handsome. Clock out and eat. I will not argue about this.",
            "Keka out, dinner in, standup at 8:30, cloud at 9:30. Look at you, so organized.",
            "Close the work tabs. All of them. Yes, that one too.",
            "Log out and stretch, Babe. Your back has been begging all day.",
            "Out of Keka, into my arms. Well, into dinner first.",
            "You worked hard today. I noticed. Now clock out.",
            "Eight o'clock, handsome. Work wife time is over, real girlfriend time begins.",
            "Log out now or I start counting. One... two...",
            "Shut the laptop lid for a bit, Babe. Your eyes deserve a break from that screen.",
            "Walk away from the desk, handsome. Ten steps to the kitchen counts as a commute.",
            "Day done at home. Stand up, drink water, and look at something that is not a monitor. Like me.",
            "Logging out is self-respect, mister. Do it before work follows you into the evening."
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
            "Go shine on that call. I will be right here waiting.",
            "Say what you shipped, say what is next, say it like a leader.",
            "If you have a blocker, say it out loud. Asking early is a superpower, Babe.",
            "Camera ready? Hair fixed? Of course it is. You always look good.",
            "Twenty minutes of focus, then you are all mine again.",
            "Speak slowly, handsome. Confident people are never in a hurry."
        )
    }
    'contest' = @{
        Title = 'Class reminder'
        Fact  = 'Scaler Contest 2 starts at 10:00 am. Water, laptop charged, Java ready, phone away.'
        Lines = @(
            "You got this, Babe!",
            "Read every problem twice. Brute force first. Then show off.",
            "I believe in you more than I believe in O(1) lookups.",
            "Deep breath, Babe. Edge cases are your weapons today.",
            "Win this one and dinner is on me.",
            "Long for big numbers, Babe. Do not let overflow steal your marks.",
            "Stuck on one problem? Move on and come back. Do not marry a single question.",
            "Five minutes to go. Close every other tab and stop scrolling."
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
            "Go get it, my future FDE. I want to be the first to hear how it went.",
            "Do not know something? Say how you would find out. That is the FDE answer.",
            "Ask clarifying questions first, Babe. Interviewers love that.",
            "Nervous is just excited without breathing. Breathe, handsome."
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

$text = "$lateNote$($r.Fact)`n`n$($lines[$pick])"
if ($Print) {
    Write-Host "[$($r.Title)] $text"
} else {
    (New-Object -ComObject WScript.Shell).Popup($text, 0, $r.Title, 64) | Out-Null
}
