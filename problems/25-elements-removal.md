# 25. Elements Removal (minimum total cost)

**Topic:** Sorting · **Pattern:** Greedy + contribution · **Status:** ✅ Works · **File:** `tasks/ElementRemoval.java` (Jul 25)

## Problem
Remove all elements one by one. Each removal **costs the sum of the elements currently in the array**. Find the minimum total cost.

```
[2, 1] → 4   (remove 2: cost 3, then remove 1: cost 1)
```

## Key idea
An element keeps adding to the cost until it's removed, so **remove the biggest first**.
After sorting ascending, `A[i]` is still present for `n - i` removals, so it contributes `A[i] * (n - i)`.

## Solution
```java
// TC: O(n log n); SC: O(1) extra
Arrays.sort(A);
int count = 0;
for (int i = 0; i < A.length; i++) {
    count += A[i] * (A.length - i);
}
```

## Gotchas
- This is the same contribution trick as #7 (sum of all subarray sums): count how many times each element gets added.
- Use `long` for big inputs, and cast **before** multiplying.

## Tests
| A | Expected |
|---|---|
| `[2,1]` | 4 |
| `[2,1,0,4,6]` | 24 |
| `[5]` | 5 |
