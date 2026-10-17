# 01. Agent Architecture

**Class:** Coding Ninjas, Sat Oct 17 2026, "Agent architecture and design patterns".
**Goal:** understand what an agent is made of, how the loop runs, and where it breaks, well enough to design one for a client.

> Frameworks and APIs (LangGraph, CrewAI, OpenAI/Anthropic SDKs, MCP) move fast. Concepts here are stable; check versions and API names on the live docs before coding.

---

## 1. Intuition first: what is an agent?

A plain LLM call is a function: text in, text out, once.

An **agent** is an LLM placed in a **loop**, given **tools** it can call, and **state/memory** it can read and update, so it can decide its own next step until the goal is done.

```
Agent = LLM (the brain) + Tools (the hands) + Memory (the notebook) + Loop (the heartbeat) + Guardrails (the rules)
```

Mental model from full stack work: the LLM is a **controller** that, instead of following hard-coded routes, *chooses* which "endpoint" (tool) to hit next based on the current state.

### Workflow vs agent (the most important distinction)

| | Workflow | Agent |
|--|---------|-------|
| Who decides the steps? | **You**, in code (fixed path) | **The LLM**, at runtime (dynamic path) |
| Predictability | High | Lower |
| Cost / latency | Lower, bounded | Higher, can loop |
| Debugging | Easy, like normal code | Harder, needs tracing |
| Use when | Task can be broken into known steps | Steps can't be known in advance (open-ended) |

**Rule of thumb:** start with the simplest thing (single LLM call with good prompt + retrieval), move to a workflow, and only use a full agent when the path truly can't be predicted. Most "agent" problems in production are really workflows.

---

## 2. Core components

### 2.1 The model (reasoning engine)
- Interprets the goal, decides the next action, writes the final answer.
- Choice trade-off: a strong model for planning/reasoning, a cheaper/faster model for simple sub-steps (classification, extraction, summarising).

### 2.2 Tools (actions)
- Functions the model can call: search, DB query, REST API, code execution, file read/write, sending email.
- Each tool is described to the model by **name + description + input schema (JSON Schema)**. The model returns a structured "call this tool with these args"; **your code** executes it and feeds the result back.
- The model never runs the tool itself. This boundary is where you put auth, validation and permissions.
- Good tool design matters more than prompt tricks:
  - Clear names and descriptions (write them like docs for a new junior dev).
  - Few, well-scoped tools beat many overlapping ones.
  - Return concise, useful output (not 5 MB of raw JSON).
  - Return helpful error messages so the model can recover.
- **MCP (Model Context Protocol):** an open standard for exposing tools/resources to any agent client. Think "USB-C for tools": write the integration once as an MCP server, reuse it across agents.

### 2.3 Memory
| Type | What it holds | Implementation |
|------|---------------|----------------|
| **Short-term / working** | Current conversation, tool results, scratchpad | The context window (messages list) |
| **Long-term** | Facts, user preferences, past tasks | Vector DB, key-value store, SQL, files |
| **Episodic** | What happened in past runs (successes/failures) | Logged trajectories, summaries |
| **Semantic / knowledge** | Domain documents | RAG over a document store |
| **Procedural** | How to do things | System prompt, instructions, skills |

Context window is finite. Techniques: summarise old turns, trim tool outputs, retrieve only what's relevant, store big artefacts outside and pass references.

### 2.4 Planning / reasoning
- **Implicit:** model decides one step at a time (ReAct style).
- **Explicit:** model writes a plan first, then executes steps (plan-and-execute), possibly re-planning.
- **Reflection:** model critiques its own output and retries.
Details in `02_design_patterns.md`.

### 2.5 The control loop (orchestrator / runtime)
Your code that runs the loop, executes tools, enforces limits, and decides when to stop.

### 2.6 Guardrails and observability
- Input/output validation, PII filters, allow-listed tools, permission checks, human approval for risky actions.
- Tracing of every step (prompt, tool call, result, tokens, latency), so you can debug and evaluate.

---

## 3. The agent loop

```
messages = [system_prompt, user_goal]

for step in range(MAX_STEPS):              # hard stop, never loop forever
    response = llm(messages, tools=TOOLS)

    if response.wants_tool_call:
        for call in response.tool_calls:
            check_permissions(call)        # guardrail at the boundary
            result = run_tool(call.name, call.args)
            messages.append(tool_result(call.id, result))
    else:
        return response.final_answer       # model says it's done

return "stopped: step limit reached"       # fail safely, escalate to a human
```

Stopping conditions to always have: final answer produced, max steps, max tokens/cost, timeout, and a human-escalation path.

---

## 4. Architecture of a production agent system

```
          User / client app (React, Slack, API)
                         |
                   API gateway / auth
                         |
              +----------v-----------+
              |  Agent orchestrator  |  loop, state, retries, limits
              +--+------+------+-----+
                 |      |      |
           LLM API   Tools   Memory
           (router:  (MCP /  (vector DB,
           big/small  REST /  session store,
           models)    DB)     cache)
                 |
     Guardrails + tracing + evals + cost tracking
```

Layers to think about when designing for a client:
1. **Interface:** chat UI, API, background job, event trigger.
2. **Orchestration:** framework (LangGraph, CrewAI, SDK-native) or plain code. Plain code is often enough and easier to debug.
3. **Model layer:** provider, model routing, fallback on outage, rate limits.
4. **Tool layer:** integrations with client systems (CRM, ERP, DBs) with least-privilege credentials.
5. **Data/memory layer:** RAG store, session state, long-term memory.
6. **Safety layer:** guardrails, human-in-the-loop approvals, audit log.
7. **Ops layer:** tracing, evals, cost dashboards, versioned prompts.

---

## 5. Real-world gotchas

- **Infinite loops / runaway cost:** always cap steps, tokens and money per run.
- **Compounding errors:** a 95%-accurate step repeated 10 times is about 60% end-to-end. Fewer steps = more reliable.
- **Tool misuse:** vague tool descriptions make the model pick the wrong tool or invent arguments. Validate args against the schema.
- **Prompt injection:** content from web pages, emails or documents can contain instructions. Treat tool output as data, never as commands; limit what tools can do.
- **Context bloat:** dumping huge tool outputs crowds out the goal. Summarise and paginate.
- **Non-determinism:** same input, different path. You need evals (test sets + graders), not just unit tests.
- **Latency:** each loop step is a model call. Parallelise independent calls, use smaller models for simple steps, stream output to the UI.
- **Irreversible actions:** deleting data, sending emails, payments need human approval or a dry-run mode.

---

## 6. Mapping to things I already know

| Agent concept | Full stack / Java equivalent |
|---------------|------------------------------|
| Tool schema | OpenAPI spec / DTO with validation |
| Tool execution | Controller calling a service |
| Orchestrator loop | Event loop / state machine / workflow engine |
| Short-term memory | Request-scoped state / Redux store |
| Long-term memory | Database + cache |
| Guardrails | Middleware, auth filters, input validation |
| Tracing | Distributed tracing (OpenTelemetry), logs |
| Multi-agent | Microservices talking to each other |

---

## 7. FDE angle

An FDE at a client site is asked: "Can we build an agent for X?" The senior answer:
1. Clarify the task and success metric (time saved, accuracy, tickets resolved).
2. Ask: can this be a single call or a fixed workflow? Pick the simplest design that works.
3. Inventory the client's systems: which tools (APIs, DBs) the agent needs, and what auth and permissions exist.
4. Define the risk boundary: what the agent may do alone vs what needs human approval.
5. Ship a thin vertical slice fast, with tracing and a small eval set from day one.
6. Iterate on tools and prompts using real failures from traces.

---

## 8. Class notes (fill during class)

- 
- 
- 

---

## 9. Self-test

1. What four or five parts make up an agent?
2. When should you choose a workflow instead of an agent?
3. Who actually executes a tool call: the model or your code? Why does that matter for security?
4. Name three stopping conditions for an agent loop.
5. Why do long multi-step agents become unreliable? How do you reduce that?
6. What is MCP, and what problem does it solve?
7. How would you defend an agent that reads customer emails against prompt injection?
