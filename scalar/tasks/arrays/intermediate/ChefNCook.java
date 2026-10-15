public class ChefNCook {
    /*
     * In a parallel universe, there exists a kingdom renowned for its unique
     * culinary traditions. In this kingdom, a famous chef is celebrated for her
     * delectable dishes. One day, the chef decided to create a new dish using a
     * sequence of ingredients, each with a distinct weight. The chef wants to
     * select a subarray of these ingredients where the weights are in strictly
     * increasing order.
     * 
     * The chef's goal is to determine the maximum possible sum of the weights of
     * the strictly increasing subarray. Can you assist the chef by writing a
     * function that returns the maximum possible sum of an strictly increasing
     * subarray from the given array of ingredient weights?
     * 
     * The array of ingredient weights is represented by the array A.
     * 
     * 
     * Problem Constraints
     * 1 <= |A| <= 105
     * 1 <= A[i] <= 109
     */

    public static void main(String[] args) {
        int[] A = { 1, 2, 3, 4, 5 };
        long ans = solve(A);
        System.out.println(ans);
    }

    static long solve(int[] A) {
        // tc: O(n)
        // sc: O(1)
        long max = A[0];
        long current = A[0];

        for (int i = 1; i < A.length; i++) {
            if (A[i] > A[i - 1]) {
                current += A[i];
            } else {
                current = A[i];
            }
            max = Math.max(max, current);
        }

        return max;
    }
}
