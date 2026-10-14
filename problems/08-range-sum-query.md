# 08. Range Sum Query

**Topic:** Arrays · **Pattern:** Prefix sums · **Status:** ✅ Solved (Oct 5, after overflow fix)

## Problem
Given array `A` and queries `B` (each `[L, R]`, **0-indexed**), return `A[L] + ... + A[R]` for every query.

```
A = [1, 2, 3, 4, 5], B = [[0,3], [1,2]]  →  [10, 5]
```

## Key idea
Looping per query is O(N × M), which is too slow for 10⁵ × 10⁵.
Build `prefix[i] = A[0] + ... + A[i]` once; then each query is one subtraction:
`sum(L, R) = prefix[R] − prefix[L − 1]` (or `prefix[R]` when `L == 0`).

## Solution (as submitted)
```java
// TC: O(n + m), SC: O(n)
static long[] rangeSum(int[] A, int[][] B) {
    int n = A.length, m = B.length;
    long[] C = new long[m];
    long[] prefixSum = new long[n];

    prefixSum[0] = A[0];
    for (int i = 1; i < n; i++)
        prefixSum[i] = prefixSum[i - 1] + A[i];

    for (int i = 0; i < m; i++) {
        int L = B[i][0], R = B[i][1];
        C[i] = L == 0 ? prefixSum[R] : prefixSum[R] - prefixSum[L - 1];
    }
    return C;
}
```

## Cleaner variant: prefix of size n + 1
`prefix[0] = 0`, `prefix[i] = sum of the first i elements`. No `L == 0` special case, and no crash on an empty array.
```java
long[] prefix = new long[n + 1];
for (int i = 0; i < n; i++)
    prefix[i + 1] = prefix[i] + A[i];
// sum(L, R) = prefix[R + 1] - prefix[L]
```

## Gotchas
- Use **`long`**: values up to 10⁹ × 10⁵ elements ≈ 10¹⁴ overflows `int`.
- Read the indexing: this problem is 0-indexed.

## Tests
| A | B | Expected |
|---|---|---|
| `[1,2,3,4,5]` | `[[0,3],[1,2]]` | `[10, 5]` |
| `[2,2,2]` | `[[0,0],[1,2]]` | `[2, 4]` |
| `[5]` | `[[0,0]]` | `[5]` |
| `[-1,3,-2,4]` | `[[0,3],[1,1],[2,3]]` | `[4, 3, 2]` |
