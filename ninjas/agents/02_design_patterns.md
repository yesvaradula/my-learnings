# 02. Agent Design Patterns

**Class:** Coding Ninjas, Sat Oct 17 2026, "Agent architecture and design patterns".
**Goal:** know the standard patterns, when each fits, and its cost, so I can pick one for a real use case in minutes.

> Names vary between courses and frameworks (e.g. "supervisor" vs "orchestrator"). Focus on the shape of the control flow, not the label.

---

## 1. The ladder: go up only when needed

```
Single LLM call  ->  Augmented LLM (+ retrieval, tools, memory)
                 ->  Workflows (code decides the path)
                 ->  Single agent (LLM decides the path)
                 ->  Multi-agent (several LLMs coordinate)
```
Each step up adds flexibility but also cost, latency and failure modes.

---

## 2. Building block: the augmented LLM

One model call, enhanced with **retrieval** (RAG), **tools**, and **memory**. Every pattern below is built from this block.

---

## 3. Workflow patterns (path defined in code)

### 3.1 Prompt chaining
```
input -> LLM A -> [gate/check] -> LLM B -> LLM C -> output
```
- Break a task into fixed sequential steps; each step's output is the next step's input. Add programmatic checks ("gates") between steps.
- **Use when:** task cleanly splits into steps. Example: write outline -> check outline -> write article -> translate.
- **Trade-off:** more latency, much better accuracy per step.

### 3.2 Routing
```
input -> classifier LLM -> route A (billing handler)
                        -> route B (tech support handler)
                        -> route C (refund handler)
```
- Classify the input, send it to a specialised prompt/model/tool set.
- **Use when:** distinct input categories need different handling. Also for cost: easy queries to a small model, hard ones to a large model.

### 3.3 Parallelization
Two variants:
- **Sectioning:** split the task into independent subtasks run at the same time. Example: one call answers the user, another checks for policy violations.
- **Voting:** run the same task several times and combine (majority, or "flag if any says unsafe"). Example: code vulnerability review by 3 prompts.
- **Use when:** subtasks are independent (speed) or you need higher confidence (voting).
- Same idea as `Promise.all` / `CompletableFuture.allOf`.

### 3.4 Orchestrator-workers
```
input -> orchestrator LLM -> decides subtasks dynamically
              |-> worker 1 --|
              |-> worker 2 --|-> orchestrator synthesises -> output
              |-> worker N --|
```
- Like parallelization, but the **subtasks are not known in advance**; the orchestrator decides them per input.
- **Use when:** e.g. a coding change touching an unknown number of files, or research across unknown sources.

### 3.5 Evaluator-optimizer (generator-critic)
```
generator LLM -> draft -> evaluator LLM -> feedback -> generator -> ... until pass / max rounds
```
- One model produces, another (or the same with a different prompt) grades against clear criteria and gives feedback.
- **Use when:** there are clear evaluation criteria and iteration measurably improves output (translation, code that must pass tests, writing to a rubric).

---

## 4. Agent patterns (LLM decides the path)

### 4.1 Tool use / function calling
The base agent skill: model picks a tool and arguments, code executes, result goes back. All patterns below rely on it.

### 4.2 ReAct (Reason + Act)
```
Thought: I need the order status.
Action: get_order(id=123)
Observation: {status: "shipped", eta: "Oct 20"}
Thought: Now I can answer.
Final answer: Your order shipped and arrives Oct 20.
```
- Interleave reasoning and actions, one step at a time, adapting to each observation. (Yao et al., 2022.)
- **Pros:** adaptive, simple. **Cons:** can wander, many calls, no big-picture plan.

### 4.3 Plan-and-execute
```
planner LLM -> plan [step1, step2, step3]
executor runs each step (can be a smaller model / ReAct agent)
re-planner updates the plan if results change things
```
- **Pros:** fewer expensive calls, clearer progress, plan is reviewable by a human.
- **Cons:** plan can go stale; needs a re-plan step.
- **Use when:** long tasks with many steps (migrations, research reports).

### 4.4 Reflection / self-critique
- Agent reviews its own output or failed attempt, writes what went wrong, and retries with that lesson. ("Reflexion", Shinn et al., 2023, stores reflections in memory across attempts.)
- Works best with an **external signal** (tests fail, schema invalid, grader score) rather than pure self-opinion.

### 4.5 Human-in-the-loop
- Agent pauses for approval before risky actions (send, delete, pay, deploy), or asks for clarification when unsure.
- Implement as an interrupt/checkpoint in the state machine; resume after approval.
- Essential for enterprise adoption: trust is built step by step.

### 4.6 Memory patterns
- **Conversation summary buffer:** keep recent turns verbatim, summarise older ones.
- **Retrieval memory:** store facts/episodes in a vector DB, retrieve relevant ones per step.
- **Scratchpad / notes file:** agent writes progress notes to an external file or state, so long tasks survive context limits.

---

## 5. Multi-agent patterns

Split work across specialised agents, each with its own prompt, tools and context.

| Pattern | Shape | Use when |
|---------|-------|----------|
| **Supervisor (manager)** | One coordinator agent delegates to specialist agents and combines results | Clear specialist roles (researcher, coder, reviewer) |
| **Hierarchical** | Supervisors of supervisors (teams of teams) | Large tasks with sub-domains |
| **Sequential / pipeline** | Agent A -> Agent B -> Agent C, like an assembly line | Fixed handoff order (draft -> review -> publish) |
| **Handoff / swarm** | Agents transfer control to each other directly based on need | Customer support: triage agent hands off to billing agent |
| **Debate / group chat** | Agents discuss or argue, a judge decides | Exploring options, reducing single-model bias |

- **Why multi-agent:** separation of concerns, smaller focused contexts, parallel work, different models per role.
- **Cost:** many more tokens, coordination failures, harder debugging. Use only when one agent with good tools clearly isn't enough.
- **Protocols:** MCP connects agents to tools; A2A (Agent2Agent) is a protocol for agents to talk to other agents. Check current status before relying on either in a client design.

---

## 6. Cross-cutting patterns (apply to any of the above)

- **Guardrails:** validate input (injection, off-topic) and output (PII, policy, schema) often with a cheap model in parallel.
- **Structured output:** force JSON matching a schema for anything downstream code consumes.
- **Model routing / fallback:** small model for easy work, large for hard; fallback provider on outage.
- **Caching:** cache tool results and prompt prefixes to cut cost and latency.
- **Checkpointing:** persist agent state after each step so a crash or human pause can resume.
- **Tracing + evals:** log every step; keep a test set of real tasks and grade runs automatically.

---

## 7. Choosing a pattern: cheat sheet

| Situation | Pattern |
|-----------|---------|
| Fixed, known steps | Prompt chaining |
| Different input types need different handling | Routing |
| Independent subtasks, need speed | Parallelization (sectioning) |
| Need higher confidence | Parallelization (voting) |
| Subtasks unknown until you see the input | Orchestrator-workers |
| Clear quality criteria, iteration helps | Evaluator-optimizer |
| Open-ended, needs to adapt step by step | ReAct agent |
| Long multi-step task, plan should be reviewable | Plan-and-execute |
| Risky or irreversible actions | Human-in-the-loop |
| Distinct expert roles, one agent overloaded | Supervisor multi-agent |

---

## 8. Example: designing for a client

**Ask:** "Automate our customer support email triage and replies."

1. **Routing:** classify email (billing / technical / refund / spam).
2. **Augmented LLM per route:** RAG over help docs + tools (`get_order`, `get_invoice`).
3. **Evaluator-optimizer:** check draft against tone and policy rubric.
4. **Guardrails:** PII filter, injection check on the incoming email (it is untrusted input).
5. **Human-in-the-loop:** refunds above a threshold need an agent's approval before sending.
6. **Tracing + evals:** 100 real historical emails as the eval set; measure accuracy and resolution rate.

Note: this is mostly a **workflow**, not a free agent. That's the point: predictable, cheap, auditable.

---

## 9. Class notes (fill during class)

- Patterns the instructor covered:
- Framework used in class:
- Code / demo notes:
- Questions to ask:

---

## 10. Self-test

1. What's the difference between parallelization and orchestrator-workers?
2. When does evaluator-optimizer actually help, and when is it wasted cost?
3. ReAct vs plan-and-execute: give one use case for each.
4. Why add human-in-the-loop even when the agent is accurate?
5. Supervisor vs handoff multi-agent: how does control flow differ?
6. Design the pattern stack for: "an agent that reads a Jira ticket, changes the code, runs tests, and opens a PR."
