# 51. Sum of Digits (recursive)

**Topic:** Recursion · **Pattern:** Last digit + recurse on the rest · **Status:** Solved Oct 21, 2026, 11/11 PASS + 2000 random cross-checks · File: `tasks/recursive/SumOfDigits.java`

## Problem
Given a non-negative integer `A`, return the sum of its digits using recursion, no loops.

```
46  ->  10   (4 + 6)
11  ->   2
0   ->   0
```

## Key idea
Every recursive function has three parts:
1. **Base case:** the smallest input, answered directly. A single digit (`A <= 9`) is its own digit sum (`A == 0 -> 0` also works).
2. **Smaller problem:** `A / 10` drops the last digit.
3. **Trust the call:** assume `solve(A / 10)` already returns the right answer, and build on it.

`solve(A) = A % 10 + solve(A / 10)`: "the last digit plus the sum of the rest."

No shared running total: the answer is built from return values as the calls unwind.

## Final solution
```java
// TC: O(log10 A), one call per digit. SC: O(log10 A) call stack (at most 10 frames for an int).
static int solve(int A) {
    if (A <= 9) {
        return A;
    }

    return A % 10 + solve(A / 10);
}
```

## How it went (Oct 21, first recursion problem, on leave, unwell)
1. First described it as a loop with a running `sum` (iterative thinking). The pieces were all right: `% 10`, `/ 10`, single digit as the stop.
2. Reframed as "6 + digit sum of 4" = `46 % 10 + solve(46 / 10)`; first code was correct, with a local `long sum` and a cast (unnecessary: the max digit sum of an int is 82).
3. Simplified to the one-line return himself.
4. Said TC O(log n), SC O(log n) in chat (correct: one call per digit, call stack depth = number of digits), but the file comment said `O(n)` / `O(1)`. Fixed when the tests were added.

## Tests
11 cases in `main`: samples, 0, 5, 9 (base case boundary), 10, 99, 100, 10^9, 1999999999 (digit sum 82), `Integer.MAX_VALUE`. Plus 2000 random ints against the loop version.

## Interview lines
- *"Sum of digits of A is the last digit plus the sum of digits of A / 10; the base case is a single digit."*
- *"There is one call per digit, so time is O(log10 A), and the call stack holds one frame per digit, so space is O(log10 A), unlike the loop version's O(1)."*
- Recursion trade-off: each pending call takes stack space; very deep recursion (around 10^4 frames and up, depending on the JVM's stack size) can throw `StackOverflowError`, where a loop would not.
