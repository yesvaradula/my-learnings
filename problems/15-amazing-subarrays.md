# 15. Amazing Subarrays (substrings starting with a vowel)

**Topic:** Strings · **Pattern:** Contribution technique · **Status:** ✅ Works · **File:** `tasks/AmazingString.java` (Jul 25)

## Problem
Count the substrings of `A` that **start with a vowel** (`a e i o u`, either case). Return the count **mod 10003**.

```
"ABEC" → 6   (A, AB, ABE, ABEC, E, EC)
```

## Key idea
A vowel at index `r` starts exactly `n - r` substrings, because the substring can end anywhere from `r` to `n-1`.
So add `n - r` for every vowel, taking the mod each step.

## Solution
```java
// TC: O(n); SC: O(1)
int count = 0;
int mod = 10003;
int n = A.length();

for (int r = 0; r < n; r++) {
    switch (Character.toLowerCase(A.charAt(r))) {
        case 'a': case 'e': case 'i': case 'o': case 'u':
            count = (count + (n - r)) % mod;
            break;
    }
}
```

## Gotchas
- `toLowerCase` handles uppercase vowels. Without it, `"ABEC"` gives 0.
- Take the mod **inside** the loop. Otherwise the sum overflows on long strings.
- The saved file has an unused `l` variable, and everything sits in `main`. Move it into a function.

## Tests
| A | Expected |
|---|---|
| `"ABEC"` | 6 |
| `"xyz"` | 0 |
| `"a"` | 1 |
| `"aaa"` | 6 |
