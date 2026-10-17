# Layered (Hybrid) Architecture: Real-World Examples, Why and Why Not

**Class:** Coding Ninjas, Sat Oct 17 2026, "Agent architecture and design patterns".
**Related:** `introduction.md` (section 4), `reactive_architecture.md`, `deliberative_architecture.md`, `bdi_architecture.md`, `guardrail.md`.

---

## 1. Quick recap

Reactive agents are fast but short-sighted. Deliberative agents are smart but slow. A **layered (hybrid) agent** stacks both, so each kind of decision is made by the layer suited to it:

| Layer | Job | Time scale | Thinks like |
|-------|-----|-----------|-------------|
| **Reactive (bottom)** | Reflexes, safety, immediate responses | Milliseconds | "Obstacle! Brake." |
| **Executive / sequencing (middle)** | Turns plans into steps, monitors progress, handles errors, switches behaviours | Seconds | "Next step: change lane, then take the exit." |
| **Deliberative (top)** | Goals, long-term planning, reasoning, reflection | Seconds to minutes | "Best route to the airport given traffic." |

Core rule: **the lower layer can always override the upper one in an emergency**, and the upper layer gives direction to the lower ones when things are calm.

Human analogy: you pull your hand off a hot pan before you even think (reactive), you follow the recipe step by step (executive), and you decide what to cook for the week (deliberative).

---

## 2. How layers are wired

### 2.1 Horizontal layering
```
            +--> Layer 3 (planning)  --+
 input  ----+--> Layer 2 (behaviour) --+--> MEDIATOR --> action
            +--> Layer 1 (reactive)  --+
```
- Every layer sees the input and proposes an action; a **mediator** (control system) picks one.
- Example: **TouringMachines** (Ferguson, early 1990s).
- **Pros:** layers are independent; easy to add a new one.
- **Cons:** the mediator gets complex, since n layers with many options each can conflict in many ways.

### 2.2 Vertical layering
```
 One-pass:   input -> L1 -> L2 -> L3 -> action          (like a pipeline)

 Two-pass:   input -> L1 -> L2 -> L3     (information flows up)
             action <- L1 <- L2 <- L3    (control flows down)
```
- Each layer talks only to its neighbours.
- Example: **InteRRaP** (Muller, 1990s), two-pass, with behaviour, local planning and cooperative planning layers.
- **Pros:** simpler interactions, fewer conflicts.
- **Cons:** a slow or broken layer blocks the chain; less fault tolerant.

### 2.3 Classic robotics example
- **3T (three-tier) architecture:** reactive skills at the bottom, a sequencer in the middle, a planner on top. Used in NASA-related robotics research.

---

## 3. Main real-world example: the self-driving car

| Layer | What it does | Example decisions |
|-------|-------------|------------------|
| **Reactive** | Hard real-time safety reflexes from sensor data | Automatic emergency braking when a pedestrian steps out; stability control; collision avoidance |
| **Executive** | Driving behaviours and manoeuvres | Lane keeping, keeping distance, overtaking, merging, stopping at signals |
| **Deliberative** | Mission and route planning | Route to destination, re-route around traffic, choose where to park |

### How they work together
1. The **deliberative** layer plans the route: "highway, exit 12, then left."
2. The **executive** layer turns that into manoeuvres: "move to the right lane, keep 2 seconds gap, take the exit."
3. The **reactive** layer watches constantly. A child runs into the road -> it **brakes immediately, overriding everything above**. No waiting for the planner.
4. Once safe, control returns upward: the executive resumes, and the planner re-plans if needed.

Why layered is the only sensible design here: pure reactive can't navigate a city; pure deliberative would still be "thinking" while the car hits the child.

### Simple code sketch

```python
def control_cycle(sensors, state):
    # 1. Reactive layer: runs every cycle, highest priority
    emergency = reactive_layer(sensors)          # e.g. obstacle within braking distance
    if emergency:
        return emergency                         # override everything above

    # 2. Deliberative layer: only when needed (no plan, plan invalid, goal changed)
    if state.plan is None or not plan_valid(state.plan, sensors):
        state.plan = deliberative_layer(state.goal, world_model(sensors))

    # 3. Executive layer: turns the current plan step into an action
    return executive_layer(state.plan, sensors)
```

Note the different speeds: the reactive check runs on every cycle, the planner runs rarely. That is how layered systems are both fast and smart.

---

## 4. More real-world examples

| Example | Reactive layer | Executive layer | Deliberative layer |
|---------|---------------|-----------------|--------------------|
| **Modern robot vacuum** | Cliff and bump sensors | Cleaning patterns, docking | Room map and route planning |
| **Drone / autopilot** | Stabilisation, collision avoidance | Holding altitude, following waypoints | Flight path / mission planning |
| **Warehouse robot** | Stop if a human is too close | Pick-and-place routines | Task assignment and path planning |
| **Video game AI** | Dodge, react to being hit | Behaviour trees (patrol, chase) | Strategy (where to attack, resource use) |
| **Human body** | Reflexes (knee jerk) | Habits, motor skills (walking, typing) | Conscious planning |
| **LLM customer support system** | Guardrails, cache, FAQ router | Agent loop: tool calls, retries, step limits | Planner + reflection for complex cases |
| **AI coding agent** | Command allow-list, secret scanning, sandbox | Edit-run-test loop | Task plan across files, re-plan on test failure |

---

## 5. Layered architecture for an LLM agent system

```
  User request
       |
  REACTIVE LAYER (fast, cheap, every request)
    - input guardrails: injection, PII, topic         (see guardrail.md)
    - cache / FAQ match -> instant answer
    - router: easy -> small model, hard -> agent
       |
  EXECUTIVE LAYER (the agent loop)
    - runs plan steps, calls tools, validates results
    - retries, timeouts, step/token/cost limits
    - human approval checkpoints
       |
  DELIBERATIVE LAYER (slow, expensive, only when needed)
    - planner: break the task into steps (plan-and-execute)
    - reflection: critique results, re-plan on failure
    - long-term memory: lessons from past runs
       |
  Output guardrails -> response
```

In practice, a large share of support traffic is often simple (FAQs, status checks) and can be handled by the cheap reactive layer, so only complex cases reach the expensive deliberative layer. That's a big cost and latency win. Measure the real split from the client's ticket data instead of assuming it.

---

## 6. Why use layered architecture (pros)

1. **Best of both worlds:** fast reactions and smart planning in one system.
2. **Safety first:** reactive safety rules always run and can override any plan.
3. **Cost and latency efficient:** cheap layers handle most cases; expensive reasoning only runs when needed.
4. **Robust:** if the planner fails or is slow, the lower layers keep the system safe and working (graceful degradation).
5. **Separation of concerns:** each layer can be built, tested and improved separately, by different teams.
6. **Matches reality:** most real problems mix instant decisions and long-term goals.

## 7. Why not use it (cons)

1. **More complex:** more components, more code, more to monitor.
2. **Layer conflicts:** the planner says "go", the reactive layer says "stop". Priority and hand-over rules must be designed carefully.
3. **Hard to design the boundaries:** what belongs in which layer is not always obvious.
4. **Harder to debug:** a bad outcome can come from the interaction between layers, not one layer.
5. **Communication overhead:** layers must share state and messages consistently.
6. **Overkill for simple tasks:** a single-step classifier doesn't need three layers.

---

## 8. When to choose it (decision guide)

**Choose layered when:**
- The system needs both instant reactions **and** multi-step goals.
- Safety or compliance rules must always hold, no matter what the planner decides.
- Traffic is mixed: many simple requests, some complex ones.
- It will run in production and has to survive failures gracefully.

**Don't choose layered when:**
- The task is a single simple decision (pure reactive is enough).
- It's an offline task with no time pressure and a stable environment (pure deliberative may be enough).
- It's an early prototype: start simple, add layers as real needs appear.

---

## 9. All three side by side

| | Reactive | Deliberative | Layered (hybrid) |
|--|----------|--------------|------------------|
| Thinks ahead? | No | Yes | Yes, in the top layer |
| Speed | Fastest | Slowest | Fast for reflexes, slow for plans |
| Cost | Lowest | Highest | Medium (pay for planning only when needed) |
| Multi-step goals | No | Yes | Yes |
| Safety in sudden events | Strong | Weak | Strong |
| Complexity | Low | Medium-high | Highest |
| Example | Airbag, spam filter | Chess engine, route planner | Self-driving car, production LLM agent |

---

## 10. Full stack analogy

| Layer | Web system equivalent |
|-------|----------------------|
| Reactive | CDN, cache, WAF, rate limiter, auth middleware: instant decisions on every request |
| Executive | API / service layer: handles business workflows step by step |
| Deliberative | Batch jobs, analytics, recommendation engines: slow, heavy reasoning |
| Lower layer overrides upper | WAF or rate limiter rejects a request before any business logic runs |
| Graceful degradation | Serve cached content when the backend is down |

Operating systems use the same idea: hardware interrupts (reactive), the scheduler (executive), and user-level applications and planning (deliberative).

---

## 11. FDE angle

When designing an agent for a client, a layered diagram is often the clearest thing you can show:
- **Bottom:** guardrails and fast paths that security and compliance teams can verify.
- **Middle:** the agent loop with limits and approval checkpoints that operations teams can monitor.
- **Top:** planning and reflection that deliver the business value.
It answers the three questions every client asks: is it safe (bottom), is it controlled (middle), is it smart (top)? It also gives a natural rollout plan: ship the reactive and executive layers first, then add deliberation where it pays off.

---

## 12. Self-test

1. What are the three typical layers, and what time scale does each work on?
2. Horizontal vs vertical layering: who decides the final action in each?
3. In a self-driving car, what happens when the planner and the emergency-braking layer disagree?
4. Why is layered architecture usually cheaper than pure deliberative for an LLM support system?
5. Give two cons of layered architecture and how you'd reduce them.
6. Draw the layers for an AI agent that monitors cloud infrastructure and fixes incidents. Which actions need human approval?
