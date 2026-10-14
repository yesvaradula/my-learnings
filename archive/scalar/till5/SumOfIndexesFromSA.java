/**
 * 
 * You are given an integer array A of length N.
 * You are also given a 2D integer array B with dimensions M x 2, where each row
 * denotes a [L, R] query.
 * For each query, you have to find the sum of all elements from L to R indices
 * in A (0 - indexed).
 * More formally, find A[L] + A[L + 1] + A[L + 2] +... + A[R - 1] + A[R] for
 * each query.
 * 
 * ============================
 * Problem Constraints
 * 1 <= N, M <= 105
 * 1 <= A[i] <= 109
 * 0 <= L <= R < N
 * ============================
 * 
 * Input Format
 * The first argument is the integer array A.
 * The second argument is the 2D integer array B.
 * 
 * Output Format
 * Return an integer array of length M where ith element is the answer for ith
 * query in B.
 * 
 * Examples:
 * 
 * xample Input
 * 
 * Input 1:
 * 
 * A = [1, 2, 3, 4, 5]
 * B = [[0, 3], [1, 2]]
 * 
 * Output 1:
 * [10, 5]
 * 
 * Input 2:
 * 
 * A = [2, 2, 2]
 * B = [[0, 0], [1, 2]]
 * 
 * Output 2:
 * [2, 4]
 * 
 */

public class SumOfIndexesFromSA {
    public static void main(String[] args) {
        int[] A = { 1, 2, 3, 4, 5 };
        int[][] B = { { 0, 3 }, { 1, 2 } };

        System.out.println("Input Array: " + java.util.Arrays.toString(A));

        System.out.println(java.util.Arrays.toString(rangeSum(A, B)));

    }

    public static long[] rangeSum(int[] A, int[][] B) {
        Long sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i];
            A[i] = sum;
        }

        long[] result = new long[B.length];

        for (int k = 0; k < B.length; k++) {
            int L = B[k][0];
            int R = B[k][1];
            result[k] = A[R] - (L > 0 ? A[L - 1] : 0);
        }

        return result;

        // TC: O(N + M)
        // SC: O(1)

    }
}
