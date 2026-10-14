# 21. Capitalize From the First Occurrence of a Character

**Topic:** Strings · **Pattern:** Find index, then one pass · **Status:** ⚠️ Bug found (Oct 11 review) · **File:** `tasks/Capz.java` (Jul 18)

## Problem
*(Inferred from the code.)* Given a string `A` and a character `c`, convert every lowercase letter **from the first occurrence of `c` to the end** into uppercase.

```
"hello world", 'w' → "hello WORLD"
"good morning", 'g' → "GOOD MORNING"
```

## Key idea
Find the first index of `c`. Then walk from there and uppercase each lowercase char in a `StringBuilder`.

## Solution (with the fix)
```java
// TC: O(n); SC: O(n) (StringBuilder)
static String solve(String A, String c) {
    StringBuilder sb = new StringBuilder(A);
    int fromIndex = A.indexOf(c.charAt(0));

    if (fromIndex == -1) {          // FIX: c not in A → nothing to change
        return A;
    }

    for (int i = fromIndex; i < A.length(); i++) {
        char ch = A.charAt(i);
        if (Character.isLowerCase(ch)) {
            sb.setCharAt(i, Character.toUpperCase(ch));
        }
    }
    return sb.toString();
}
```

## Gotchas
- **Bug in the saved file:** if `c` isn't in the string, `fromIndex` stays `-1`, and `A.charAt(-1)` throws `StringIndexOutOfBoundsException`. I confirmed this with `("hello", "z")`.
- `A.indexOf(ch)` replaces the manual search loop.
- `isLowerCase` leaves spaces, digits, and capitals alone.

## Tests
| A | c | Expected |
|---|---|---|
| `"hello world"` | `w` | `"hello WORLD"` |
| `"good morning"` | `g` | `"GOOD MORNING"` |
| `"hello"` | `z` | `"hello"` *(old code crashes)* |
| `"abc"` | `c` | `"abC"` |
