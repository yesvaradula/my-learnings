# Deliberative Architecture: Real-World Examples, Why and Why Not

**Class:** Coding Ninjas, Sat Oct 17 2026, "Agent architecture and design patterns".
**Related:** `introduction.md` (section 3, deliberative architecture), `reactive_architecture.md` (the opposite approach).

---

## 1. Quick recap

A **deliberative agent** thinks before it acts. It keeps an internal **world model**, reasons about its **goal**, builds a **plan** (a sequence of actions), then executes it.

```
Sense -> update world model -> Plan (search over options) -> Act -> (re-plan if needed)
```

- Classic form: **Sense-Plan-Act**, with symbolic planners such as STRIPS.
- Common mental model: **BDI (Belief-Desire-Intention)**:
  - **Beliefs:** what the agent thinks is true about the world (the world model).
  - **Desires:** the goals it wants to reach.
  - **Intentions:** the plan it has committed to right now.
- Historical example: **Shakey the robot** (SRI, late 1960s), the first mobile robot to reason about its actions, using the STRIPS planner. It was very slow: it could take a long time to plan a move across a room.

---

## 2. Main real-world example: GPS navigation (Google Maps style)

### How it maps to deliberative architecture

| Part | In GPS navigation |
|------|-------------------|
| **World model (beliefs)** | Road network as a graph (junctions = nodes, roads = edges), speed limits, live traffic, closures |
| **Goal (desire)** | Reach the destination, usually as fast as possible |
| **Planning** | Search the graph for the best route (shortest-path algorithms like Dijkstra or A*, with travel time as edge weights) |
| **Plan (intention)** | The ordered list of turns: "in 500 m turn left, then keep right..." |
| **Execution** | Give turn-by-turn instructions while tracking your position |
| **Re-planning** | You miss a turn or traffic changes -> update the model and compute a new route |

### Why it must be deliberative
A reactive rule like "at each junction, turn towards the destination" can lead into dead ends, one-way streets and traffic jams. You only get a good route by **looking ahead over the whole map** before driving. That is deliberation.

### Simple code sketch

```python
def deliberative_agent(goal):
    world = build_world_model()                # beliefs: map, traffic
    plan = search_best_plan(world, goal)       # e.g. A* over the road graph

    while plan:
        step = plan.pop(0)
        act(step)                              # execute the next turn
        world.update(read_sensors())           # GPS position, new traffic
        if not plan_still_valid(plan, world):  # missed turn / jam ahead
            plan = search_best_plan(world, goal)   # re-plan
```

Note: the costly part is `search_best_plan`. It looks at many possible futures before committing to one. That is where both the intelligence and the slowness come from.

---

## 3. More real-world examples

| Example | World model | What it plans |
|---------|-------------|---------------|
| **Chess engine** | Board state + rules | Searches many moves ahead, picks the best line |
| **Warehouse robots (Amazon-style)** | Warehouse grid, shelf and robot positions | Collision-free paths and task order for many robots |
| **Mapping robot vacuum** | A map of your home built with sensors | Efficient row-by-row cleaning route |
| **Mars rovers** | Terrain maps from cameras | Safe driving paths and daily activity plans (with communication delay, it can't be remote-controlled live) |
| **Airline crew / flight scheduling** | Aircraft, crews, rules, airports | A full schedule meeting rest rules and minimising cost |
| **Supply-chain / logistics planning** | Stock, demand, routes, trucks | Delivery routes and stock movements |
| **LLM plan-and-execute agent** | Task description, repo or data context | A step-by-step plan, then executes each step |
| **AI coding agent on a big change** | The codebase | "Edit these 5 files, run tests, then update docs" before touching code |

LLM note: the "world model" is often the context (documents, codebase, tool results) and the "planner" is the LLM writing an explicit plan. The plan can be shown to a human for approval before execution.

---

## 4. Why use deliberative architecture (pros)

1. **Handles complex, multi-step goals:** it can chain dependent steps (flight, then hotel near the venue, then a cab).
2. **Better overall results:** it compares options and picks a good global plan, not just a locally good move (shortest route vs "turn towards the destination").
3. **Foresight:** it avoids traps by thinking ahead (chess, dead-end roads).
4. **Explainable and reviewable:** the plan is visible. A human can read, approve or edit it before anything happens. Big plus for enterprise trust and audits.
5. **Uses knowledge and memory:** it builds on what it knows about the world and past states.
6. **Efficient execution:** once the plan is good, execution wastes fewer actions (mapping vacuum vs random bouncing).

## 5. Why not use it (cons)

1. **Slow:** planning takes time. Bad for split-second situations (an airbag can't "plan").
2. **Expensive:** search and reasoning cost CPU or, for LLM agents, many tokens and model calls.
3. **Depends on an accurate world model:** if the model is wrong (closed road not on the map, outdated docs), the plan is confidently wrong.
4. **Plans go stale:** the world changes during execution; without re-planning, the agent follows a plan that no longer fits.
5. **Complex to build:** world modelling, search, re-planning and plan validation are hard engineering.
6. **Search explodes:** the number of possible plans grows very fast with each step (combinatorial explosion), so you need heuristics and limits.
7. **For LLM agents:** long plans can contain hallucinated or impossible steps; each step still needs validation.

---

## 6. When to choose it (decision guide)

**Choose deliberative when:**
- The task has several dependent steps and the order matters.
- Choosing the best overall option matters (routes, schedules, resource allocation).
- There is time to think before acting (seconds to minutes are acceptable).
- A human should review the plan before execution (high-value or risky work).
- You have a reasonably accurate model of the environment.

**Don't choose deliberative when:**
- The response must be instant (safety reflexes, every-request filtering).
- The environment changes faster than you can plan.
- The task is a single simple decision (classify, route, block): it is overkill and wastes cost.
- You can't build or trust a world model.

**In practice:** pure deliberative systems are rare today. Real systems are **layered**: a deliberative layer plans (route, task plan), and a reactive layer handles the instant stuff (emergency braking in a self-driving car, guardrails in an LLM system). Even GPS navigation is effectively hybrid, because it keeps re-planning as the world changes.

---

## 7. Reactive vs deliberative side by side

| | Reactive | Deliberative |
|--|----------|--------------|
| Thinks ahead? | No | Yes |
| World model | None | Central |
| Speed | Milliseconds | Seconds to minutes |
| Cost | Very low | Higher |
| Handles multi-step goals | No | Yes |
| Plan quality | Locally OK | Globally good |
| Robust to sudden change | Very | Only with re-planning |
| Example | Robot vacuum bump-and-turn, airbag | GPS route planning, chess engine |

---

## 8. Full stack analogy

| Deliberative agent | What I already use |
|--------------------|--------------------|
| World model | Database schema + statistics |
| Planner | SQL query planner (chooses the cheapest execution plan before running) |
| Plan review | `EXPLAIN` / `EXPLAIN ANALYZE` output, or a reviewed DB migration plan |
| Re-planning | Retry with a new strategy, circuit breaker with fallback |
| Plan-then-execute | CI/CD pipeline: stages defined first, then executed in order |

---

## 9. FDE angle

Clients love deliberative agents for **high-value, multi-step work** (report generation, data migrations, onboarding workflows) because the plan can be shown and approved before anything changes. The FDE move: make the plan visible, add a human approval checkpoint before risky steps, validate each step's result, and re-plan when something fails. Keep reactive guardrails in front for speed and safety, and mention the cost and latency trade-off openly when setting expectations.

---

## 10. Self-test

1. What are the Beliefs, Desires and Intentions in a GPS navigation app?
2. Why does "always turn towards the destination" fail as a navigation strategy?
3. What happens to a deliberative agent when its world model is wrong? Give an example.
4. Why is re-planning important, and what does it cost?
5. Give one task where deliberative is clearly better than reactive, and one where it is clearly worse.
6. How would you turn an LLM agent into a deliberative one? What would you add for safety?
