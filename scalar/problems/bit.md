# Bit manipulation: introduction and deep reference

A quick-review guide for contests and interviews, in Java. Read top to bottom the first time; after that, jump to the section you need. The short version lives in `revision-cheatsheet.md`.

---

## 1. Why bits?

Every integer is stored as a row of bits (0s and 1s). Bit manipulation means working on those bits directly instead of on the number as a whole.

- **Speed:** a bit operation is a single CPU instruction. Checking "is this number odd?" with `x & 1` is as cheap as it gets.
- **Compact sets:** an `int` can hold 32 yes/no flags. Bit `i` = 1 means "item `i` is in the set". This is how subsets, permissions and feature flags are often stored.
- **Hidden structure:** many problems that look like counting or searching (single number, XOR of pairs, power of 2) become one-line answers once you look at the bits.
- **Contest signal:** if a problem mentions XOR, AND, OR, "powers of 2", "subsets of n <= 20 items", or asks you to avoid extra space while finding a unique element, think bits.

---

## 2. Binary in one minute

Binary is base 2. Each position is a power of 2, counted from the right starting at 0.

```
bit position:   7   6   5   4   3   2   1   0
place value:  128  64  32  16   8   4   2   1

13 = 8 + 4 + 1  ->  0000 1101
```

- Bit 0 is the **least significant bit (LSB)**, the rightmost one. It decides odd or even.
- The leftmost bit is the **most significant bit (MSB)**.
### Bit positions and their values

Positions are counted **from the right, starting at 0**, and **position `k` is worth `2^k`**:

```
position (bit k):   3     2     1     0
value (2^k):        8     4     2     1
```

It is the same idea as decimal places, where position `k` is worth `10^k`:

```
decimal 345:   position 2 = hundreds (100), position 1 = tens (10), position 0 = ones (1)
               300 + 40 + 5
```

In binary each place is worth 2 times the one to its right instead of 10 times. So "bit 2" means "the third box from the right, worth 4".

Worked example, `x = 10`:

```
position:   3   2   1   0
value:      8   4   2   1
bits:       1   0   1   0     ->  10 = 8 + 2
```

- Bit 3 sits under 8 and is 1: 10 contains an 8, so bit 3 is **set**.
- Bit 2 sits under 4 and is 0: 10 has no 4, so bit 2 is **not set**.
- Bit 1 sits under 2 and is 1: **set**.
- Bit 0 sits under 1 and is 0: **not set** (10 is even).

**Key question:** "Is bit `k` set?" means "does `2^k` appear when `x` is built out of powers of 2?" Use this to sanity-check any bit answer in your head.

### The number is already stored in binary

`int x = 13;` does not keep the digits "1" and "3". The compiler stores the bits `00000000 00000000 00000000 00001101` once, and `println` turns them back into "13" for humans to read. Decimal, hex and binary are different clothes for the same value; bit operations work on the value as it really sits in memory. Nothing is converted when you shift or mask.

- A number `x` has about `log2(x) + 1` bits, so a loop over the bits of `x` is O(log x), which is at most 32 iterations for an `int` and 64 for a `long`. Treat it as O(1) in complexity answers, but say why.

Decimal to binary by hand: divide by 2 repeatedly and read the remainders bottom to top.

```
13 / 2 = 6 r 1
 6 / 2 = 3 r 0
 3 / 2 = 1 r 1
 1 / 2 = 0 r 1   ->  read upwards: 1101
```

Java helpers:

```java
Integer.toBinaryString(13)        // "1101" (negative numbers print all 32 bits)
Integer.parseInt("1101", 2)       // 13
int mask = 0b1010_1010;           // binary literal, underscores allowed
int hex  = 0xFF;                  // 255, hex is a short way to write bit patterns (4 bits per digit)
```

---

## 3. Negative numbers: two's complement

Java `int` is 32 bits, `long` is 64 bits, both **signed**, both in **two's complement**. The examples below use **4 bits** to stay readable; 32 bits follow the same rules with more digits.

### Binary addition (needed to see why two's complement works)

Same as school addition, but you carry at 2 instead of 10.

| Add | Result | Write | Carry |
|---|---|---|---|
| 0 + 0 | 0 | 0 | 0 |
| 0 + 1 | 1 | 1 | 0 |
| 1 + 1 | 2 = `10` | 0 | 1 |
| 1 + 1 + 1 (with carry) | 3 = `11` | 1 | 1 |

Work right to left, column by column. Example `0101 + 1101`:

```
carry:   1 1 0 1
           0 1 0 1
         + 1 1 0 1
         ---------
         1 0 0 1 0     5 bits; only 4 fit, the leftmost 1 falls off -> 0010
```

| Column | Add | Write | Carry |
|---|---|---|---|
| bit 0 | 1 + 1 | 0 | 1 |
| bit 1 | 0 + 0 + 1 | 1 | 0 |
| bit 2 | 1 + 1 | 0 | 1 |
| bit 3 | 0 + 1 + 1 | 0 | 1 |
| beyond | leftover carry | 1 (dropped) | |

Sanity check as unsigned numbers: 5 + 13 = 18 = `10010`; dropping the 16 leaves 2 = `0010`.

### The problem: memory has no minus sign

Only 0s and 1s are stored, so a negative number has to be encoded in bits.

**The naive idea (sign-magnitude):** use the first bit as the sign, so +5 = `0101` and -5 = `1101`. It breaks:
- There are two zeros: `0000` (+0) and `1000` (-0).
- Addition fails: 5 + (-5) should be 0, but `0101 + 1101 = 0010` (2), as computed above.

### Two's complement: the odometer

Picture a 4-bit counter as a car odometer. Counting down past 0 wraps around:

```
 2 = 0010
 1 = 0001
 0 = 0000
-1 = 1111    one step below 0 wraps to all ones
-2 = 1110
-3 = 1101
```

-1 is simply "the number that gives 0 when you add 1": `1111 + 0001 = 10000`, the extra carry falls off, leaving `0000`.

Now addition works with no special cases: 5 + (-5) with -5 = `1011`:

```
  0101    (5)
+ 1011    (-5)
------
 10000  -> drop the overflow bit -> 0000 = 0
```

This is why every computer uses it: one adder circuit handles positive and negative numbers alike.

### The full 4-bit table

| Bits | Value | | Bits | Value |
|---|---|---|---|---|
| `0000` | 0 | | `1000` | -8 |
| `0001` | 1 | | `1001` | -7 |
| `0010` | 2 | | `1010` | -6 |
| `0011` | 3 | | `1011` | -5 |
| `0100` | 4 | | `1100` | -4 |
| `0101` | 5 | | `1101` | -3 |
| `0110` | 6 | | `1110` | -2 |
| `0111` | 7 | | `1111` | -1 |

1. **The leftmost bit (MSB) is the sign:** 0 means non-negative, 1 means negative.
2. **Only one zero.**
3. **The range is lopsided:** -8 to +7. Zero takes one of the non-negative patterns, so there is one extra negative.

### Reading a negative number: the top bit is worth a negative amount

In two's complement the place value of the top bit is **negative**. For 4 bits:

```
position:    3     2    1    0
value:      -8     4    2    1
```

- `1011` = -8 + 0 + 2 + 1 = **-5**
- `1111` = -8 + 4 + 2 + 1 = **-1**
- `1000` = **-8**

For a 32-bit `int`, the top bit is worth `-2^31`, and every other bit keeps its normal `2^k`.

### Negating: flip all bits, then add 1 (`-x == ~x + 1`)

Why it works: `x + ~x` is all ones, because every bit is 1 in exactly one of the two.

```
 x  = 0101   (5)
~x  = 1010
sum = 1111   = -1
```

So `x + ~x = -1`, which gives `~x = -x - 1`, and therefore `~x + 1 = -x`.

```
 5      = 0101
~5      = 1010   (= -6)
~5 + 1  = 1011   (= -5)
```

### The edge cases that cause bugs

**The minimum has no positive twin.** Negate -8 in 4 bits: `~1000 = 0111`, plus 1 = `1000`, which is -8 again. +8 does not fit (max is 7), so it wraps back. In Java:

```java
-Integer.MIN_VALUE == Integer.MIN_VALUE    // true
Math.abs(Integer.MIN_VALUE)                // still negative
```

**Overflow wraps silently.** `0111 + 1 = 1000`: 7 + 1 becomes -8. In Java, `Integer.MAX_VALUE + 1 == Integer.MIN_VALUE`, with no error and no warning. Use `long` for sums and products that can get big.

**`1 << 31` is negative.** It sets the sign bit, giving `Integer.MIN_VALUE`, not 2^31.

### Back to 32 bits: sign extension

`toBinary(-5)` from `ConvertToBinary.java` prints:

```
11111111111111111111111111111011
```

It is the 4-bit `1011` with more 1s on the left. Negative numbers carry a run of leading 1s, non-negative numbers a run of leading 0s. Copying the sign bit leftward to fill a wider type is called **sign extension**, and it is why `>>` fills with 1s on negatives: it keeps a negative number negative after the shift.

Ranges:

| Type | Bits | Min | Max |
|---|---|---|---|
| `int` | 32 | `-2^31` = -2,147,483,648 | `2^31 - 1` = 2,147,483,647 |
| `long` | 64 | `-2^63` | `2^63 - 1` |

### Summary

Negatives are stored so that addition just works. The top bit has a negative place value, so a 1 there makes the number negative. To negate, flip all bits and add 1. The range is lopsided (one extra negative), so `MIN_VALUE` cannot be negated, and going past `MAX_VALUE` wraps to `MIN_VALUE` without any error.

Self-check: write -3 in 4 bits by flipping `0011` and adding 1, then confirm with the -8 trick (answer in the table above).

---

## 4. The operators

| Operator | Name | Rule per bit | Example (`a = 1100`, `b = 1010`) |
|---|---|---|---|
| `a & b` | AND | 1 only if both are 1 | `1000` |
| `a \| b` | OR | 1 if at least one is 1 | `1110` |
| `a ^ b` | XOR | 1 if they are different | `0110` |
| `~a` | NOT | flips every bit | `...0011` (and `~a == -a - 1`) |
| `a << k` | left shift | move bits left `k` places, fill 0s on the right | `x << k == x * 2^k` (until overflow) |
| `a >> k` | signed right shift | move right, fill with the **sign bit** | `x >> k == floor(x / 2^k)` |
| `a >>> k` | unsigned right shift | move right, fill with **0** | for bit-walking negatives |

Intuition for each:
- **AND is a filter:** `x & mask` keeps only the bits that are 1 in the mask. Use it to **read** or **clear** bits.
- **OR is a stamp:** `x | mask` forces the mask's bits to 1. Use it to **set** bits.
- **XOR is a toggle:** `x ^ mask` flips the mask's bits. Use it to **flip** bits and to **cancel** pairs.
- **Shifts move things into position:** `1 << k` builds a mask with only bit `k` on; `x >> k` brings bit `k` down to position 0.

### Shift details that bite

- `>>` vs `/` on negatives: `-7 >> 1` is `-4` (rounds toward minus infinity), but `-7 / 2` is `-3` (rounds toward zero).
- `>>` vs `>>>`: `-8 >> 1` is `-4`; `-8 >>> 1` is `2147483644` (a large positive number). Use `>>>` when you walk the bits of a possibly negative number, or the loop never ends because `>>` keeps shifting in 1s.
- Java uses only the low 5 bits of the shift distance for `int` (low 6 for `long`): `1 << 32 == 1`, not 0. For bits 31 and above, use a `long`: `1L << k`.

### Precedence trap

Bitwise `&`, `^`, `|` bind **weaker** than `==`, and shifts bind weaker than `+` and `-`.

```java
if (x & 1 == 0)          // parses as x & (1 == 0): does not compile
if ((x & 1) == 0)        // correct
int y = 1 << n - 1;      // means 1 << (n - 1)
```

Rule: **always parenthesize bit expressions.**

---

## 5. Single-bit operations (bit `k`, counted from 0 at the right)

```java
boolean isSet = ((x >> k) & 1) == 1;   // check: bring bit k down, look at it
boolean isSet2 = (x & (1 << k)) != 0;  // check: same thing, mask style (use != 0, not == 1)
x = x | (1 << k);                      // set bit k to 1
x = x & ~(1 << k);                     // clear bit k to 0
x = x ^ (1 << k);                      // toggle bit k
```

### How `(x >> k) & 1` reads one bit, step by step

1. `x >> k` slides the bits right by `k` places. The `k` rightmost bits fall off, so bit `k` lands at position 0.
2. `& 1` keeps only position 0. `1` is `000...0001`, and AND gives 1 only where both sides are 1, so every other bit is wiped out. The result is exactly 0 or 1.

Without `& 1`, `13 >> 2` gives `11` (3), not a clean yes/no.

All bits of `x = 13` (`1101`):

| k | `13 >> k` | `& 1` | bit k |
|---|---|---|---|
| 0 | `1101` | 1 | 1 |
| 1 | `110` | 0 | 0 |
| 2 | `11` | 1 | 1 |
| 3 | `1` | 1 | 1 |

Read the last column from bottom to top: `1101` = 13. Looping `k` from 31 down to 0 this way prints the full binary of any `int`, negatives included (`scalar/tasks/bits/ConvertToBinary.java`).

`x = 10` (`1010`):

```
10 >> 1 = 101   & 1 = 1   bit 1 set      (10 contains 2)
10 >> 2 = 10    & 1 = 0   bit 2 not set  (10 has no 4)
10 >> 3 = 1     & 1 = 1   bit 3 set      (10 contains 8)
```

Picture: the number is a train sliding right past a window at position 0. `>> k` slides carriage `k` into the window; `& 1` looks only through the window.

Recipe: write `x` in binary, drop `k` digits off the right end, look at the last digit.

### All four single-bit operations

Why each works, with `x = 1101` (13) and `k = 1`:
- `1 << 1 = 0010`, a mask with only bit 1 on.
- **Check:** `1101 & 0010 = 0000`, so bit 1 is 0.
- **Set:** `1101 | 0010 = 1111` (15).
- **Clear:** `~0010 = ...1101`, then `1101 & 1101 = 1101` (bit 1 already 0, so no change).
- **Toggle:** `1101 ^ 0010 = 1111`.

Trap: with the mask style, `(x & (1 << k)) == 1` is wrong for any `k > 0`, because the result is `1 << k`, not 1. Compare with `!= 0`.

### Masks of many bits

```java
int lowK  = (1 << k) - 1;     // k ones at the bottom: k = 3 -> 0111
x & ((1 << k) - 1)            // keep the lowest k bits (same as x % 2^k for x >= 0)
x & (-1 << k)                 // clear the lowest k bits
x & 1                         // parity: 1 if odd, 0 if even (works for negatives too)
```

Odd check trap: `x % 2 == 1` is **false** for negative odd numbers in Java (`-3 % 2 == -1`). `(x & 1) == 1` is always right.

---

## 6. The lowest set bit: the two most useful tricks

```java
x & (x - 1)    // x with its lowest set bit removed
x & -x         // only the lowest set bit of x
```

Why `x - 1` works: subtracting 1 turns the lowest 1 into 0 and every 0 below it into 1. ANDing with `x` wipes out exactly that region.

```
x     = 1011 0100   (180)
x - 1 = 1011 0011
x & (x - 1) = 1011 0000   (lowest 1 removed)
```

Why `-x` works: `-x = ~x + 1`. Flipping turns the trailing zeros into ones, and the +1 carries through them, landing on the position of the lowest 1. Everything above is flipped, so AND keeps only that one bit.

```
x  = 1011 0100
-x = 0100 1100
x & -x = 0000 0100   (4)
```

What you build from them:

| Task | Code | Why |
|---|---|---|
| Is `x` a power of 2? | `x > 0 && (x & (x - 1)) == 0` | A power of 2 has exactly one set bit; removing it leaves 0. The `x > 0` check excludes 0 and negatives. |
| Is `x` a power of 4? | `x > 0 && (x & (x - 1)) == 0 && (x & 0x55555555) != 0` | Power of 2, and the one bit sits at an even position (0x5 = 0101). |
| Count set bits (Brian Kernighan) | `while (x != 0) { x &= x - 1; count++; }` | Each step removes one set bit, so it loops once per set bit. Works for negatives too. |
| Lowest set bit's value | `x & -x` | Used in Fenwick trees and to split numbers into two groups. |

---

## 7. Counting set bits (popcount): three ways

```java
// 1. Walk all bits: O(32). Use >>> so negatives terminate.
int count = 0;
while (x != 0) { count += x & 1; x >>>= 1; }

// 2. Brian Kernighan: O(number of set bits).
int count2 = 0;
while (x != 0) { x &= x - 1; count2++; }

// 3. Built-in (fine in contests; know 1 or 2 for interviews).
Integer.bitCount(x);  Long.bitCount(y);
```

---

## 8. XOR: the cancelling operator

Rules (memorize all five):

```
a ^ 0 = a          a ^ a = 0          a ^ b = b ^ a
(a ^ b) ^ c = a ^ (b ^ c)            if a ^ b = c, then a ^ c = b
```

Together they mean: **XOR a list of numbers, and every value that appears an even number of times disappears.** Order does not matter.

### Pattern A: single number (every other value appears twice)

```java
int ans = 0;
for (int v : A) ans ^= v;   // pairs cancel, the single one remains. O(n) time, O(1) space.
```

### Pattern B: missing number in `0..n` (one is missing)

```java
int ans = n;
for (int i = 0; i < n; i++) ans ^= i ^ A[i];   // every present number cancels with its index
```

No overflow risk, unlike the sum formula `n(n+1)/2 - sum`.

### Pattern C: two single numbers (every other value appears twice)

1. `xr` = XOR of everything = `a ^ b` (the two singles). It is non-zero because `a != b`.
2. `bit = xr & -xr`: a bit where `a` and `b` differ.
3. Split all numbers by that bit and XOR each group: each group holds one single plus complete pairs.

```java
int xr = 0;
for (int v : A) xr ^= v;
int bit = xr & -xr;
int a = 0, b = 0;
for (int v : A) {
    if ((v & bit) != 0) a ^= v; else b ^= v;
}
```

### Pattern D: single number (every other value appears three times)

XOR cannot cancel triples. Count per bit instead: for each of the 32 bits, count how many numbers have it set. If `count % 3 != 0`, the single number has that bit.

```java
int ans = 0;
for (int k = 0; k < 32; k++) {
    int cnt = 0;
    for (int v : A) cnt += (v >> k) & 1;
    if (cnt % 3 != 0) ans |= (1 << k);
}
```

O(32 n) time, O(1) space. Works for negatives because bit 31 is handled like any other bit. The same idea works for "every other value appears `m` times" with `% m`.

### XOR swap (know it, do not use it)

```java
a ^= b; b ^= a; a ^= b;
```

It breaks when both refer to the same slot (`A[i]` and `A[j]` with `i == j`: the value becomes 0). A temp variable is clearer and just as fast.

---

## 9. Contribution per bit (pairs problems)

When a problem asks for a sum over **all pairs** of `XOR`, `AND` or `OR`, do not loop over pairs (O(n^2)). Each bit contributes independently. For bit `b`, let `ones` = how many numbers have bit `b` set, `zeros = n - ones`.

| Sum over all pairs `i < j` of | Pairs where bit `b` is 1 | Contribution of bit `b` |
|---|---|---|
| `A[i] ^ A[j]` | bits differ: `ones * zeros` | `ones * zeros * 2^b` |
| `A[i] & A[j]` | both 1: `ones * (ones - 1) / 2` | that count `* 2^b` |
| `A[i] \| A[j]` | not both 0: `totalPairs - zeros * (zeros - 1) / 2` | that count `* 2^b` |

Total = sum over all bits. O(32 n). Use `long` for the counts and the sum. This is exactly Contest 1's Bitville XOR pairs problem (`contest1-01-xor-pairs-bitville.md`).

---

## 10. Bitmasks as sets: generating subsets

For `n` items (`n` up to about 20), every integer `mask` from `0` to `2^n - 1` is one subset: item `i` is in it if bit `i` of `mask` is set.

```java
for (int mask = 0; mask < (1 << n); mask++) {
    for (int i = 0; i < n; i++) {
        if (((mask >> i) & 1) == 1) {
            // A[i] is in this subset
        }
    }
}
```

O(2^n * n). `n = 3` gives masks `000` to `111`: 8 subsets, including the empty one (`000`) and the full one (`111`).

Set operations on masks: union `a | b`, intersection `a & b`, difference `a & ~b`, add item `mask | (1 << i)`, remove item `mask & ~(1 << i)`, size `Integer.bitCount(mask)`.

---

## 11. Handy identities

```
a + b  == (a ^ b) + 2 * (a & b)     XOR is addition without carries; AND finds the carries
a | b  == (a ^ b) + (a & b)
a ^ b  == (a | b) - (a & b)
~x     == -x - 1
x << k == x * 2^k                   (until it overflows)
x >> k == floor(x / 2^k)
x & (2^k - 1) == x % 2^k            (for x >= 0)
```

Highest set bit:

```java
int pos = 31 - Integer.numberOfLeadingZeros(x);  // position of the highest set bit (x > 0)
int val = Integer.highestOneBit(x);              // its value: highestOneBit(13) == 8
```

---

## 12. Java built-ins worth knowing

| Method | Returns | Example |
|---|---|---|
| `Integer.bitCount(x)` | number of set bits | `bitCount(13) == 3` |
| `Integer.toBinaryString(x)` | binary string, no leading zeros | `"1101"` |
| `Integer.parseInt(s, 2)` | value of a binary string | `parseInt("1101", 2) == 13` |
| `Integer.highestOneBit(x)` | value of the highest set bit | `highestOneBit(13) == 8` |
| `Integer.lowestOneBit(x)` | value of the lowest set bit (same as `x & -x`) | `lowestOneBit(12) == 4` |
| `Integer.numberOfTrailingZeros(x)` | zeros below the lowest set bit | `numberOfTrailingZeros(12) == 2` |
| `Integer.numberOfLeadingZeros(x)` | zeros above the highest set bit (out of 32) | `numberOfLeadingZeros(13) == 28` |
| `Integer.reverse(x)` | the 32 bits in reverse order | |

All exist on `Long` too, for 64 bits.

---

## 13. Trap list (check before submitting)

1. Missing parentheses: `(x & 1) == 0`, never `x & 1 == 0`.
2. `1 << 31` is negative; `1 << 32 == 1`. Use `1L << k` when `k` can reach 31 or more, or when the result goes into a `long`.
3. Walking the bits of a negative number with `>>` never ends. Use `>>>`, or loop exactly 32 times.
4. Mask-style check compared with `== 1`: use `!= 0`, or shift first and then `& 1`.
5. `x % 2 == 1` fails for negative odd numbers. Use `(x & 1) == 1`.
6. `>> 1` and `/ 2` differ for negative odd numbers.
7. Power-of-2 test without `x > 0` wrongly accepts 0 (and `Integer.MIN_VALUE`).
8. Pair-counting answers overflow `int`: use `long` for `ones * zeros` and the sum.
9. Bits are counted from 0 at the right. Check whether the problem counts from 1.

---

## 14. Practice ladder

Do them in order. Each line names the trick it practises.

| # | Problem | Trick | Status |
|---|---|---|---|
| 1 | Check / set / clear / toggle bit `k` | Section 5 | |
| 2 | Count set bits (both ways) | Sections 6, 7 | |
| 3 | Power of 2 | `x & (x - 1)` | |
| 4 | Single number (others twice) | XOR cancel | |
| 5 | Missing number `0..n` | XOR with indices | |
| 6 | Single number (others three times) | Per-bit count `% 3` | |
| 7 | Two single numbers | XOR + split on `x & -x` | |
| 8 | Sum of XOR over all pairs | Contribution per bit | Done: Contest 1, Bitville XOR pairs |
| 9 | All subsets of an array | Bitmask loop | |
| 10 | Min XOR value pair | Sort, then check adjacent pairs only | |

---

## 15. Interview lines

- **Complexity:** "The bit loop runs at most 32 times for an `int`, so it's O(1) per number; over the array it's O(32 n), which is O(n)."
- **Why XOR for single number:** "XOR is commutative and associative, `a ^ a = 0` and `a ^ 0 = a`, so every pair cancels regardless of order, leaving the unique value in O(1) space."
- **Why `x & (x - 1)`:** "Subtracting 1 flips the lowest set bit and every bit below it, so ANDing clears exactly that lowest bit."
- **Why per-bit counting for triples:** "XOR only cancels even counts. Counting each bit modulo 3 removes the triples bit by bit."
