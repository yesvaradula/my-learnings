class SecondLargestElement {
    public static void main(String[] args) {
        int[] A = { 6, 6, 6, 6 };
        // int[] A = { 10, 8, 8, 2 };

        System.out.println(java.util.Arrays.toString(A));

        int result = solve(A);
        System.out.println(result);
    }

    static int solve(int[] A) {

        if (A.length == 0)
            throw new IllegalArgumentException("Array must have at least 2 elements");

        if (A.length == 1)
            return A[0];

        int max = Integer.MIN_VALUE;
        int sMax = max;

        for (int num : A) {
            if (num > max) {
                sMax = max;
                max = num;
            } else if (num > sMax && num < max) {
                sMax = num;
            }
        }

        if (sMax == Integer.MIN_VALUE)
            throw new IllegalArgumentException("No second largest distinct element exists");

        return sMax;
    }
}