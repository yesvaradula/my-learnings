# 46. Rotate Matrix 90 Degrees Clockwise (in place)

**Topic:** 2D Arrays · **Pattern:** Transpose, then reverse each row · **Status:** Solved Oct 18, 2026, 7/7 PASS + 500 random cross-checks · File: `tasks/arrays/intermediate/RotateMatrix.java`

## Problem
Given an `n x n` matrix `A`, rotate it 90 degrees clockwise **in place** (no second matrix).

```
1 2 3        7 4 1
4 5 6   ->   8 5 2
7 8 9        9 6 3
```

Sanity check: the first column read bottom to top (7, 4, 1) becomes the first row.

## Key idea
1. **Transpose:** swap `A[i][j]` with `A[j][i]`, only for cells above the diagonal (`j = i + 1`). The diagonal stays put, and starting at `j = 0` swaps every pair twice, so nothing changes.
2. **Reverse each row** with two pointers. Each row needs its **own** `left = 0`, `right = n - 1`.
3. Counter-clockwise variant: transpose, then reverse each column.

Intermediate state after the transpose (useful for debugging):
```
1 4 7
2 5 8
3 6 9
```

## Final solution
```java
static int[][] solve(int[][] A) {
    // TC: O(n^2), every cell touched a constant number of times. SC: O(1).
    int n = A.length;

    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            int temp = A[i][j];
            A[i][j] = A[j][i];
            A[j][i] = temp;
        }
    }

    for (int i = 0; i < n; i++) {
        int x = 0, y = n - 1;
        while (x < y) {
            int temp = A[i][x];
            A[i][x] = A[i][y];
            A[i][y] = temp;
            x++;
            y--;
        }
    }
    return A;
}
```

## Bugs hit along the way (Oct 16-18)
- Transpose with `j = 0`: every pair swapped twice, matrix unchanged.
- Shared `c--` across rows in the reverse step: only the first row reversed correctly.
- Single-loop transpose (`x++, y--`) instead of a nested loop, then renamed variables to silence compile errors (`i`, `j` undefined).
- `r = A.length - 1` with `i < r` skipped the last row.
- Cleanup round: `j = i` and `x <= y` worked but did pointless self-swaps; unused `r`; a `println` inside `solve`.

Breakthrough: a fill-in skeleton (two blanks: `j = i + 1`, `right = n - 1`) after three rounds of patching compile errors.

## Tests
7/7 PASS: 3x3, 1x1, 2x2, 4x4, negatives, all equal, symmetric. Plus 500 random `n x n` matrices (n = 1..8) checked against the copy-based rotation `B[j][n-1-i] = A[i][j]`: 0 mismatches.

## Interview lines
- "Why start the transpose at `i + 1`?" The diagonal maps to itself; only the upper triangle needs swapping.
- "Can you do it without the transpose?" Rotate four cells at a time, layer by layer (`top-left <- bottom-left <- bottom-right <- top-right`); same O(n^2) / O(1), harder to get right under pressure.
