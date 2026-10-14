# 18. Add-to-Suffix Queries

**Topic:** Arrays · **Pattern:** Prefix sum (one-sided difference array) · **Status:** ✅ Works · **File:** `tasks/BQuery.java` (Aug 23)

## Problem
*(Inferred from the code.)* Start with `N` zeros. Each query `[i, v]` adds `v` to **every element from index `i` to the end**. Return the final array.

```
N = 8, queries = [[1,4], [0,5], [2,2], [4,6]]
→ [5, 9, 11, 11, 17, 17, 17, 17]
```

## Key idea
This is #17 with no right edge. Do `A[i] += v` for each query, then one prefix sum carries every value to the end.

## Solution
```java
// TC: O(N + Q); SC: O(1) extra
int[] A = new int[N];

for (int[] q : B) {
    A[q[0]] += q[1];
}

for (int i = 1; i < A.length; i++) {
    A[i] += A[i - 1];
}
```

## Gotchas
- The same index can appear in several queries, so use `+=`, not `=`.
- Use `long` if the values or the number of queries are large.

## Tests
| N | queries | Expected |
|---|---|---|
| 8 | `[[1,4],[0,5],[2,2],[4,6]]` | `[5,9,11,11,17,17,17,17]` |
| 4 | `[[2,3]]` | `[0,0,3,3]` |
| 3 | `[[0,1],[0,1]]` | `[2,2,2]` |
