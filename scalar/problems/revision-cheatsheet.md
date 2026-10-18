# Revision cheatsheet: 2D arrays, bit manipulation, subarrays

For Contest 2 (Wed Oct 21, 10 pm) and the mock interview (Fri Oct 23, 8:30 am). Saved Oct 17.

## 2D arrays

`A[i][j]` = row `i`, column `j`. `n = A.length` rows, `m = A[0].length` columns.

| Pattern | Core logic | Trap |
|---|---|---|
| Row / column traversal | Outer loop fixes the row (or column), inner loop walks it | Mixing up `n` and `m` on non-square matrices |
| Main diagonal | `i == j` | |
| Anti-diagonal | `i + j == n - 1` | |
| Transpose (square, in place) | Swap `A[i][j]` and `A[j][i]` for cells above the diagonal only: `j = i + 1` (the diagonal stays put; #46) | `j = 0` swaps every pair twice, so nothing changes |
| Rotate 90 clockwise | Transpose, then reverse each row | Each row needs its own `left` / `right` pointers |
| Rotate 90 counter-clockwise | Transpose, then reverse each column | Common follow-up |
| Boundary (#22) | Four edge loops, each stopping one short | 1x1, single row, single column |
| Spiral | Walls `top, bottom, left, right`. While `top <= bottom && left <= right`: right along `top`, `top++`; down along `right`, `right--`; if `top <= bottom`: left along `bottom`, `bottom--`; if `left <= right`: up along `left`, `left++` | Without the two `if` guards a single row or column repeats elements |
| Staircase search (#20, #27, #44) | Start top-right: too big go left, too small go down | O(n + m), not O(n * m) |
| 2D prefix sum | `P[i][j] = A[i][j] + P[i-1][j] + P[i][j-1] - P[i-1][j-1]` | Query: `P[r2][c2] - P[r1-1][c2] - P[r2][c1-1] + P[r1-1][c1-1]` (add back the corner subtracted twice) |

## Bit manipulation

```java
(x >> k) & 1                  // check bit k (0 or 1)
x | (1 << k)                  // set bit k
x & ~(1 << k)                 // clear bit k
x ^ (1 << k)                  // toggle bit k
x & (x - 1)                   // drop the lowest set bit
x & -x                        // keep only the lowest set bit
x > 0 && (x & (x - 1)) == 0   // power of 2
```

Count set bits:
- Simple: test `x & 1`, then `x >>>= 1`. Use `>>>`: `>>` on a negative number shifts in 1s forever.
- Brian Kernighan: `while (x != 0) { x &= x - 1; count++; }`. One iteration per set bit.

XOR rules: `a ^ a = 0`, `a ^ 0 = a`, order does not matter.
- Single number (others appear twice): XOR everything; pairs cancel.
- Single number (others appear three times): for each bit, count 1s across all numbers; `count % 3` is that bit of the answer.

Contribution per bit (contest 1 XOR pairs): pairs differing at bit `b` = `ones * zeros`. Sum of XOR over all pairs = sum over b of `ones * zeros * 2^b`.

Java traps:
- `1 << 31` is negative. For bit 31 and above in a `long`, use `1L << k`.
- `x & 1 == 0` parses as `x & (1 == 0)` and does not compile. Write `(x & 1) == 0`.

## Subarrays

| Question looks like | Technique | Solved |
|---|---|---|
| Max / min sum, contiguous | Kadane (init with `A[0]`) | #1, #28, #45 |
| Range sums, many queries | Prefix sum (`long`) | #8, #36 |
| Fixed length `k` | Fixed sliding window | #9, #31 |
| Count / longest with sum `< B`, positives only | Variable window (shrink while invalid) | #23 |
| Sum over all subarrays | Contribution: `A[i] * (i+1) * (n-i)` | #7, #15 |
| Range add, many updates | Difference array | #17, #19 |
| Count pairs with X before Y | Carry forward (running count of X) | #29 |
| Count subarrays with sum = k (negatives allowed) | Prefix sum + HashMap of prefix counts | Not done yet (hashing, contest topic) |

Facts: an array has `n(n+1)/2` subarrays. Interview narration: brute force O(n^3), then prefix sums O(n^2), then the pattern above at O(n).
