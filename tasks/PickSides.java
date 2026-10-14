public class PickSides {
    public static void main(String[] args) {
        int[] A = { 5, -2, 3, 1, 2 };
        int B = 3;
        int R = pickSides(A, B);
        System.out.println(R);
    }

    // TC: O(B), SC: O(1)
    static int pickSides(int[] A, int B) {
        int maxSum = 0;

        for (int i = 0; i < B; i++) {
            maxSum += A[i];
        }

        int currentSum = maxSum;
        int left = B - 1;
        int right = A.length - 1;

        while (left >= 0 && right >= A.length - B) {
            currentSum = currentSum - A[left] + A[right];
            maxSum = Math.max(maxSum, currentSum);
            left--;
            right--;
        }

        return maxSum;
    }
}

// claude --resume 3766fa6a-bbcc-4429-84c6-bb47db28896f