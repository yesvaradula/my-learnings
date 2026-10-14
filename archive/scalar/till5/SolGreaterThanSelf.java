public class SolGreaterThanSelf {
    public static void main(String[] args) {
        int[] A = { 7, 2, 10, 5, 10 }; // 1, 3, 4, 0, 2
        int result = OptimizedGreaterThanSelf(A);
        System.out.println("Result: " + result);
    }

    public static int OptimizedGreaterThanSelf(int[] A) {
        if (A == null || A.length == 0)
            return 0;

        int maxVal = Integer.MIN_VALUE;
        int maxCount = 0;

        // Single pass to find max value and its frequency
        for (int num : A) {
            if (num > maxVal) {
                maxVal = num;
                maxCount = 1; // Reset count for the new maximum
            } else if (num == maxVal) {
                maxCount++; // Increment count if we see the max again
            }
        }

        // Total elements minus the ones that have nothing greater than themselves
        return A.length - maxCount;
    }

    public static int greaterThanSelf(int[] A) {
        int count = 0;

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                if (A[j] > A[i]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
