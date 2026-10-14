# 33. Merge Overlapping Intervals

**Topic:** Sorting · **Pattern:** Sort by start, then extend or close the current interval · **Status:** ✅ Works (input must be sorted) · **File:** `tasks/NonOverlapping.java` (Aug 28)

## Problem
Given intervals `[start, end]`, merge all the overlapping ones and return the result.

```
[[1,3], [2,6], [8,10], [15,18]] → [[1,6], [8,10], [15,18]]
```

## Key idea
Once the intervals are sorted by start, an overlap can only happen with the **current** merged interval.
- `next.start <= cur.end` → overlap, so extend: `cur.end = max(cur.end, next.end)`
- otherwise → close `cur`, add it to the result, and start a new one

Add the last open interval after the loop.

## Solution
```java
// TC: O(n log n) with the sort (O(n) if already sorted); SC: O(n) for the result
Arrays.sort(A, (x, y) -> Integer.compare(x[0], y[0]));   // needed if input isn't sorted
List<int[]> result = new ArrayList<>();
int s1 = A[0][0], e1 = A[0][1];

for (int i = 1; i < A.length; i++) {
    int s2 = A[i][0], e2 = A[i][1];
    if (s2 <= e1) {
        e1 = Math.max(e1, e2);
    } else {
        result.add(new int[]{s1, e1});
        s1 = s2;
        e1 = e2;
    }
}
result.add(new int[]{s1, e1});      // don't forget the last one
return result.toArray(new int[result.size()][]);
```

## Gotchas
- The saved file **assumes the input is already sorted** by start. Sort first if the problem doesn't promise that.
- With sorted input, `s1 = Math.min(s1, s2)` is never needed.
- `e1 = max(e1, e2)`, not `e2`. Example: `[1,10]` & `[2,3]` must stay `[1,10]`.
- `s2 <= e1` merges intervals that only touch (`[1,4]` & `[4,5]` → `[1,5]`). Check whether the problem treats touching as overlapping.
- Empty input crashes on `A[0]`, so guard it.

## Tests
| A | Expected |
|---|---|
| `[[1,3],[2,6],[8,10],[15,18]]` | `[[1,6],[8,10],[15,18]]` |
| `[[1,4],[4,5]]` | `[[1,5]]` |
| `[[1,10],[2,3]]` | `[[1,10]]` |
| `[[1,2]]` | `[[1,2]]` |
| `[[1,2],[2,4],[6,8]]` | `[[1,4],[6,8]]` |
