class Beggers {
    public static void main(String[] args) {
        /*
         * There are A beggars sitting in a row outside a temple. Each beggar initially
         * has an empty pot. When the devotees come to the temple, they donate some
         * amount of coins to these beggars. Each devotee gives a fixed amount of
         * coin(according to their faith and ability) to some K beggars sitting next to
         * each other.
         * 
         * Given the amount P donated by each devotee to the beggars ranging from L to R
         * index, where 1 <= L <= R <= A, find out the final amount of money in each
         * beggar's pot at the end of the day, provided they don't fill their pots by
         * any other means.
         * For ith devotee
         * B[i][0] = L, B[i][1] = R, B[i][2] = P, given by the 2D array B
         * 
         * 
         * Input 1:-
         * A = 5
         * B = [[1, 2, 10], [2, 3, 20], [2, 5, 25]]
         * 
         * output:-
         * 10 55 45 25 25
         */
        int[][] B = {
                { 1, 2, 10 }, { 2, 3, 20 }, { 2, 5, 25 }
        };

        int A = 5;
        int[] ans = solution(B, A);
        System.out.println(java.util.Arrays.toString(ans));
    }

    static int[] solution(int[][] B, int A) {
        // A is the bichagaalu.

        int[] ans = new int[A];

        for (int[] donation : B) {
            int l = donation[0] - 1; // converting to 0 based from 1-based.
            int r = donation[1] - 1;
            int v = donation[2];

            ans[l] += v;
            if (r + 1 < A)
                ans[r + 1] -= v;
        }

        // now calcuate prefix sum.;
        for (int i = 1; i < A; i++) {
            ans[i] += ans[i - 1];
        }

        return ans;
    }
}