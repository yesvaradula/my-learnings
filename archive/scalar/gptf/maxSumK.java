public class maxSumK {

    static void main() {
        int[] nums = {2, 1, 5, 1, 3, 2};
        int k = 3;

        int sum = bruteforce(nums, k);
        System.out.println("sum = " + sum);
    }

    static int bruteforce(int[] A, int target) {
        int sum = 0;
        int maxSum = 0;
        for (int i = 0; i <= A.length - target; i++) {
            sum = 0;
            for (int j = i; j < i + target; j++) {
                sum += A[j];
            }
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }
    
}
