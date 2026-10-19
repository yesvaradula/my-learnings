# Hashing in Java: HashMap, HashSet and friends

A quick-review guide for contests and interviews. Read top to bottom the first time; after that, jump to the section you need. Every Java behaviour quoted here was checked on JDK 21.

---

## 1. Why hashing?

Hashing answers one question fast: **"have I seen this before, and what do I know about it?"**

Without hashing, checking whether a value appeared earlier means scanning: O(n) per check, O(n^2) for the whole array. A hash table makes each check **O(1) on average**, so the whole array becomes O(n).

Picture a class queue and an attendance register:

- The **array** is the queue: 7 students standing in order, some with the same name.
- The **HashMap** is the register: **one row per unique name**, with a count (or index, or anything) next to it.
- You walk the queue in order, and for each student you look up **their name** in the register.

The register is usually **smaller** than the queue (duplicates share one row). That is fine, because you never look up the register by position, only by name.

**Contest signal:** the problem says "count", "frequency", "duplicate", "distinct", "first repeating / non-repeating", "pair with sum K", "subarray with sum K / 0", "anagram", or the brute force is two nested loops where the inner loop only *searches* for something. Think hashing.

---

## 2. How a HashMap works inside

```
key --hashCode()--> int --spread--> bucket index --> bucket (list or tree of entries)
```

1. **`hashCode()`** turns the key into an `int`. `Integer` uses the value itself (`7` -> `7`); `String` uses `s[0]*31^(n-1) + ... + s[n-1]` (`"ab"` -> `97*31 + 98 = 3105`).
2. **Spread:** HashMap mixes the high bits into the low bits (`h ^ (h >>> 16)`) so keys that differ only in high bits still land in different buckets.
3. **Bucket index:** `index = (capacity - 1) & hash`. Capacity is always a power of 2, so this `&` is a fast `% capacity` (see `bit.md`).
4. **Collision:** two different keys landing in the same bucket. They are stored together in that bucket, and **`equals()`** decides which one you asked for.
5. **Load factor and resize:** default capacity is **16**, default load factor **0.75**. When `size > capacity * 0.75` (13th entry for capacity 16), the table **doubles** and every entry is re-bucketed. A single resize is O(n), but it happens rarely, so `put` is **O(1) amortized**.
6. **Treeify (Java 8+):** if one bucket grows past **8** entries (and the table has at least 64 buckets), that bucket becomes a red-black tree, so a bad bucket costs O(log n) instead of O(n).

| | Average | Worst case |
|---|---|---|
| `put`, `get`, `containsKey`, `remove` | **O(1)** | O(n) (O(log n) per bucket after treeify) |
| `containsValue` | **O(n)** (scans everything) | O(n) |
| Iterate all entries | O(n + capacity) | |

In interviews, say: *"O(1) on average, assuming a good hash function; worst case degrades with collisions."*

---

## 3. The `hashCode` / `equals` contract

- If `a.equals(b)`, then `a.hashCode() == b.hashCode()` **must** hold. (Same hash does not imply equal: that is a collision.)
- `Integer`, `Long`, `String`, `Character`, `List`, `Set` already follow it. **Arrays do not**: `int[]` uses identity, so two arrays with the same contents are different keys.

```java
Map<int[], Integer> m = new HashMap<>();
m.put(new int[]{1, 2}, 1);
m.containsKey(new int[]{1, 2});          // false: different array object

Map<List<Integer>, Integer> m2 = new HashMap<>();
m2.put(List.of(1, 2), 1);
m2.containsKey(Arrays.asList(1, 2));     // true: List compares contents
```

For a pair key, use `List.of(a, b)`, a `String` like `a + "," + b`, or pack two ints into one long: `((long) a << 32) | (b & 0xFFFFFFFFL)`.

- **Never mutate a key** after putting it in a map. Its hash changes, it is now in the wrong bucket, and you will never find it again.
- Your own class as a key needs both `equals` and `hashCode` overridden (a `record` gives you both for free).

---

## 4. HashMap API cheat sheet

```java
import java.util.*;

HashMap<Integer, Integer> hm = new HashMap<>();
```

| Call | Does | Returns |
|---|---|---|
| `hm.put(k, v)` | Insert or overwrite | the **old** value, or `null` if `k` was new |
| `hm.get(k)` | Look up | the value, or **`null`** if missing |
| `hm.getOrDefault(k, d)` | Look up with fallback | value, or `d` if missing |
| `hm.containsKey(k)` | Key present? O(1) | `boolean` |
| `hm.containsValue(v)` | Value present? **O(n)** | `boolean` |
| `hm.remove(k)` | Delete | the removed value, or `null` |
| `hm.putIfAbsent(k, v)` | Insert only if missing | the existing value, or `null` if it inserted |
| `hm.merge(k, 1, Integer::sum)` | Missing: put 1. Present: old + 1 | the new value |
| `hm.computeIfAbsent(k, x -> new ArrayList<>())` | Create the value once, then reuse it | the value (existing or new) |
| `hm.size()`, `hm.isEmpty()`, `hm.clear()` | | |
| `hm.keySet()`, `hm.values()`, `hm.entrySet()` | Views for iterating | live views (changes write through) |

### Iterating

```java
for (Map.Entry<Integer, Integer> e : hm.entrySet()) {
    int key = e.getKey();
    int value = e.getValue();
}

for (int key : hm.keySet()) { ... }
for (int value : hm.values()) { ... }
```

Prefer `entrySet()` when you need both: `keySet()` + `get(key)` does an extra lookup per key.

**Iteration order of a HashMap is not defined.** It often looks sorted for small `Integer` keys (because `Integer.hashCode` is the value itself), but never rely on it. If order matters, iterate the **array**, or use `LinkedHashMap` / `TreeMap`.

---

## 5. HashSet: a HashMap with only keys

A `HashSet` is literally backed by a `HashMap` whose values are a dummy object. Use it when you only need "seen or not", with no count or index.

```java
HashSet<Integer> set = new HashSet<>();
```

| Call | Does | Returns |
|---|---|---|
| `set.add(x)` | Insert | **`true` if new, `false` if already there** |
| `set.contains(x)` | Present? O(1) | `boolean` |
| `set.remove(x)` | Delete | `true` if it was there |
| `set.size()` | Number of **distinct** elements | `int` |

`add` returning `false` is a free duplicate check:

```java
if (!set.add(x)) {
    // x was already in the set: duplicate found
}
```

Distinct count of an array in one line of thought: add everything to a set, answer is `set.size()`.

---

## 6. The idioms you will type every day

### Counting (frequency map)

```java
for (int x : A) {
    hm.put(x, hm.getOrDefault(x, 0) + 1);   // "get my count or 0, then add 1"
}
// same thing:
for (int x : A) {
    hm.merge(x, 1, Integer::sum);
}
```

The `+ 1` goes **outside** `getOrDefault(...)`. Writing `getOrDefault(x + 1, 1)` looks up the *next number*, not `x`.

### Remember the first index of each value

```java
for (int i = 0; i < A.length; i++) {
    hm.putIfAbsent(A[i], i);                // later copies do not overwrite
}
```

### Grouping (value -> list)

```java
Map<String, List<String>> groups = new HashMap<>();
for (String w : words) {
    char[] c = w.toCharArray();
    Arrays.sort(c);
    groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);   // anagram groups
}
```

### Decrement and drop at zero (sliding windows)

```java
if (hm.merge(out, -1, Integer::sum) == 0) {
    hm.remove(out);                         // keep size() == number of distinct values
}
```

### Characters

For lowercase letters only, an `int[26]` is simpler and faster than a map: `count[ch - 'a']++`. For any characters, use `HashMap<Character, Integer>`.

---

## 7. Map vs array vs other maps

| Use | When |
|---|---|
| `int[] count = new int[R]` | Values are small and non-negative (`0..R-1`, R up to ~10^6), e.g. letters, digits, ages |
| `HashMap` / `HashSet` | Values are large (up to 10^9), negative, or not numbers. O(1) average, **no order** |
| `LinkedHashMap` / `LinkedHashSet` | Same as above, but iteration follows **insertion order** |
| `TreeMap` / `TreeSet` | Keys **sorted**; O(log n) per operation; gives `firstKey()`, `lastKey()`, `floorKey(x)` (largest <= x), `ceilingKey(x)` (smallest >= x) |

An array indexed by value crashes when the value is bigger than the array (`count[A[i]]` with `A[i] = 10` and `A.length = 7` is `ArrayIndexOutOfBoundsException`), and it cannot take negative values at all. That is exactly the job a HashMap does: **the map is the counter**.

---

## 8. Problem patterns

### A. Frequency, then a second pass over the array

**First repeating element** (repeats, and its *first occurrence* is earliest):

```java
static int firstRepeating(int[] A) {
    HashMap<Integer, Integer> hm = new HashMap<>();
    for (int x : A) {
        hm.put(x, hm.getOrDefault(x, 0) + 1);
    }
    for (int x : A) {                       // walk the ARRAY: it keeps first-appearance order
        if (hm.get(x) > 1) return x;
    }
    return -1;
}
```

- `[10, 5, 3, 4, 3, 5, 6]` -> **5**, not 3. The 3 is the first value *seen twice* while scanning, but 5 *appeared first*.
- Pass 2 loops over `A`, not over the map: the map has no order, and it is smaller than `A`. Every `A[i]` is guaranteed to be a key because pass 1 inserted all of them.
- TC O(n), SC O(n).

**First non-repeating element:** same two passes, return the first `x` with `hm.get(x) == 1`.

### B. One pass from the right (try it yourself before reading)

First repeating in one pass: scan from `n-1` down to `0` with a set. When `A[i]` is already in the set, a later copy exists, so `A[i]` is a candidate; keep overwriting. The last candidate written is the leftmost one.

```java
static int firstRepeatingOnePass(int[] A) {
    HashSet<Integer> seen = new HashSet<>();
    int ans = -1;
    for (int i = A.length - 1; i >= 0; i--) {
        if (seen.contains(A[i])) ans = A[i];
        seen.add(A[i]);
    }
    return ans;
}
```

### C. Complement lookup: pair with sum K

"Is there `i < j` with `A[i] + A[j] == K`?" For each `x`, the partner is `K - x`. Check the set **before** adding `x`, so an element never pairs with itself.

```java
static boolean hasPair(int[] A, int K) {
    HashSet<Integer> seen = new HashSet<>();
    for (int x : A) {
        if (seen.contains(K - x)) return true;
        seen.add(x);
    }
    return false;
}
```

**Count** such pairs: use a map of counts instead of a set.

```java
static long countPairs(int[] A, int K) {
    HashMap<Integer, Integer> seen = new HashMap<>();
    long pairs = 0;
    for (int x : A) {
        pairs += seen.getOrDefault(K - x, 0);
        seen.put(x, seen.getOrDefault(x, 0) + 1);
    }
    return pairs;
}
```

Same trick for difference K: look up both `x - K` and `x + K` (careful with K = 0).

### D. Prefix sum + hashing: subarrays

`sum(A[l..r]) = prefix[r] - prefix[l-1]`. A subarray with sum K ends at `r` whenever `prefix[r] - K` was seen as an earlier prefix.

**Any subarray with sum 0?** Two equal prefix sums mean the part between them sums to 0. Seed the set with `0` so a subarray starting at index 0 counts.

```java
static boolean hasZeroSumSubarray(int[] A) {
    HashSet<Long> seen = new HashSet<>();
    seen.add(0L);
    long prefix = 0;
    for (int x : A) {
        prefix += x;
        if (!seen.add(prefix)) return true;
    }
    return false;
}
```

**Count subarrays with sum K** (works with negatives, where a sliding window does not):

```java
static long countSubarrays(int[] A, long K) {
    HashMap<Long, Integer> freq = new HashMap<>();
    freq.put(0L, 1);                         // the empty prefix
    long prefix = 0, count = 0;
    for (int x : A) {
        prefix += x;
        count += freq.getOrDefault(prefix - K, 0);
        freq.merge(prefix, 1, Integer::sum);
    }
    return count;
}
```

Longest subarray with sum 0: store the **first index** of each prefix (`putIfAbsent`), and length = `i - firstIndex[prefix]`. Seed with `0 -> -1`.

### E. Set as a lookup table: longest consecutive sequence

`[100, 4, 200, 1, 3, 2]` -> 4 (1, 2, 3, 4), in O(n) without sorting. Only start counting at `x` when `x - 1` is missing, so each run is walked once.

```java
static int longestConsecutive(int[] A) {
    HashSet<Integer> set = new HashSet<>();
    for (int x : A) set.add(x);
    int best = 0;
    for (int x : set) {
        if (set.contains(x - 1)) continue;   // not the start of a run
        int len = 1;
        while (set.contains(x + len)) len++;
        best = Math.max(best, len);
    }
    return best;
}
```

### F. Sliding window + map: distinct elements in every window of size B

```java
static int[] distinctInWindows(int[] A, int B) {
    int n = A.length;
    if (B > n) return new int[0];
    int[] ans = new int[n - B + 1];
    HashMap<Integer, Integer> freq = new HashMap<>();
    for (int i = 0; i < n; i++) {
        freq.merge(A[i], 1, Integer::sum);
        if (i >= B) {                        // A[i - B] leaves the window
            int out = A[i - B];
            if (freq.merge(out, -1, Integer::sum) == 0) freq.remove(out);
        }
        if (i >= B - 1) ans[i - B + 1] = freq.size();
    }
    return ans;
}
```

`[1, 2, 1, 3, 4, 3]`, B = 3 -> `[2, 3, 3, 2]`.

---

## 9. Trap list (check before submitting)

1. **Key and value swapped.** `put(i, A[i])` makes the *index* the key; then `get(A[i])` looks up a value as if it were an index. Ask: *"what do I look things up by?"* That is the key.
2. **`containsValue` is O(n).** Inside a loop it turns O(n) into O(n^2). Only key operations are O(1).
3. **`get` returns `null` for a missing key**, and assigning it to `int` (or comparing `null > 1`) throws `NullPointerException` from unboxing. Use `getOrDefault`, or only `get` keys you know exist.
4. **Looping over a map by position.** `for (i < hm.size()) hm.get(i)` treats the loop counter as a key. A map has no positions.
5. **Order.** HashMap iteration order is undefined. "First" in the problem means first in the **array**, so loop over the array.
6. **`Integer` with `==`.** `Integer` objects compare by reference. Values -128..127 are cached, so `==` *happens* to work for small numbers and silently breaks for `1000`. Use `.equals()`, or unbox to `int` first: `hm.get(a).equals(hm.get(b))`.
7. **Array as a key.** `int[]` keys compare by identity; use `List.of(...)`, a `String`, or a packed `long`.
8. **Removing while iterating** with a for-each throws `ConcurrentModificationException`. Use `hm.entrySet().removeIf(e -> ...)`, `hm.values().removeIf(v -> v == 0)`, or an explicit `Iterator` and `it.remove()`.
9. **Overflow in prefix sums or `K - x`.** Use `long` and a `HashMap<Long, ...>`; then look up with a `long` (a `HashMap<Long, Integer>` will not find the `Integer` key `5`, because `Long.valueOf(5)` is not equal to `Integer.valueOf(5)`).
10. **Forgetting the seed** in prefix-sum problems: `0 -> 1` (count) or `0 -> -1` (first index), otherwise subarrays starting at index 0 are missed.
11. **Array counter indexed by a big or negative value** crashes. If the values are not small and non-negative, use the map.
12. **Post-increment inside `put`.** `hm.put(x, c++)` stores the **old** `c`.

---

## 10. Practice ladder

| # | Problem | Pattern | Status |
|---|---|---|---|
| 1 | First repeating element | Count + second pass over the array (A) | Solved Oct 20 (two-pass) |
| 2 | First repeating, one pass from the right | Set, scan right to left (B) | |
| 3 | First non-repeating element | Count + second pass (A) | |
| 4 | Count distinct elements | Set size | |
| 5 | Pair with sum K exists | Complement lookup (C) | |
| 6 | Count pairs with sum K | Complement + counts (C) | |
| 7 | Subarray with sum 0 exists | Prefix + set, seed 0 (D) | |
| 8 | Count subarrays with sum K | Prefix + count map, seed `0 -> 1` (D) | |
| 9 | Longest subarray with sum 0 | Prefix + first index, seed `0 -> -1` (D) | |
| 10 | Longest consecutive sequence | Set lookup, start only at run heads (E) | |
| 11 | Distinct elements in every window of size B | Sliding window + map (F) | |
| 12 | Check two strings are anagrams | `int[26]` or char count map | |
| 13 | Group anagrams | `computeIfAbsent` with sorted key | |

---

## 11. Interview lines

- *"The brute force compares every pair, O(n^2). The inner loop only searches, so I replace it with a hash lookup: O(n) time, O(n) extra space."*
- *"HashMap operations are O(1) on average with a good hash function; in the worst case collisions degrade them, which Java 8 limits to O(log n) per bucket by turning long buckets into trees."*
- *"I loop over the array in the second pass, not the map, because the map has no order and the problem asks for the first by position."*
- *"I use `getOrDefault` so a missing key counts as 0 instead of returning null."*
- *"If the values were small and non-negative, an `int[]` counter would do the same job with less overhead."*
- Edge cases to name out loud: empty array, single element, no duplicates, all elements equal, negative values, large values / overflow, K = 0.
