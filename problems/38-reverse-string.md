# 38. Reverse a String

**Topic:** Strings · **Pattern:** Two pointers swap · **Status:** ✅ Works · **File:** `tasks/StringReverse.java` (Jul 21)

## Problem
Return `A` reversed.

```
"abcedfgh" → "hgfdecba"
```

## Key idea
Strings are immutable in Java, so copy into a `char[]`, swap from both ends moving inward, and build a new `String`.

## Solution
```java
// TC: O(n); SC: O(n) (char array)
static String solve(String A) {
    char[] B = A.toCharArray();
    int i = 0;
    int j = B.length - 1;

    while (i < j) {
        char temp = B[j];
        B[j] = B[i];
        B[i] = temp;
        i++;
        j--;
    }
    return new String(B);
}
```

## Gotchas
- The saved file loops on `i <= j`, which swaps the middle char with itself. That's harmless, but `i < j` is cleaner.
- One-liner alternative: `new StringBuilder(A).reverse().toString()`. Know the manual version for interviews.

## Tests
| A | Expected |
|---|---|
| `"abc"` | `"cba"` |
| `"ab"` | `"ba"` |
| `"a"` | `"a"` |
| `""` | `""` |
