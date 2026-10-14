# 28. Maximum Product Subarray

**Topic:** Arrays · **Pattern:** Kadane's variant (track max and min) · **Status:** ✅ Works · **File:** `tasks/MaxProduct.java` (Jul 24)

## Problem
Find the largest product of any contiguous subarray.

```
[2, 3, -2, 4]     → 6
[2, 3, -2, 4, -1] → 48   (whole array)
```

## Key idea
A negative number flips the biggest product into the smallest and the smallest into the biggest. So track **both** `maxP` and `minP` that end at `i`.
At each element, the best is one of: the element alone, `maxP * c`, or `minP * c`.

## Solution
```java
// TC: O(n); SC: O(1)
int maxP = A[0], minP = A[0], result = A[0];

for (int i = 1; i < A.length; i++) {
    int c = A[i];
    if (c >= 0) {
        maxP = Math.max(c, maxP * c);
        minP = Math.min(c, minP * c);
    } else {
        int temp = maxP;               // negative: max and min swap roles
        maxP = Math.max(c, minP * c);
        minP = Math.min(c, temp * c);
    }
    result = Math.max(result, maxP);
}
```

## Gotchas
- Initialize with `A[0]`, not 0 or 1. The all-negative case `[-2]` must return -2 (same lesson as Kadane's #1).
- Save `maxP` in `temp` before overwriting it.
- Including `c` alone in the max/min "restarts" the product after a 0.
- Products overflow quickly. Use `long` if values can be big.
- The saved file prints debug output inside the loop. Remove it before submitting.

## Tests
| A | Expected |
|---|---|
| `[2,3,-2,4]` | 6 |
| `[2,3,-2,4,-1]` | 48 |
| `[-2,0,-1]` | 0 |
| `[-2,3,-4]` | 24 |
| `[-2]` | -2 |
