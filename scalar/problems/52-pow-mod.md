# 52. Implement Power Function (A^B % C)

**Topic:** Math (modular arithmetic) + Recursion · **Pattern:** Fast power by halving B · **Status:** Solved Oct 21, 2026, 15/15 PASS + 2000 random cross-checks vs `BigInteger.modPow` · File: `tasks/math/PowMod.java` (practice: `tasks/recursive/Power.java`)

## Problem
Return `(A^B) % C` as a value in `[0, C - 1]`. `-10^9 <= A <= 10^9`, `0 <= B <= 10^9`, `1 <= C <= 10^9`.

```
A = 2,  B = 3, C = 3   ->  2
A = 3,  B = 3, C = 1   ->  0
A = -1, B = 1, C = 20  -> 19
```

## Key idea
- **Slow:** `a^b = a * a^(b-1)`: b shrinks by 1, O(b) calls; a billion for b = 10^9.
- **Fast:** `a^b = half * half` (even b) or `a * half * half` (odd b), with `half = a^(b/2)` computed **once**. b halves each call: O(log b), about 30 calls for 10^9. Calling `power(a, b/2)` twice doubles the calls at every level and brings it back to O(b).
- **Mod rules:** `(x * y) % c = ((x % c) * (y % c)) % c`, so take `% c` after every multiplication; every value stays below c.
- **Overflow:** `half` can be up to 10^9, so `half * half` (10^18) needs `long`, and the multiply-by-`a` needs a `% c` in between (three big numbers in a row would reach 10^27).
- **Negative A:** Java's `%` keeps the sign (`-1 % 20 == -1`), so normalise once: `((A % C) + C) % C`.
- **C = 1:** the base case returns `1 % c`, not `1`.
- **Signature:** Scaler's is `int pow(int A, int B, int C)`; keep it `int` and do the work in a `long` helper.

Hand trace `3^5 % 7`: base 1 -> b=1: 3 -> b=2: 9 % 7 = 2 -> b=5: 4 * 3 % 7 = 5. (243 % 7 = 5.)

## Final solution
```java
// TC: O(log n) SC: O(log n)
static int solve(int A, int B, int C) {
    long a = ((A % C) + C) % C;
    return (int) power(a, B, C);
}

static long power(long A, int B, int C) {
    if (B == 0) {
        return 1 % C;
    }
    A = ((A % C) + C) % C;
    long half = power(A, B / 2, C);
    long result = half * half % C;

    if (B % 2 == 1) {
        return result * A % C;
    }

    return result;
}
```
(The normalisation inside `power` is redundant after `solve` does it once; harmless.)

## How it went (Oct 21, evening, still unwell)
1. Said he was not getting PowMod and asked for a worked practice first. Did `fastPower` without mod (`Power.java`): hardcoded `2 * half * half` (only tested with a = 2), then switched to `long` but left the `2`; then 243 for 3^5.
2. PowMod first version: every trap handled (`1 % C`, negative fix, mod between multiplications, half once), but all `int`: passed his small tests, failed against `BigInteger` with C up to 10^9 (negative outputs from overflow).
3. Switched to `long` but changed `solve`'s return type to `long` (breaks Scaler's signature). Said "done, added helper" before the helper was saved. Final version has the helper; Claude changed `solve` back to return `int` when adding tests.

## Tests
15 cases in `main` (samples, hand trace, B = 0 with C = 1, 0^0, zero base, A multiple of C, negative A with odd/even powers, int-overflowing half, B = 10^9, big negative A, all near the limits), expected values from `BigInteger.modPow`. Plus 2000 random inputs over the full constraint range.

## Interview lines
- *"Halving the exponent gives O(log B) multiplications; I compute the half power once and square it, multiplying by A once more when B is odd."*
- *"I take mod after every multiplication and multiply in long, since two values below 10^9 can reach 10^18."*
- *"Java's % keeps the sign of the dividend, so I normalise A with ((A % C) + C) % C first, and the base case returns 1 % C for C = 1."*
- Iterative version (O(1) space): loop over the bits of B, squaring the base each step (see `scalar/problems/bit.md` for bit reading).
