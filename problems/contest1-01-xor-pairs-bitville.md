# Contest 1, Missed #1: XOR Pairs (Bitville)

**Topic:** Bit Manipulation · **Pattern:** Count 1s × 0s per bit · **Status:** ✅ Solved (Oct 13, 2026), 6/6 PASS

## Problem
Given array `A` (N integers) and queries `B`, for each query bit `b` count the pairs `(i, j)` (i < j) where the XOR of the b-th bits of `A[i]` and `A[j]` is 1.

Constraints: `1 ≤ N ≤ 4×10⁴`, `1 ≤ A[i] ≤ 10⁹`, `1 ≤ Q ≤ 100`, `0 ≤ B[i] < 32`.

```
A = [1, 2, 3], B = [0, 1]    →  [2, 2]
A = [2, 4, 7, 11], B = [3]   →  [3]
```

## Strip the story
XOR of two bits is 1 **only when the bits differ**. So each query asks: how many pairs have different b-th bits?

## Key idea
Every pair of (one number with bit = 1, one with bit = 0) counts:
**answer = ones × zeros**, where `zeros = N − ones`.
No pair loop needed. (Same "count choices and multiply" idea as the contribution technique.)

Check a bit: `(x >> b) & 1`.

```
A = [2, 4, 7, 11], b = 3
 2 → 0010 → 0
 4 → 0100 → 0
 7 → 0111 → 0
11 → 1011 → 1     ones = 1, zeros = 3 → 3
```

## Questions to answer while coding
1. Brute force checks every pair per query (~8×10⁸). Why is that too slow?
2. Counting ones per query: what's the TC? (Q × N = 4×10⁶.)
3. Max answer = (2×10⁴)² = 4×10⁸. Does it fit in `int`?
4. Bonus: precompute ones for all 32 bits once → O(1) per query.

## Signature
```java
static int[] solve(int[] A, int[] B)
```

## Your solution
```java
static long[] solve(int[] A, int[] B) {
    long[] ans = new long[B.length];
    for (int i = 0; i < B.length; i++) {
        long ones = 0;
        for (int j = 0; j < A.length; j++) {
            if ((A[j] & (1 << B[i])) != 0) ones++;
        }
        ans[i] = ones * (A.length - ones);   // ones is long, so the product is long
    }
    return ans;
}
```
TC: O(N · Q). SC: O(1) extra (+ O(Q) output).

## Bugs hit along the way
- TC first written as O(n²); it's O(N · Q) (two different arrays).
- `long[] ans` with `int` math: `ones * (n - ones)` overflowed **before** being stored. Fixed by making `ones` a `long`.
- With the real constraints (N ≤ 4×10⁴) the max is 4×10⁸, which fits in `int`, and the judge expects `int[]`. `long` is still the safe habit; just match the required return type in the contest.
- Only one test, checked by eye → replaced with an `expected` array + PASS/FAIL loop.

## Tests
| A | B | Expected |
|---|---|---|
| `[1,2,3]` | `[0,1]` | `[2,2]` |
| `[2,4,7,11]` | `[3]` | `[3]` |
| `[5]` | `[0]` | `[0]` |
| `[1,1,1]` | `[0,1]` | `[0,0]` |
| `[1,2,4,8]` | `[0,31]` | `[3,0]` |
| 100,000 × `k % 2` | `[0]` | `[2500000000]` (overflow check) |
