class MoveZeros {
    public static void main(String[] args) {
        int[] A = { 0, 12, 0, 4, -1, 1, 0, 3, 12 };
        System.out.println(java.util.Arrays.toString(A));
        solve(A);
        System.out.println(java.util.Arrays.toString(A));
    }

    static int[] solve(int[] A) {

        int j = 0;

        for (int i = 0; i < A.length; i++) {
            if (A[i] != 0) {
                int temp = A[i];
                A[i] = A[j];
                A[j] = temp;
                j++;
            }
        }
        return A;
    }
}