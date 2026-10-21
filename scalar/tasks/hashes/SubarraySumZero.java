// Subarray with 0 Sum
// Given an int array A, return 1 if there is a non-empty contiguous subarray
// whose sum is 0, else return 0.
//
// A = [4, 2, -3, 1, 6]  ->  1   ([2, -3, 1])
// A = [1, 2, 3]         ->  0
// A = [-1, 1]           ->  1   (the whole array)

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class SubarraySumZero {
    public static void main(String[] args) {
        int[][] tests = {
                { 4, 2, -3, 1, 6 },                    // 1. sample: [2, -3, 1], repeated prefix 4
                { 1, 2, 3 },                           // 2. all positive, no zero sum
                { -1, 1 },                             // 3. whole array, prefix hits 0
                { 1, 2, -3, 7 },                       // 4. zero sum starting at index 0
                { 0 },                                 // 5. single zero
                { 5 },                                 // 6. single non-zero
                {},                                    // 7. empty
                { 3, -3 },                             // 8. pair cancels
                { 4, 2, -2 },                          // 9. repeat of the very first prefix
                { 5, 0, 7 },                           // 10. a zero in the middle
                { 1, -1, 1, -1 },                      // 11. many zero-sum subarrays
                { -5, -2, -9 },                        // 12. all negative, no zero sum
                { 2147483647, 2147483647, 2 },         // 13. int prefix would wrap to 0
                { -2147483648, -2147483648 },          // 14. large negatives, no zero sum
                { 1000000000, -999999999, -1 },        // 15. big values that cancel
        };
        int[] expected = { 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1 };

        int passed = 0;
        for (int t = 0; t < tests.length; t++) {
            int[] before = tests[t].clone();
            int got = solve(tests[t]);
            boolean ok = got == expected[t] && Arrays.equals(before, tests[t]);
            if (ok)
                passed++;
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + " (got " + got + ", expected " + expected[t] + ")");
        }
        System.out.println(passed + "/" + tests.length + " passed (input left unchanged)");

        // Cross-check against the O(N^2) brute force on random arrays
        Random rnd = new Random(42);
        int mismatches = 0;
        for (int r = 0; r < 2000; r++) {
            int[] A = new int[rnd.nextInt(10)];
            for (int i = 0; i < A.length; i++)
                A[i] = rnd.nextInt(11) - 5;
            if (solve(A) != brute(A))
                mismatches++;
        }
        System.out.println("Random cross-check: " + (mismatches == 0 ? "PASS" : mismatches + " mismatches"));
    }

    // Brute force: sum every subarray [i..j], extending j one step at a time. TC O(N^2), SC O(1).
    static int brute(int[] A) {
        for (int i = 0; i < A.length; i++) {
            long s = 0;
            for (int j = i; j < A.length; j++) {
                s += A[j];
                if (s == 0)
                    return 1;
            }
        }
        return 0;
    }

    // TC: O(n) SC: O(n)
    static int solve(int[] A) {

        HashSet<Long> hs = new HashSet<>();
        long sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i];
            if (sum == 0) {
                return 1;
            }

            if (hs.contains(sum)) {
                return 1;
            }

            hs.add(sum);
        }

        return 0;
    }
}
