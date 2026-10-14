# 30. Leaders in an Array

**Topic:** Arrays · **Pattern:** Scan from the right with a running max · **Status:** ⚠️ Bug found (Oct 11 review) · **File:** `tasks/MaxRight.java` (Jul 21)

## Problem
An element is a **leader** if it's strictly greater than every element to its right. The last element is always a leader. Return all the leaders.

```
[16, 17, 4, 3, 5, 2] → [17, 5, 2]
```

## Key idea
Walk from the right and keep the max seen so far. Any element bigger than that max is a leader.

## Solution (with the fix)
```java
// TC: O(n); SC: O(n) for the result
static int[] solve(int[] A) {
    int maxRight = Integer.MIN_VALUE;
    ArrayList<Integer> al = new ArrayList<>();

    for (int i = A.length - 1; i >= 0; i--) {     // FIX: i >= 0, not i > 0
        if (A[i] > maxRight) {
            al.add(A[i]);                        // append (O(1)) instead of add(0, ...)
            maxRight = A[i];
        }
    }
    Collections.reverse(al);                     // back to left-to-right order

    int[] B = new int[al.size()];
    for (int i = 0; i < B.length; i++) B[i] = al.get(i);
    return B;
}
```

## Gotchas
- **Bug in the saved file:** the loop is `i > 0`, so **index 0 is never checked**. `[20, 1]` returns `[1]` instead of `[20, 1]`. I confirmed this by running it.
- `al.add(0, x)` shifts the whole list every time, which is O(n²) worst case (e.g. a descending array). Append, then reverse once.
- Check whether the problem wants left-to-right order or any order.

## Tests
| A | Expected |
|---|---|
| `[16,17,4,3,5,2]` | `[17,5,2]` |
| `[20,1]` | `[20,1]` *(old code: [1])* |
| `[5]` | `[5]` *(old code: [])* |
| `[1,2,3]` | `[3]` |
| `[3,2,1]` | `[3,2,1]` |
