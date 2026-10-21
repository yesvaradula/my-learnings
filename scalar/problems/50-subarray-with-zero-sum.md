# 50. Subarray with 0 Sum

**Topic:** Hashing · **Pattern:** Running prefix sum + set · **Status:** Solved Oct 21, 2026, 15/15 PASS + 2000 random cross-checks · File: `tasks/hashes/SubarraySumZero.java`

## Problem
Given an int array `A`, return `1` if some non-empty contiguous subarray sums to 0, else `0`.

```
[4, 2, -3, 1, 6]  ->  1   ([2, -3, 1])
[1, 2, 3]         ->  0
[-1, 1]           ->  1   (the whole array)
```

## Key idea
- **Not a sliding window:** the window rule (grow raises the sum, shrink lowers it) only holds for positives, and a zero sum needs negatives or zeros.
- **Brute force:** for each start `i`, extend `j` and keep a running sum. O(N^2).
- **Prefix sums:** `sum(A[i+1..j]) = prefix[j] - prefix[i]`. If two prefix sums are equal, the elements between them add up to 0.
  ```
  A      =  4,  2, -3,  1,  6
  prefix =  4,  6,  3,  4, 10     4 repeats -> [2, -3, 1] sums to 0
  ```
- **A prefix of 0 by itself** means the subarray from index 0 sums to 0 (`[-1, 1]`, `[1, 2, -3, 7]`, `[0]`). There is no earlier prefix to match, so either check `sum == 0` (his version) or seed the set with `0` before the loop (the common one-liner). Special-casing array sizes does not work.
- **One pass, running `long sum`:** no prefix array, input untouched, no overflow.

## Final solution
```java
// TC: O(n) SC: O(n)
static int solve(int[] A) {
    HashSet<Long> hs = new HashSet<>();
    long sum = 0;
    for (int i = 0; i < A.length; i++) {
        sum += A[i];
        if (sum == 0) {
            return 1;
        }

        if (hs.contains(sum)) {
            return 1;
        }

        hs.add(sum);
    }

    return 0;
}
```

## Bugs hit along the way (Oct 21, unwell, on leave)
1. Idea right first time (prefix sums + set, check before add), but missed the prefix-equals-0 case (`[-1, 1]`). Tried a size-2 special case; `[1, 2, -3, 7]` and `[0]` showed why the fix is a seed / `sum == 0` check instead.
2. First code: correct logic with `A[i] == 0`, but built the prefix **in place** in `int A[]`: overwrote the caller's array and overflowed (`[2147483647, 2147483647, 2]` wrapped to 0 and returned 1).
3. Switched to a running `long sum` but started with `sum = A[0]` and the loop at `i = 1`: the first prefix was never checked or stored. The sample itself failed, `[0]` failed, `[]` crashed. Fixed with `sum = 0`, loop from 0.
4. Brute force still not said out loud before coding (skipped deliberately; given in the tests).

## Tests
15 cases in `main` (each also checks the input is left unchanged): sample, all positive, `[-1, 1]`, zero sum from index 0, `[0]`, `[5]`, empty, `[3, -3]`, repeat of the first prefix `[4, 2, -2]`, a zero in the middle, alternating `1, -1`, all negative, int-wrapping positives, `Integer.MIN_VALUE` twice, big values that cancel. Plus 2000 random arrays with negatives against the brute force.

## Interview lines
- *"Sliding window fails because of negatives. Equal prefix sums mean the part between them sums to zero, so I store prefix sums in a set: O(N) time, O(N) space."*
- *"A prefix of 0 means the subarray from the start sums to zero; I seed the set with 0 (or check sum == 0) so that case is caught."*
- *"I keep the prefix in a long running variable: no overflow, and I don't modify the input."*
- Follow-ups: count zero-sum subarrays (map of prefix counts, seed `0 -> 1`), longest zero-sum subarray (first index of each prefix, seed `0 -> -1`). See `tasks/hashes/hashing.md`, pattern D.
