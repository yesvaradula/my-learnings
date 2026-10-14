# 39. Single Element in a Sorted Array

**Topic:** Binary Search · **Pattern:** Pair-index parity · **Status:** ✅ Solved (Oct 14, 2026), 11/11 PASS · File: `tasks/FindSingleElement.java`

## Problem
Sorted array `A`; every element appears twice (adjacent) except one. Return the single one in **O(log N)**.

Constraints: `1 ≤ |A| ≤ 10⁵`, `1 ≤ A[i] ≤ 10⁹`.

```
[1, 1, 7] → 7
[2, 3, 3] → 2
```

## Key idea
Before the single element, pairs start at **even** indices. After it, they start at **odd** indices.
1. Handle the edges first: `n == 1`, `A[0] != A[1]`, `A[n-1] != A[n-2]`. After that, `m - 1` and `m + 1` are always in bounds.
2. `m` differs from both neighbours → that's the answer.
3. If `A[m] == A[m-1]`, move `m` back to the **first** element of the pair.
4. Pair starts at an even index → single is to the right: `s = m + 2` (skip **both** elements of the pair).
   Odd index → single is to the left: `e = m - 1`.

## Final solution
```java
static int solve(int[] A) {
    // TC: O(log n), SC: O(1)
    int n = A.length;
    if (n == 1) return A[0];
    if (A[0] != A[1]) return A[0];
    if (A[n - 1] != A[n - 2]) return A[n - 1];

    int s = 0, e = n - 1;
    while (s <= e) {
        int m = s + (e - s) / 2;
        if (A[m] != A[m - 1] && A[m] != A[m + 1]) return A[m];
        if (A[m] == A[m - 1]) m = m - 1;   // move to the first element of the pair
        if (m % 2 == 0) s = m + 2;          // skip the whole pair
        else e = m - 1;
    }
    return -1;
}
```

## Bugs hit along the way
- `s = m + 1` after moving `m` to the pair start → `s` lands on the pair's second element → **infinite loop** (e.g. `s = 3, e = 4` in the 61-element test). Fix: `s = m + 2`.
- He reported `[1,1,2,2,3]` as failing, but the code returns 3 (the edge check catches it). Most likely a stale `.class` or a typo in the test. Lesson: keep tests in a PASS/FAIL loop instead of swapping arrays in `main`.
- First version had no TC/SC and only one test in `main`.

## Tests
`[1,1,7]`→7 · `[2,3,3]`→2 · `[5]`→5 · `[1,1,2,3,3]`→2 · `[1,1,2,2,3]`→3 · `[1,2,2,3,3]`→1 · `[1,1,2,2,3,4,4]`→3 · `[1,1,2,3,3,4,4]`→2 · `[1,1,2,2,3,3,4,4,5]`→5 · `[999,1e9,1e9]`→999 · 61-element array → 27.
Also brute-forced: every odd length 1–41 with the single at every position → 0 failures.
