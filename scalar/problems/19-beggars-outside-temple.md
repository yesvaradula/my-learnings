# 19. Beggars Outside Temple

**Topic:** Arrays · **Pattern:** Difference array + prefix sum · **Status:** ✅ Works · **File:** `tasks/Beggers.java` (Oct 4)

## Problem
`A` beggars sit in a row with empty pots. Devotee `i` gives `P` coins to every beggar from `L` to `R` (**1-indexed**, inclusive), given as `B[i] = [L, R, P]`. Return each pot's final amount.

```
A = 5, B = [[1,2,10], [2,3,20], [2,5,25]] → [10, 55, 45, 25, 25]
```

## Key idea
Same as #17: `ans[L] += P`, `ans[R+1] -= P`, then a prefix sum. Convert to 0-indexed first.

## Solution
```java
// TC: O(A + Q); SC: O(1) extra (output array only)
static int[] solution(int[][] B, int A) {
    int[] ans = new int[A];

    for (int[] donation : B) {
        int l = donation[0] - 1;   // 1-based → 0-based
        int r = donation[1] - 1;
        int v = donation[2];

        ans[l] += v;
        if (r + 1 < A)
            ans[r + 1] -= v;
    }

    for (int i = 1; i < A; i++) {
        ans[i] += ans[i - 1];
    }
    return ans;
}
```

## Gotchas
- The input is **1-indexed**, so subtract 1 from both L and R.
- Guard `r + 1 < A` when a range reaches the last beggar.

## Tests
| A | B | Expected |
|---|---|---|
| 5 | `[[1,2,10],[2,3,20],[2,5,25]]` | `[10,55,45,25,25]` |
| 1 | `[[1,1,7]]` | `[7]` |
| 3 | `[]` | `[0,0,0]` |
| 4 | `[[1,4,5],[4,4,1]]` | `[5,5,5,6]` |
