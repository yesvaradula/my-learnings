// First Repeating Element
// Given an int array A, return the first element that occurs more than once,
// where "first" means its FIRST occurrence has the smallest index.
// If no element repeats, return -1.
//
// A = [10, 5, 3, 4, 3, 5, 6]  ->  5
// A = [6, 10, 5, 4, 9, 120]   -> -1

import java.util.HashMap;
import java.util.Random;

public class FirstRepeating {
    public static void main(String[] args) {
        int[][] tests = {
                { 10, 5, 3, 4, 3, 5, 6 },          // 1. sample: 3 is seen twice first, but 5 appears first
                { 6, 10, 5, 4, 9, 120 },           // 2. sample: no repeats
                {},                                // 3. empty
                { 7 },                             // 4. single element
                { 1, 1 },                          // 5. two equal
                { 4, 4, 4, 4 },                    // 6. all the same
                { 1, 2, 2, 5 },                    // 7. adjacent pair in the middle
                { 1, 2, 3, 3 },                    // 8. pair at the very end
                { 5, 1, 2, 5 },                    // 9. pair at both ends
                { 7, 1, 2, 7, 9, 3, 4 },           // 10. first element repeats
                { 1, 2, 3, 2, 1 },                 // 11. 2 closes first, but 1 appears first
                { -3, 4, -3, 4 },                  // 12. negatives
                { 1000000000, 1, 1000000000 },     // 13. large values (an int[] counter would crash)
                { 1000, 2000, 1000 },              // 14. outside the Integer cache (== would break)
        };
        int[] expected = { 5, -1, -1, -1, 1, 4, 2, 3, 5, 7, 1, -3, 1000000000, 1000 };

        int passed = 0;
        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t]);
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
            int[] A = new int[rnd.nextInt(12)];
            for (int i = 0; i < A.length; i++)
                A[i] = rnd.nextInt(10) + 1;
            if (solve(A) != brute(A))
                mismatches++;
        }
        System.out.println("Random cross-check: " + (mismatches == 0 ? "PASS" : mismatches + " mismatches"));
    }

    // Brute force: the smallest i that has an equal element somewhere after it. TC O(N^2), SC O(1).
    static int brute(int[] A) {
        for (int i = 0; i < A.length; i++)
            for (int j = i + 1; j < A.length; j++)
                if (A[i] == A[j])
                    return A[i];
        return -1;
    }

    // TC: O(N), SC: O(N)
    static int solve(int[] A) {

        HashMap<Integer, Integer> hm = new HashMap<>();

        for (int i = 0; i < A.length; i++) {
            hm.put(A[i], hm.getOrDefault(A[i], 0) + 1);
        }

        for (int i = 0; i < A.length; i++) {
            if (hm.get(A[i]) > 1)
                return A[i];
        }

        return -1;
    }
}
