public class EvenIndRangeSum {
    public static void main(String[] args) {
        int[] A = { 5, 3, 8, 6, 2, 7, 4, 9 };
        System.out.println("The Original Array: " + java.util.Arrays.toString(A));
        int[] prefixOfA = createEvenPrefixSum(A);
        System.out.println("The Even Prefix Sum Array: " + java.util.Arrays.toString(prefixOfA));
        int r = evenRangeSum(prefixOfA, -2, 69);
        System.out.println("The Even Range Sum : " + r);
    }

    static int[] createEvenPrefixSum(int[] A) {
        int[] prefix = new int[A.length];

        if (A.length == 0) {
            return new int[0];
        }

        // As this is even, we make 0 as it is.
        prefix[0] = A[0];
        for (int i = 1; i < A.length; i++) {
            if (i % 2 == 0) {
                prefix[i] = prefix[i - 2] + A[i];
            } else {
                prefix[i] = prefix[i - 1];
            }
        }
        return prefix;
    }

    static int evenRangeSum(int[] A, int left, int right) {

        if (left > right || left < 0 || right >= A.length) {
            return 0;
        }

        if (left == 0) {
            return A[right];
        }
        return A[right] - A[left - 1];
    }
}
