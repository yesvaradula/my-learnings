// Implement Power Function (fast power with mod)
// Given integers A, B and C, return (A^B) % C, as a value in [0, C - 1].
// Constraints (Scaler style): -10^9 <= A <= 10^9, 0 <= B <= 10^9, 1 <= C <= 10^9.
// B is too big to multiply A by itself B times: use recursion that halves B.
//
// A = 2,  B = 3, C = 3   ->  2   (8 % 3)
// A = 3,  B = 3, C = 1   ->  0   (anything % 1)
// A = -1, B = 1, C = 20  -> 19   (-1 % 20 must become 19, not -1)

import java.math.BigInteger;
import java.util.Random;

public class PowMod {
    public static void main(String[] args) {
        int[][] tests = {
                { 2, 3, 3 },                       // 1. sample: 8 % 3
                { 3, 3, 1 },                       // 2. sample: anything % 1 = 0
                { -1, 1, 20 },                     // 3. sample: negative A
                { 3, 5, 7 },                       // 4. hand trace: 243 % 7
                { 5, 0, 7 },                       // 5. B = 0 -> 1
                { 5, 0, 1 },                       // 6. B = 0 and C = 1 -> 0, not 1
                { 0, 0, 5 },                       // 7. 0^0 taken as 1
                { 0, 5, 7 },                       // 8. zero base
                { 7, 3, 7 },                       // 9. A is a multiple of C
                { -2, 3, 5 },                      // 10. negative A, odd power: -8 % 5 = 2
                { -2, 2, 5 },                      // 11. negative A, even power: 4
                { 123456789, 2, 1000000000 },      // 12. half * half overflows int
                { 2, 1000000000, 1000000007 },     // 13. B = 10^9: ~30 calls
                { -1000000000, 3, 999999999 },     // 14. big negative A
                { 1000000000, 1000000000, 999999937 }, // 15. everything near the limits
        };
        int[] expected = new int[tests.length];
        for (int t = 0; t < tests.length; t++)
            expected[t] = viaBigInteger(tests[t][0], tests[t][1], tests[t][2]);

        int passed = 0;
        for (int t = 0; t < tests.length; t++) {
            int got = solve(tests[t][0], tests[t][1], tests[t][2]);
            boolean ok = got == expected[t];
            if (ok)
                passed++;
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + " (got " + got + ", expected " + expected[t] + ")");
        }
        System.out.println(passed + "/" + tests.length + " passed");

        // Cross-check against BigInteger.modPow on random inputs across the full constraint range
        Random rnd = new Random(42);
        int mismatches = 0;
        for (int r = 0; r < 2000; r++) {
            int A = rnd.nextInt(2000000001) - 1000000000;
            int B = rnd.nextInt(1000000001);
            int C = rnd.nextInt(1000000000) + 1;
            if (solve(A, B, C) != viaBigInteger(A, B, C))
                mismatches++;
        }
        System.out.println("Random cross-check: " + (mismatches == 0 ? "PASS" : mismatches + " mismatches"));
    }

    // Reference answer from Java's arbitrary-precision modPow (always returns a value in [0, C-1]).
    static int viaBigInteger(int A, int B, int C) {
        return BigInteger.valueOf(A).modPow(BigInteger.valueOf(B), BigInteger.valueOf(C)).intValue();
    }

    // TC: O(log n) SC: O(log n)
    static int solve(int A, int B, int C) {
        long a = ((A % C) + C) % C;
        return (int) power(a, B, C);
    }

    static long power(long A, int B, int C) {
        if (B == 0) {
            return 1 % C;
        }
        A = ((A % C) + C) % C;
        long half = power(A, B / 2, C);
        long result = half * half % C;

        if (B % 2 == 1) {
            return result * A % C;
        }

        return result;
    }
}
