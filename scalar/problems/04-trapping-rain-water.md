# 04. Trapping Rain Water

**Topic:** Arrays · **Pattern:** Prefix/suffix max → two pointers · **Status:** ✅ Solved both ways (Oct 4) · ✅ Solved in Contest 1 (Oct 7)

## Problem
`height[i]` is the height of a bar of width 1. After rain, how many units of water are trapped?

```
[4, 2, 0, 3, 2, 5]  →  9

5 |                     █
4 | █   ~   ~   ~   ~   █
3 | █   ~   ~   █   ~   █
2 | █   █   ~   █   █   █
1 | █   █   ~   █   █   █
    4   2   0   3   2   5   ← height
    0   2   4   1   2   0   ← water
```

## Key idea
Water above bar `i` = **min(tallest on the left, tallest on the right) − height[i]**.
Use `min` because water spills over the shorter wall. Including `height[i]` in its own maxes keeps the result non-negative.

## Solution 1: prefix and suffix max arrays
```java
// TC: O(n), SC: O(n)
static int trap(int[] height) {
    int n = height.length;
    if (n < 3) return 0;

    int[] leftMax = new int[n];
    int[] rightMax = new int[n];

    leftMax[0] = height[0];
    for (int i = 1; i < n; i++)
        leftMax[i] = Math.max(leftMax[i - 1], height[i]);

    rightMax[n - 1] = height[n - 1];
    for (int i = n - 2; i >= 0; i--)
        rightMax[i] = Math.max(rightMax[i + 1], height[i]);

    int ans = 0;
    for (int i = 0; i < n; i++)
        ans += Math.min(leftMax[i], rightMax[i]) - height[i];
    return ans;
}
```

## Solution 2: two pointers
If `height[left] < height[right]`, some wall on the right is at least that tall, so the left side is the bottleneck: the water at `left` depends only on `leftMax`. Otherwise, mirror it on the right.

```java
// TC: O(n), SC: O(1)
static int twoPointerTrap(int[] height) {
    if (height.length < 3) return 0;

    int ans = 0, leftMax = 0, rightMax = 0;
    int left = 0, right = height.length - 1;

    while (left < right) {
        if (height[left] < height[right]) {
            leftMax = Math.max(leftMax, height[left]);   // update first...
            ans += leftMax - height[left];               // ...then add
            left++;
        } else {
            rightMax = Math.max(rightMax, height[right]);
            ans += rightMax - height[right];
            right--;
        }
    }
    return ans;
}
```

## Gotchas
- The first and last bars never hold water.
- Update the max **before** adding water.
- Loop `left < right`: the bar where they meet is the tallest seen, so it holds nothing.

## Tests
| Input | Expected |
|---|---|
| `[0,1,0,2,1,0,1,3,2,1,2,1]` | 6 |
| `[4,2,0,3,2,5]` | 9 |
| `[3,0,3]` | 3 |
| `[1,2,3,4,5]` | 0 |
| `[5,4,3,2,1]` | 0 |
| `[2]` | 0 |
