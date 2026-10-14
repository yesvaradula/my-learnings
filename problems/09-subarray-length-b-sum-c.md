# 09. Subarray with Length B and Sum C

**Topic:** Arrays · **Pattern:** Fixed-size sliding window · **Status:** ✅ Solved (Oct 5, with full test loop)

## Problem
Return `1` if some subarray of **exactly length B** has sum `C`, otherwise `0`.

```
A = [4, 3, 2, 6, 1], B = 3, C = 11
[4,3,2]=9  [3,2,6]=11 ✅  [2,6,1]=9   →  1
```

## Key idea
Consecutive windows overlap. When the window slides right, one element leaves and one enters:
`newSum = oldSum − A[i − B] + A[i]`, so each slide is O(1).

## Solution
```java
// TC: O(n), SC: O(1)
static int solve(int[] A, int B, int C) {
    if (B > A.length) return 0;

    long sum = 0;
    for (int i = 0; i < B; i++)          // first window
        sum += A[i];
    if (sum == C) return 1;

    for (int i = B; i < A.length; i++) { // slide
        sum += (long) A[i] - A[i - B];
        if (sum == C) return 1;
    }
    return 0;
}
```

## Test loop (used for this problem)
```java
int[][] As = { {4,3,2,6,1}, {4,2,2,5,1}, {1}, {1,2}, {-1,2,-1,3}, {5,5,5} };
int[] Bs = { 3, 4, 1, 3, 2, 3 };
int[] Cs = { 11, 6, 1, 3, 1, 15 };
for (int t = 0; t < As.length; t++)
    System.out.println(java.util.Arrays.toString(As[t]) + " B=" + Bs[t] + " C=" + Cs[t]
        + " -> " + solve(As[t], Bs[t], Cs[t]));
```

## Gotchas
- `B > n` crashes the first loop without the guard.
- Check the **first** window before sliding.
- `A[i] - A[i - B]` is computed in `int` first; cast to `long` if values can be near the `int` limits.
- Prefix sums also work (`prefix[i+1] - prefix[i+1-B]`), but need O(n) space.

## Tests
| A | B | C | Expected |
|---|---|---|---|
| `[4,3,2,6,1]` | 3 | 11 | 1 |
| `[4,2,2,5,1]` | 4 | 6 | 0 |
| `[1]` | 1 | 1 | 1 |
| `[1,2]` | 3 | 3 | 0 |
| `[-1,2,-1,3]` | 2 | 1 | 1 |
| `[5,5,5]` | 3 | 15 | 1 |
