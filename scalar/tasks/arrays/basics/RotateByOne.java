class RotateByOne {
    public static void main(String[] args) {
        int[] A = { 5, 4, 3, 6, 8, 9, 0, 1 };
        System.out.println(java.util.Arrays.toString(A));
        solve(A);
        System.out.println(java.util.Arrays.toString(A));
    }

    static int[] solve(int[] A) {

        int temp = A[0];

        for (int i = 1; i < A.length; i++) {
            A[i - 1] = A[i];
        }

        A[A.length - 1] = temp;

        return A;
    }
}