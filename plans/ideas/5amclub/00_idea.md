# 5 AM Club: web app plan (v0 draft)

Status: draft by Claude from a one-line idea ("a web app which encourages people to wake up at 5 am"). Everything marked **[assumption]** needs Sai's confirmation.

> "The world belongs to those who greet it before the sun does."
> Sai's favourite quote; the working tagline for 5amclub.

## 1. Vision
A web app that helps people build a lasting 5 AM wake-up habit through commitment, honest check-ins, streaks and a small accountable community.

**One-line pitch:** "Don't just set an alarm. Commit publicly, check in at 5, keep the streak."

## 2. Why people fail at early waking (design from this)
- Alarm apps are easy to dismiss and nobody sees the failure.
- The habit breaks on one bad night; there is no gentle recovery path.
- Motivation fades by week 2; social accountability and streaks outlast it.
- Waking at 5 without sleeping early backfires, so the real habit is **consistent bedtime + wake time**.

## 3. Users **[assumption]**
- Primary: working professionals and students who want a quiet morning for study, exercise or reflection.
- Secondary: small friend groups / teams who want to do it together.

## 4. Core loop
1. **Commit:** pick a wake-up time (default 05:00) and a purpose ("DSA", "gym", "Gita reading").
2. **Wake and check in** inside a window (e.g. 04:45-05:15 in the user's local time).
3. **Prove it lightly:** one-tap "I'm up" plus a one-line intention for the day.
4. **Reward:** streak +1, club feed shows who is up.
5. **Recover:** a missed day uses a "grace token" instead of resetting to zero.

## 5. MVP features (v1)
- Sign in with Google (and email magic link).
- Set wake time, timezone, and purpose.
- Daily check-in with a server-side timestamp and window validation.
- Streak counter, longest streak, weekly stats.
- Morning intention (one line) and evening reflection (optional).
- Reminders: email first; web push later.
- Accountability buddy: invite one friend, see each other's check-ins.
- "Club feed": who is up right now (opt-in, privacy-safe).

## 6. Later (not MVP)
- Challenge to dismiss (type a phrase / simple puzzle) to reduce half-asleep taps.
- Groups/leaderboards, badges, weekly digest.
- Bedtime commitment and sleep-hours nudges.
- WhatsApp / Telegram reminders.
- Mobile app (PWA first).
- Wearable or calendar integration.

## 7. Anti-cheat and fairness
- Window is validated on the **server**, never trusted from the client clock.
- Store timezone per user (IANA name, e.g. `Asia/Kolkata`); compute window in UTC per day.
- Handle travel and DST (not in India, but the app should be global-ready).
- Light friction over strict policing; the community is for support, not surveillance.

## 8. Suggested tech stack **[assumption: fits Sai's skills and GCP learning]**
| Layer | Choice | Why |
|-------|--------|-----|
| Frontend | React (Vite) PWA | Sai's strength; installable on phones |
| Backend | Node.js (Express or Fastify) | Sai's strength |
| Auth | Firebase Auth or Google Identity | Fast and secure |
| Database | PostgreSQL on Cloud SQL (or Firestore for speed) | Relational fits users, check-ins, streaks |
| Hosting | Cloud Run (container) | Scale-to-zero, cheap, ties to the GCP course |
| Scheduling | Cloud Scheduler + Pub/Sub (or Cloud Tasks) | Send reminders at 04:45 per timezone |
| Notifications | Email first (SendGrid/Mailgun), FCM web push later | |
| CI/CD | GitHub Actions to Cloud Run | Client-style delivery |
| IaC | Terraform (stretch) | Enterprise/FDE credibility |
| Observability | Cloud Logging + Error Reporting | |

## 9. Data model (first cut)
```
users(id, email, name, timezone, created_at)
commitments(id, user_id, wake_time, purpose, start_date, active)
checkins(id, user_id, local_date, checked_in_at_utc, status, intention)
streaks(user_id, current, longest, grace_tokens, updated_at)   // derivable; cache if needed
buddies(user_a, user_b, status)
reminders(id, user_id, send_at_utc, channel, sent_at)
```
Key rule: `UNIQUE(user_id, local_date)` on `checkins` so one check-in per local day.

## 10. Architecture sketch
```
React PWA  ->  Cloud Run (Node API)  ->  Cloud SQL (Postgres)
                    ^
Cloud Scheduler -> Pub/Sub -> reminder worker -> email / push
```
Single service for MVP; split the reminder worker only when needed.

## 11. Roadmap (fits Sai's schedule: build in the weekday gaps and weekend afternoons)
| Phase | Goal | Done when |
|-------|------|-----------|
| 0 (this week) | Confirm vision, user stories, wireframes | This doc agreed |
| 1 | Repo, CI, auth, "set commitment" screen | Can sign in and save a wake time |
| 2 | Check-in + window validation + streaks | A real day-1 check-in at 5 AM |
| 3 | Reminders + buddy | A friend can join and see you |
| 4 | Deploy to Cloud Run with a public URL | Shared link works on phone |
| 5 | Dogfood with 5 friends for 2 weeks | Feedback list + retention numbers |

## 12. Success metrics
- Day-7 and day-30 retention of check-ins.
- Average streak length.
- % of users with a buddy (should correlate with retention).

## 13. Risks and honest notes
- **Sleep deprivation:** pushing 5 AM without an early bedtime harms health and kills the habit. Include bedtime guidance and a "slept enough?" prompt; never shame a missed day.
- **Retention is the hard part**, not the code. Build the buddy loop early.
- **Scope creep:** ship the check-in + streak + reminder first.
- **Privacy:** check-in times reveal routines; keep the feed opt-in.

## 14. FDE angle
Treat it like a client delivery: clear README, one-command local run, CI/CD, a deployed URL, a short architecture doc, and a demo script. That is exactly the portfolio proof an FDE interview rewards.

## 15. Open questions for Sai
1. Is this for **everyone** or for a specific group (your learning community, colleagues, friends)?
2. What is the **hook** you imagined: streaks, a public club, buddies, a leaderboard, or something else?
3. Free forever, or a paid tier eventually?
4. Build it **solo**, or invite a friend or two to co-build?
5. Name: keep `5amclub` or something else?
6. First version **web only (PWA)**, correct?
