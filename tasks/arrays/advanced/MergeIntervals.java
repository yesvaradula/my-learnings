public class MergeIntervals {
    public static void main(String args[]) {
        int[][] A = { { 1, 3 }, { 6, 9 } };
        int[] B = { 2, 5 };

        int[][] C = mergeIntervals(A, B);
    }

    static int[][] mergeIntervals(int[][] A, int[] B) {

        int[][] T = new int[1][2];
        for (int i = 0; i < A.length; i++) {
            // int start = A[i][0];
            // int end = A[i][1];

            T[i][0] = Math.min(A[i][0], B[0]);
            T[i][1] = Math.max(A[i][1], B[1]);

        }

        System.out.println(A);

        return A;
    }

}
