// Sum of Digits (recursive)
// Given a non-negative integer A, return the sum of its digits using recursion (no loops).
//
// A = 46   ->  10   (4 + 6)
// A = 11   ->   2
// A = 0    ->   0

import java.util.Random;

public class SumOfDigits {
    public static void main(String[] args) {
        int[] tests = {
                46,             // 1. sample
                11,             // 2. sample
                0,              // 3. zero
                5,              // 4. single digit
                9,              // 5. largest single digit (base case boundary)
                10,             // 6. smallest two-digit number
                99,             // 7. all nines
                100,            // 8. zeros in the middle and end
                1000000000,     // 9. 10^9: one 1 and nine 0s
                1999999999,     // 10. largest digit sum of an int: 82
                2147483647,     // 11. Integer.MAX_VALUE
        };
        int[] expected = { 10, 2, 0, 5, 9, 1, 18, 1, 1, 82, 46 };

        int passed = 0;
        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t]);
            boolean ok = got == expected[t];
            if (ok)
                passed++;
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + " (A = " + tests[t] + ", got " + got + ", expected " + expected[t] + ")");
        }
        System.out.println(passed + "/" + tests.length + " passed");

        // Cross-check against the loop version on random numbers
        Random rnd = new Random(42);
        int mismatches = 0;
        for (int r = 0; r < 2000; r++) {
            int A = rnd.nextInt(Integer.MAX_VALUE);
            if (solve(A) != iterative(A))
                mismatches++;
        }
        System.out.println("Random cross-check: " + (mismatches == 0 ? "PASS" : mismatches + " mismatches"));
    }

    // Loop version, for the cross-check. TC O(log10 A), SC O(1).
    static int iterative(int A) {
        int sum = 0;
        while (A > 0) {
            sum += A % 10;
            A /= 10;
        }
        return sum;
    }

    // TC: O(log10 A), one call per digit. SC: O(log10 A) call stack (at most 10 frames for an int).
    static int solve(int A) {
        if (A <= 9) {
            return A;
        }

        return A % 10 + solve(A / 10);
    }
}
