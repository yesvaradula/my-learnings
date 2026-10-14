public class FindSingleElement {
    public static void main(String[] args) {
        int[][] tests = {
                {1, 1, 7},                      // 1. sample: single at the end
                {2, 3, 3},                      // 2. sample: single at the start
                {5},                            // 3. only one element
                {1, 1, 2, 3, 3},                // 4. single in the middle
                {1, 1, 2, 2, 3},                // 5. single at the end, longer
                {1, 2, 2, 3, 3},                // 6. single at the start, longer
                {1, 1, 2, 2, 3, 4, 4},          // 7. single just right of the middle
                {1, 1, 2, 3, 3, 4, 4},          // 8. single just left of the middle
                {1, 1, 2, 2, 3, 3, 4, 4, 5},    // 9. single at the end, more pairs
                {999, 1000000000, 1000000000},  // 10. huge values
                {                               // 11. long array: single 27 at index 4
                        13, 13, 21, 21, 27, 50, 50, 102, 102, 108, 108, 110, 110, 117, 117, 120, 120, 123, 123, 124, 124, 132,
                        132, 164, 164, 166, 166, 190, 190, 200, 200, 212, 212, 217, 217, 225, 225, 238, 238, 261, 261, 276, 276,
                        347, 347, 348, 348, 386, 386, 394, 394, 405, 405, 426, 426, 435, 435, 474, 474, 493, 493
                }
        };
        int[] expected = {7, 2, 5, 2, 3, 1, 3, 2, 5, 999, 27};

        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t]);
            boolean ok = got == expected[t];
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + "  got=" + got + "  expected=" + expected[t]);
        }
    }

    static int solve(int[] A) {

        // TC: O(log n)
        // SC: O(1)

        int n = A.length;

        if (n == 0)
            return 0;
        if (n == 1)
            return A[0];
        if (A[0] != A[1])
            return A[0];
        if (A[n - 1] != A[n - 2])
            return A[n - 1];

        int s = 0;
        int e = n - 1;
        int m = 0;

        while (s <= e) {
            m = s + (e - s) / 2;

            if (A[m] != A[m - 1] && A[m] != A[m + 1]) {
                return A[m];
            }

            if (A[m] == A[m - 1]) {
                m = m - 1;
            }

            if (m % 2 == 0) {
                s = m + 2; // going to rhs
            } else {
                e = m - 1; // going to lhs
            }
        }
        return 0;
    }
}
