# 23. Count Subarrays With Sum Less Than B

**Topic:** Arrays · **Pattern:** Variable-size sliding window · **Status:** ✅ Works (positive numbers only) · **File:** `tasks/CountSubArray.java` (Jul 24)

## Problem
Given an array `A` of **positive** integers and `B`, count the subarrays whose sum is **strictly less than `B`**.

```
A = [1, 11, 2, 3, 15], B = 10 → 4   ([1], [2], [3], [2,3])
```

## Key idea
Grow the window by moving `end`. While the sum is `>= B`, shrink from `start`.
Once the window `[start..end]` is valid, **every** subarray ending at `end` that starts inside it is also valid. That's `end - start + 1` new subarrays.

## Solution
```java
// TC: O(n) (each element enters and leaves once); SC: O(1)
int count = 0, start = 0, sum = 0;

for (int end = 0; end < A.length; end++) {
    sum += A[end];

    while (start <= end && sum >= B) {
        sum -= A[start];
        start++;
    }
    count += (end - start + 1);
}
```

## Gotchas
- Only works for **non-negative** numbers. With negatives, shrinking the window doesn't always reduce the sum.
- `count` can reach n²/2, so use `long` when n is large.
- `start <= end` stops the shrink when a single element is already `>= B` (it adds 0 that round).

## Tests
| A | B | Expected |
|---|---|---|
| `[1,11,2,3,15]` | 10 | 4 |
| `[2,5,6]` | 10 | 4 |
| `[1,2,3]` | 100 | 6 |
| `[5]` | 5 | 0 |
