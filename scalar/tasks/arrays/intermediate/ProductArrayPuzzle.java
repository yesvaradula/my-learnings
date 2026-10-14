public class ProductArrayPuzzle {
    public static void main(String[] args) {
        int[] A = { 1, 2, 3, 4, 5 };
        int[] R = productArrayLR(A);
        System.out.println(java.util.Arrays.toString(R));
    }

    static int[] productArrayLR(int[] A) {

        // first lets do left[i];
        int[] ans = new int[A.length];
        ans[0] = 1;
        for (int i = 1; i < A.length; i++) {
            ans[i] = i == 0 ? 1 : ans[i - 1] * A[i - 1];
        }

        int right = 1;
        for (int i = A.length - 1; i >= 0; i--) {
            ans[i] *= right;
            right *= A[i];
        }
        return ans;
    }

    // TC: O(n), SC: O(1)
    static int[] productArray(int[] A) {

        int product = 1;

        for (int a : A) {
            product *= a;
        }

        for (int i = 0; i < A.length; i++) {
            A[i] = A[i] > 0 ? (int) (product / A[i]) : 0;
        }

        return A;
    }
}
