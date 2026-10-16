# 41. Highest Product of Three Numbers

**Topic:** Arrays · **Pattern:** Track top-3 max and bottom-2 min in one pass · **Status:** ✅ Solution provided (Oct 14, 2026), 12/12 PASS · File: `tasks/arrays/intermediate/MaxProductOfThree.java`

## Problem
Given an integer array `A` of size `N`, return the highest product of any 3 numbers in it. The answer fits in a 32-bit signed int. `N` up to 5 * 10^5.

```
[1, 2, 3]           → 6
[-10, -10, 1, 3, 2] → 300   (-10 * -10 * 3)
[-5, -4, -3, -2]    → -24   (all negative: the 3 closest to zero)
```

## Key idea
1. Order doesn't matter: pick **any** 3 numbers, not 3 neighbours, so this isn't a window problem.
2. The winner is always one of two candidates:
   - the **three largest**: `max1 * max2 * max3`
   - the **largest x the two smallest**: `max1 * min1 * min2` (two big negatives make a big positive)
3. Easy version: sort, then compare `A[n-1]*A[n-2]*A[n-3]` with `A[0]*A[1]*A[n-1]`, O(N log N).
   Better: one pass keeping `max1 >= max2 >= max3` and `min1 <= min2`, O(N).

## Final solution
```java
static int maxp3(int[] A) {
    // TC: O(N), SC: O(1)
    int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;
    int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;

    for (int x : A) {
        if (x > max1)      { max3 = max2; max2 = max1; max1 = x; }
        else if (x > max2) { max3 = max2; max2 = x; }
        else if (x > max3) { max3 = x; }

        if (x < min1)      { min2 = min1; min1 = x; }
        else if (x < min2) { min2 = x; }
    }

    long allMax = (long) max1 * max2 * max3;
    long twoMin = (long) max1 * min1 * min2;
    return (int) Math.max(allMax, twoMin);
}
```
Scaler wrapper: `public class Solution { public int maxp3(int[] A) { ... } }`.

## Bugs in his first attempt (sliding window of "big" numbers)
- Skipped `-1, 0, 1` entirely: `[1,2,3]`, `[0,0,0]`, `[-1,-2,-3]` all returned the initial `product = 1`.
- `Math.abs` dropped the sign: `[-5,-4,-3,-2]` → 60 instead of -24.
- Reset `minremove` to `MAX_VALUE` after each removal, so the next removal threw out the newest number, not the smallest: `[10,5,9,20,15]` → 1800 instead of 3000.
- `current / minremove` with a negative divisor flips the sign of `current`.
- He was exhausted and asked for the full code, so the solution was provided. **Rewrite it from memory** next session.

## Tests
`[1,2,3]`→6 · `[0,0,0]`→0 · `[-1,-2,-3]`→-6 · `[-5,-4,-3,-2]`→-24 · `[10,5,9,20,15]`→3000 · `[-10,-10,1,3,2]`→300 · `[-10,-3,5,6,-2]`→180 · `[0,-1,3,100,-70,-50]`→350000 · `[1,1,1,1]`→1 · `[-1,0,1]`→0 · `[-5,-6,0,1]`→30 · `[1000,1000,1000,-1]`→1000000000.

## Rewrite from memory (Oct 16)
`scalar/reharsals/MaxProductOfThreeV2.java`, written from a blank file. Top-3 max chain and the two candidates were right first time. Bugs hit:
- `return` inside the `for` loop: "missing return statement" compile error, and it would have returned after the first element.
- `min_2 = min;` (the `Integer.MIN_VALUE` sentinel) instead of `min_2 = num;`. Only shows when the second-smallest arrives after the smallest via the `else if`: `[-10,-10,1,3,2]` → 0 instead of 300. Cause: sentinels named `max`/`min` (holding MAX/MIN for the opposite chains). Rename to `INF`/`NEG_INF`.
- Products in `int`: fixed with `(long) a * b * c` (cast before the first multiply).
- TC O(n), SC O(1): stated after being asked, not unprompted.
Final: 12/12 PASS + 0 mismatches against an O(n³) brute force on 2000 random arrays.
