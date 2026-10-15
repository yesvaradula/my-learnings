public class FindInMetrics {
    /*
     * Given a matrix of integers A of size N x M and an integer B.
     *
     * In the given matrix every row and column is sorted in non-decreasing order.
     * Find and return the position of B in the matrix in the given form:
     * If A[i][j] = B then return (i * 1009 + j)
     * If B is not present return -1.
     *
     * Note 1: Rows are numbered from top to bottom and columns are numbered from
     * left to right.
     * Note 2: If there are multiple B in A then return the smallest value of i1009
     * +j such that A[i][j]=B.
     * Note 3: Expected time complexity is linear
     * Note 4: Use 1-based indexing
     *
     *
     * Problem Constraints
     * 1 <= N, M <= 1000
     * -100000 <= A[i] <= 100000
     * -100000 <= B <= 100000
     */

    public static void main(String[] args) {
        int[][][] matrices = {
                { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } },
                { { 1, 2 }, { 3, 3 } },
                { { 1, 2 }, { 3, 4 } },
                { { 1, 2, 2 }, { 2, 3, 4 } },
                { { 7 } },
                { { 7 } },
                { { 1, 3, 5, 7 } },
                { { 1 }, { 3 }, { 5 }, { 7 } },
                { { 2, 8, 8, 8 }, { 2, 8, 8, 8 }, { 2, 8, 8, 8 } },
                { { -5, -3 }, { -4, 0 } },
                { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } },
                { { 5, 6 }, { 7, 8 } },
                { { 5, 6 }, { 7, 8 } },
        };
        int[] bs = { 5, 3, 5, 2, 7, 3, 5, 5, 8, -4, 3, 1, 9 };
        int[] expected = { 2020, 2019, -1, 1011, 1010, -1, 1012, 3028, 1011, 2019, 1012, -1, -1 };

        int failed = 0;
        for (int t = 0; t < matrices.length; t++) {
            int got = solve(matrices[t], bs[t]);
            int brute = solveBrute(matrices[t], bs[t]);
            boolean ok = got == expected[t] && brute == expected[t];
            if (!ok) {
                failed++;
            }
            System.out.println("test " + (t + 1) + ": B=" + bs[t] + " expected=" + expected[t]
                    + " staircase=" + got + " brute=" + brute + " -> " + (ok ? "PASS" : "FAIL"));
        }
        System.out.println(failed == 0 ? "ALL PASS" : failed + " FAILED");
    }

    // staircase from the top-right
    // tc: O(n+m)
    // sc: O(1)
    static int solve(int[][] A, int B) {
        int r = 0;
        int c = A[0].length - 1;
        int ans = Integer.MAX_VALUE;

        while (r < A.length && c >= 0) {
            if (A[r][c] == B) {
                ans = Math.min(ans, ((r + 1) * 1009) + (c + 1));
                c--;
            } else if (A[r][c] > B) {
                c--;
            } else {
                r++;
            }
        }

        return (ans == Integer.MAX_VALUE) ? -1 : ans;
    }

    // brute force, kept to cross-check the staircase
    // tc: O(n*m)
    // sc: O(1)
    static int solveBrute(int[][] A, int B) {
        int ans = Integer.MAX_VALUE;

        for (int r = 0; r < A.length; r++) {
            for (int c = 0; c < A[r].length; c++) {
                if (B == A[r][c]) {
                    ans = Math.min(ans, ((r + 1) * 1009) + (c + 1));
                }
            }
        }

        return (ans == Integer.MAX_VALUE) ? -1 : ans;
    }
}
