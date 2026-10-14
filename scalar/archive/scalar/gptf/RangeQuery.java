class RangeQuery {

    public static void main(String[] args) {
        int[] A = { 2, 4, 6, 8, 10 };
        int[] prefixA = calPrefixSum(A);

        System.out.println(rangeSum(prefixA, 0, 4));
    }

    static int[] calPrefixSum(int[] A) {
        int[] prefix = new int[A.length];

        prefix[0] = A[0];
        for (int i = 1; i < prefix.length; i++) {
            prefix[i] = prefix[i - 1] + A[i];
        }

        return prefix;
    }

    static int rangeSum(int[] prefixA, int left, int right) {
        if (left == 0) {
            return prefixA[right];
        }
        return prefixA[right] - prefixA[left - 1];
    }
}
