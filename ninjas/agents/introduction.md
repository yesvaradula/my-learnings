# Introduction: Agentic Architecture (Reactive, Deliberative, Layered, CoALA)

**Class:** Coding Ninjas, Sat Oct 17 2026, "Agent architecture and design patterns".
**Goal:** know the classic agent architecture families, how they map to LLM agents, and the CoALA framework for describing any language agent.

---

## 1. What is agentic architecture?

**Agentic architecture** is the blueprint of an agent: how it **perceives** (inputs), **decides** (reasoning/planning), **acts** (tools/outputs), **remembers** (memory), and how control flows between those parts.

Same idea as software architecture (MVC, microservices, event-driven), but the "controller" is an autonomous decision-maker that pursues a goal.

```
Environment --percepts--> [ Perception -> Memory/State -> Decision -> Action ] --actions--> Environment
                                         ^                                |
                                         +----------- feedback -----------+
```

The core design question every architecture answers differently:
**How much does the agent think before it acts?**

- Not at all -> **Reactive**
- A lot -> **Deliberative**
- Both, at different levels -> **Layered (hybrid)**
- CoALA is not a fourth "type"; it's a **framework for describing LLM agents** (memory + actions + decision loop).

---

## 2. Reactive architecture

**Idea:** stimulus -> response. Condition-action rules map the current input directly to an action. No world model, no planning, little or no memory.

```
percept --> if condition then action --> act
```

- Origin: Rodney Brooks' **subsumption architecture** (MIT, mid-1980s) for robots. Simple behaviours stacked so higher ones can override (subsume) lower ones, e.g. "avoid obstacle" overrides "wander".
- **Pros:** very fast, cheap, robust, predictable, easy to test.
- **Cons:** no foresight, can't handle multi-step goals, can't learn from history, breaks on situations the rules don't cover.
- **Examples:** thermostat, robot vacuum bump-and-turn, spam filter, FAQ bot, an LLM router/classifier, a guardrail that blocks PII.
- **Full stack analogy:** an event handler or middleware: `onClick -> do X`. No memory of the session.

---

## 3. Deliberative architecture

**Idea:** think, then act. The agent keeps an internal **world model**, reasons about goals, **plans** a sequence of actions, then executes.

```
Sense -> update world model -> Plan (search over options) -> Act
```

- Classic form: **Sense-Plan-Act**, symbolic planners like STRIPS.
- Best-known model: **BDI (Belief-Desire-Intention)**:
  - **Beliefs:** what the agent thinks is true about the world.
  - **Desires:** goals it would like to achieve.
  - **Intentions:** the goals/plans it has committed to right now.
- **Pros:** handles complex multi-step goals, explainable (you can read the plan), optimises over options.
- **Cons:** slow, expensive, plan goes stale if the world changes, the world model can be wrong.
- **LLM examples:** plan-and-execute agents, tree-of-thought style search, an agent that writes a full migration plan before touching code.
- **Full stack analogy:** a query planner in a DB, or a CI pipeline definition built before it runs.

---

## 4. Layered (hybrid) architecture

**Idea:** combine both. A fast **reactive layer** handles immediate situations, a **deliberative layer** handles planning, often with a middle layer that coordinates them.

Typical three layers:
| Layer | Job | Speed |
|-------|-----|-------|
| Reactive (bottom) | Immediate responses, safety reflexes | Milliseconds |
| Executive / sequencing (middle) | Turns plans into steps, monitors progress, handles errors | Seconds |
| Deliberative (top) | Long-term goals, planning, reflection | Slow |

Two ways to wire layers:
- **Horizontal layering:** every layer sees the input and proposes an action; a mediator picks one. (e.g. TouringMachines.) Flexible, but the mediator gets complex.
- **Vertical layering:** input goes up through layers and control comes back down; each layer only talks to its neighbours. (e.g. InteRRaP.) Simpler control, but one broken layer blocks the chain.
- Robotics example: the **3T (three-tier)** architecture.

- **Pros:** fast when it needs to be, smart when it has time. Most real systems end up here.
- **Cons:** more complex; layers can conflict and need clear priority rules.
- **LLM example:**
  - Reactive: input guardrails, cache hits, a router sending FAQs to a canned answer.
  - Executive: the agent loop running tool calls, retries, step limits.
  - Deliberative: a planner model that decomposes the task and a reflection step that critiques results.
- **Full stack analogy:** CDN/cache (reactive) -> API service layer (executive) -> batch analytics/ML jobs (deliberative).

---

## 5. CoALA: Cognitive Architectures for Language Agents

Paper: Sumers, Yao, Narasimhan, Griffiths, 2023 (arXiv 2309.02427). Borrows from classic cognitive architectures (Soar, ACT-R) to give a **common vocabulary for describing any LLM agent**.

CoALA describes an agent with three parts:

### 5.1 Memory
| Memory | Holds | LLM implementation |
|--------|-------|--------------------|
| **Working memory** | Current state: goal, recent observations, intermediate reasoning | The prompt / context window, agent state variables |
| **Episodic** (long-term) | Past experiences: what happened in earlier runs | Logged trajectories, reflections in a DB |
| **Semantic** (long-term) | Facts about the world and the user | Vector store / knowledge base (RAG) |
| **Procedural** (long-term) | How to do things | The LLM weights themselves + the agent's code and prompts |

### 5.2 Action space
- **External actions (grounding):** affect the outside world: call APIs, browse, run code, talk to the user, move a robot.
- **Internal actions:** affect only the agent's own memory:
  - **Retrieval:** read from long-term memory into working memory.
  - **Reasoning:** use the LLM to process working memory and write new thoughts back to it.
  - **Learning:** write to long-term memory (save an episode, add a fact, even update code/prompts).

### 5.3 Decision-making procedure
A repeating loop (decision cycle):
1. **Planning stage:** use reasoning and retrieval to **propose** candidate actions, **evaluate** them, **select** one.
2. **Execution stage:** perform the selected action (internal or external), observe the result, update working memory.
3. Repeat.

```
          +------------------ Long-term memory -------------------+
          |   Episodic        Semantic        Procedural          |
          +-------^---------------^----------------^--------------+
        retrieve/learn      retrieve/learn      (weights + code)
                  |               |
            +-----v---------------v-----+
            |       Working memory      | <-- reasoning (LLM)
            +-------------+-------------+
                          | decision cycle: propose -> evaluate -> select
                          v
                 External actions (tools, APIs, user)  <-->  Environment
```

**Why CoALA matters:** it lets you compare agents on the same axes. Examples:
- **ReAct:** working memory + reasoning + external actions; no long-term memory.
- **Reflexion:** adds episodic memory (stored reflections) and a learning action.
- **RAG chatbot:** semantic memory + retrieval, one-step decision, almost no planning.
- **Voyager (Minecraft agent):** procedural memory that grows by writing new skill code.

---

## 6. Comparison

| | Reactive | Deliberative | Layered | CoALA |
|--|----------|--------------|---------|-------|
| What it is | Architecture type | Architecture type | Architecture type | Descriptive framework for LLM agents |
| Thinks before acting? | No | Yes, plans first | Both, by layer | Explicit decision cycle (propose/evaluate/select) |
| World model / memory | None or minimal | Rich internal model | Per layer | Working + episodic + semantic + procedural |
| Speed | Fastest | Slowest | Mixed | Depends on design |
| LLM example | Router, guardrail | Plan-and-execute | Guardrails + agent loop + planner | Any agent, described in its terms |

---

## 7. FDE angle

When a client says "build us an agent", decide the architecture with them:
- Need sub-second, predictable answers (fraud check, routing)? Mostly **reactive**.
- Long, multi-step, high-value tasks (report generation, migrations)? **Deliberative** with a reviewable plan.
- Real production systems: **layered**. Reactive guardrails and caches up front, an agent loop in the middle, planning/reflection on top.
- Use **CoALA** as a checklist in the design doc: what's in working memory, which long-term memories exist, which internal and external actions are allowed, and how the agent picks the next action. It makes reviews and security discussions concrete.

---

## 8. Class notes (fill during class)

- 
- 

---

## 9. Self-test

1. Define agentic architecture in one sentence.
2. Give one strength and one weakness each of reactive and deliberative agents.
3. What do Beliefs, Desires and Intentions mean in BDI?
4. Horizontal vs vertical layering: how does control flow differ?
5. Name CoALA's four memory types with an LLM implementation for each.
6. In CoALA, is "searching the vector DB" an internal or external action? What about "calling a weather API"?
7. Describe Reflexion in CoALA terms.
