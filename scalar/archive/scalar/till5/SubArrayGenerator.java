import java.util.Arrays;

public class SubArrayGenerator {
    public static void main(String[] args) {
        int[] A = { 1, 2, 3 };
        int[][] result = createSubArrayFromArray(A);

        // Printing the 2D array to verify the output
        System.out.println(Arrays.deepToString(result));
    }

    public static int[][] createSubArrayFromArray(int[] A) {
        int n = A.length;
        // Total number of subarrays for an array of size N is N * (N + 1) / 2
        int totalSubarrays = n * (n + 1) / 2;
        int[][] result = new int[totalSubarrays][];

        int rowIdx = 0;

        // Outer loop sets the starting point
        for (int left = 0; left < n; left++) {
            // Inner loop sets the ending point
            for (int right = left; right < n; right++) {
                // Arrays.copyOfRange extracts the subarray in O(1) effective native execution
                // per element, eliminating the manual 3rd loop in your code.
                // Note: right + 1 because the ending index is exclusive.
                result[rowIdx] = Arrays.copyOfRange(A, left, right + 1);
                rowIdx++;
            }
        }

        return result;
    }
}

// TC: O(N^2) - The outer loop runs N times and the inner loop runs up to N
// times, leading to a quadratic time complexity. The space complexity is also
// O(N^2) for storing the subarrays.

// SC: O(N^2) - The space complexity is O(N^2) because we are storing all the
// subarrays in a 2D array.