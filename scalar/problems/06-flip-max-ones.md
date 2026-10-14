# 06. Flip (Maximize 1s in a Binary String)

**Topic:** Arrays / Strings · **Pattern:** Kadane's in disguise · **Status:** ✅ Solved (Oct 5, after guided build + two fixes)

## Problem
Given a binary string `A`, you may flip **at most one** contiguous range `[L, R]` (0 ↔ 1) to maximize the number of 1s.
- Return `[L, R]` (**1-indexed**), or an empty array if no flip helps.
- If several answers tie, return the **lexicographically smallest** pair.

```
"010"         →  [1, 1]
"111"         →  []
"1101010001"  →  [3, 9]
```

## Key idea
Flipping a `0` gains +1, flipping a `1` loses 1. Map `'0' → +1`, `'1' → -1` and find the **maximum-sum subarray**. That's Kadane's.
- No positive gain → return `[]`.
- Ties: reset only when the gain goes `< 0` (keeps the earliest `L`), and update the best only on `>` (keeps the earliest answer).

## Solution
```java
// TC: O(n), SC: O(1)
static int[] flip(String A) {
    int n = A.length();
    int curGain = 0, maxGain = 0;
    int start = 0;                   // left edge of the CURRENT section
    int bestL = -1, bestR = -1;      // edges of the BEST section

    for (int i = 0; i < n; i++) {
        curGain += A.charAt(i) == '0' ? 1 : -1;

        if (curGain < 0) {           // negative baggage → restart
            curGain = 0;
            start = i + 1;
            continue;
        }

        if (curGain > maxGain) {     // strictly better → save BOTH edges
            maxGain = curGain;
            bestL = start;
            bestR = i;
        }
    }

    if (bestL == -1) return new int[] {};
    return new int[] { bestL + 1, bestR + 1 };   // 1-indexed
}
```

## Trace: `"1101010001"`
| i | char | curGain | Action | best (0-based) |
|---|---|---|---|---|
| 0 | 1 | -1 | reset, start = 1 | |
| 1 | 1 | -1 | reset, start = 2 | |
| 2 | 0 | 1 | new best | [2, 2] |
| 3 | 1 | 0 | keep (not < 0) | |
| 4–6 | | 1, 0, 1 | ties ignored | |
| 7 | 0 | 2 | new best | [2, 7] |
| 8 | 0 | 3 | new best | [2, 8] |
| 9 | 1 | 2 | | |

1-indexed: **[3, 9]**

## Bugs along the way
- Loop `i < n - 1` skipped the last character (`"0"` → `[]`).
- Merging `start` and `bestL` into one variable: a later reset overwrote the saved best (`"0110"` → `[4, 1]`).
- **Rule:** when saving the best, save both edges at the same moment.

## Gotchas
- Output is **1-indexed** (`1 ≤ L ≤ R ≤ N` in the statement).
- `start` moves only on reset; `i` moves every step. They are equal only when the section is one character long.

## Tests
| Input | Expected |
|---|---|
| `"010"` | `[1, 1]` |
| `"111"` | `[]` |
| `"1101010001"` | `[3, 9]` |
| `"0"` | `[1, 1]` |
| `"000"` | `[1, 3]` |
| `"1001"` | `[2, 3]` |
