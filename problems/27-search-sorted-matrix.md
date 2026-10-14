# 27. Search in a Row- and Column-Sorted Matrix

**Topic:** 2D Arrays · **Pattern:** Staircase search from the top-right · **Status:** ✅ Works · **File:** `tasks/FindNumber.java` (Aug 25)

## Problem
Each row and each column is sorted ascending. Find `B`. If it occurs more than once, return the **smallest** `(i+1) * 1009 + (j+1)` (1-indexed). If it's not there, return `-1`.

```
[[2,8,8,8],
 [2,8,8,8],
 [2,8,8,8]], B = 8 → 1011   (row 1, col 2)
```

## Key idea
Start at the top-right. Everything to its left is smaller, and everything below it is bigger.
- `A[i][j] > B` → move left (`j--`)
- `A[i][j] < B` → move down (`i++`)
- equal → record it, then move **left** to look for an earlier column in the same row

## Solution
```java
// TC: O(N + M); SC: O(1)
static int findNumber(int[][] A, int find) {
    int i = 0;
    int j = A[0].length - 1;
    int result = Integer.MAX_VALUE;

    while (i < A.length && j >= 0) {
        if (A[i][j] == find) {
            result = Math.min(result, (i + 1) * 1009 + (j + 1));
            j--;
        } else if (A[i][j] > find) {
            j--;
        } else {
            i++;
        }
    }
    return (result == Integer.MAX_VALUE) ? -1 : result;
}
```

## Gotchas
- The output is **1-indexed**, so use `(i+1)` and `(j+1)`.
- On a match, keep going. A smaller column might hold the same value.
- Starting top-left doesn't work, because both directions increase there.

## Tests
| A | B | Expected |
|---|---|---|
| example above | 8 | 1011 |
| `[[1,2],[3,4]]` | 5 | -1 |
| `[[1,2],[3,4]]` | 3 | 2019 |
