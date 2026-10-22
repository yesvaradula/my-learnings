// Greatest Common Divisor
// Given two non-negative integers A and B, return gcd(A, B):
// the largest number that divides both. gcd(A, 0) = A.
// Constraints (Scaler style): 0 <= A, B <= 10^9, and A + B > 0.
//
// A = 4,  B = 6   ->  2
// A = 6,  B = 7   ->  1
// A = 12, B = 0   -> 12

public class GCD {
    public static void main(String[] args) {
        System.out.println(gcd(4, 6)); // 2
        System.out.println(gcd(6, 7)); // 1
        System.out.println(gcd(12, 0)); // 12

        int[][] tests = {
                { 4, 6, 2 },                    // sample
                { 6, 7, 1 },                    // sample, coprime
                { 12, 0, 12 },                  // sample, base case
                { 0, 5, 5 },                    // A = 0: first step swaps
                { 7, 7, 7 },                    // equal
                { 48, 18, 6 },                  // hand trace
                { 18, 48, 6 },                  // A < B
                { 1000, 999, 1 },               // consecutive numbers are coprime
                { 21, 13, 1 },                  // Fibonacci pair: worst case
                { 1000000000, 0, 1000000000 },  // max with 0
                { 1000000000, 999999999, 1 },   // max, coprime
                { 1000000000, 500000000, 500000000 },
                { 1, 1000000000, 1 },
                { 701408733, 433494437, 1 },    // Fibonacci F44, F43: ~43 steps
                { 999999998, 999999996, 2 }
        };
        int passed = 0;
        for (int t = 0; t < tests.length; t++) {
            int got = gcd(tests[t][0], tests[t][1]);
            boolean ok = got == tests[t][2];
            if (ok)
                passed++;
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + "  gcd(" + tests[t][0] + ", " + tests[t][1] + ") got=" + got
                    + "  expected=" + tests[t][2]);
        }
        System.out.println(passed + "/" + tests.length + " passed");

        java.util.Random rnd = new java.util.Random(53);
        int bad = 0;
        for (int i = 0; i < 2000; i++) {
            int a = rnd.nextInt(1000000001), b = rnd.nextInt(1000000001);
            if (a + b == 0)
                continue;
            int want = java.math.BigInteger.valueOf(a).gcd(java.math.BigInteger.valueOf(b)).intValue();
            if (gcd(a, b) != want)
                bad++;
        }
        System.out.println("Random vs BigInteger.gcd: " + (bad == 0 ? "2000/2000 PASS" : bad + " FAIL"));
    }

    // TC: O(log(min(A, B))), SC: O(1)
    static int gcd(int A, int B) {
        while (B != 0) {
            int r = A % B;
            A = B;
            B = r;
        }
        return A;
    }
}
