# 13. Count Intersecting Intervals (open/closed ends)

**Topic:** Arrays / Math · **Pattern:** Normalize to closed intervals + check all pairs · **Status:** ✅ Solved (Oct 11)

## Problem
Given `N` rows `[type, L, R]` (integers, intervals on the real line):

| type | interval |
|---|---|
| 1 | `[L, R]` |
| 2 | `[L, R)` |
| 3 | `(L, R]` |
| 4 | `(L, R)` |

Count pairs `(i, j)`, `i < j`, whose intersection is non-empty.

```
[[1,1,2], [2,2,3], [3,2,4]]  →  [1,2], [2,3), (2,4]  →  2
```

## Key idea
Closed intervals overlap iff `max(a, c) <= min(b, d)`. Open ends only matter when two intervals touch at one point.
Endpoints are integers, so shift every open end inward by **0.5** (`L + 0.5`, `R - 0.5`) to turn everything into closed intervals, then check all pairs.
Alternative with no doubles: multiply by 2 and use `2L + 1` / `2R - 1` in `long`.

## Solution
```java
// TC: O(N^2) (all pairs); SC: O(N) (normalized intervals)
static int calculateAns(int[][] A) {
    int count = 0;
    double[][] intervals = new double[A.length][2];

    for (int i = 0; i < A.length; i++) {
        intervals[i] = getInterval(A[i][0], A[i][1], A[i][2]);
    }

    for (int i = 0; i < intervals.length; i++) {
        for (int j = i + 1; j < intervals.length; j++) {
            if (isOverlap(intervals[i], intervals[j])) {
                count++;
            }
        }
    }
    return count;
}

static boolean isOverlap(double[] i1, double[] i2) {
    return Math.max(i1[0], i2[0]) <= Math.min(i1[1], i2[1]);
}

static double[] getInterval(int type, double l, double r) {
    switch (type) {
        case 1:  return new double[]{l, r};
        case 2:  return new double[]{l, r - 0.5};
        case 3:  return new double[]{l + 0.5, r};
        default: return new double[]{l + 0.5, r - 0.5};
    }
}
```

## Gotchas
- Why 0.5 works: with integer ends, a real overlap is either a shared integer point or a stretch of length ≥ 1, which always contains a `.5` point.
- `.5` values are exact in `double` up to 1e9, so there's no precision issue.
- `count` as `int` is fine for N ≤ ~2000; use `long` if N can be larger.
- First review: only one test case in `main`, no TC/SC. Second version printed results without expected/PASS/FAIL.

## Tests
| # | A | Expected |
|---|---|---|
| 1 | `[1,1,2],[2,2,3],[3,2,4]` | 2 |
| 2 | `[1,5,10]` | 0 |
| 3 | `[1,1,3],[1,3,5]` | 1 |
| 4 | `[2,1,3],[1,3,5]` | 0 |
| 5 | `[1,1,3],[3,3,5]` | 0 |
| 6 | `[3,1,3],[2,3,5]` | 1 |
| 7 | `[2,1,3],[3,3,5]` | 0 |
| 8 | `[4,1,2],[4,2,3]` | 0 |
| 9 | `[4,1,2],[4,1,2]` | 1 |
| 10 | `[4,1,3],[4,2,4]` | 1 |
| 11 | `[1,1,10],[4,3,4]` | 1 |
| 12 | `[1,1,2],[1,5,6],[1,9,10]` | 0 |
| 13 | `[1,1,10],[2,2,9],[3,3,8],[4,4,7]` | 6 |
| 14 | `[1,1,1e9],[4,999999999,1e9]` | 1 |
| 15 | `[1,1,3],[2,3,5],[4,5,7],[1,7,8]` | 1 |
