# 24. Count Vowels and Consonants

**Topic:** Strings · **Pattern:** Single pass with switch · **Status:** ✅ Works (letters-only input) · **File:** `tasks/CountVowels.java` (Jul 21)

## Problem
Return `[vowels, consonants]` for a string `S`.

```
"aeioubcd" → [5, 3]
```

## Key idea
Lowercase each char and use switch fall-through on `a e i o u`. Consonants = length − vowels.

## Solution
```java
// TC: O(n); SC: O(1)
public static int[] solve(String S) {
    int[] count = new int[2];

    for (int i = 0; i < S.length(); i++) {
        switch (Character.toLowerCase(S.charAt(i))) {
            case 'a': case 'e': case 'i': case 'o': case 'u':
                count[0]++;
                break;
        }
    }
    count[1] = S.length() - count[0];
    return count;
}
```

## Gotchas
- `length - vowels` counts **spaces, digits, and punctuation as consonants**. `"hi there"` gives `[3, 5]`, but there are only 4 real consonants. If the input can have non-letters, count consonants with `Character.isLetter(ch)`.

## Tests
| S | Expected |
|---|---|
| `"aeioubcd"` | `[5,3]` |
| `"Hello"` | `[2,3]` |
| `"xyz"` | `[0,3]` |
| `""` | `[0,0]` |
| `"hi there"` | `[3,4]` with the isLetter fix *(current code: [3,5])* |
