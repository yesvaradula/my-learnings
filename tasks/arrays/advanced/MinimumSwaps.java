public class MinimumSwaps {
    public static void main(String args[]) {
        int[] A = { 1, 12, 10, 3, 14, 10, 5 };
        int B = 8;

        System.out.println("Array: " + java.util.Arrays.toString(A));

        int smallerNumbers = 0;
        for (int num : A) {
            if (num <= B) {
                smallerNumbers++;
            }
        }

        System.out.println("Smaller numbers: " + smallerNumbers);

        if (smallerNumbers <= 1) {
            return 0;
        }

        int greaterNumbers = 0;
        // find the number of greater numbers in the
        // first window of size smallerNumbers
        for (int i = 0; i < smallerNumbers; i++) {
            if (A[i] > B) {
                greaterNumbers++;
            }
        }

        int minSwaps = greaterNumbers;

        for (int i = 0, j = smallerNumbers; j < A.length; i++, j++) {
            if (A[i] > B) {
                greaterNumbers--;
            }
            if (A[j] > B) {
                greaterNumbers++;
            }
            minSwaps = Math.min(minSwaps, greaterNumbers);
        }

        System.out.println("Minimum swaps required: " + minSwaps);
    }
}
