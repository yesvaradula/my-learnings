import java.util.Arrays;

public class RotateMatrix {
    public static void main(String[] args) {
        int[][][] tests = {
                { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } },
                { { 5 } },
                { { 1, 2 }, { 3, 4 } },
                { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } },
                { { -1, -2 }, { -3, -4 } },
                { { 7, 7, 7 }, { 7, 7, 7 }, { 7, 7, 7 } },
                { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } },
        };
        int[][][] expected = {
                { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 } },
                { { 5 } },
                { { 3, 1 }, { 4, 2 } },
                { { 13, 9, 5, 1 }, { 14, 10, 6, 2 }, { 15, 11, 7, 3 }, { 16, 12, 8, 4 } },
                { { -3, -1 }, { -4, -2 } },
                { { 7, 7, 7 }, { 7, 7, 7 }, { 7, 7, 7 } },
                { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } },
        };

        int pass = 0;
        for (int t = 0; t < tests.length; t++) {
            String input = Arrays.deepToString(tests[t]);
            int[][] got = solve(tests[t]);
            boolean ok = Arrays.deepEquals(got, expected[t]);
            if (ok)
                pass++;
            System.out.println((ok ? "PASS" : "FAIL") + " " + input + " -> " + Arrays.deepToString(got)
                    + " (expected " + Arrays.deepToString(expected[t]) + ")");
        }
        System.out.println(pass + "/" + tests.length + " passed");

        // Cross-check against the copy-based rotation: new[j][n-1-i] = old[i][j]
        java.util.Random rnd = new java.util.Random(46);
        int bad = 0;
        for (int r = 0; r < 500; r++) {
            int n = 1 + rnd.nextInt(8);
            int[][] A = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    A[i][j] = rnd.nextInt(201) - 100;
            int[][] want = solveBrute(A);
            if (!Arrays.deepEquals(solve(A), want))
                bad++;
        }
        System.out.println("random cross-check: " + bad + " mismatches out of 500");
    }

    static int[][] solveBrute(int[][] A) {
        int n = A.length;
        int[][] B = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                B[j][n - 1 - i] = A[i][j];
        return B;
    }

    static int[][] solve(int[][] A) {
        // tc: o(n*n);
        // sc: o(1);
        int n = A.length;

        for (int i = 0; i < A.length; i++) {
            for (int j = i + 1; j < A[0].length; j++) {
                int temp = A[i][j];
                A[i][j] = A[j][i];
                A[j][i] = temp;
            }
        }

        for (int i = 0; i < A.length; i++) {
            int x = 0;
            int y = n - 1;

            while (x < y) {
                int temp = A[i][x];
                A[i][x] = A[i][y];
                A[i][y] = temp;

                x++;
                y--;
            }

        }

        return A;
    }

    // static void swap(A, )
}
