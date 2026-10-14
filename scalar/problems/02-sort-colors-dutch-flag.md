# 02. Sort 0s, 1s, 2s (Dutch National Flag)

**Topic:** Arrays · **Pattern:** Three pointers / partitioning · **Status:** ✅ Solved (Oct 4, rewritten from memory)

## Problem
An array contains only 0s, 1s and 2s. Sort it **in place, in one pass**, with no `Arrays.sort()` and no counting-then-overwriting.

```
[2, 0, 2, 1, 1, 0]  →  [0, 0, 1, 1, 2, 2]
```

## Key idea
Keep three zones with three pointers:
```
[ 0 0 0 | 1 1 1 | ? ? ? ? | 2 2 2 ]
  0..low-1  low..mid-1  mid..high  high+1..end
```
Look at `nums[mid]`:
- **0** → swap with `low`, then `low++`, `mid++`
- **1** → `mid++`
- **2** → swap with `high`, then `high--` only (**don't** `mid++`: the swapped-in value is unchecked)

## Solution
```java
// TC: O(n) single pass, SC: O(1)
static void sortColors(int[] nums) {
    int low = 0, mid = 0, high = nums.length - 1;

    while (mid <= high) {
        switch (nums[mid]) {
            case 0:
                swap(nums, low, mid);
                low++;
                mid++;
                break;
            case 1:
                mid++;
                break;
            default: // 2
                swap(nums, mid, high);
                high--;
                break;
        }
    }
}

static void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

## Alternative (two passes, the first attempt)
Count 0s, 1s and 2s, then overwrite. Correct, O(n) time and O(1) space, but two passes. A good fallback if you blank in a contest.

## Gotchas
- Loop condition is `mid <= high`, not `<`.
- No `mid++` after swapping with `high`. This is the #1 bug.
- `default` catches anything that isn't 0 or 1; fine only because the input is guaranteed to be 0/1/2.

## Tests
| Input | Expected |
|---|---|
| `[2,0,2,1,1,0]` | `[0,0,1,1,2,2]` |
| `[2,0,1]` | `[0,1,2]` |
| `[2,2,2,0,0,0]` | `[0,0,0,2,2,2]` |
| `[1,1,1]` | `[1,1,1]` |
| `[1]` | `[1]` |
