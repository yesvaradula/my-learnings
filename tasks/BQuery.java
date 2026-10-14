public class BQuery {

    public static void main(String args[]) {
        int[][] B = {
                { 1, 4, },
                { 0, 5 },
                { 2, 2 },
                { 4, 6 }
        };

        int[] A = new int[8];

        System.out.println(java.util.Arrays.toString(A));

        for (int i = 0; i < B.length; i++) {
            int index = B[i][0];
            int v = B[i][1];

            A[index] += v;
        }

        for (int i = 1; i < A.length; i++) {
            A[i] += A[i - 1];
        }
        System.out.println(java.util.Arrays.toString(A));

        // TC: O(n + m), SC: O(n)
    }
}