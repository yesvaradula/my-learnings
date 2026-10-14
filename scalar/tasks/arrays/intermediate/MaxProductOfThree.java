public class MaxProductOfThree {
    public static void main(String[] args) {

        int[][] tests = {
                {1, 2, 3},                 // 1. exactly 3, all positive
                {0, 0, 0},                 // 2. all zeros
                {-1, -2, -3},              // 3. all negative, N = 3
                {-5, -4, -3, -2},          // 4. all negative: pick the 3 closest to zero
                {10, 5, 9, 20, 15},        // 5. best 3 are not neighbours
                {-10, -10, 1, 3, 2},       // 6. two negatives x biggest positive wins
                {-10, -3, 5, 6, -2},       // 7. max(5*6*-2, 6*-10*-3) = 180
                {0, -1, 3, 100, -70, -50}, // 8. 100 * -70 * -50
                {1, 1, 1, 1},              // 9. all same
                {-1, 0, 1},                // 10. zero in the middle
                {-5, -6, 0, 1},            // 11. -5 * -6 * 1
                {1000, 1000, 1000, -1}     // 12. big values (fits in int per problem)
        };

        int[] expected = {6, 0, -6, -24, 3000, 300, 180, 350000, 1, 0, 30, 1000000000};

        for (int t = 0; t < tests.length; t++) {
            int got = maxp3(tests[t]);
            boolean ok = got == expected[t];
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                + "  got=" + got + "  expected=" + expected[t]);
        }
    }

    // Answer is either the 3 largest, or the largest x the 2 smallest (two negatives make a positive).
    // TC: O(N), single pass
    // SC: O(1)
    static int maxp3(int[] A) {
        int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;

        for (int x : A) {
            // shift the max chain down when x beats one of them
            if (x > max1) {
                max3 = max2;
                max2 = max1;
                max1 = x;
            } else if (x > max2) {
                max3 = max2;
                max2 = x;
            } else if (x > max3) {
                max3 = x;
            }

            // same for the two smallest
            if (x < min1) {
                min2 = min1;
                min1 = x;
            } else if (x < min2) {
                min2 = x;
            }
        }

        // long so the intermediate product can't overflow before we compare
        long allMax = (long) max1 * max2 * max3;
        long twoMin = (long) max1 * min1 * min2;
        return (int) Math.max(allMax, twoMin);
    }
}
