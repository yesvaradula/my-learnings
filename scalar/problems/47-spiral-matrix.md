# 47. Spiral Order of a Matrix

**Topic:** 2D Arrays · **Pattern:** Four shrinking walls · **Status:** Solved Oct 18, 2026, 9/9 PASS + 500 random cross-checks · File: `tasks/arrays/intermediate/SpiralMatrix.java`

## Problem
Given an `n x m` matrix (not necessarily square), return its elements in clockwise spiral order as an `int[]`.

```
 1  2  3 10
 4  5  6 11    ->  [1, 2, 3, 10, 11, 12, 9, 8, 7, 4, 5, 6]
 7  8  9 12
```

## Key idea
Keep four walls: `top`, `bottom`, `left`, `right`. Each round reads one ring:

| Side | Fixed | Walks | From -> to | Then |
|---|---|---|---|---|
| Top row | row `top` | `j` | `left -> right` | `top++` |
| Right column | column `right` | `j` | `top -> bottom` | `right--` |
| Bottom row | row `bottom` | `j` | `right -> left` | `bottom--` |
| Left column | column `left` | `j` | `bottom -> top` | `left++` |

- Loop `while (top <= bottom && left <= right)`. Use `<=`: equal walls still mean one row or column is left.
- A separate loop variable `j` walks between the walls; the walls only move one step after their side.
- **Guards on the last two sides:** after the top row and right column, the walls have moved, so the `while` check is stale. `if (top <= bottom)` before the bottom row and `if (left <= right)` before the left column. Without them, a leftover single row or column is read a second time in the opposite direction (duplicates, then an out-of-bounds write).
- The stop condition is not "one element left in the middle": a 2x2 ends with nothing, a 3x4 ends with a single middle row.

## Final solution
```java
static int[] solve(int[][] A) {
    // TC: O(n*m), each cell read once. SC: O(n*m) for the output, O(1) extra.
    int n = A.length, m = A[0].length;
    int[] ans = new int[n * m];
    int top = 0, bottom = n - 1, left = 0, right = m - 1;
    int k = 0;

    while (top <= bottom && left <= right) {
        for (int j = left; j <= right; j++) ans[k++] = A[top][j];
        top++;

        for (int j = top; j <= bottom; j++) ans[k++] = A[j][right];
        right--;

        if (top <= bottom) {
            for (int j = right; j >= left; j--) ans[k++] = A[bottom][j];
            bottom--;
        }

        if (left <= right) {
            for (int j = bottom; j >= top; j--) ans[k++] = A[j][left];
            left++;
        }
    }
    return ans;
}
```

## Bugs hit along the way (Oct 18)
1. Used the walls as loop counters (`left++` inside the walk), then patched with `left = 0`, `top = 1`: worked for the outer ring only, the inner `5 6` was never read (`[..., 4, 0, 0]`).
2. Row/column swapped on a column side, twice: `A[right][j]` (crash, index 3 of 3) and later `A[left][j]` (silently read 2 instead of 4). Rule: row first, column second; ask which index is fixed on this side.
3. Bottom-row loop started at `bottom` with `j <= left` while counting down. Rule: counting down (`j--`) needs `>=`.
4. Dropped the outer `while` while rewriting: only one ring read.
5. Missing `if` guards: `Index 12 out of bounds for length 12` on 3x4 (middle row read twice).
6. `println` inside `solve`, TC/SC missing until the fourth reminder.

## Tests
9/9 PASS: 3x3, 3x4, 1x1, single row, single column, 2x2, 4x4, 4x3, negatives 2x3. Plus 500 random matrices (1..7 x 1..7) against a walk-and-turn-when-blocked simulation with a visited grid: 0 mismatches.

## Interview lines
- "Why the guards?" The first two sides can use up the last row or column. Without a fresh check, the bottom row (or left column) re-reads that same row (or column) backwards, producing duplicates.
- "Simpler alternative?" Walk with a direction array and turn when the next cell is out of bounds or already visited: same O(n*m) time but O(n*m) extra space for the visited grid.
