/**
 * 
 * Given an array A, find the size of the smallest subarray such that it
 * contains at least one occurrence of the maximum value of the array
 * 
 * and at least one occurrence of the minimum value of the array.
 * 
 * A = [1, 3, 2] // 2
 * A = [2, 6, 1, 6, 9] // 3
 */

public class ClosetMinMax {

    public static void main(String[] args) {
        int[] A = { 2, 6, 1, 6, 9 };
        System.out.println("The original array is : " + java.util.Arrays.toString(A));
        int results = findCloseMinMax(A);
        System.out.println("The close min max set - size is : " + results);
    }

    static int findCloseMinMax(int[] A) {

        // lets find the min and max - indexes

        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;

        for (int i = 0; i < A.length; i++) { // O(N)
            if (A[i] < minValue) {
                minValue = A[i];
            }
            if (A[i] > maxValue) {
                maxValue = A[i];
            }
        }

        int lastMinIdx = -1;
        int lastMaxIdx = -1;
        int minLength = Integer.MAX_VALUE;
        int currentLength = 0;

        for (int i = 0; i < A.length; i++) {
            if (A[i] == minValue) {
                lastMinIdx = i;
            }

            if (A[i] == maxValue) {
                lastMaxIdx = i;
            }

            if (lastMinIdx != -1 && lastMaxIdx != -1) {
                currentLength = Math.abs(lastMaxIdx - lastMinIdx) + 1;
                if (currentLength < minLength) {
                    minLength = currentLength;
                }
            }

        }

        return minLength;
    }
}
