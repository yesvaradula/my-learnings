# 05. Add One To Number

**Topic:** Arrays · **Pattern:** Carry from the back · **Status:** ✅ Solved (Oct 5, after two fixes)

## Problem
A non-negative number is given as an array of digits (most significant first). Add 1 and return the digits.
- The input **may** have leading zeros (`0 1 2 3` is valid).
- The output must **not** have leading zeros.

```
[1, 2, 3]     →  [1, 2, 4]
[9, 9, 9]     →  [1, 0, 0, 0]
[0, 1, 2, 3]  →  [1, 2, 4]
[0, 0, 0]     →  [1]
```

## Key idea
Walk from the **last** digit backwards:
- digit < 9 → add 1 and stop
- digit == 9 → set it to 0 and carry on

If every digit was 9, the array grows by one (`1` followed by zeros).
Strip leading zeros **after** adding, so `[0, 9, 9]` → `[1, 0, 0]` needs no special case.

The number can be 100+ digits long, so converting to `int`/`long` overflows.

## Solution
```java
// TC: O(n), SC: O(n) (output copy)
static int[] plusOne(int[] digits) {
    int n = digits.length;

    // Step 1: add from right to left
    boolean carriedAllTheWay = true;
    for (int i = n - 1; i >= 0; i--) {
        if (digits[i] < 9) {
            digits[i]++;
            carriedAllTheWay = false;
            break;
        }
        digits[i] = 0;
    }

    // Step 2: all digits were 9 → grow the array
    if (carriedAllTheWay) {
        int[] result = new int[n + 1];
        result[0] = 1;
        return result;
    }

    // Step 3: strip leading zeros (at least one digit is non-zero after +1)
    int leadingZeros = 0;
    while (leadingZeros < n && digits[leadingZeros] == 0) {
        leadingZeros++;
    }

    int[] result = new int[n - leadingZeros];
    System.arraycopy(digits, leadingZeros, result, 0, result.length);
    return result;
}
```

## `System.arraycopy(src, srcPos, dest, destPos, length)`
"From `src`, starting at `srcPos`, copy `length` elements into `dest`, starting at `destPos`."
Alternative in one line: `Arrays.copyOfRange(digits, leadingZeros, n)`.

## Bugs along the way
1. The LeetCode version (no zero stripping) returned `[0, 1, 2, 4]` for `[0, 1, 2, 3]`.
2. A rewrite lost `digits[i] = 0;`, so `[1, 2, 9]` gave `[1, 3, 9]`. `[9, 9, 9]` still passed, which hid the bug.

## Tests
| Input | Expected |
|---|---|
| `[1,2,3]` | `[1,2,4]` |
| `[1,2,9]` | `[1,3,0]` |
| `[9,9,9]` | `[1,0,0,0]` |
| `[0,1,2,3]` | `[1,2,4]` |
| `[0,0,9,9]` | `[1,0,0]` |
| `[0]` | `[1]` |
| `[0,0,0]` | `[1]` |
