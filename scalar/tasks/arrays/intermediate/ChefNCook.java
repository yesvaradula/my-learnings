public class ChefNCook {
    /*
     * In a parallel universe, there exists a kingdom renowned for its unique
     * culinary traditions. In this kingdom, a famous chef is celebrated for her
     * delectable dishes. One day, the chef decided to create a new dish using a
     * sequence of ingredients, each with a distinct weight. The chef wants to
     * select a subarray of these ingredients where the weights are in strictly
     * increasing order.
     *
     * The chef's goal is to determine the maximum possible sum of the weights of
     * the strictly increasing subarray. Can you assist the chef by writing a
     * function that returns the maximum possible sum of an strictly increasing
     * subarray from the given array of ingredient weights?
     *
     * The array of ingredient weights is represented by the array A.
     *
     *
     * Problem Constraints
     * 1 <= |A| <= 105
     * 1 <= A[i] <= 109
     */

    public static void main(String[] args) {
        int big = 100000;
        int[] bigRun = new int[big];
        for (int i = 0; i < big; i++) {
            bigRun[i] = 900_000_000 + i;
        }
        long bigExpected = (long) big * 900_000_000L + (long) big * (big - 1) / 2;

        int[][] tests = {
                { 1, 2, 3, 4, 5 },
                { 7 },
                { 3, 3, 3 },
                { 5, 4, 3 },
                { 1, 2, 10, 3, 4 },
                { 5, 1, 2, 3 },
                { 1, 2, 3, 1 },
                { 4, 5, 1, 2 },
                { 10, 1, 2, 3, 4, 5 },
                { 1_000_000_000, 1_000_000_000 },
                { 999_999_999, 1_000_000_000 },
                bigRun,
        };
        long[] expected = { 15, 7, 3, 5, 13, 6, 6, 9, 15, 1_000_000_000L, 1_999_999_999L, bigExpected };

        int failed = 0;
        for (int t = 0; t < tests.length; t++) {
            long got = solve(tests[t]);
            boolean ok = got == expected[t];
            if (!ok) {
                failed++;
            }
            System.out.println("test " + (t + 1) + ": expected=" + expected[t] + " got=" + got
                    + " -> " + (ok ? "PASS" : "FAIL"));
        }
        System.out.println(failed == 0 ? "ALL PASS" : failed + " FAILED");
    }

    static long solve(int[] A) {
        // tc: O(n)
        // sc: O(1)
        long max = A[0];
        long current = A[0];

        for (int i = 1; i < A.length; i++) {
            if (A[i] > A[i - 1]) {
                current += A[i];
            } else {
                current = A[i];
            }
            max = Math.max(max, current);
        }

        return max;
    }
}
