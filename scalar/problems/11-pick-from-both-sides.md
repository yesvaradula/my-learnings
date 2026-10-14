# 11. Pick from Both Sides

**Topic:** Arrays · **Pattern:** Wrap-around sliding window · **Status:** ✅ Solved (Oct 5)

## Problem
Perform exactly `B` operations; each removes the leftmost or the rightmost element. Return the maximum possible sum of the removed elements.

Constraints: `1 ≤ N ≤ 10⁵`, `1 ≤ B ≤ N`, `-10³ ≤ A[i] ≤ 10³`.

```
A = [5, -2, 3, 1, 2], B = 3
front 3: 5,-2,3  = 6
front 2 + back 1 = 5
front 1 + back 2 = 8 ✅
back 3:  3,1,2   = 6
→ 8
```

## Key idea
Order doesn't matter, only the split: `i` from the front and `B − i` from the back, for `i = 0..B`.
Start with all `B` from the front, then repeatedly give back the last front element and take one more from the back. Each swap is O(1).

```
[5, -2, 3]  1   2         sum = 6
[5, -2]  3  1  [2]        sum = 6 − 3 + 2 = 5
[5]  -2  3  [1, 2]        sum = 5 + 2 + 1 = 8 ✅
 5   -2 [3, 1, 2]         sum = 8 − 5 + 3 = 6
```

## Solution
```java
// TC: O(B), SC: O(1)
static int pickSides(int[] A, int B) {
    int maxSum = 0;
    for (int i = 0; i < B; i++)
        maxSum += A[i];                   // all B from the front

    int currentSum = maxSum;
    int left = B - 1;                     // front element to give back
    int right = A.length - 1;             // back element to take

    while (left >= 0 && right >= A.length - B) {
        currentSum = currentSum - A[left] + A[right];
        maxSum = Math.max(maxSum, currentSum);
        left--;
        right--;
    }
    return maxSum;
}
```

## Gotchas
- Initialize `maxSum` with the starting sum, **not 0** (all-negative case).
- The starting split (all from the front) is itself a valid answer.
- `int` is enough here: max |sum| = 10⁵ × 10³ = 10⁸.
- Both loop conditions end together; `for (k = 0; k < B; k++)` works too.

## Tests
| A | B | Expected |
|---|---|---|
| `[5,-2,3,1,2]` | 3 | 8 |
| `[2,3,-1,4,2,1]` | 4 | 9 |
| `[1,2]` | 1 | 2 |
| `[-5,-3,-1]` | 2 | -4 |
| `[1,2,3]` | 3 | 6 |
