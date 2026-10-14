public class SubArrayGivenLength {
    public static void main(String[] args) {
        int[][] As = { { 4, 3, 2, 6, 1 }, { 4, 2, 2, 5, 1 }, { 1 }, { 1, 2 }, { -1, 2, -1, 3 }, { 5, 5, 5 } };
        int[] Bs = { 3, 4, 1, 3, 2, 3 };
        int[] Cs = { 11, 6, 1, 3, 1, 15 };
        for (int t = 0; t < As.length; t++)
            System.out.println(java.util.Arrays.toString(As[t]) + " B=" + Bs[t] + " C=" + Cs[t]
                    + " -> " + ifSubArray(As[t], Bs[t], Cs[t]));
    }

    // TC: O(n), SC: O(1)
    static int ifSubArray(int[] A, int B, int C) {

        if (B > A.length)
            return 0;

        long sum = 0;
        for (int i = 0; i < B; i++) {
            sum += A[i];
        }
        if (sum == C) {
            return 1;
        }

        for (int i = B; i < A.length; i++) {
            sum += A[i] - A[i - B];
            if (sum == C) {
                return 1;
            }
        }
        return 0;
    }
}
