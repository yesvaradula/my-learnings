# 40. Greatest Common Divisor of Strings (LeetCode 1071)

**Topic:** Strings · **Pattern:** Concatenation commutes + GCD of lengths · **Status:** ✅ Solved (Oct 14, 2026), 9/9 PASS · File: `tasks/strings/intermediate/GCDString.java`

## Problem
`t` divides `s` if `s = t + t + ... + t` (one or more times). Given `str1` and `str2`, return the largest string `x` that divides both (or `""`).

```
"ABCABC", "ABC"   → "ABC"
"ABABAB", "ABAB"  → "AB"
"LEET",   "CODE"  → ""
```

## Key idea
1. **Existence check:** a common block exists **iff** `s1 + s2 == s2 + s1`. If both are powers of `x`, both concatenations are `x` repeated `a + b` times.
2. **Length:** a divisor's length must divide both lengths, so the largest is `gcd(len1, len2)`. The answer is `s1.substring(0, gcd)`.
3. Brute force to say first in an interview: try every prefix length from `min(n, m)` down to 1 and check it divides both. The two facts above collapse that to one check plus one GCD.

## Final solution
```java
static String gcdOfStrings(String s1, String s2) {
    // TC: O(n + m), SC: O(n + m) (the two concatenations)
    if (!(s1 + s2).equals(s2 + s1)) return "";
    return s1.substring(0, gcd(s1.length(), s2.length()));
}

static int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);   // Euclid, depth O(log min(a, b))
}
```

LeetCode wrapper: `class Solution { public String gcdOfStrings(String str1, String str2) { ... } }`, no `main`.

## Bugs / gotchas
- SC comment said O(1); the concatenations allocate `n + m` chars, so it's **O(n + m)**. O(1) space needs a prefix-check loop with no concatenation.
- Failed on the LeetCode web app at first: the class name and `static` helper layout don't match LeetCode's fixed `Solution` signature (wrapper issue, not logic).

## Tests
`ABCABC/ABC`→`ABC` · `ABABAB/ABAB`→`AB` · `LEET/CODE`→`` · `ABCDEF/ABC`→`` · `AAAAAA/AAA`→`AAA` · `XYZXYZXYZ/XYZXYZ`→`XYZ` · `A/A`→`A` · `A/B`→`` · `""/""`→``.
Worth adding: `ABABAB/ABA` → `""` (lengths share a divisor, strings don't).
