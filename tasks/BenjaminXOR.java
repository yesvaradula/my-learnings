public class BenjaminXOR {
    public static void main(String[] args) {

       

       int[][] As = {
            {1, 2, 3},
            {2, 4, 7, 11},
            {5},
            {1, 1, 1},
            {1, 2, 4, 8}
        };
        int[][] Bs = {
            {0, 1},
            {3},
            {0},
            {0, 1},
            {0, 31}
        };
        long[][] expected = {
            {2, 2},
            {3},
            {0},
            {0, 0},
            {3, 0}
        };

        for (int t = 0; t < As.length; t++) {
            long[] got = solve(As[t], Bs[t]);
            boolean ok = java.util.Arrays.equals(got, expected[t]);
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                + "  got=" + java.util.Arrays.toString(got)
                + "  expected=" + java.util.Arrays.toString(expected[t]));
        }


        int[] big = new int[100000];
for (int k = 0; k < big.length; k++) big[k] = k % 2;   // half have bit 0 set
long[] got = solve(big, new int[]{0});
System.out.println(got[0] == 50000L * 50000L ? "PASS" : "FAIL");
    }

    static long[] solve(int[] A, int[] B) {

         // TC: O(A.length * B.length);
        // SC:  O(1) extra (+ O(m) output)

        long[] ans = new long[B.length];
        long ones = 0;


        for( int i = 0; i < B.length; i++ ) {
            ones = 0;

            int j = 0;
            while(j < A.length) {
                if ((A[j] & (1 << B[i])) != 0) {
                    ones++;
                }
                j++;
            }

            ans[i] = ones * (A.length - ones);

        }

        return ans;
    }
}
