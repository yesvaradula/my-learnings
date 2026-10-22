# 53. Greatest Common Divisor

**Topic:** Math (GCD) | **Pattern:** Euclid, `gcd(A, B) = gcd(B, A % B)` | **Status:** Solved Oct 22, 2026, 15/15 PASS + 2000 random cross-checks vs `BigInteger.gcd` | File: `tasks/math/GCD.java` | Guide: `tasks/math/gcd.md`

## Problem
Return the largest number that divides both A and B. `0 <= A, B <= 10^9`, `A + B > 0`.

```
A = 4,  B = 6   ->  2
A = 6,  B = 7   ->  1
A = 12, B = 0   -> 12
```

## Key idea
- `A % B = A - q*B` with `q = A / B` (integer division): the leftover after removing whole B's.
- **Nothing is lost:** a `d` dividing A and B divides `A - q*B`, which is `A % B`.
- **Nothing is added:** `A = q*B + A % B`, so a `d` dividing B and `A % B` divides A.
- Same set of common divisors, so the same greatest one. Shrink until `B == 0`; then the answer is A.
- The divisor sets are only the proof. The code never lists divisors (that is the O(min(A, B)) brute force).
- **Why log:** `A % B < A / 2` (B small: remainder < B <= A/2; B big: remainder = A - B < A/2), so values halve every two steps. Worst case is consecutive Fibonacci numbers: about 44 steps for 10^9.
- If A < B, the first step only swaps them (`4 % 6 = 4`).

Traces: `gcd(48, 18) -> (18, 12) -> (12, 6) -> (6, 0) = 6`; `gcd(1000, 999) -> (999, 1) -> (1, 0) = 1` (consecutive numbers are coprime).

## Final solution
```java
// TC: O(log(min(A, B))), SC: O(1)
static int gcd(int A, int B) {
    while (B != 0) {
        int r = A % B;
        A = B;
        B = r;
    }
    return A;
}
```

## How it went (Oct 22)
1. Wrote the iterative version (commit message said "recursive"). The comment said `O(log n)` without naming n and `SC: O(1) due to recursion stack`, which contradicts the loop. Two of the three sample tests were commented out.
2. Asked for number examples of "d divides A - q*B" and confirmed `A % B = A - q*B` himself.
3. Asked whether the algorithm has to list all divisors for every pair: no, the sets are only the proof.
4. Explain-back: first said `A - q*B` "is B and A % B" (it is only `A % B`) and skipped the other two blanks; traces of `gcd(48, 18)` and `gcd(1000, 999)` correct.
5. Skipped the "fix the comment" question twice, then fixed the comment and uncommented the tests.

## Tests
15 cases in `main`: the samples, `A = 0`, equal values, A < B, consecutive numbers, a small and a large Fibonacci pair (F44, F43, the worst case), values at 10^9 with 0 / coprime / a multiple. Plus 2000 random pairs over the full range vs `BigInteger.gcd`.

## Interview lines
- *"Any common divisor of A and B also divides A mod B, and the other way round, so gcd(A, B) = gcd(B, A mod B). I repeat until B is 0."*
- *"The remainder is below half of A, so the values halve every two steps: O(log(min(A, B))) time, O(1) space iteratively, O(log) stack recursively."*
- Next: gcd of an array (fold from `g = 0`), delete-one (prefix + suffix GCD), LCM as `(long) (a / g) * b`. See `gcd.md` section 7.
