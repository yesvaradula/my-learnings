// Check Pair Sum
// Given an int array A and an integer K, return 1 if there exist two
// DIFFERENT indices i != j with A[i] + A[j] == K, else return 0.
//
// A = [3, 5, 1, 2, 1, 2], K = 7  ->  1   (5 + 2)
// A = [1, 2, 3, 4],       K = 8  ->  0   (4 + 4 would need the same index twice)

import java.util.HashSet;
import java.util.Random;

public class PairSum {
    public static void main(String[] args) {
        int[][] tests = {
                { 3, 5, 1, 2, 1, 2 }, // 1. sample: 5 + 2
                { 1, 2, 3, 4 }, // 2. sample: 4 + 4 needs the same index twice
                { 4, 4 }, // 3. two copies of K/2
                { 4 }, // 4. single element equal to K/2
                {}, // 5. empty
                { 4, 1, 4 }, // 6. two copies of K/2, not adjacent
                { -3, 10, 2 }, // 7. negative + positive
                { -5, -2, 1 }, // 8. two negatives
                { 0, 0 }, // 9. zeros, K = 0
                { 1, 2, 3, 9 }, // 10. no pair at all
                { 1, 2, 3, 9 }, // 11. pair at both ends: 1 + 9
                { 1000000000, 1000000000 }, // 12. large values: 2 * 10^9 still fits in int
                { 7, 7, 7, 7 }, // 13. all equal, K = 14
                { 7, 7, 7, 7 }, // 14. all equal, K = 7
                { 2000000000, 294967296 }, // 15. K - A[i] overflows int and wraps onto 2000000000
        };
        int[] Ks = { 7, 8, 8, 8, 5, 8, 7, -7, 0, 20, 10, 2000000000, 14, 7, -2000000000 };
        int[] expected = { 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0 };

        int passed = 0;
        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t], Ks[t]);
            boolean ok = got == expected[t];
            if (ok)
                passed++;
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + " (got " + got + ", expected " + expected[t] + ")");
        }
        System.out.println(passed + "/" + tests.length + " passed");

        // Cross-check against the O(N^2) brute force on random arrays
        Random rnd = new Random(42);
        int mismatches = 0;
        for (int r = 0; r < 2000; r++) {
            int[] A = new int[rnd.nextInt(10)];
            for (int i = 0; i < A.length; i++)
                A[i] = rnd.nextInt(21) - 10;
            int K = rnd.nextInt(41) - 20;
            if (solve(A, K) != brute(A, K))
                mismatches++;
        }
        System.out.println("Random cross-check: " + (mismatches == 0 ? "PASS" : mismatches + " mismatches"));
    }

    // Brute force: every pair with j starting at i + 1, so i != j. TC O(N^2), SC
    // O(1).
    static int brute(int[] A, int K) {
        for (int i = 0; i < A.length; i++)
            for (int j = i + 1; j < A.length; j++)
                if ((long) A[i] + A[j] == K)
                    return 1;
        return 0;
    }

    // TC: O(n) SC: O(n)
    static int solve(int[] A, int K) {
        HashSet<Long> hs = new HashSet<>();

        for (int i = 0; i < A.length; i++) {
            long need = (long) K - A[i];
            if (hs.contains(need)) {
                return 1;
            }
            hs.add((long) A[i]);
        }

        return 0;
    }
}
