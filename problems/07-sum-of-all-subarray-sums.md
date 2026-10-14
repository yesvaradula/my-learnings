# 07. Sum of All Subarray Sums

**Topic:** Arrays / Math · **Pattern:** Contribution technique · **Status:** ✅ Solved (Oct 5)

## Problem
Return the sum of the sums of **all** subarrays of `A`. Beware of integer overflow.

```
[1, 2, 3]
Subarrays: [1] [2] [3] [1,2] [2,3] [1,2,3]
Sums:       1   2   3    3     5      6     →  20
```

## Key idea
Instead of listing subarrays, ask: **how many subarrays contain `A[i]`?** (0-based)
- Start choices: `0..i` → `i + 1`
- End choices: `i..n-1` → `n - i`

So `A[i]` contributes **`A[i] × (i + 1) × (n − i)`**.
(1-based indices: `A[i] × i × (n − i + 1)`.)

Note: it's `n - i`, **not** `n - 1`.

## Solution
```java
// TC: O(n), SC: O(1)
public long subarraySum(int[] A) {
    long sum = 0;
    int n = A.length;

    for (int i = 0; i < n; i++) {
        long contribution = (long) (i + 1) * (n - i);
        sum += (long) A[i] * contribution;
    }
    return sum;
}
```

## Check with `[1, 2, 3]`
| i | A[i] | i+1 | n−i | Contribution |
|---|---|---|---|---|
| 0 | 1 | 1 | 3 | 3 |
| 1 | 2 | 2 | 2 | 8 |
| 2 | 3 | 3 | 1 | 9 |
| | | | | **20** |

## Gotchas
- Cast **before** multiplying: `(long) A[i] * (i + 1) * (n - i)`.
  `(long) (A[i] * (i + 1) * (n - i))` overflows inside the parentheses first.
- Brute force is O(n²) (or O(n³)); use it only to verify.

## Tests
| Input | Expected |
|---|---|
| `[1,2,3]` | 20 |
| `[2,1,3]` | 19 |
| `[5]` | 5 |
| `[-1,2]` | 2 |
| `[100000,100000,100000]` | 1000000 |
