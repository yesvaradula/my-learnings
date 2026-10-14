class RotateByD {
    public static void main(String[] args) {
        int[] A = { 1, 2, 3, 4, 5 };
        int D = 2;
        System.out.println(java.util.Arrays.toString(A));
        solve(A, D);
        System.out.println(java.util.Arrays.toString(A));
    }

    static int[] solve(int[] A, int D) {

        if (A.length == D)
            return A;

        D = D % A.length;

        if (D == 0)
            return A;

        int i = 0;
        int j = A.length - 1;
        int k = D - 1;

        while (i < k) {
            int temp = A[i];
            A[i] = A[k];
            A[k] = temp;
            i++;
            k--;
        }

        i = D;

        while (i < j) {
            int temp = A[i];
            A[i] = A[j];
            A[j] = temp;
            i++;
            j--;
        }

        int z = 0;
        int b = A.length - 1;
        while (z < b) {
            int temp = A[z];
            A[z] = A[b];
            A[b] = temp;
            z++;
            b--;
        }

        return A;
    }
}