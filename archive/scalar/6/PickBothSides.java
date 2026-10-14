/**
 * You are given an integer array A of size N.
 * 
 * You have to perform B operations. In one operation, you can remove either the
 * leftmost or the rightmost element of the array A.
 * 
 * Find and return the maximum possible sum of the B elements that were removed
 * after the B operations.
 * 
 * NOTE: Suppose B = 3, and array A contains 10 elements, then you can:
 * 
 * Remove 3 elements from front and 0 elements from the back, OR
 * Remove 2 elements from front and 1 element from the back, OR
 * Remove 1 element from front and 2 elements from the back, OR
 * Remove 0 elements from front and 3 elements from the back.
 * 
 * Examples
 * Input 1: A = [5, -2, 3 , 1, 2], B = 3 :: O/P : 8
 * Input 2: A = [ 2, 3, -1, 4, 2, 1 ], B = 4 :: O/P: 9
 */

public class PickBothSides {
    public static void main(String[] args) {
        int[] A = { 2, 3, -1, 4, 2, 1 };
        int B = 4;
        int results = findPick(A, B);
        System.out.println(results);
    }

    static int findPick(int[] A, int B) {

        int highValue = 0;
        int currentSum = 0;

        for (int i = 0; i < B; i++) {
            currentSum += A[i];
        }

        highValue = currentSum;

        int left = B - 1;
        int right = A.length - 1;

        while (left >= 0) {
            currentSum = currentSum - A[left];
            currentSum = currentSum + A[right];
            highValue = Math.max(highValue, currentSum);
            left--;
            right--;
        }

        return highValue;
    }
}
