/*

Problem Description

You are given an array A of N integers.
Return a 2D array consisting of all the subarrays of the array

Note : The order of the subarrays in the resulting 2D array does not matter.


Problem Constraints

1 <= N <= 100
1 <= A[i] <= 105


Input Format

First argument A is an array of integers.


Output Format

Return a 2D array of integers in any order.


Example Input

Input 1:
A = [1, 2, 3]
Output: [[1], [1, 2], [1, 2, 3], [2], [2, 3], [3]]


Input 2:
A = [5, 2, 1, 4]
Output 2:
[[1 ], [1 4 ], [2 ], [2 1 ], [2 1 4 ], [4 ], [5 ], [5 2 ], [5 2 1 ], [5 2 1 4 ] ]

*/
public class CreateSubArrays {
    public static void main(String[] args) {
        int[] A = { 5, 2, 1, 4 };
        int[][] result = createSubArrayFromArray(A);
        System.out.println("Subarrays:");
        for (int[] subarray : result) {
            System.out.println(java.util.Arrays.toString(subarray));
        }
    }

    public static int[][] createSubArrayFromArray(int[] A) {
        int Left = 0;
        int Right = 0;
        int[][] result = new int[A.length * (A.length + 1) / 2][];

        while (Left < A.length) {
            Right = Left;
            while (Right < A.length) {
                int[] subarray = new int[Right - Left + 1];
                for (int i = Left; i <= Right; i++) {
                    subarray[i - Left] = A[i];
                }
                result[Left * A.length + Right - (Left * (Left + 1)) / 2] = subarray;
                Right++;
            }
            Left++;
        }

        return result;
    }
}
