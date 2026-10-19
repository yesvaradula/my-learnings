# 48. First Repeating Element

**Topic:** Hashing · **Pattern:** Frequency map + second pass over the array · **Status:** Solved Oct 20, 2026, 14/14 PASS + 2000 random cross-checks · File: `tasks/hashes/FirstRepeating.java`

## Problem
Given an int array `A`, return the first element that occurs more than once, where "first" means its **first occurrence** has the smallest index. If nothing repeats, return `-1`.

```
[10, 5, 3, 4, 3, 5, 6]  ->  5
[6, 10, 5, 4, 9, 120]   -> -1
```

**The trap:** scanning left to right, `3` is the first value *seen twice* (index 4), but `5` *appeared first* (index 1 vs index 2), so the answer is 5.

## Key idea
- **Brute force:** for each `i`, look for an equal `A[j]` with `j > i`; the first `i` that finds one is the answer. O(N^2).
- **Hashing, two passes:**
  1. Count every value: `hm.put(x, hm.getOrDefault(x, 0) + 1)`.
  2. Walk the **array** left to right and return the first `A[i]` whose count is `> 1`.
- Pass 2 loops over `A`, not the map: the map has no order (it iterates in bucket order, which gives 3 here), and "first" is defined by position in the array. The map is only there for O(1) count lookups. Every `A[i]` is guaranteed to be a key because pass 1 inserted all of them, so the map being smaller than `A` does not matter.
- One-pass alternative: scan right to left with a `HashSet`; whenever `A[i]` is already in the set, overwrite the answer with `A[i]`. See `tasks/hashes/hashing.md`, pattern B.

## Final solution
```java
// TC: O(N), SC: O(N)
static int solve(int[] A) {
    HashMap<Integer, Integer> hm = new HashMap<>();

    for (int i = 0; i < A.length; i++) {
        hm.put(A[i], hm.getOrDefault(A[i], 0) + 1);
    }

    for (int i = 0; i < A.length; i++) {
        if (hm.get(A[i]) > 1)
            return A[i];
    }

    return -1;
}
```

## Bugs hit along the way (Oct 20)
1. **Key and value swapped:** `hm.put(i, A[i])` (index as key), then `hm.get(A[i])` looked a value up as if it were an index. Printed `4`. `hm.get` on a missing key returns `null`, so `[10, 10]` would have thrown a NullPointerException.
2. **`containsValue`** is O(N), making the loop O(N^2) while the comment said O(N).
3. Switched to `containsKey` but left `put(i, A[i])`: still `4`, by coincidence (key 3 was index 3).
4. **Two pointers meeting in the middle:** only compared right-side elements against left-side ones. Passed both samples, failed `[1, 1]`, `[1, 2, 2, 5]`, `[7, 1, 2, 7, 9, 3, 4]`.
5. **`int[] count` indexed by value** (`count[A[i]]` with `A.length = 7`, `A[i] = 10`): out of bounds. Plus `count[A[i]]++` inside `put` stored the old value (post-increment).
6. **`getOrDefault(A[i] + 1, 1)`:** `+ 1` inside the brackets looked up the next number; every count stayed 1.
7. **Pass 2 rewritten to loop `i < hm.size()` with `hm.get(i)`:** the loop counter used as a key, `null > 1` threw a NullPointerException. The previous pass 2 had been correct.

Explained back correctly: looping the map would return whatever count-2 key comes first in the map, not the first in `A`.

## Tests
14 cases in `main`: both samples, empty, single, `[1, 1]`, all equal, adjacent pair in the middle, pair at the end, pair at both ends, first element repeats, `[1, 2, 3, 2, 1]` (2 closes first, 1 appears first), negatives, `10^9` values, values outside the Integer cache. Plus 2000 random arrays checked against the O(N^2) brute force.

## Interview lines
- *"Brute force compares every pair, O(N^2). The inner loop only searches, so I count with a HashMap first: O(N) time, O(N) space."*
- *"Pass 2 walks the array, not the map, because the map has no order and 'first' means first by position."*
- *"I use `getOrDefault` so a new value starts at 0 instead of returning null."*
- Edge cases: empty, single element, no repeats, all equal, negatives, large values. If `-1` itself can repeat, the `-1` sentinel is ambiguous (Scaler's constraints keep values positive).
