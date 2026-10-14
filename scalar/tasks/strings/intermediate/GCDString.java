public class GCDString {
    /*
     * 1071. Greatest Common Divisor of Strings
     * 
     * For two strings s and t, we say "t divides s" if and only if s = t + t + t +
     * ... + t + t (i.e., t is concatenated with itself one or more times).
     * 
     * Given two strings str1 and str2, return the largest string x such that x
     * divides both str1 and str2.
     * 
     * 
     */
    public static void main(String[] args) {
        String[][] tests = {
                { "ABCABC", "ABC" }, // 1. sample: str2 divides str1
                { "ABABAB", "ABAB" }, // 2. sample: str2 divides str1
                { "LEET", "CODE" }, // 3. sample: no common divisor
                { "ABCDEF", "ABC" }, // 4. str2 does not divide str1
                { "AAAAAA", "AAA" }, // 5. str2 divides str1
                { "XYZXYZXYZ", "XYZXYZ" }, // 6. str2 divides str1
                { "A", "A" }, // 7. both strings are the same
                { "A", "B" }, // 8. different single characters
                { "", "" } // 9. both strings are empty
        };
        String[] expected = { "ABC", "AB", "", "", "A", "XYZ", "A", "", "" };

        for (int t = 0; t < tests.length; t++) {
            String got = gcdOfStrings(tests[t][0], tests[t][1]);
            boolean ok = got.equals(expected[t]);
            System.out.println("Test " + (t + 1) + ": " + (ok ? "PASS" : "FAIL")
                    + "  got=" + got + "  expected=" + expected[t]);
        }
    }

    static String gcdOfStrings(String s1, String s2) {

        // TC: O(n + m)
        // SC: O(1)

        if (!(s1 + s2).equals(s2 + s1)) {
            return "";
        }

        int gcdLength = gcd(s1.length(), s2.length());
        return s1.substring(0, gcdLength);
    }

    static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
