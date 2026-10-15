# 44. Find in Matrix (staircase, practice contest repeat of #27)

**Topic:** 2D Arrays · **Pattern:** Staircase search from the top-right · **Status:** ✅ 13/13 PASS · **File:** `tasks/arrays/intermediate/FindInMetrics.java` (Oct 15)

## Problem
Same as [#27](27-search-sorted-matrix.md). `N x M` matrix, every row and column non-decreasing. Find `B`; if it occurs more than once return the smallest `(i+1) * 1009 + (j+1)` (1-indexed), else `-1`. Note 3 asks for "linear" time.

Constraints: `1 <= N, M <= 1000`, `-100000 <= A[i][j], B <= 100000`.

## What happened
- In the practice contest (Oct 15, ~1 am) he wrote the **O(N·M) brute force**. It is correct and fast enough for the constraints (≤ 10⁶ cells), but it ignores the sortedness. This is the exact problem he solved on Aug 25 as #27.
- The saved file also had a stray `}` (did not compile). Fixed Oct 15.
- Rewrote the staircase on his own the same morning; traced correct on `{{1,2},{3,3}}`, B=3 → 2019.

## Key idea
Top-right corner: everything left is smaller, everything below is bigger.
- `A[r][c] > B` → `c--`
- `A[r][c] < B` → `r++`
- equal → record `min(ans, (r+1)*1009 + (c+1))`, then `c--` (a duplicate further left gives a smaller value; rows below can only be bigger)

## Solution
```java
// TC: O(N + M); SC: O(1)
static int solve(int[][] A, int B) {
    int r = 0;
    int c = A[0].length - 1;
    int ans = Integer.MAX_VALUE;

    while (r < A.length && c >= 0) {
        if (A[r][c] == B) {
            ans = Math.min(ans, ((r + 1) * 1009) + (c + 1));
            c--;
        } else if (A[r][c] > B) {
            c--;
        } else {
            r++;
        }
    }
    return (ans == Integer.MAX_VALUE) ? -1 : ans;
}
```
Optional: after a match, if you would move down, you can stop (any later match has a bigger `i`).

## Gotchas
- **1-indexed** output: `(r+1)` and `(c+1)`.
- On a match keep moving left, don't return.
- Negatives are allowed, so `-1` / `0` can't be the "not found" sentinel inside the search; `Integer.MAX_VALUE` is.
- No overflow: max answer ≈ `1000*1009 + 1000`.
- Interview line: "brute force is O(N·M); sortedness gives O(N+M)".

## Tests (PASS/FAIL loop in the file, staircase cross-checked against `solveBrute`)
| A | B | Expected |
|---|---|---|
| `[[1,2,3],[4,5,6],[7,8,9]]` | 5 | 2020 |
| `[[1,2],[3,3]]` | 3 | 2019 |
| `[[1,2],[3,4]]` | 5 | -1 |
| `[[1,2,2],[2,3,4]]` | 2 | 1011 |
| `[[7]]` | 7 / 3 | 1010 / -1 |
| `[[1,3,5,7]]` (row) | 5 | 1012 |
| `[[1],[3],[5],[7]]` (column) | 5 | 3028 |
| `[[2,8,8,8]]×3` | 8 | 1011 |
| `[[-5,-3],[-4,0]]` | -4 | 2019 |
| `[[1,2,3],[2,3,4],[3,4,5]]` | 3 | 1012 |
| `[[5,6],[7,8]]` | 1 / 9 | -1 / -1 |
