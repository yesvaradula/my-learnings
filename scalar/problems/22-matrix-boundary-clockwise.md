# 22. Matrix Boundary, Clockwise

**Topic:** 2D Arrays · **Pattern:** Four edge loops · **Status:** ⚠️ Works for ≥ 2×2, breaks on a single row/column · **File:** `tasks/ClockWiseBoundary.java` (Aug 28)

## Problem
Return the boundary elements of an `R × C` matrix in clockwise order, starting from the top-left.

```
 1  2  3  4
 5  6  7  8      → 1 2 3 4 8 12 16 15 14 13 9 5
 9 10 11 12
13 14 15 16
```

## Key idea
Four loops. Each one **stops one short** so a corner isn't printed twice:
1. top row, left → right (`c - 1` items)
2. right column, top → bottom (`r - 1` items)
3. bottom row, right → left (`c - 1` items)
4. left column, bottom → top (`r - 1` items)

Total: `2 * (r + c - 2)`.

## Solution (with the edge-case fix)
```java
// TC: O(R + C); SC: O(R + C) for the result
int r = A.length;
int c = A[0].length;

if (r == 1 || c == 1) {                 // FIX: one row/column → just read it in order
    int[] all = new int[r * c];
    int k = 0;
    for (int[] row : A)
        for (int v : row)
            all[k++] = v;
    return all;
}

int[] result = new int[2 * (r + c - 2)];
int index = 0;

for (int i = 0; i < c - 1; i++)  result[index++] = A[0][i];       // top
for (int i = 0; i < r - 1; i++)  result[index++] = A[i][c - 1];   // right
for (int i = c - 1; i > 0; i--)  result[index++] = A[r - 1][i];   // bottom
for (int i = r - 1; i > 0; i--)  result[index++] = A[i][0];       // left
```

## Gotchas
- **Single row** `[[1,2,3]]`: the bottom loop walks the same row back, giving `1 2 3 2` instead of `1 2 3`.
- **Single column** `[[1],[2],[3]]` gives `1 2 3 2`.
- **1×1**: the size is 0, so it prints nothing instead of the element.
- Special-case `r == 1 || c == 1` before the four loops.
- This is the warm-up for **spiral print** (parked practice). Spiral is this loop repeated layer by layer.

## Tests
| A | Expected |
|---|---|
| 4×4 above | `1 2 3 4 8 12 16 15 14 13 9 5` |
| `[[1,2,3],[4,5,6],[7,8,9]]` | `1 2 3 6 9 8 7 4` |
| `[[1,2,3]]` | `1 2 3` *(old code: 1 2 3 2)* |
| `[[1],[2],[3]]` | `1 2 3` *(old code: 1 2 3 2)* |
| `[[5]]` | `5` *(old code: nothing)* |
