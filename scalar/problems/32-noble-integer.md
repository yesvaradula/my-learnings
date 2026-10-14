# 32. Noble Integer

**Topic:** Sorting · **Pattern:** Sort, then count "greater than" by index · **Status:** ⚠️ Bug with duplicates (Oct 11 review) · **File:** `tasks/NobIndex.java` (Jul 24)

## Problem
Does some integer `p` in `A` have **exactly `p` elements greater than it**? Return `1` if so, otherwise `-1`.

```
[3, 2, 1, 3] → 1   (p = 2: the two 3s are greater)
[1, 1, 3, 3] → -1
```

## Key idea
Sort ascending. Then the number of elements greater than `A[i]` is `n - 1 - i`, **but only if `A[i]` is the last copy of its value**. Equal elements to its right aren't "greater".

## Solution (with the fix)
```java
// TC: O(n log n); SC: O(1) extra
static int solve(int[] A) {
    Arrays.sort(A);
    int n = A.length;

    for (int i = 0; i < n; i++) {
        if (i + 1 < n && A[i] == A[i + 1]) continue;   // FIX: only check the last copy
        if (A[i] == n - 1 - i) return 1;
    }
    return -1;
}
```

## Gotchas
- **Bug in the saved file:** it doesn't skip duplicates. For `[1, 1]` it returns `1` (at `i = 0`, `n-1-i = 1`), but nothing is greater than 1, so the answer is `-1`. I confirmed this by running it.
- Negative numbers can never be noble, because a count can't be negative. That case is already handled.

## Tests
| A | Expected |
|---|---|
| `[3,2,1,3]` | 1 |
| `[1,1,3,3]` | -1 |
| `[1,1]` | -1 *(old code: 1)* |
| `[0]` | 1 |
| `[-1,-2]` | -1 |
| `[1,3,4,2,10,9,7,9]` | 1 *(p = 4)* |
