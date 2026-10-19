# Scalar (DSA): Contest Prep, Progress & Context

Language: **Java**. Platform: Scaler (InterviewBit-style problems).
Format: timed coding contests, usually 4 problems in 90 minutes.

Paths below are from the repo root. Solutions: `scalar/tasks/{arrays,strings}/{basics,intermediate,advanced}/` and `scalar/tasks/bs/`. Write-ups: `scalar/problems/`.
Known compile failure: `scalar/tasks/arrays/advanced/MinimumSwaps.java` (`return 0` in `void main`, #31). No `package` lines; compile each file alone (`javac File.java`).
Renamed Oct 14: `FindMissingNumber (2)` → `arrays/basics/MissingNumberSum`; `FindMIssingNumber` → `arrays/intermediate/FirstMissingPositive` (#26); `WaterTrap (2)` deleted (wrong).

## Contests

| # | Date | Time | Topics | Result |
|---|------|------|--------|--------|
| 1 | 2026-10-07 | 7:00–8:30 am | Arrays, 2D Arrays, Bit Manipulation, Recursion, Math, Hashing, Sorting | 1/4 solved (Trapping Rain Water) |
| 2 | 2026-10-22 (Thu; moved Oct 20 from Wed Oct 21, originally Oct 13) | 9:30 pm (clashes with Binary Cloud 9:30-11: watch the recording) | Same topics, 4 new problems | Goal: solve all 4 |

### Contest 1: missed problems (review these)
1. **Bitville XOR pairs**: for each query bit `b`, count pairs whose b-th bits differ.
   Key idea: `answer = ones * zeros` for that bit; get a bit with `(x >> b) & 1`.
   Status: ✅ **SOLVED Oct 13** (6/6 PASS incl. 100k overflow test). TC O(N·Q), SC O(1) extra. Write-up: `scalar/problems/contest1-01-xor-pairs-bitville.md`.
   Tests: `[1,2,3]`, B=`[0,1]` → `[2,2]`; `[2,4,7,11]`, B=`[3]` → `[3]`; `[5]`, B=`[0]` → `[0]`; `[1,1,1]`, B=`[0,1]` → `[0,0]`; `[1,2,4,8]`, B=`[0,31]` → `[3,0]`.
2. **Max sum of a strictly decreasing subarray (dishes)**: Kadane-style. Extend while `A[i] < A[i-1]`, else restart at `A[i]`. Init `ans = cur = A[0]`.
   Status: ✅ **SOLVED Oct 13–14** (7/7 PASS). TC O(n), SC O(1). Write-up: `scalar/problems/contest1-02-max-decreasing-subarray-sum.md`.
3. **Count intersecting intervals (open/closed ends)**: this is practice problem **#13**, solved Oct 11 with his 0.5-shift idea (15/15 tests).
   Status: ✅ **SOLVED** (confirmed Oct 14 that it was the contest problem). Write-up: `scalar/problems/13-count-intersecting-intervals.md`. `scalar/tasks/arrays/advanced/CountIntervals.java` now has an `expected` array + PASS/FAIL loop (15/15, Oct 14).

**All of Contest 1 is now covered.**

## Interview: DSA Fundamentals Mastery (Scaler mock)

**Postponed again (told Oct 20): tentatively Sun Oct 25, time TBD** (was Fri Oct 23, 8:30 am; originally Sun Oct 18, 9:30 pm). Sunday has Coding Ninjas 11 am-2 pm: check for a clash once the time is known. Live coding with a real interviewer, Java, explain scenarios out loud.
**Topics corrected (Oct 20):** the interview topics are the **same as the contests**: Arrays, 2D Arrays, Bit Manipulation, Recursion, Math, Hashing, Sorting. The earlier list (strings, prefix sum, carry forward, subarrays) was his mistake; those still help under "Arrays" but are no longer the focus.
Coverage: arrays strong (#1-11, 17-18, 23, 28-33, 41); 2D arrays covered (#20, 22, 27, 44, 46, 47); bit manipulation thin (XOR pairs + `bit.md` guide); **recursion, math, hashing, sorting not practised yet** (the Oct 18-19 slots for them did not happen). The day-by-day table below is the old Oct 18 plan; see "Combined plan (Oct 20-25)".

| Day | Focus |
|-----|-------|
| Oct 14 (Wed) | Scalar class 7-9:30 am, Cloud class 9:30 pm: no DSA slot. Optional 5-min traversal warm-up only |
| Oct 15 (Thu) | Morning: 2D arrays (traversal, spiral, rotate 90° in place). Afternoon: bit manipulation (check/set/clear bit, count set bits, power of 2, single number) |
| Oct 16 (Fri) | After Scalar class: prefix sum, carry forward, subarrays: re-solve 5-6 old ones from memory, out loud |
| Oct 17 (Sat) | After Coding Ninjas (2 pm): strings + the real timed mock (3 problems, 60 min, explained aloud) |
| Oct 18 (Sun) | Coding Ninjas 11-2. Light warm-up ~4-6 pm, no new topics, rest. Interview 9:30 pm |

### Combined plan (Oct 20-25): contest first, interview after (same topics for both)

| Day | Focus |
|-----|-------|
| Tue Oct 20 | Free gap 2-9:30 pm: hashing (frequency map, pair sum, first repeating, distinct count) + sorting (comparator, sort-then-scan, count inversions idea). Park RotatedSortedArraySearch (binary search is not on the list) |
| Wed Oct 21 | After Scalar class: recursion warm-up (sum of digits, palindrome, fast power, subsets) + math (gcd, mod, primes) |
| Thu Oct 22 | Daytime: bit manipulation ladder from `bit.md` + one 90-min mixed timed mock (4 problems). Light by evening. **Contest 2 at 9:30 pm** |
| Fri Oct 23 | After Scalar class: review contest misses, re-solve the weakest topic out loud |
| Sat Oct 24 | After Coding Ninjas (2 pm): 60-min interview-mode mock, mixed topics, explained aloud. Early sleep |
| Sun Oct 25 | **Mock interview (tentative, time TBD)**. Light warm-up only |

Interview script for every problem: restate, walk an example, brute force, improve, code, edge-case test loop, state TC & SC unprompted.

## Prep plan for Contest 2 (interview takes priority until Oct 18; recursion/hashing/sorting days slip)

| Day | Focus |
|-----|-------|
| Oct 8 | Missed contest problems + Bit Manipulation |
| Oct 9 | Math + Recursion |
| Oct 10 | Recursion + Hashing |
| Oct 11 | Hashing + Sorting |
| Oct 12 | *(no session logged)* |
| Oct 13 | ✅ XOR pairs solved. Share missed problems 2 & 3 |
| Oct 14 | Recursion |
| Oct 15 | Hashing |
| Oct 16 | Sorting |
| Oct 17 | Math + 2D arrays (spiral, rotate 90°) |
| Oct 18 | Full 90-min timed mock (4 mixed problems) |
| Oct 19 | Fix whatever broke in the mock |
| Oct 20 | Light review, early sleep |
| Oct 21 | Contest 2 |

## Solved problems (with the key idea / gotcha)

Full write-ups (problem, idea, final code, bugs hit, tests) live in `scalar/problems/`, one file per task:
`01-` … `43-` for practice problems (⚠️ = bug found in the saved `scalar/tasks/` file; the write-up has the fix), `contest<N>-<k>-…` for missed contest problems.
When a new problem is solved, add a file there and a row here.
Reference notes (not problems): `revision-cheatsheet.md` (2D arrays, bits, subarrays) and `bit.md` (bit manipulation guide with a practice ladder; tick its Status column as ladder problems are solved).

| # | Problem | Pattern | Remember |
|---|---------|---------|----------|
| 1 | Max subarray sum (Kadane's) | Kadane's | Init with `nums[0]`, not 0 (all-negative case). Left pointer "moves" when running sum goes negative. |
| 2 | Sort 0s, 1s, 2s | Dutch National Flag (3 pointers) | Loop `mid <= high`; on a 2, swap with `high` and **don't** `mid++`. |
| 3 | Rotate array right by k | Reverse trick | `k %= n`; reverse all, reverse `[0, k-1]`, reverse `[k, n-1]`. Guard `n == 0`. |
| 4 | Trapping Rain Water | Prefix/suffix max, then two pointers | Water = `min(leftMax, rightMax) - h[i]`. Two-pointer version: O(1) space, move the smaller side. |
| 5 | Add One To Number | Carry from the back | Set 9s to 0 while carrying; all 9s grows the array; strip leading zeros **after** adding. |
| 6 | Flip (max 1s in binary string) | Kadane's in disguise | `'0'` → +1, `'1'` → -1. Reset on `< 0`, update on `>` (lexicographic ties). Keep `start` separate from `bestL`. Output is **1-indexed**. Loop `i < n`. |
| 7 | Sum of all subarray sums | Contribution technique | `A[i] * (i + 1) * (n - i)`. Cast to `long` **before** multiplying. |
| 8 | Range Sum Query | Prefix sums | Use `long[]`. `prefix[R] - prefix[L-1]` (handle `L == 0`), or size `n+1` prefix: `prefix[R+1] - prefix[L]`. |
| 9 | Subarray of length B with sum C | Fixed-size sliding window | Guard `B > n`. Slide: `sum += A[i] - A[i - B]`. Use `long`. |
| 10 | Product Array Puzzle | Left × right products | No division. Left products into `ans`, then a running `right` var: multiply first, then update. O(1) auxiliary. |
| 11 | Pick from both sides (B elements) | Wrap-around sliding window | Start with all B from front; swap one front for one back each step. Init max with the starting sum, not 0. |
| 12 | Merge strings alternately | Two pointers | Loop while both have letters, then append leftovers. `StringBuilder`, not `+=`. TC/SC is O(n + m), so name both lengths. |
| 13 | Count intersecting intervals (open/closed ends) | Normalize + all pairs | Integer ends: shift open ends by 0.5 (or ×2 and ±1 in `long`), then `max(starts) <= min(ends)`. O(N²) time, O(N) space. |
| 14 | Palindrome after removing ≤ 1 char ⚠️ | Two pointers | On first mismatch try **both** skips: `(l+1, r) \|\| (l, r-1)`. Saved file only tries the left one. |
| 15 | Amazing subarrays (start with vowel) | Contribution | Vowel at `r` starts `n - r` substrings. `toLowerCase`, mod each step. |
| 16 | Can form arithmetic progression | Sort + adjacent diff | Guard `n < 2` before `A[1]`. |
| 17 | Range update queries | Difference array | `A[l] += v`, `A[r+1] -= v` (guard `r+1 < n`), then prefix sum. O(N + Q). |
| 18 | Add-to-suffix queries | Prefix sum | `A[i] += v` per query, then one prefix sum. |
| 19 | Beggars outside temple | Difference array | Same as #17, but **1-indexed**: subtract 1 first. |
| 20 | Row with max 1s (sorted rows) | Staircase from top-right | 1 → record row, go left; 0 → go down. O(N + M). |
| 21 | Capitalize from first occurrence ⚠️ | Find index + pass | Char not found → `-1` index crashes; return early. |
| 22 | Matrix boundary clockwise ⚠️ | Four edge loops | Each loop stops one short. Single row/column and 1×1 need a special case. |
| 23 | Count subarrays with sum < B | Variable sliding window | Shrink while `sum >= B`, then `count += end - start + 1`. Positives only. |
| 24 | Count vowels / consonants | Switch fall-through | `length - vowels` counts spaces as consonants; use `isLetter`. |
| 25 | Elements removal (min cost) | Greedy + contribution | Sort, `A[i] * (n - i)`: remove biggest first. |
| 26 | First missing positive | Cyclic sort | Swap while `A[i] != A[A[i]-1]` (not `!= i+1`, loops on duplicates). Answer in `1..n+1`. |
| 27 | Search row/col-sorted matrix | Staircase from top-right | Bigger → left, smaller → down, match → record and go left. 1-indexed `i*1009 + j`. |
| 28 | Max product subarray | Kadane's with max & min | Negative swaps max/min. Init with `A[0]`. |
| 29 | Best time to buy & sell stock | Running min | Update profit **before** min. Printed "buy" day in file is wrong. |
| 30 | Leaders in array ⚠️ | Right-to-left running max | Loop `i >= 0` (file has `i > 0`). Append + reverse, not `add(0, …)`. |
| 31 | Min swaps to group elements ≤ B ⚠️ | Fixed sliding window | Window size = count of `≤ B`; min "bad" count over windows. File has `return 0` in `void main` (doesn't compile). |
| 32 | Noble integer ⚠️ | Sort + index count | Greater count = `n-1-i` only for the **last copy** of a value. `[1,1]` → -1. |
| 33 | Merge overlapping intervals | Sort by start + extend | `e1 = max(e1, e2)`. Add the last interval after the loop. Sort if the input isn't sorted. |
| 34 | First non-repeating char | Frequency array | Second pass over the **string**, not the counters. |
| 35 | Palindrome check | Two pointers | `while (i < j)`; the extra `i <= j` check is redundant. |
| 36 | Multiples of 7 in range queries | Prefix count | Same as #8 with 0/1 flags. Don't overwrite the input. |
| 37 | Special index (count) | Even/odd prefix sums | After deleting `i`, the suffix parity **flips**: even = `pfE[i-1] + (pfO[n-1] - pfO[i])`. |
| 38 | Reverse a string | Two-pointer swap | `char[]`, swap while `i < j`. |
| 39 | Single element in sorted array | Binary search on pair parity | Check edges first. Move `m` to the pair start; even → `s = m + 2` (**not** `m + 1`, infinite loop), odd → `e = m - 1`. O(log n). |
| 40 | GCD of strings (LC 1071) | Concat commutes + gcd of lengths | `s1+s2 == s2+s1` else `""`; answer = prefix of length `gcd(n, m)`. SC is O(n+m), not O(1). LeetCode needs `class Solution`. |
| 41 | Highest product of 3 numbers | Top-3 max + bottom-2 min, one pass | Answer = `max(max1*max2*max3, max1*min1*min2)`. Pick **any** 3, not a window. Multiply in `long`. Rewritten from a blank file Oct 16 (`reharsals/MaxProductOfThreeV2.java`, 12/12 + 2000 random cross-check); bugs: `return` inside loop, `min_2 = min` sentinel typo. |
| 42 | Local minima (distinct) | Binary search, walk downhill | Check **both ends** first (covers `n == 2`), then search `[1, n-2]` so `m±1` stay in bounds. `m = s + (e - s) / 2`. |
| 43 | Peak element (duplicates allowed) | Binary search, walk uphill | "Not smaller than" = `>=` in the peak test **and** both end checks. Move toward the strictly bigger neighbour. |
| 45 | Max sum strictly increasing subarray (ChefNCook) | Kadane-style restart | `A[i] > A[i-1]` extend else `current = A[i]`. `long` (10^14). Equal breaks the run. Init `A[0]`. |
| 44 | Find in sorted matrix (repeat of #27, practice contest) | Staircase from top-right | Contest brute force O(N·M) passes (≤ 10⁶ cells) but ignores sortedness; interview answer is O(N+M). On a match `c--`, 1-indexed. 13/13 PASS with `solveBrute` cross-check. |
| 46 | Rotate matrix 90° clockwise, in place | Transpose + reverse each row | Transpose from `j = i + 1` (`j = 0` double-swaps). Fresh `left = 0, right = n - 1` per row. `while (x < y)`. 7/7 + 500 random cross-check (Oct 18), after three bug rounds Oct 16-17. |
| 47 | Spiral order (n x m) | Four shrinking walls | Separate `j` walks, walls move one step. `while (top <= bottom && left <= right)`; `if` guards on bottom row and left column (else a leftover row/column is re-read). Row first, column second. 9/9 + 500 random (Oct 18). |

## Parked practice (not started yet)
- **Recursion warm-up:** sum of digits, palindrome check, fast power (O(log n)), print all subsets

## Habits to watch
- **Strengths:** asks *why* until it clicks; rewrites solutions from memory; keeps going when stuck.
- **Forgets TC & SC** on submissions → always ask for them.
- **Tests only one case** in `main` → Claude adds the test loop after he finishes (see standing rule Oct 15); cover edge cases (empty, single element, negatives, k > n, all same, overflow-sized values).
- Sometimes ignores problem constraints (e.g. used division / counting when told not to) → reread the rules.
- Off-by-one in loop bounds (`i < n - 1` vs `i < n`) and 0- vs 1-indexed output.
- **One variable doing two jobs** (shared `c--` in #46, walls used as loop counters in #47) and **row/column swapped** in 2D indexing (`A[right][j]`). Ask "which index is fixed?" before each `A[..][..]`.
- Says "fixed all" when some fixes are unsaved or missing → rerun before claiming done.
- **Edge cases are the main bug source:** the Oct 11 audit of old `tasks/` files found 7 buggy ones, nearly all n = 1, duplicates, index 0, or a "not found" value.
- **Eyeballs test output** instead of comparing to expected values → insist on an `expected` array + PASS/FAIL print.
- Postpones the hard task ("first thing tomorrow") → hold him to it at the start of the session.

## Session log

Full log lives in `scalar/LOG.md` (read it only when you need history). Latest entry:
- **Oct 14 (late night):** #41 max product of 3: his sliding-window attempt failed (skipped -1/0/1, `abs` lost signs); he was exhausted, so the solution was provided. Rewrite #41 from memory Thu as a warm-up.
- **Oct 14–15 (past midnight, after Cloud class):** Solved **#42** local minima and **#43** peak element (binary search). Bugs were all at the edges: missing end checks, the `n == 2` guard, swapped mid formula (`s - (e+s)/2` → index -1), strict `>` where the problem says "not smaller". Wrote TC/SC unprompted on #43. Still **no PASS/FAIL loop** in either file (I tested them in scratch).
- **Oct 15 (~1 am):** Scaler **practice contest**: 2/4 in 25 min. Missed #41 (max product of 3, not rewritten yet) and one more; code + 4th statement to be shared Thu morning.
- **Oct 15 (Thu, morning):** Reviewed the practice-contest code. `ChefNCook.java` (max strictly increasing subarray sum, Kadane-style, `long`): logic correct, but no TC/SC and a one-case `main`: **test loop still owed**. `FindInMetrics.java` (= #27): brute force O(N·M) plus a stray `}`; he fixed the brace, then rewrote the **staircase** himself (correct; hand-traced `{{1,2},{3,3}}`, B=3 → 2019). Now **#44** with a 13-case PASS/FAIL loop. The 4th problem (`SmallestValue.java`, array B of length A, `B[i] != B[j]` when `i | j`, minimize the max, then lexicographically smallest) he **did not understand**: explained in plain English, he is to fill A = 1, 2, 4, 6, 8 by hand and find the pattern (hint: tracks the prime factorization of `j`). No code yet.
- **Oct 15 (Thu, afternoon):** #41 approach explained correctly but pasted code was a copy of the provided solution; **rewrite from a blank file is parked as "later"**. SmallestValue, 2D arrays and bit manipulation still open. Pivoted to Binary Cloud (`binary-cloud/gcp/01_account.md`).
- **Next (Thu Oct 15, morning):** review the 2 practice-contest solutions + the 4th problem, #41 from memory (10 min), then 2D arrays (traversal warm-up, then spiral, rotate 90°) for the Oct 18 interview; bit manipulation in the afternoon. He chose to skip tonight's slot (Wed has Cloud class at 9:30 pm). Recursion postponed.
- **Oct 16 (Fri):** #41 rewritten from a blank file: fixed `return`-in-loop, `long` cast, and the `min_2 = min` sentinel bug (missed it once, caught by `[-10,-10,1,3,2]`). TC/SC given only when asked. Today's re-plan: 2D arrays (rotate 90°, spiral) + bit manipulation, since Thu's slot went to GCP; prefix-sum re-solves move to Sat.
- **Oct 18 (Sun, Navratri; Ninjas class cancelled):** **#46 RotateMatrix solved** (7/7 + 500 random cross-check). Did the cleanup himself (`j = i + 1`, `x < y`, `n`). TC/SC written unprompted. Saturday's spiral + bit basics carried over; today: spiral, bits, then hashing + sorting. Later: **#47 spiral solved** (9/9 + 500 random) after 5 bug rounds: walls as loop counters, row/column swapped twice, missing guards. Guard reason explained back only vaguely ("check if anything is remaining"); drill the "re-reads the same row backwards" answer before the interview.

## Session tips
- Start from the repo root (`D:\learnings\sc\dsa`) or from `scalar/`; both load this file.
- Use `claude -c` to continue the last conversation mid-problem, `claude -r` to pick an older one.
- At the end of each session, update the solved table / habits here and **append** to `scalar/LOG.md`.
- **Standing rule (Oct 14, from Sai):** by default, at the end of every conversation, or mid-way after each solved/explained task, do all of: add a `scalar/problems/NN-*.md` write-up, update this file (solved table, habits, session log), append to `LOG.md`, then commit and push to git. Don't wait to be asked.

- **Standing rule (Oct 15, from Sai):** Claude writes and runs the test loop (`expected` array + PASS/FAIL, edge cases, brute-force cross-check where useful). Sai only writes the solution and tries 1-2 examples himself; once he says the task is done, add the tests to `main`, run them, report failures. This replaces the old "write the test loop alone" rule: scaffolding was eating his learning time. He should still be able to name the edge cases out loud (interview skill).

## Review workflow
1. Explain the task simply (strip the story), give examples, edge cases, and guiding questions; hints in levels, not full answers.
2. User codes first; review like a strict judge (correctness, edge cases, overflow, TC & SC).
3. After a provided/fixed solution: user rewrites it from memory.
4. Contest tips: read 0/1-indexing and constraints, use `long` for big sums, move on after ~15 min stuck.

