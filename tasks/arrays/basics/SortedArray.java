class SortedArray {
    public static void main(String[] args) {
        int[] A = { 1, 2, 2, 6, 3, 4 };

        boolean isSorted = solve(A);

        System.out.println(isSorted);
    }

    static boolean solve(int[] A) {

        if (A.length < 2)
            return true;

        int i = 0;
        while (i < A.length - 1) {
            if (A[i] > A[i + 1]) {
                return false;
            }
            i++;
        }
        return true;
    }
}