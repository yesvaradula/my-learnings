# 16. Can the Array Form an Arithmetic Progression?

**Topic:** Sorting · **Pattern:** Sort, then check adjacent differences · **Status:** ✅ Works (needs an `n < 2` guard) · **File:** `tasks/Arithmetic.java` (Jul 25)

## Problem
Can the elements of `A` be rearranged into an arithmetic progression, where every adjacent difference is equal? Return `1` or `0`.

```
[3, 5, 1] → 1   (1, 3, 5)
[2, 4, 1] → 0
```

## Key idea
If an AP exists, sorting puts it in order. So sort, take `diff = A[1] - A[0]`, and check every adjacent pair.

## Solution
```java
// TC: O(n log n) (sort); SC: O(1) extra
java.util.Arrays.sort(A);
int diff = A[1] - A[0];
int ret = 1;

for (int r = 1; r < A.length; r++) {
    if (A[r] - A[r - 1] != diff) { ret = 0; break; }
}
```

## Gotchas
- The saved file keeps a separate `l` that is always `r - 1`. Writing `A[r] - A[r-1]` is simpler.
- **Guard `n < 2`:** `A[1]` crashes on a single element. A single element is trivially an AP, so return 1.
- There's an O(n) hashing approach: find min and max, compute `d = (max-min)/(n-1)`, then check that every term is present.

## Tests
| A | Expected |
|---|---|
| `[3,5,1]` | 1 |
| `[2,4,1]` | 0 |
| `[5,5,5]` | 1 |
| `[1,2]` | 1 |
| `[7]` | 1 *(needs the guard)* |
