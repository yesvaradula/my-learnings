# 37. Special Index (count)

**Topic:** Arrays · **Pattern:** Even/odd prefix sums · **Status:** ✅ Works · **File:** `tasks/SpecialIndex.java` (Jul 21)

## Problem
An index `i` is **special** if, after deleting `A[i]`, the sum of elements at even indices equals the sum at odd indices. Count the special indices.

```
[2, 1, 6, 4] → 1   (delete index 1 → [2, 6, 4]: even 2+4 = 6, odd 6)
```

## Key idea
Deleting `A[i]` shifts every element **after** `i` left by one, so their even/odd parity **flips**. Elements before `i` keep theirs.
- new even sum = `evenPrefix[i-1]` + `oddSum(i+1 .. n-1)`
- new odd sum = `oddPrefix[i-1]` + `evenSum(i+1 .. n-1)`

Build `pfE` and `pfO` (prefix sums of even-index and odd-index values) so each check is O(1).

## Solution
```java
// TC: O(n); SC: O(n)
static int solve(int[] A) {
    int n = A.length;
    int[] pfE = new int[n];
    int[] pfO = new int[n];
    pfE[0] = A[0];

    for (int i = 1; i < n; i++) {
        pfE[i] = (i % 2 == 0) ? pfE[i - 1] + A[i] : pfE[i - 1];
        pfO[i] = (i % 2 != 0) ? pfO[i - 1] + A[i] : pfO[i - 1];
    }

    int count = 0;
    for (int i = 0; i < n; i++) {
        int evenSum = ((i == 0) ? 0 : pfE[i - 1]) + pfO[n - 1] - pfO[i];
        int oddSum  = ((i == 0) ? 0 : pfO[i - 1]) + pfE[n - 1] - pfE[i];
        if (evenSum == oddSum) count++;
    }
    return count;
}
```

## Gotchas
- The suffix part takes the **opposite** prefix array, because the parity flips. That's the whole trick.
- Use `pfX[n-1] - pfX[i]` (excluding `i`) for the suffix, and `pfX[i-1]` for the prefix, guarding `i == 0`.
- The saved file prints debug lines. Remove them before submitting. Use `long` for big sums.

## Tests
| A | Expected |
|---|---|
| `[2,1,6,4]` | 1 |
| `[1,1,1]` | 3 |
| `[5]` | 1 *(empty array: 0 == 0)* |
