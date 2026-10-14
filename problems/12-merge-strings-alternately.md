# 12. Merge Strings Alternately

**Topic:** Strings · **Pattern:** Two pointers · **Status:** ✅ Solved (Oct 8, warm-up)

## Problem
Given two strings `word1` and `word2`, merge them by taking letters alternately, starting with `word1`. If one string is longer, append its remaining letters to the end.

```
"abc",  "pqr"  → "apbqcr"
"ab",   "pqrs" → "apbqrs"   (extra "rs" from word2)
"abcd", "pq"   → "apbqcd"   (extra "cd" from word1)
```

## Key idea
Walk both strings together while both still have letters, taking one from each. Then copy whatever is left in the longer one (at most one of the two leftover loops runs).

## Solution
```java
// TC: O(n + m); SC: O(n + m) for the output (O(1) extra)
static String mergeWords(String w1, String w2) {
    StringBuilder sb = new StringBuilder();
    int n = w1.length();
    int m = w2.length();
    int i = 0;
    int j = 0;

    while (i < n && j < m) {
        sb.append(w1.charAt(i++)).append(w2.charAt(j++));
    }

    while (i < n) {
        sb.append(w1.charAt(i++));
    }

    while (j < m) {
        sb.append(w2.charAt(j++));
    }

    return sb.toString();
}
```

## Gotchas
- Use `StringBuilder`, not `String +=` in a loop (that copies the string every time, O((n + m)²)).
- TC has **two** lengths: O(n + m), not O(n).
- SC: the output itself is O(n + m); say "O(1) extra" only if you mean excluding the output.
- First review: unused variable `z` left in, only one test case in `main`.

## Tests
| word1 | word2 | Expected |
|---|---|---|
| `"abc"` | `"pqr"` | `"apbqcr"` |
| `"ab"` | `"pqrs"` | `"apbqrs"` |
| `"abcd"` | `"pq"` | `"apbqcd"` |
| `""` | `"xyz"` | `"xyz"` |
| `"abc"` | `""` | `"abc"` |
| `""` | `""` | `""` |
| `"a"` | `"b"` | `"ab"` |
