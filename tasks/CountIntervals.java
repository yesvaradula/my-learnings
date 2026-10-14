public class CountIntervals {
    public static void main(String[] args) {

        int[][][] tests = {
                {{1,1,2}, {2,2,3}, {3,2,4}},                       // 1. sample
                {{1,5,10}},                                        // 2. single interval
                {{1,1,3}, {1,3,5}},                                // 3. [1,3] & [3,5]: touch, both closed
                {{2,1,3}, {1,3,5}},                                // 4. [1,3) & [3,5]: left one open at 3
                {{1,1,3}, {3,3,5}},                                // 5. [1,3] & (3,5]: right one open at 3
                {{3,1,3}, {2,3,5}},                                // 6. (1,3] & [3,5): both include 3
                {{2,1,3}, {3,3,5}},                                // 7. [1,3) & (3,5]: both exclude 3
                {{4,1,2}, {4,2,3}},                                // 8. (1,2) & (2,3): open, gap of 1
                {{4,1,2}, {4,1,2}},                                // 9. identical open intervals
                {{4,1,3}, {4,2,4}},                                // 10. (1,3) & (2,4): share 2.5
                {{1,1,10}, {4,3,4}},                               // 11. nested
                {{1,1,2}, {1,5,6}, {1,9,10}},                      // 12. nothing overlaps
                {{1,1,10}, {2,2,9}, {3,3,8}, {4,4,7}},             // 13. every pair overlaps
                {{1,1,1000000000}, {4,999999999,1000000000}},      // 14. huge values
                {{1,1,3}, {2,3,5}, {4,5,7}, {1,7,8}}               // 15. chain of touching ends
        };

        int[] expected = {2, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 6, 1, 1};

        // for(int i = 0; i < tests.length; i++) {
        //     System.out.print(calculateAns(tests[i]) + ", ");    
        // }

        for (int t = 0; t < tests.length; t++) {
            int got = calculateAns(tests[t]);
            boolean ok = got == expected[t];          // hint: it's an int now, not an array
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                + "  got=" + got + "  expected=" + expected[t]);
        }

        
    }

    static int calculateAns(int[][] A) {
        
        // TC: O(N*N);
        // SC: o(n);
        int count = 0;
        double[][] intervals = new double[A.length][2];

        for(int i = 0; i < A.length; i++) {
            intervals[i] = getIntervals(A[i][0], A[i][1], A[i][2]);
        }

        for(int i = 0; i < intervals.length; i++ ) {
            for ( int j = i + 1; j < intervals.length; j++) {
                if( isOverlap(intervals[i], intervals[j])) {
                    count++;
                }
            }
        }

        return count;
    }

    static boolean isOverlap(double[] i1, double[] i2) {
        return Math.max(i1[0], i2[0]) <= Math.min(i1[1], i2[1]);
    }

    static double[] getIntervals(int type, double l, double r) {
        switch(type) {
            case 1:
                return new double[]{l,r};
            case 2:
                return new double[]{l, r - 0.5};
            case 3:
                return new double[]{l + 0.5,r};
            default:
                return new double[]{l + 0.5, r - 0.5};
        }
    }
}
