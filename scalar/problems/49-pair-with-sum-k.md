# 49. Pair with Sum K

**Topic:** Hashing · **Pattern:** Complement lookup, check before add · **Status:** Solved Oct 20, 2026, 15/15 PASS + 2000 random cross-checks · File: `tasks/hashes/PairSum.java`

## Problem
Given an int array `A` and an integer `K`, return `1` if there are two **different** indices `i != j` with `A[i] + A[j] == K`, else `0`.

```
[3, 5, 1, 2, 1, 2], K = 7  ->  1   (5 + 2)
[1, 2, 3, 4],       K = 8  ->  0   (4 + 4 would use the same index twice)
[4, 4],             K = 8  ->  1   (two copies)
```

## Key idea
- **Brute force:** two loops, `j` from `i + 1` (guarantees `i != j`, no pair counted twice). O(N^2).
- **Hashing:** for each `x`, the partner it needs is `K - x`. Keep a set of the elements seen **so far**.
- **Check before add:** when looking at `A[i]`, the set holds only earlier elements, so `A[i]` can never pair with itself. One `4` with `K = 8` returns 0; a second `4` finds the first.
- **Overflow:** `K - A[i]` can leave the `int` range when values are near ±2×10^9 and wrap onto a value that is in the set (a fake match). Compute it in `long` with the cast **before** the subtraction: `(long) K - A[i]`. `long need = K - A[i]` does not help: the right side is computed in `int` first, then widened. Store `Long`s so the lookup matches (`HashSet<Integer>.contains(Long)` is always false).

## Final solution
```java
// TC: O(n) SC: O(n)
static int solve(int[] A, int K) {
    HashSet<Long> hs = new HashSet<>();

    for (int i = 0; i < A.length; i++) {
        long need = (long) K - A[i];
        if (hs.contains(need)) {
            return 1;
        }
        hs.add((long) A[i]);
    }

    return 0;
}
```

## Bugs hit along the way (Oct 20)
1. First version (int `need`, `HashSet<Integer>`) correct on the first try for normal inputs, brute force said out loud first.
2. Variable named `seen` for the complement; renamed to `need`.
3. Overflow test `[2000000000, 294967296]`, `K = -2000000000`: `K - A[1]` wrapped to `2000000000` and matched. Shrinking the numbers to `20, 29, K = -20` hid the bug (overflow only appears near the limits); traced with the 4-bit version `[7, 1], K = -8`.
4. First fix `long need = K - A[i]` still failed: cast came after the int subtraction.

## Tests
15 cases in `main`: samples, `[4, 4]` vs `[4]` with `K = 8`, empty, non-adjacent copies, negatives, zeros with `K = 0`, no pair, pair at both ends, `10^9 + 10^9`, all equal with `K = 14` and `K = 7`, and the overflow case. Plus 2000 random arrays checked against the brute force.

## Interview lines
- *"Brute force checks every pair with j from i + 1, O(N^2). For each element the partner is fixed, K minus it, so I look it up in a set of earlier elements: O(N) time and space."*
- *"I check before I add, so an element never pairs with itself."*
- *"K - x can overflow int near the limits, so I compute it in long and store longs; a Long set costs a constant factor more memory, same Big-O."*
- Counting pairs instead: use a map of counts and add `count(K - x)` at each step (`hashing.md`, pattern C).
