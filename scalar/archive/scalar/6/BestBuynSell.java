/*

Say you have an array, A, for which the ith element is the price of a given stock on day i.
If you were only permitted to complete at most one transaction 
(ie, buy one and sell one share of the stock), design an algorithm to find the maximum profit.

Return the maximum possible profit.

*/

class BestBuynSell {

    public static void main(String[] args) {
        int[] A = {}; // o/p is : 4
        int bf = bruteFroce(A);
        System.out.println("Results from Brute force method : " + bf);
        int om = findBS(A);
        System.out.println("Results from Optimized method : " + om);
    }

    static int findBS(int[] A) {
        // lets build a prefix sum.

        if (A.length == 0) {
            return 0;
        }
        int minPrice = A[0];
        int profit = 0;

        for (int i = 1; i < A.length; i++) {
            if (A[i] < minPrice)
                minPrice = A[i]; // minimum price to buy.

            // now calculate the profit ( selling - buying )
            if (A[i] - minPrice > profit)
                profit = A[i] - minPrice;
        }

        return profit;
    }

    static int bruteFroce(int[] A) {
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < A.length; i++) {
            for (int j = i; j < A.length; j++) {
                if (A[j] - A[i] > max) {
                    max = A[j] - A[i];
                }
            }
        }

        // Time Complexity: O(N*N)

        return max;
    }
}