# 36. Count Multiples of 7 in Range Queries

**Topic:** Arrays · **Pattern:** Prefix count · **Status:** ✅ Works · **File:** `tasks/RangeDivisibility.java` (Jul 21)

## Problem
*(Inferred from the code.)* For each query `[L, R]` (0-indexed, inclusive), count how many elements of `A[L..R]` are divisible by 7.

```
A = [7, 14, 3, 21], queries = [[0,3], [2,2], [1,2]] → [3, 0, 1]
```

## Key idea
Turn each element into `1` (divisible) or `0`, then build a prefix sum. Each query becomes `pf[R] - pf[L-1]`, just like Range Sum Query (#8).

## Solution
```java
// TC: O(n + q); SC: O(n)
static int[] solve(int[] A, int[][] B) {
    int[] pf = new int[A.length];
    pf[0] = (A[0] % 7 == 0) ? 1 : 0;

    for (int i = 1; i < A.length; i++) {
        pf[i] = pf[i - 1] + ((A[i] % 7 == 0) ? 1 : 0);
    }

    int[] result = new int[B.length];
    for (int i = 0; i < B.length; i++) {
        int L = B[i][0], R = B[i][1];
        result[i] = (L == 0) ? pf[R] : pf[R] - pf[L - 1];
    }
    return result;
}
```

## Gotchas
- Handle `L == 0` separately, or use a size `n+1` prefix (`pf[R+1] - pf[L]`).
- The saved file overwrites `A[i]` with 0/1, which destroys the input. Compute the flag inline instead, as above.
- It also prints `pf` for debugging. Remove that before submitting.
- `% 7 == 0` also works for negatives and 0 (0 counts as divisible).

## Tests
| A | queries | Expected |
|---|---|---|
| `[7,14,3,21]` | `[[0,3],[2,2],[1,2]]` | `[3,0,1]` |
| `[1,2,3]` | `[[0,2]]` | `[0]` |
| `[0]` | `[[0,0]]` | `[1]` |
