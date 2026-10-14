# 34. First Non-Repeating Character

**Topic:** Hashing / Strings · **Pattern:** Frequency array, two passes · **Status:** ✅ Works (lowercase input) · **File:** `tasks/NonRepeating.java` (Jul 21)

## Problem
Return the first character in `S` that appears exactly once. If there isn't one, return `' '`.

```
"aabcb" → 'c'
"aa"    → ' '
```

## Key idea
Pass 1 counts every char. Pass 2 walks the string **in its original order** and returns the first char with count 1.

## Solution
```java
// TC: O(n); SC: O(1) (26 counters)
static char solve(String S) {
    int[] freq = new int[26];
    for (int i = 0; i < S.length(); i++) {
        freq[S.charAt(i) - 'a']++;
    }
    for (int i = 0; i < S.length(); i++) {
        if (freq[S.charAt(i) - 'a'] == 1) {
            return S.charAt(i);
        }
    }
    return ' ';
}
```

## Gotchas
- Pass 2 has to scan the **string**, not the 26 counters. Scanning the counters gives alphabetical order instead of "first in the string".
- `- 'a'` assumes lowercase letters only. Any other char is out of bounds. For general input, use `int[256]` or a `HashMap<Character, Integer>`.

## Tests
| S | Expected |
|---|---|
| `"aabcb"` | `'c'` |
| `"aa"` | `' '` |
| `"ab"` | `'a'` |
| `""` | `' '` |
