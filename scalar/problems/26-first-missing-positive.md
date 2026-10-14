# 26. First Missing Positive Integer

**Topic:** Arrays / Hashing · **Pattern:** Cyclic sort (use the array as its own hash) · **Status:** ✅ Works · **File:** `tasks/FindMIssingNumber.java` (Aug 25)

## Problem
Given an unsorted array (can have negatives, zeros, and duplicates), find the **smallest positive integer that is missing**. Use O(n) time and O(1) extra space.

```
[3, 4, -1, 1] → 2
[1, 2]        → 3
```

## Key idea
The answer is always in `1 .. n+1`. Put every value `v` in `1..n` at its "home" index `v - 1` by swapping.
After that, the first index `j` where `A[j] != j + 1` gives the answer `j + 1`. If every slot is correct, the answer is `n + 1`.

## Solution
```java
// TC: O(n) (each swap puts one number home for good); SC: O(1)
public static int findMissingNumber(int[] A) {
    int n = A.length;

    for (int i = 0; i < n; i++) {
        while (A[i] > 0 && A[i] <= n && A[i] != A[A[i] - 1]) {
            int temp = A[i];
            A[i] = A[temp - 1];
            A[temp - 1] = temp;
        }
    }

    for (int j = 0; j < n; j++) {
        if (A[j] != j + 1) {
            return j + 1;
        }
    }
    return n + 1;
}
```

## Gotchas
- Write the check as `A[i] != A[A[i] - 1]`, **not** `A[i] != i + 1`. The second version loops forever on duplicates like `[1, 1]`.
- Save `A[i]` in `temp` **before** swapping, because the index `A[i] - 1` changes mid-swap.
- It's a `while`, not an `if`: keep swapping until the value at `i` can't move.
- This mutates the input. The `missingNumber` variable in the saved file is never used, so return `n + 1` directly.

## Tests
| A | Expected |
|---|---|
| `[3,4,-1,1]` | 2 |
| `[1,2]` | 3 |
| `[7,8,9]` | 1 |
| `[1,1]` | 2 |
