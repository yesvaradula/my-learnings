public class FindSingle {
    /**
     * Given an array of integers A, every element appears twice except for one.
     * Find that integer that occurs once.
     */

    public static void main(String[] args) {
        int[] A = { 1, 2, 2, 3, 3, 4, 4, 6, 5, 6, 5 };
        System.out.println(solve(A));
    }

    private static int solve(int[] A) {
        int ans = 0;

        for (int i = 0; i < A.length; i++) {
            ans = ans ^ A[i];
        }

        return ans;
    }
}
