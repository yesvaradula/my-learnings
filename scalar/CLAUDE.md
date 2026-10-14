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
| 2 | 2026-10-21 (postponed from Oct 13) | TBD | Same topics, 4 new problems | Goal: solve all 4 |

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

**Sun Oct 18, 9:30–11:00 pm.** Live coding with a real interviewer, Java, explain scenarios out loud. Topics: Strings, Arrays, Prefix sum, Carry forward, Array techniques/subarrays, Bit manipulation, 2D arrays. Recursion is **not** on the list.
Coverage: arrays/prefix/carry forward/subarrays strong (#1-11, 17-18, 23, 28-31); strings decent (#12, 14, 21, 34, 35, 38); **bit manipulation thin** (only XOR pairs); **2D arrays gap** (spiral, rotate 90° not started).

| Day | Focus |
|-----|-------|
| Oct 14 (9 pm) | 2D arrays: traversal, spiral, rotate 90° in place |
| Oct 15 | Bit manipulation: check/set/clear bit, count set bits, power of 2, single number |
| Oct 16 | Prefix sum, carry forward, subarrays: re-solve 5-6 old ones from memory, out loud |
| Oct 17 | Strings + timed mock (3 problems, 60 min, explained aloud) |
| Oct 18 | Light warm-up, sleep, interview 9:30 pm |

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
`01-` … `39-` for practice problems (⚠️ = bug found in the saved `scalar/tasks/` file; the write-up has the fix), `contest<N>-<k>-…` for missed contest problems.
When a new problem is solved, add a file there and a row here.

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

## Parked practice (not started yet)
- **2D arrays:** spiral print, rotate matrix 90° in place
- **Recursion warm-up:** sum of digits, palindrome check, fast power (O(log n)), print all subsets

## Habits to watch
- **Strengths:** asks *why* until it clicks; rewrites solutions from memory; keeps going when stuck.
- **Forgets TC & SC** on submissions → always ask for them.
- **Tests only one case** in `main` → insist on a test loop covering edge cases (empty, single element, negatives, k > n, all same, overflow-sized values).
- Sometimes ignores problem constraints (e.g. used division / counting when told not to) → reread the rules.
- Off-by-one in loop bounds (`i < n - 1` vs `i < n`) and 0- vs 1-indexed output.
- **Edge cases are the main bug source:** the Oct 11 audit of old `tasks/` files found 7 buggy ones, nearly all n = 1, duplicates, index 0, or a "not found" value.
- **Eyeballs test output** instead of comparing to expected values → insist on an `expected` array + PASS/FAIL print.
- Postpones the hard task ("first thing tomorrow") → hold him to it at the start of the session.

## Session log

Full log lives in `scalar/LOG.md` (read it only when you need history). Latest entry:
- **Next (Oct 14, 9 pm, he committed to this time):** 2D arrays (traversal warm-up, then spiral, rotate 90°) for the Oct 18 interview. Recursion day postponed. Write the test loop **alone**, no exceptions.

## Session tips
- Start from the repo root (`D:\learnings\sc\dsa`) or from `scalar/`; both load this file.
- Use `claude -c` to continue the last conversation mid-problem, `claude -r` to pick an older one.
- At the end of each session, update the solved table / habits here and **append** to `scalar/LOG.md`.

## Review workflow
1. Explain the task simply (strip the story), give examples, edge cases, and guiding questions; hints in levels, not full answers.
2. User codes first; review like a strict judge (correctness, edge cases, overflow, TC & SC).
3. After a provided/fixed solution: user rewrites it from memory.
4. Contest tips: read 0/1-indexing and constraints, use `long` for big sums, move on after ~15 min stuck.

