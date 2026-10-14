# 31. Minimum Swaps to Group Elements ≤ B

**Topic:** Arrays · **Pattern:** Fixed-size sliding window · **Status:** ⚠️ Logic correct, but the file **doesn't compile** · **File:** `tasks/MinimumSwaps.java` (Aug 25)

## Problem
Given `A` and `B`, find the minimum number of swaps needed to bring **all elements ≤ B** together into one contiguous block.

```
A = [1, 12, 10, 3, 14, 10, 5], B = 8 → 2
```

## Key idea
Let `k` = how many elements are `≤ B`. The final block has length `k`.
For every window of length `k`, each "bad" element (`> B`) inside needs exactly one swap. So the answer is the **minimum number of bad elements over all windows of size k**. Slide the window and update the count in O(1).

## Solution
```java
// TC: O(n); SC: O(1)
static int minSwaps(int[] A, int B) {
    int k = 0;
    for (int num : A) if (num <= B) k++;
    if (k <= 1) return 0;

    int bad = 0;
    for (int i = 0; i < k; i++) if (A[i] > B) bad++;

    int minSwaps = bad;
    for (int i = 0, j = k; j < A.length; i++, j++) {
        if (A[i] > B) bad--;      // leaves the window
        if (A[j] > B) bad++;      // enters the window
        minSwaps = Math.min(minSwaps, bad);
    }
    return minSwaps;
}
```

## Gotchas
- **Compile error in the saved file:** `return 0;` inside `void main` → `incompatible types: unexpected return value`. Move the logic into a function that returns `int`, as above.
- `k <= 1` means there's nothing to group, so return 0.
- This is the same slide as #9: drop `A[i]` and add `A[j]`.

## Tests
| A | B | Expected |
|---|---|---|
| `[1,12,10,3,14,10,5]` | 8 | 2 |
| `[1,2,3]` | 5 | 0 |
| `[5,6,1]` | 3 | 0 |
| `[1,9,2,9,3]` | 5 | 1 |
