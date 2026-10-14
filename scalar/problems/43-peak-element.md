# 43. Peak Element

**Topic:** Binary Search · **Pattern:** Walk uphill, ends checked first · **Status:** ✅ Solved (Oct 15, 2026) · File: `tasks/bs/PeakElement.java`

## Problem
Return a peak element: one that is **not smaller than** its neighbours (`>=`; corner elements have one neighbour). **Duplicates are allowed.** Target **O(log N)**.

```
[5, 20, 10, 9]  → 20
[1, 1, 1]       → 1
[5, 5]          → 5
[1, 2, 2, 1]    → 2
```

## Key idea
Mirror of #42 (local minima), with two twists:
1. Every comparison in the **peak test** is `>=` (the problem says "not smaller"), including the two **end checks**.
2. Navigation: if `A[m]` isn't a peak, some neighbour is **strictly** bigger, so walk uphill: `A[m-1] > A[m]` → `e = m - 1`, else `s = m + 1`.

Why it still works with duplicates: after the end checks, `A[0] < A[1]` and `A[n-1] < A[n-2]` **strictly**. Moving toward a strictly bigger neighbour keeps "rises at the left edge, falls at the right edge" true for the new range, so a peak must be inside it.

## Final solution
```java
static int solve(int[] A) {
    // TC: O(log n), SC: O(1)
    int n = A.length;
    if (n == 1) return A[0];
    if (A[0] >= A[1]) return A[0];
    if (A[n - 1] >= A[n - 2]) return A[n - 1];

    int s = 1, e = n - 2;
    while (s <= e) {
        int m = s + (e - s) / 2;
        if (A[m] >= A[m - 1] && A[m] >= A[m + 1]) return A[m];
        if (A[m - 1] > A[m]) e = m - 1;
        else s = m + 1;
    }
    return -1; // unreachable once the ends are checked
}
```

## Bugs hit along the way
- First version flipped #42 correctly but used strict `>` everywhere → `[1,1,1]`, `[5,5]`, `[1,2,2,1]`, `[3,3,1,3,3]` returned **0** (32,103 / 200,000 random arrays with duplicates failed).
- Second version fixed the mid check but left the **end checks** strict → `[5,5]` still returned 0.
- Wrote TC/SC unprompted this time 👍. Still no PASS/FAIL loop in `main`.

## Tests
`[1]`→1 · `[1,5]`→5 · `[5,1]`→5 · `[1,2,3,4]`→4 · `[4,3,2,1]`→4 · `[5,20,10,9]`→20 · `[1,3,20,4,1,0]`→20 · `[1,1,1]`→1 · `[5,5]`→5 · `[1,2,2,1]`→2 · `[3,3,1,3,3]`→3.
Also checked 200,000 random distinct arrays and 200,000 random arrays with values 0–2 (many duplicates): 0 failures.
