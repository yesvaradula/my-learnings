# 10. Product Array Puzzle (Product of Array Except Self)

**Topic:** Arrays · **Pattern:** Left × right products · **Status:** ✅ Solved both ways (Oct 5)

## Problem
Return an array where element `i` is the product of all elements except `A[i]`. **No division.** The answers fit in 32-bit integers.

```
[1, 2, 3, 4, 5]  →  [120, 60, 40, 30, 24]
```

## Key idea
Everything except `A[i]` = **(product of everything left of i) × (product of everything right of i)**.
The same structure as Trapping Rain Water's `leftMax[]`/`rightMax[]`, but with products, and here `left[i]` **excludes** `A[i]`.
The empty product is `1`.

Why not divide: the problem forbids it, and a zero in the array breaks division.

## Solution 1: two arrays
```java
// TC: O(n), SC: O(n)
static int[] productArrayLR(int[] A) {
    int n = A.length;
    int[] left = new int[n];
    int[] right = new int[n];

    left[0] = 1;
    for (int i = 1; i < n; i++)
        left[i] = left[i - 1] * A[i - 1];

    right[n - 1] = 1;
    for (int i = n - 2; i >= 0; i--)
        right[i] = right[i + 1] * A[i + 1];

    for (int i = 0; i < n; i++)
        A[i] = left[i] * right[i];
    return A;
}
```

## Solution 2: O(1) auxiliary space
Store the left products directly in `ans`, then walk backwards with a single `right` variable.
```java
// TC: O(n), SC: O(1) auxiliary (O(n) including the output array)
static int[] productArray(int[] A) {
    int n = A.length;
    int[] ans = new int[n];

    ans[0] = 1;
    for (int i = 1; i < n; i++)
        ans[i] = ans[i - 1] * A[i - 1];

    int right = 1;
    for (int i = n - 1; i >= 0; i--) {
        ans[i] *= right;     // ① use right (excludes A[i])
        right *= A[i];       // ② then include A[i] for the next index
    }
    return ans;
}
```

## Space complexity: total vs auxiliary
- **Total:** counts the output → O(n)
- **Auxiliary:** only extra memory beyond the output → O(1)

Say: *"O(1) auxiliary, O(n) if we count the output array."*

## First attempt (rejected)
Used division (forbidden), multiplied `A[0]` twice (`product = A[0]` and then looped over all of `A`), and returned 0 for negatives and zeros. It passed `[1,2,3,4,5]` only because `A[0] = 1`.

## Tests
| Input | Expected |
|---|---|
| `[1,2,3,4,5]` | `[120,60,40,30,24]` |
| `[5,1,10,1]` | `[10,50,5,50]` |
| `[1,2,0,4]` | `[0,0,8,0]` |
| `[0,0,2]` | `[0,0,0]` |
| `[-1,2,-3]` | `[-6,3,-2]` |
| `[2,3]` | `[3,2]` |
