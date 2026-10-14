public class DecreasingDishes {
    public static void main(String[] args) {
       int[][] tests = {
            {3, 2, 1},
            {3, 3, 5, 0, 1},
            {7},
            {1, 2, 3},
            {5, 5, 5},
            {0, 0},
            {10, 1, 20, 19, 18}
        };
        int[] expected = {6, 5, 7, 3, 5, 0, 57};

        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t]);
            boolean ok = got == expected[t];          // hint: it's an int now, not an array
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                + "  got=" + got + "  expected=" + expected[t]);
        }
    }

    static int solve(int[] A) {
        // TC: O(n);
        // SC: O(1);

        if( A.length == 0 )return 0;
        if(A.length == 1) return A[0];

        int ans = A[0];
        int currentSum = A[0];

        for(int i = 1; i < A.length; i++) {
            if( A[i] < A[i - 1]) {
                currentSum += A[i];
            } else {
                currentSum = A[i];
            }

            ans = Math.max(ans, currentSum);
        }

        return ans;
    }
}
