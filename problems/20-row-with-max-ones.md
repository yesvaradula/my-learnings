# 20. Row With Maximum Number of 1s

**Topic:** 2D Arrays · **Pattern:** Staircase walk from the top-right · **Status:** ✅ Works · **File:** `tasks/BestOnesRow.java` (Aug 25)

## Problem
You get a binary matrix where every row is **sorted** (0s then 1s). Return the index of the row with the most 1s. On a tie, return the smallest index.

```
[[0,0,1,1],
 [0,0,0,1],
 [0,0,1,1],
 [0,1,1,1]]  → 3
```

## Key idea
Start at the top-right corner.
- On a `1`, this row beats the best so far. Record it and move **left** to look for an even longer run.
- On a `0`, this row can't beat the best, so move **down**.

Every step moves either left or down, so there are at most `N + M` steps.

## Solution
```java
// TC: O(N + M); SC: O(1)
static int BestOnes(int[][] A) {
    int rows = A.length;
    int cols = A[0].length;
    int r = 0;
    int c = cols - 1;
    int bestRow = 0;   // default if every row is all 0s

    while (r < rows && c >= 0) {
        if (A[r][c] == 1) {
            bestRow = r;
            c--;
        } else {
            r++;
        }
    }
    return bestRow;
}
```

## Gotchas
- Ties go to the earlier row automatically, because a later row only wins if it has a 1 **further left**.
- An all-zero matrix returns 0. Check whether the problem wants `-1` instead.

## Tests
| A | Expected |
|---|---|
| example above | 3 |
| `[[0,0],[0,0]]` | 0 |
| `[[0,1],[1,1]]` | 1 |
| `[[1,1],[1,1]]` | 0 *(tie)* |
