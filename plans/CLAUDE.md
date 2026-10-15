# plans: roadmaps, goals and ideas

Theme: this folder is where Sai **thinks before he builds**. Career roadmaps, goal tracking, and product ideas live here, as plain Markdown, so a plan can be read, challenged and revised before any code or course time is spent.

Everything here ties back to the main goal: **Forward Deployed Engineer (FDE)**: rapid client deployment, enterprise architecture, customer-facing engineering impact.

## Layout

| Folder | Purpose | Status |
|--------|---------|--------|
| `ideas/` | Product / project ideas worth building, one subfolder per idea | active |
| `ideas/5amclub/` | Web app that encourages people to wake up at 5 AM (PWA on GCP) | v0 plan drafted: `00_idea.md`, open questions in section 15 |

Planned, not created yet: `roadmaps/` (e.g. the FDE roadmap built around his weekly schedule) and `goals/` (milestones and trackers).

## Conventions
- One subfolder per idea or plan; files numbered `00_`, `01_`, ... in reading order. `00_idea.md` is the idea/vision doc.
- A plan doc has: vision, user/problem, MVP vs later, stack, risks, roadmap, **open questions for Sai**. Mark guesses as **[assumption]** until he confirms.
- Product ideas should double as **FDE portfolio proof**: deployed URL, README, CI/CD, short architecture doc, demo script.
- Prefer his stack (React, Angular, Node.js, Java) and the cloud he is learning (GCP first, see `binary-cloud/`).
- Schedule-aware: build in his free windows (weekday 2 pm-9:30 pm, Tue/Thu after 9:30 am, weekend afternoons/evenings); never plan over Scalar, Coding Ninjas or Binary Cloud classes.
- Be honest about risk and sleep: no plan that silently assumes he can add hours he does not have.
- Saved notes stay emoji-free; chat can be as playful as ever.

## Rules for Claude
- Read this file first when working in `plans/`; update the layout table and status at the end of the session.
- When an idea moves from planning to building, say where the code will live (new folder at the repo root or a separate repo) and link it here.
- Don't delete or rewrite his decisions; add changes as dated notes or a new numbered file.

## Open items
- Sai to answer the open questions in `ideas/5amclub/00_idea.md` (audience, hook, solo or co-build, name, web-only).
- Decide whether to create the FDE roadmap (`roadmaps/`) next.
