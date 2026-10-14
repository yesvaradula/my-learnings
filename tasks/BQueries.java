public class BQueries {

    public static void main(String args[]) {
        int[][] B = {
                { 1, 4, 3 },
                { 0, 5, -1 },
                { 2, 2, 4 },
                { 4, 6, 3 }
        };

        int[] A = new int[8];

        System.out.println(java.util.Arrays.toString(A));

        for (int i = 0; i < B.length; i++) {
            int l = B[i][0];
            int r = B[i][1];
            int v = B[i][2];

            A[l] += v;
            if (r + 1 < A.length) {
                A[r + 1] -= v;
            }
        }

        for (int i = 1; i < A.length; i++) {
            A[i] += A[i - 1];
        }

        System.out.println(java.util.Arrays.toString(A));
        // TC: O(n + m), SC: O(n)
    }
}