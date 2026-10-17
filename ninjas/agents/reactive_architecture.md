# Reactive Architecture: Real-World Examples, Why and Why Not

**Class:** Coding Ninjas, Sat Oct 17 2026, "Agent architecture and design patterns".
**Related:** `introduction.md` (section 2, reactive architecture), `why_architecture.md`.

---

## 1. Quick recap

A **reactive agent** maps the current input straight to an action using condition-action rules:

```
percept --> if condition then action --> act
```

- No internal world model, no planning, little or no memory of the past.
- Origin: Rodney Brooks' **subsumption architecture** (MIT, 1980s): simple behaviours stacked in layers, higher-priority behaviours override (subsume) lower ones.

---

## 2. Main real-world example: the robot vacuum cleaner

Early robot vacuums (iRobot's Roomba; Brooks co-founded iRobot) are the classic case of behaviour-based, reactive design.

### Behaviours, highest priority first

| Priority | Condition (sensor) | Action |
|----------|--------------------|--------|
| 1 | Cliff sensor detects a drop (stairs) | Stop, back up, turn away |
| 2 | Bump sensor hits an obstacle | Back up, turn a random angle |
| 3 | Dirt sensor detects heavy dirt | Spiral in place to clean the spot |
| 4 | Battery low | Head towards the dock signal |
| 5 | Nothing special | Drive forward (wander) |

### How it works
- It never builds a map or plans a route. Every few milliseconds it reads its sensors and the **highest-priority behaviour that fires wins**.
- "Clean the whole room" emerges from simple rules plus randomness: given enough time, random bouncing covers most of the floor.

### Simple code sketch

```python
def decide(sensors):
    if sensors.cliff:        return "back_up_and_turn"   # safety first
    if sensors.bump:         return "back_up_random_turn"
    if sensors.dirt_high:    return "spiral_clean"
    if sensors.battery_low:  return "go_to_dock"
    return "move_forward"                                 # default behaviour

while True:
    act(decide(read_sensors()))   # no memory, no plan: just react
```

The order of the `if` statements is the subsumption hierarchy: safety rules subsume everything below them.

---

## 3. More real-world examples

| Example | Condition -> action | Why reactive fits |
|---------|---------------------|-------------------|
| **Airbag** | Crash deceleration above threshold -> inflate | Must act in milliseconds; no time to think |
| **ABS brakes** | Wheel about to lock -> release and reapply brake | Fast, safety-critical, predictable |
| **Thermostat** | Temp below set point -> heating on | The simplest possible agent |
| **Spam / rule-based fraud filter** | Rule matches -> block or flag | Cheap and instant on every request |
| **Game NPC (basic)** | Player in range -> attack, health low -> flee | Fast, simple, believable enough |
| **Auto-scaling rule** | CPU > 70% for 5 min -> add instance | Simple threshold, easy to reason about |
| **LLM router** | Intent = "billing" -> billing flow | One classification, no multi-step reasoning |
| **LLM guardrail** | PII or injection detected -> block or redact | Must run on every request, fast and cheap |
| **FAQ chatbot / cache hit** | Question matches a known FAQ -> return canned answer | No reasoning needed; instant and free |

Note on LLM examples: the model call itself does "thinking", but the **architecture** is still reactive when there is one input -> one decision -> one action, with no plan, no loop and no memory of past steps.

---

## 4. Why use reactive architecture (pros)

1. **Speed:** decisions in milliseconds. Critical for safety (airbags, brakes) and for high-traffic APIs.
2. **Low cost:** no planning, no long reasoning chains, few or no model calls.
3. **Robust in changing environments:** it reacts to what is happening *now*, so it never acts on a stale plan or a wrong world model. A moved chair doesn't confuse a robot vacuum.
4. **Predictable and testable:** each rule can be unit-tested; same input gives the same output.
5. **Simple to build and explain:** rules can be read by non-engineers, which helps with audits and client sign-off.
6. **Scales easily:** stateless, so you can run many copies in parallel (like stateless microservices).
7. **Fails gracefully:** simple components have fewer ways to break.

## 5. Why not use it (cons)

1. **No foresight or planning:** can't handle multi-step goals ("book a trip: flight, then hotel near the venue, then a cab").
2. **No memory, no learning from history:** repeats the same mistakes; can't say "I already tried that".
3. **Inefficient:** the robot vacuum cleans some spots many times and misses others; a mapping robot cleans in neat rows in less time.
4. **Brittle outside its rules:** a situation no rule covers gets the default action, which may be wrong.
5. **Can get stuck in loops:** two rules can trigger each other back and forth (bump, turn, bump, turn in a corner).
6. **Rule explosion:** as needs grow, hundreds of rules start interacting in surprising ways and become hard to maintain.
7. **No global optimisation:** it chooses a locally good action, never the best plan overall.

---

## 6. When to choose it (decision guide)

**Choose reactive when:**
- The response must be immediate (safety, real-time systems, every-request checks).
- Situations are well understood and can be covered by clear rules.
- The task is single-step: classify, filter, route, block, alert.
- Cost per request must be tiny.

**Don't choose reactive when:**
- The task needs several dependent steps or a plan.
- The agent must remember context, past attempts or user history.
- Efficiency over the whole task matters (routes, schedules, optimisation).
- The environment produces lots of unseen situations.

**In practice:** reactive is usually the **bottom layer** of a layered system, not the whole system. Modern robot vacuums keep reactive safety reflexes (cliff, bump) but add a deliberative layer (room mapping and route planning) on top. LLM systems do the same: reactive guardrails and routers in front, an agent loop and planner behind them.

---

## 7. Full stack analogy

| Reactive agent | What I already use |
|----------------|--------------------|
| Condition-action rule | Event handler (`onClick`, `@EventListener`) |
| Priority order of behaviours | Middleware chain / Spring filter chain order |
| Safety rule that overrides all | Auth middleware that rejects before anything else runs |
| Stateless decisions | Stateless REST endpoints, horizontally scaled |

---

## 8. FDE angle

When a client wants "an AI agent", check whether a reactive design solves it first. Fraud flags, ticket routing, content moderation and alerting are often best as fast, cheap, auditable reactive layers, with an LLM used only as the classifier. Proposing the simpler design builds trust, cuts cost, and ships faster. Then add deliberative layers only where multi-step reasoning truly pays off.

---

## 9. Self-test

1. Why can a robot vacuum clean a room without a map?
2. In the code sketch, why is the cliff check first?
3. Give two real-world systems where reactive is the only sensible choice, and why.
4. Give two tasks where reactive would fail, and what architecture you would use instead.
5. How can a reactive agent get stuck in a loop, and how would a layered design fix it?
6. Is an LLM-based intent router reactive or deliberative? Justify it.
