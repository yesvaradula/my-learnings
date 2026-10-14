/* 

Given an array A of length N, return the subarray from B to C.
Problem Constraints

1 <= N <= 105

1 <= A[i] <= 109

0 <= B <= C < N



Input Format

The first argument A is an array of integers

The remaining argument B and C are integers.



Output Format

Return a subarray


Example Input

Input 1:
A = [4, 3, 2, 6]
B = 1
C = 3
Input 2:

A = [4, 2, 2]
B = 0
C = 1


Example Output

Output 1:
[3, 2, 6]
Output 2:

[4, 2]


*/

public class ReturnSubArray {

    public static void main(String[] args) {
        int[] A = { 4, 2, 2 };
        int B = 1; // 0
        int C = 3; // 3
        int[] result = solve(A, B, C);
        System.out.println(java.util.Arrays.toString(result)); // Output: [3, 2, 6]
    }

    public static int[] solve(int[] A, int B, int C) {
        int[] subArray = new int[C - B + 1];
        for (int i = B; i <= C; i++) {
            subArray[i - B] = A[i];
        }
        return subArray;
    }
}

// TC: O(C - B + 1) - We are iterating from index B to C to create the subarray,
// which takes linear time proportional to the size of the subarray.

// SC: O(C - B + 1) - We are creating a new array to store the subarray, which
// takes space proportional to the size of the subarray.