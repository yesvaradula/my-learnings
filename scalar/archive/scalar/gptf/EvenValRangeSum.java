public class EvenValRangeSum {
    public static void main(String[] args) {
        int[] nums = {5, 3, 8, 6, 2, 7, 4, 9};
        System.out.println("The Original Array is " + java.util.Arrays.toString(nums));
        int[] prefixOfA = createEvenValuePrefix(nums);
        System.out.println("The Prefix even values of A " + java.util.Arrays.toString(prefixOfA));
        int r = calSumEvenValues(prefixOfA, 0, 1);
        System.out.println("Result is : " + r);

    }

    static int[] createEvenValuePrefix(int[] A) {
        int[] B = new int[A.length];
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            if( A[i] % 2 == 0 ) {
                sum += A[i];
            }
            B[i] = sum;
        }

        return B;
    }

    static int calSumEvenValues(int[] A, int left, int right) {
        if (left > right || left < 0 || right >= A.length) {
            return 0;
        }
        if( left == 0) {
            return A[right];
        }
        return A[right] - A[left-1];
    }
}
