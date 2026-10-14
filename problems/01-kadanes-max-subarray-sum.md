# 01. Maximum Subarray Sum (Kadane's Algorithm)

**Topic:** Arrays · **Pattern:** Kadane's · **Status:** ✅ Solved (Oct 4)

## Problem
Given an integer array `nums`, find the contiguous subarray with the largest sum and return that sum.

```
[-2, 1, -3, 4, -1, 2, 1, -5, 4]  →  6   (subarray [4, -1, 2, 1])
```

## Key idea
At every index, decide: **extend** the current subarray, or **start fresh** here?
`currentSum = max(nums[i], currentSum + nums[i])`

Starting fresh wins exactly when `currentSum` is negative, because a negative prefix can never help a future subarray. That is the "hidden left pointer".

## Solution
```java
// TC: O(n), SC: O(1)
public int maxSubArray(int[] nums) {
    int maxSum = nums[0];
    int currentSum = nums[0];

    for (int i = 1; i < nums.length; i++) {
        currentSum = Math.max(nums[i], currentSum + nums[i]);
        maxSum = Math.max(maxSum, currentSum);
    }
    return maxSum;
}
```

## Variant: also return start and end indices
```java
int maxSum = nums[0], currentSum = nums[0];
int start = 0, bestStart = 0, bestEnd = 0;

for (int i = 1; i < nums.length; i++) {
    if (nums[i] > currentSum + nums[i]) {   // same as: currentSum < 0
        currentSum = nums[i];
        start = i;                          // left pointer jumps here
    } else {
        currentSum += nums[i];
    }
    if (currentSum > maxSum) {
        maxSum = currentSum;
        bestStart = start;
        bestEnd = i;
    }
}
```

## Trace: `{-1, -1, 2, 5, -1}`
| i | nums[i] | currentSum | What happened | maxSum |
|---|---|---|---|---|
| 0 | -1 | -1 | start | -1 |
| 1 | -1 | -1 | restart at 1 | -1 |
| 2 | 2 | 2 | restart at 2 (skips the -1s) | 2 |
| 3 | 5 | 7 | extend | 7 |
| 4 | -1 | 6 | extend, worse | 7 |

## Gotchas
- Initialize with `nums[0]`, **not 0**. All negatives: `[-3, -1, -2]` → `-1`.
- Empty array would crash on `nums[0]`; check the constraints.

## Tests
| Input | Expected |
|---|---|
| `[-2,1,-3,4,-1,2,1,-5,4]` | 6 |
| `[-3,-1,-2]` | -1 |
| `[-1,-1,2,5,-1]` | 7 |
| `[5]` | 5 |
