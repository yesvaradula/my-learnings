# 42. Local Minima

**Topic:** Binary Search · **Pattern:** Walk downhill, ends checked first · **Status:** ✅ Solved (Oct 14–15, 2026) · File: `tasks/bs/LocalMinima.java`

## Problem
Array `A` of **distinct** integers. Return any local minimum: an element smaller than its neighbours (corner elements have one neighbour). Target **O(log N)**.

```
[1, 20, 10, 12]   → 1 (or 10)
[100, 20, 10, 12] → 10
[5, 1]            → 1
```

## Key idea
1. `n == 1` → `A[0]`. Check both ends: `A[0] < A[1]` → `A[0]`; `A[n-1] < A[n-2]` → `A[n-1]`. This also covers `n == 2`, so no special case is needed.
2. Once both ends are ruled out, the array goes **down** at the left end and **down** into the right end, so a valley must exist in `[1, n-2]`. Searching only that range keeps `m-1` and `m+1` in bounds.
3. If `A[m]` is smaller than both neighbours, return it. Otherwise **walk downhill**: `A[m-1] < A[m]` → `e = m - 1`, else `s = m + 1`.

## Final solution
```java
static int solve(int[] A) {
    // TC: O(log n), SC: O(1)
    int n = A.length;
    if (n == 1) return A[0];
    if (A[0] < A[1]) return A[0];
    if (A[n - 1] < A[n - 2]) return A[n - 1];

    int s = 1, e = n - 2;
    while (s <= e) {
        int m = s + (e - s) / 2;
        if (A[m] < A[m - 1] && A[m] < A[m + 1]) return A[m];
        if (A[m - 1] < A[m]) e = m - 1;   // smaller on the left → valley on the left
        else s = m + 1;
    }
    return -1; // unreachable once the ends are checked
}
```
(His saved file has the same logic, written as three branches.)

## Bugs hit along the way
- No end checks, search started at `s = 1` → `{1,20,10,12}` returned **0** (the minimum at index 0 was never looked at).
- `n == 2 && A[0] < A[1]` guard → `[5,1]` returned 0.
- `m = s - (e + s) / 2` (signs swapped) plus `s = 0` → `ArrayIndexOutOfBoundsException` on `A[-1]`. Correct: `m = s + (e - s) / 2`, `s = 1`.
- No PASS/FAIL test loop in `main` (single case only).

## Tests
`[1]`→1 · `[1,5]`→1 · `[5,1]`→1 · `[1,2,3,4]`→1 · `[4,3,2,1]`→1 · `[1,20,10,12]`→1 · `[100,20,10,12]`→10 · `[9,8,1,7,6,5]`→5.
Also checked 200,000 random distinct arrays (n = 1–12): every result was a valid local minimum.
