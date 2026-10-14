# 29. Best Time to Buy and Sell Stock (one transaction)

**Topic:** Arrays · **Pattern:** Running minimum · **Status:** ⚠️ Profit is correct; the printed buy price is wrong · **File:** `tasks/MaxProfit.java` (Jul 24)

## Problem
`A[i]` is the price on day `i`. Buy once and sell once later. Return the max profit, or 0 if no profit is possible.

```
[7, 1, 5, 3, 6, 4] → 5   (buy 1, sell 6)
```

## Key idea
For each day, the best sale today = today's price − the cheapest price **before** today. Keep a running minimum and the best difference.

## Solution
```java
// TC: O(n); SC: O(1)
int minPrice = A[0];
int profit = 0;

for (int i = 1; i < A.length; i++) {
    profit = Math.max(profit, A[i] - minPrice);
    minPrice = Math.min(minPrice, A[i]);
}
```

## Gotchas
- Update the profit **before** updating the min, so you never sell on the same day you buy.
- **Bug in the saved file's printout:** `Buy` is the running minimum, not the buy day of the best trade. For `[3, 5, 1]` it prints "buying at 1", but the trade was buy 3, sell 5. To report the days, save the buy price whenever you update `profit`.
- `int sell = A[1]` crashes when `n == 1`.

## Tests
| A | Expected |
|---|---|
| `[7,1,5,3,6,4]` | 5 |
| `[7,6,4,3,1]` | 0 |
| `[3,5,1]` | 2 |
| `[5]` | 0 |
