/**
 * You are given an integer array C of size A. Now you need to find a subarray
 * (contiguous elements) so that the sum of contiguous elements is maximum.
 * But the sum must not exceed B.
 * 
 * 
 * Problem Constraints
 * 
 * 1 <= A <= 103
 * 1 <= B <= 109
 * 1 <= C[i] <= 106
 * 
 * 
 * Input Format
 * 
 * The first argument is the integer A.
 * The second argument is the integer B.
 * The third argument is the integer array C.
 * 
 * 
 * Output Format
 * 
 * Return a single integer which denotes the maximum sum.
 * 
 * 
 * A = 5
 * B = 12
 * C = [2, 1, 3, 4, 5]
 * Output 1: 12
 * 
 * 
 * A = 3
 * B = 1
 * C = [2, 2, 2]
 * 
 * Output 2: 0
 * 
 * SumSubArraysLimited
 */

public class SumSubArraysLimited {
    static void main() {

        int[] C = {1,2 ,4 ,4, 5,5,5,7,5};
        int B = 14;

        int A = C.length; // 5

        long results = findSum(A, B, C);
        System.out.println("The max length is : " + results);
    }

    static int findSum(int A, int B, int[] C) {
        int maxSum = 0; // 12
        int currentSum = 0; // 13
        int start = 0;

        for (int end = 0; end < A; end++) {
            currentSum += C[end];

            while (currentSum > B && start <= end) {
                currentSum -= C[start];
                start++;
            }

            
            if (currentSum <= B && currentSum > maxSum) {
                maxSum = currentSum;
            }
        }

        return maxSum;
    }
}
