public class WaterTrap {
    public static void main(String[] args) {
        int[] heights = { 4, 2, 0, 3, 2, 5 };
        int trappedWater = twoPointerTrap(heights);
        System.out.println("Trapped Water: " + trappedWater);
    }

    static int twoPointerTrap(int[] height) {

        if (height.length < 3) {
            return 0;
        }

        int ans = 0;
        int leftMax = 0;
        int rightMax = 0;

        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            if (height[left] < height[right]) {
                leftMax = Math.max(leftMax, height[left]);
                ans += leftMax - height[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, height[right]);
                ans += rightMax - height[right];
                right--;
            }
        }

        return ans;
        // Time complexity: O(n)
        // Space complexity: O(1)

    }

    static int trap(int[] height) {
        int ans = 0;
        int n = height.length;

        if (n < 3) {
            return 0;
        }

        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        leftMax[0] = height[0];
        // store the left max for each index.
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }

        rightMax[n - 1] = height[n - 1];
        // store the right max for each index.
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        // calculate the trapped water for each index.
        for (int i = 0; i < n; i++) {
            ans += Math.min(leftMax[i], rightMax[i]) - height[i];
        }

        return ans;
        // Time complexity: O(n)
        // Space complexity: O(n)
    }
}
