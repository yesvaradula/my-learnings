# Contest 1, Missed #2: Max Sum of a Decreasing Subarray (Dishes)

**Topic:** Arrays · **Pattern:** Kadane-style running sum with a reset rule · **Status:** ✅ Solved (Oct 13–14, 2026), 7/7 PASS

## Problem
Given array `A` (ingredient weights), find the maximum sum of a subarray whose weights are **decreasing**.

Constraints: `1 ≤ N ≤ 10⁵`, `0 ≤ A[i] ≤ 10⁵`, sum of all elements `≤ 10⁹`.

```
A = [3, 2, 1]        →  6
A = [3, 3, 5, 0, 1]  →  5
```

## Strip the story
Find a contiguous chunk where each element is **strictly** smaller than the one before it, and return the biggest sum of such a chunk.
Equal values break the run: `[3, 3]` is not decreasing, so example 2 is `5` (`[5, 0]` or `[5]`), not 6.

## Key idea
One pass, like Kadane's:
- `A[i] < A[i-1]` → extend the run: `cur += A[i]`
- otherwise → start a new run: `cur = A[i]`
- `ans = max(ans, cur)` after every step

Init **both** `ans` and `cur` with `A[0]`, not 0. Otherwise a single-element array returns 0.

## Final solution
```java
static int solve(int[] A) {
    // TC: O(n), SC: O(1)
    int ans = A[0];
    int cur = A[0];
    for (int i = 1; i < A.length; i++) {
        if (A[i] < A[i - 1]) cur += A[i];
        else cur = A[i];
        ans = Math.max(ans, cur);
    }
    return ans;
}
```
Sum ≤ 10⁹ fits in `int`, so no `long` is needed.

## Bugs hit along the way
- `ans = 0` → `[7]` returned 0. Same gotcha as Kadane's (#1).
- `main` called `solve` and threw the result away, and only tested one case.
- After the fix, he added `if (n == 1) return A[0];`, which is redundant once `ans = A[0]`.

## Tests
| A | Expected | Why |
|---|---|---|
| `[3,2,1]` | 6 | whole array |
| `[3,3,5,0,1]` | 5 | equal values break the run |
| `[7]` | 7 | single element |
| `[1,2,3]` | 3 | every run has length 1 |
| `[5,5,5]` | 5 | all equal |
| `[0,0]` | 0 | zeros |
| `[10,1,20,19,18]` | 57 | the later run wins |
