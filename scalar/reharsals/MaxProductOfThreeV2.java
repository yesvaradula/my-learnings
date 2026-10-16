public class MaxProductOfThreeV2 {
    public static void main(String[] args) {
        int[][] tests = {
                { 1, 2, 3 },
                { -10, -10, 1, 3, 2 },
                { 5, 1, -3, -4 },
                { 0, 0, 0 },
                { -1, -2, -3 },
                { -5, -4, -3, -2 },
                { 10, 5, 9, 20, 15 },
                { 0, -1, 3, 100, 70, 50 },
                { -100, -98, -1, 2, 3, 4 },
                { 1, 1, 1, 1 },
                { -5, 0, 2, 3 },
                { 1000, 1000, 1000 },
        };
        int[] expected = { 6, 300, 60, 0, -6, -24, 3000, 350000, 39200, 1, 0, 1000000000 };

        int pass = 0;
        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t]);
            boolean ok = got == expected[t];
            if (ok) pass++;
            System.out.println((ok ? "PASS" : "FAIL") + " " + java.util.Arrays.toString(tests[t])
                    + " -> " + got + " (expected " + expected[t] + ")");
        }
        System.out.println(pass + "/" + tests.length + " passed");

        // Cross-check against the O(n^3) brute force on random arrays
        java.util.Random rnd = new java.util.Random(41);
        int bad = 0;
        for (int r = 0; r < 2000; r++) {
            int n = 3 + rnd.nextInt(8);
            int[] A = new int[n];
            for (int i = 0; i < n; i++) A[i] = rnd.nextInt(201) - 100;
            if (solve(A) != solveBrute(A)) bad++;
        }
        System.out.println("random cross-check: " + bad + " mismatches out of 2000");
    }

    static int solveBrute(int[] A) {
        long best = Long.MIN_VALUE;
        for (int i = 0; i < A.length; i++)
            for (int j = i + 1; j < A.length; j++)
                for (int k = j + 1; k < A.length; k++)
                    best = Math.max(best, (long) A[i] * A[j] * A[k]);
        return (int) best;
    }

    // TC: O(n), one pass. SC: O(1), five tracking variables.
    static int solve(int[] A) {

        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;

        int min_1 = max, min_2 = max;
        int max_1 = min, max_2 = min, max_3 = min;

        for (int num : A) {
            if (num > max_1) {
                max_3 = max_2;
                max_2 = max_1;
                max_1 = num;
            } else if (num > max_2) {
                max_3 = max_2;
                max_2 = num;
            } else if (num > max_3) {
                max_3 = num;
            }

            if (num < min_1) {
                min_2 = min_1;
                min_1 = num;
            } else if (num < min_2) {
                min_2 = num;
            }
        }

        long maxProducts = (long) max_1 * max_2 * max_3;
        long minProducts = (long) max_1 * min_1 * min_2;
        return (int) Math.max(maxProducts, minProducts);
    }
}
