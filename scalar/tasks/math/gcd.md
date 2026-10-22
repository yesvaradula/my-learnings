# GCD (Greatest Common Divisor): one-page guide

Language: Java. All snippets below were compiled and tested (5000 random pairs vs `BigInteger.gcd`, plus a brute-force cross-check for the delete-one problem). Related: `GCD.java` (your solution), practice #40 (GCD of strings) in `scalar/CLAUDE.md`.

## 1. What it is

`gcd(A, B)` = the largest number that divides both A and B.
`gcd(A, 0) = A` (everything divides 0). `gcd(0, 0)` is undefined; Scaler constraints say `A + B > 0`.

```
gcd(4, 6)  = 2
gcd(6, 7)  = 1     (coprime)
gcd(12, 0) = 12
gcd(7, 7)  = 7
```

## 2. The one idea: Euclid

```
gcd(A, B) = gcd(B, A % B)
```

Why it is true: any number that divides both A and B also divides `A - q*B`, and `A % B` is exactly that leftover. So `(A, B)` and `(B, A % B)` have the **same set of common divisors**. Keep shrinking until `B == 0`, then the answer is A.

Trace of `gcd(4, 6)`:

```
gcd(4, 6) -> gcd(6, 4)   // 4 % 6 = 4: first step just swaps when A < B
gcd(6, 4) -> gcd(4, 2)
gcd(4, 2) -> gcd(2, 0)
gcd(2, 0) -> 2
```

## 3. Code: recursive and iterative

```java
// Recursive. TC: O(log(min(A, B))); SC: O(log(min(A, B))) for the call stack
static int gcdRec(int A, int B) {
    if (B == 0) return A;
    return gcdRec(B, A % B);
}

// Iterative. TC: O(log(min(A, B))); SC: O(1)
static int gcd(int A, int B) {
    while (B != 0) {
        int r = A % B;
        A = B;
        B = r;
    }
    return A;
}
```

## 4. Complexity (say it with names, not just "n")

- **Time: O(log(min(A, B)))**.
- **Space: recursive O(log(min(A, B))), iterative O(1).**
- Why log: `A % B < A / 2` always. If `B <= A/2` the remainder is `< B`. If `B > A/2` the remainder is `A - B`, which is `< A/2`. So the values at least halve every two calls.
- Worst case = two consecutive Fibonacci numbers (Lame's theorem). For 10^9 that is only about 44 calls, so the stack is never a problem.

## 5. Edge cases to name out loud

| Input | Result | Why |
|-------|--------|-----|
| `(12, 0)` | 12 | base case |
| `(0, 5)` | 5 | first call swaps to `(5, 0)` |
| `(7, 7)` | 7 | `7 % 7 = 0` |
| `(6, 7)` | 1 | coprime |
| `(10^9, 10^9 - 1)` | 1 | consecutive numbers are always coprime; fits in `int` |
| `(0, 0)` | undefined | excluded by constraints |

## 6. Patterns built on GCD

**A. GCD of an array.** Fold: `g = gcd(g, x)` starting from `g = 0` (because `gcd(0, x) = x`). Early exit when `g == 1`.

```java
// TC: O(N + log(max)); SC: O(1)
// (N gcd calls, but the running g only shrinks, so total divisions stay about N + log(max))
static int gcdArray(int[] A) {
    int g = 0;
    for (int x : A) {
        g = gcd(g, x);
        if (g == 1) break;
    }
    return g;
}
```

**B. LCM.** `lcm(a, b) * gcd(a, b) = a * b`. Divide **first** to avoid overflow, and use `long`.

```java
// TC: O(log(min(a, b))); SC: O(1)
static long lcm(int a, int b) {
    if (a == 0 || b == 0) return 0;
    return (long) (a / gcd(a, b)) * b;
}
```

**C. Coprime check.** `gcd(a, b) == 1`.

**D. Reduce a fraction p/q.** Divide both by `gcd(p, q)`.

**E. Prefix/suffix GCD** (when one element is removed). See task 3 below.

**F. GCD of strings** (#40): `s1 + s2 == s2 + s1`, then the answer is the prefix of length `gcd(n, m)`.

## 7. Follow-up tasks (do in this order)

| # | Task | Pattern | Status |
|---|------|---------|--------|
| 1 | Rewrite `gcd` **iteratively** from a blank file (O(1) space) | Euclid loop | done Oct 22 (#53) |
| 2 | GCD of an array | Fold with `g = 0` | todo |
| 3 | **Delete one:** max GCD of the array after deleting exactly one element | Prefix + suffix GCD | todo |
| 4 | LCM of two numbers (overflow-safe) | `a / g * b` in `long` | todo |
| 5 | Largest coprime divisor of A with respect to B | Strip common factors | todo |
| 6 | Reduce a fraction; count coprime pairs in an array (brute force) | gcd == 1 | todo |
| 7 | Extended Euclid (find x, y with `a*x + b*y = g`) | Back-substitute | stretch |

## 8. Solutions

### Task 2: GCD of an array
See pattern A above. Tests: `[12, 18, 24] -> 6`, `[7] -> 7`, `[0, 0, 9] -> 9`.
Gotcha: start `g` at **0**, not at 1 (1 would force the answer to 1).

### Task 3: Delete one element, maximize the GCD
Idea: `pre[i]` = gcd of `A[0..i]`, `suf[i]` = gcd of `A[i..n-1]`. Removing index `i` leaves `gcd(pre[i-1], suf[i+1])`. The two ends have only one side. A single element: nothing can be removed, return it.

```java
// TC: O(N log(max)); SC: O(N)
static int deleteOne(int[] A) {
    int n = A.length;
    if (n == 1) return A[0];
    int[] pre = new int[n], suf = new int[n];
    pre[0] = A[0];
    for (int i = 1; i < n; i++) pre[i] = gcd(pre[i - 1], A[i]);
    suf[n - 1] = A[n - 1];
    for (int i = n - 2; i >= 0; i--) suf[i] = gcd(suf[i + 1], A[i]);

    int best = Math.max(suf[1], pre[n - 2]);            // delete first / delete last
    for (int i = 1; i < n - 1; i++) {
        best = Math.max(best, gcd(pre[i - 1], suf[i + 1]));
    }
    return best;
}
```

Tests: `[12, 15, 18] -> 6` (delete 15), `[5] -> 5`, `[6, 9] -> 9`, `[2, 3, 5] -> 1`.
Brute force is O(N^2 log) (try every deletion); the prefix/suffix trick is the carry-forward idea you already know from "Special index" (#37) and "Product array" (#10).

### Task 4: LCM
See pattern B. Tests: `lcm(4, 6) = 12`; `lcm(10^9, 10^9 - 1) = 999999999000000000` (needs `long`; `a * b` in `int` would overflow).

### Task 5: Largest coprime divisor of A w.r.t. B
Find the largest divisor of A that shares no factor with B. Keep dividing A by `gcd(A, B)` until the gcd becomes 1.

```java
// TC: O(log(A) * log(min(A, B))); SC: O(1)
static int largestCoprimeDivisor(int A, int B) {
    int g = gcd(A, B);
    while (g != 1) {
        A /= g;
        g = gcd(A, B);
    }
    return A;
}
```

Tests: `(30, 12) -> 5`, `(5, 10) -> 1`, `(7, 3) -> 7`.
Why a loop and not one division: `A = 12, B = 2` has `gcd = 2`, but `12 / 2 = 6` still shares a 2 with B. Repeat until nothing is shared.

### Task 7 (stretch): Extended Euclid
Returns `{g, x, y}` with `a*x + b*y = g`. This is the base of the modular inverse (Modular Arithmetic is next on your syllabus).

```java
// TC: O(log(min(a, b))); SC: O(log(min(a, b)))
static long[] extGcd(long a, long b) {
    if (b == 0) return new long[]{a, 1, 0};
    long[] r = extGcd(b, a % b);
    return new long[]{r[0], r[2], r[1] - (a / b) * r[2]};
}
```

Test: `extGcd(30, 12) = [6, 1, -2]`, and `30*1 + 12*(-2) = 6`. If `g == 1`, then `x` is the modular inverse of `a` mod `b` (normalize with `((x % b) + b) % b`).

## 9. Trap list

- Writing "O(log n)" without saying what n is. Say `O(log(min(A, B)))`.
- Starting an array fold at `g = 1` instead of `0`.
- LCM as `a * b / gcd`: the product overflows `int`. Do `a / gcd * b` in `long` (for int inputs the result is at most about 10^18, which fits).
- Forgetting `gcd(0, x) = x` when the array has zeros.
- Delete-one: handling the first and last index separately (they have only one side) and the `n == 1` case.
- One test in `main`. Always run the edge cases from section 5.

## 10. Interview script

1. Restate and give an example (`gcd(12, 18) = 6`).
2. Brute force: try every number from `min(A, B)` down to 1, O(min(A, B)). Too slow for 10^9.
3. Improve: Euclid, `gcd(A, B) = gcd(B, A % B)`, say why (common divisors are preserved).
4. Code it (iterative preferred), then trace `(4, 6)`.
5. State TC `O(log(min(A, B)))` and SC (`O(1)` iterative, `O(log)` recursive) unprompted.
6. Edge cases: `B = 0`, `A = 0`, equal numbers, coprime, 10^9 values.
