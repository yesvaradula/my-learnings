# 17. Range Update Queries (difference array)

**Topic:** Arrays · **Pattern:** Difference array + prefix sum · **Status:** ✅ Works · **File:** `tasks/BQueries.java` (Aug 23)

## Problem
*(Inferred from the code.)* Start with an array of `N` zeros. Each query `[l, r, v]` (0-indexed, inclusive) adds `v` to every element from `l` to `r`. Return the final array.

```
N = 8, queries = [[1,4,3], [0,5,-1], [2,2,4], [4,6,3]]
→ [-1, 2, 6, 2, 5, 2, 3, 0]
```

## Key idea
Don't loop over the range for every query, because that's O(N·Q). Mark only the edges:
`A[l] += v` starts adding at `l`, and `A[r+1] -= v` stops it after `r`. One prefix sum at the end spreads every update across its range.

## Solution
```java
// TC: O(N + Q); SC: O(1) extra (works in the output array)
int[] A = new int[N];

for (int[] q : B) {
    int l = q[0], r = q[1], v = q[2];
    A[l] += v;
    if (r + 1 < A.length) {
        A[r + 1] -= v;
    }
}

for (int i = 1; i < A.length; i++) {
    A[i] += A[i - 1];
}
```

## Gotchas
- Guard `r + 1 < N`: when the range ends at the last index, there's nothing to cancel.
- If the input is 1-indexed, subtract 1 first (see #19 Beggars).
- Use `long` for big values or many queries.

## Tests
| N | queries | Expected |
|---|---|---|
| 8 | `[[1,4,3],[0,5,-1],[2,2,4],[4,6,3]]` | `[-1,2,6,2,5,2,3,0]` |
| 5 | `[[0,4,2]]` | `[2,2,2,2,2]` |
| 3 | `[[1,1,5]]` | `[0,5,0]` |
| 3 | `[]` | `[0,0,0]` |
