# 14. Palindrome After Removing At Most One Character

**Topic:** Strings · **Pattern:** Two pointers · **Status:** ⚠️ Bug found (Oct 11 review) · **File:** `tasks/AFterRPalind.java` (Aug 23)

## Problem
*(Inferred from the code. The original statement isn't saved.)*
Given a string `s`, can it become a palindrome by deleting **at most one** character?

```
"abca"  → true   (delete 'c' → "aba")
"abc"   → false
"abbax" → true   (delete 'x' → "abba")
```

## Key idea
Walk `l` and `r` inward. At the **first mismatch** you have one deletion to spend, so try both options:
skip the left char (`l+1 .. r`) **or** skip the right char (`l .. r-1`). If either one is a palindrome, the answer is true.

## Solution (with the fix)
```java
// TC: O(n); SC: O(1)
public static boolean afterRCheck(String s) {
    int l = 0;
    int r = s.length() - 1;

    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) {
            return isPalindrome(s, l + 1, r) || isPalindrome(s, l, r - 1);   // FIX: try both sides
        }
        l++;
        r--;
    }
    return true;
}

public static boolean isPalindrome(String str, int l, int r) {
    while (l < r) {
        if (str.charAt(l) != str.charAt(r)) {
            return false;
        }
        l++;
        r--;
    }
    return true;
}
```

## Gotchas
- **Bug in the saved file:** it only tries skipping the **left** char. `"abbax"` returns `false`, but deleting `'x'` gives `"abba"`. I confirmed this by running it.
- You get one mismatch and one deletion, so `isPalindrome` never needs to skip again.

## Tests
| s | Expected |
|---|---|
| `"abca"` | true |
| `"aba"` | true |
| `"abc"` | false |
| `"abbax"` | true *(old code: false)* |
| `"xabba"` | true |
| `""` | true |
