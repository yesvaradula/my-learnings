public class MergeWords {
    public static void main(String[] args) {
        String[][] tests = {
                { "abc", "pqr", "apbqcr" },
                { "ab", "pqrs", "apbqrs" },
                { "abcd", "pq", "apbqcd" },
                { "", "xyz", "xyz" },
                { "", "", "" },
                { "a", "b", "ab" }
        };
        for (String[] t : tests) {
            String got = mergeWords(t[0], t[1]);
            System.out.println((got.equals(t[2]) ? "PASS " : "FAIL ") + t[0] + "," + t[1] + " -> " + got);
        }
    }

    // TC: O(n + m); SC: O(n + m)
    static String mergeWords(String w1, String w2) {
        StringBuilder sb = new StringBuilder();
        int n = w1.length();
        int m = w2.length();
        int i = 0;
        int j = 0;

        while (i < n && j < m) {
            sb.append(w1.charAt(i++)).append(w2.charAt(j++));
        }

        while (i < n) {
            sb.append(w1.charAt(i++));
        }

        while (j < m) {
            sb.append(w2.charAt(j++));
        }

        return sb.toString();
    }
}
