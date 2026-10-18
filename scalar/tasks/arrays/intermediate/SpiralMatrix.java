import java.util.Arrays;

public class SpiralMatrix {
    public static void main(String[] args) {
        int[][][] tests = {
                { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } },
                { { 1, 2, 3, 10 }, { 4, 5, 6, 11 }, { 7, 8, 9, 12 } },
                { { 5 } },
                { { 1, 2, 3 } },
                { { 1 }, { 2 }, { 3 } },
                { { 1, 2 }, { 3, 4 } },
                { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } },
                { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } },
                { { -1, -2, -3 }, { -4, -5, -6 } },
        };
        int[][] expected = {
                { 1, 2, 3, 6, 9, 8, 7, 4, 5 },
                { 1, 2, 3, 10, 11, 12, 9, 8, 7, 4, 5, 6 },
                { 5 },
                { 1, 2, 3 },
                { 1, 2, 3 },
                { 1, 2, 4, 3 },
                { 1, 2, 3, 4, 8, 12, 16, 15, 14, 13, 9, 5, 6, 7, 11, 10 },
                { 1, 2, 3, 6, 9, 12, 11, 10, 7, 4, 5, 8 },
                { -1, -2, -3, -6, -5, -4 },
        };

        int pass = 0;
        for (int t = 0; t < tests.length; t++) {
            int[] got = solve(tests[t]);
            boolean ok = Arrays.equals(got, expected[t]);
            if (ok)
                pass++;
            System.out.println((ok ? "PASS" : "FAIL") + " " + Arrays.deepToString(tests[t]) + " -> "
                    + Arrays.toString(got) + " (expected " + Arrays.toString(expected[t]) + ")");
        }
        System.out.println(pass + "/" + tests.length + " passed");

        // Cross-check against a direction-turning walk with a visited grid
        java.util.Random rnd = new java.util.Random(47);
        int bad = 0;
        for (int r = 0; r < 500; r++) {
            int n = 1 + rnd.nextInt(7), m = 1 + rnd.nextInt(7);
            int[][] A = new int[n][m];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    A[i][j] = rnd.nextInt(201) - 100;
            if (!Arrays.equals(solve(A), solveBrute(A)))
                bad++;
        }
        System.out.println("random cross-check: " + bad + " mismatches out of 500");
    }

    static int[] solveBrute(int[][] A) {
        int n = A.length, m = A[0].length;
        int[] dr = { 0, 1, 0, -1 }, dc = { 1, 0, -1, 0 };
        boolean[][] seen = new boolean[n][m];
        int[] ans = new int[n * m];
        int r = 0, c = 0, d = 0;
        for (int k = 0; k < n * m; k++) {
            ans[k] = A[r][c];
            seen[r][c] = true;
            int nr = r + dr[d], nc = c + dc[d];
            if (nr < 0 || nr >= n || nc < 0 || nc >= m || seen[nr][nc]) {
                d = (d + 1) % 4;
                nr = r + dr[d];
                nc = c + dc[d];
            }
            r = nr;
            c = nc;
        }
        return ans;
    }

    static int[] solve(int[][] A) {

        // tc: O(n*m);
        // sc: O(n*m)

        int n = A.length;
        int m = A[0].length;

        int[] ans = new int[n * m];

        int top = 0;
        int left = 0;
        int right = m - 1;
        int bottom = n - 1;

        int k = 0;

        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) {
                ans[k++] = A[top][j];
            }
            top++;

            for (int j = top; j <= bottom; j++) {
                ans[k++] = A[j][right];
            }
            right--;

            if (top <= bottom) { // still a row left?
                for (int j = right; j >= left; j--) {
                    ans[k++] = A[bottom][j];
                }
                bottom--;
            }

            if (left <= right) { // still a column left?
                for (int j = bottom; j >= top; j--) {
                    ans[k++] = A[j][left];
                }
                left++;
            }
        }

        return ans;
    }
}
