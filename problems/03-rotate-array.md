# 03. Rotate Array Right by k

**Topic:** Arrays · **Pattern:** Reversal trick · **Status:** ✅ Solved (Oct 4, after one fix)

## Problem
Rotate `nums` to the right by `k` steps, in place, with O(1) extra space. `k` can be larger than the array length.

```
[1,2,3,4,5,6,7], k = 3  →  [5,6,7,1,2,3,4]
[1,2], k = 5            →  [2,1]
```

## Key idea
1. `k = k % n`
2. Reverse the **whole** array
3. Reverse the first `k` elements
4. Reverse the rest

```
reverse all     → [7, 6, 5, 4, 3, 2, 1]
reverse 0..2    → [5, 6, 7, 4, 3, 2, 1]
reverse 3..6    → [5, 6, 7, 1, 2, 3, 4] ✅
```

## Solution
```java
// TC: O(n), SC: O(1)
static void rotate(int[] arr, int k) {
    if (arr.length == 0) return;          // k % 0 would throw

    k = k % arr.length;
    reverse(arr, 0, arr.length - 1);
    reverse(arr, 0, k - 1);               // no-op when k == 0
    reverse(arr, k, arr.length - 1);
}

static void reverse(int[] arr, int start, int end) {
    while (start < end) {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
        start++;
        end--;
    }
}
```

## The bug from the first attempt
```java
reverse(arr, 0, k);               // reverses k + 1 elements (off by one)
reverse(arr, k + 1, n - 1);
reverse(arr, 0, n - 1);           // parts first, then whole = LEFT rotation
```
That is a left rotation by `k + 1`. It passed `[1..7], k = 3` **by coincidence** (left by 4 = right by 3 when n = 7) and failed `[1,2,3,4,5], k = 1`.

**Lesson:** one passing test proves nothing.

## Gotchas
- `k % n` handles `k > n` and `k == n`.
- Guard only `n == 0`; the `k == 0` case is handled naturally.

## Tests
| Input | k | Expected |
|---|---|---|
| `[1,2,3,4,5,6,7]` | 3 | `[5,6,7,1,2,3,4]` |
| `[1,2,3,4,5]` | 1 | `[5,1,2,3,4]` |
| `[1,2]` | 5 | `[2,1]` |
| `[1,2,3]` | 3 | `[1,2,3]` |
| `[-1,-100,3,99]` | 2 | `[3,99,-1,-100]` |
