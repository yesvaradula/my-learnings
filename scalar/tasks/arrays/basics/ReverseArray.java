class ReverseArray {
    public static void main(String[] args) {
        int[] A = { 1, 2, 3, 4, 5 };
        System.out.println(java.util.Arrays.toString(A));
        solve(A);
        System.out.println(java.util.Arrays.toString(A));
    }

    static int[] solve(int[] A) {

        int i = 0;
        int j = A.length - 1;

        while (i < j) {
            int temp = A[i];
            A[i] = A[j];
            A[j] = temp;
            i++;
            j--;
        }

        return A;
    }
}