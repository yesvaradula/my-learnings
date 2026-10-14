# 35. Palindrome Check

**Topic:** Strings · **Pattern:** Two pointers · **Status:** ✅ Works · **File:** `tasks/Polindrome.java` (Jul 21)

## Problem
Is the string `S` a palindrome?

```
"abccba" → true
"ab"     → false
```

## Key idea
Compare `S[i]` and `S[j]` from both ends, moving inward. One mismatch means it's not a palindrome.

## Solution
```java
// TC: O(n); SC: O(1)
static boolean solve(String S) {
    int i = 0;
    int j = S.length() - 1;

    while (i < j) {
        if (S.charAt(i) != S.charAt(j)) {
            return false;
        }
        i++;
        j--;
    }
    return true;
}
```

## Gotchas
- The saved file checks `i <= j` again inside the loop. The `while` condition already guarantees it, so it's redundant.
- The file name is `Polindrome.java`, but the class is `Polindrom`. Java only allows that because the class isn't `public`.
- The follow-up with one allowed deletion is #14. The recursive version is in parked practice.

## Tests
| S | Expected |
|---|---|
| `"abccba"` | true |
| `"abcba"` | true |
| `"ab"` | false |
| `"a"` | true |
| `""` | true |
