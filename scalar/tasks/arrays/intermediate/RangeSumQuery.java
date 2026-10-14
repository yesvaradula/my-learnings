import java.util.Arrays;

public class RangeSumQuery {
    public static void main(String[] args) {
        int[] nums = { -1, 3, -2, 4 };
        int[][] queries = { { 0, 3 }, { 1, 1 }, { 2, 3 } };
        long[] result = rangeSum(nums, queries);
        System.out.println(Arrays.toString(result));
    }

    // TC: O(n + m), SC: O(n)
    static long[] rangeSum(int[] A, int[][] B) {
        int n = A.length;
        int m = B.length;

        long[] C = new long[m];
        long[] prefixSum = new long[n];

        // first let me calculate the prefix of Array A
        prefixSum[0] = A[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + A[i];
        }

        // working on the queries.
        for (int i = 0; i < m; i++) {
            int L = B[i][0];
            int R = B[i][1];
            C[i] = L == 0 ? prefixSum[R] : prefixSum[R] - prefixSum[L - 1];
        }
        return C;
    }
}
