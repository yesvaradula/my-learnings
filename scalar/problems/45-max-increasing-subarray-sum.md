# 45. Max Sum of a Strictly Increasing Subarray (ChefNCook, practice contest)

**Topic:** Arrays · **Pattern:** Kadane-style restart · **Status:** ✅ 12/12 PASS · **File:** `tasks/arrays/intermediate/ChefNCook.java` (Oct 15)

## Problem
Given `A` (`1 <= |A| <= 10^5`, `1 <= A[i] <= 10^9`), return the maximum sum over all **strictly increasing** subarrays.

## Key idea
Mirror of contest1-02 (strictly decreasing). Keep the running sum of the current strictly increasing run.
- `A[i] > A[i-1]` → `current += A[i]`
- otherwise → restart: `current = A[i]` (equal values break the run)
- `max = max(max, current)` every step; init both with `A[0]`

## Solution
```java
// TC: O(n); SC: O(1)
static long solve(int[] A) {
    long max = A[0];
    long current = A[0];
    for (int i = 1; i < A.length; i++) {
        if (A[i] > A[i - 1]) current += A[i];
        else current = A[i];
        max = Math.max(max, current);
    }
    return max;
}
```

## Gotchas
- **`long`**: worst case ≈ 10^5 × 10^9 = 10^14. Two values near 10^9 already overflow `int`.
- Strictly: `[3,3,3]` → 3, not 9.
- Init with `A[0]`, not 0 (single element).
- All positive, so restarting is always right (no negatives to drop).

## Tests (12 in the file, all PASS)
| A | Expected |
|---|---|
| `[1,2,3,4,5]` | 15 |
| `[7]` | 7 |
| `[3,3,3]` | 3 |
| `[5,4,3]` | 5 |
| `[1,2,10,3,4]` | 13 |
| `[4,5,1,2]` | 9 |
| `[10,1,2,3,4,5]` | 15 |
| `[999999999, 1000000000]` | 1999999999 |
| 100000 values `900000000..` ascending | 90004999950000 |
