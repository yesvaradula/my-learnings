public class AG {
    public static void main(String[] args) {
        String A = "AGAGAG";

        Long result = solve(A);
        System.out.println(result);
    }

    public static Long solve(String A) {
        long countOfA = 0;
        long totalPairs = 0;

        for (int i = 0; i < A.length(); i++) {
            if (A.charAt(i) == 'A') {
                countOfA++;
            } else if (A.charAt(i) == 'G') {
                totalPairs += countOfA;
            }
        }

        return totalPairs;

    }
}
