public class AddOneToNum {
    public static void main(String args[]) {
        int[] A = { 0, 3, 7, 6, 4, 0, 5, 5, 5 };

        int[] result = addOne(A);
        System.out.println("Array: " + java.util.Arrays.toString(result));
    }

    public static int[] addOne(int[] digits) {
        int n = digits.length;

        // Step 1: Perform the addition from right to left
        boolean carriedAllTheWay = true;
        for (int i = n - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                carriedAllTheWay = false;
                break;
            }
            digits[i] = 0;
        }

        // Step 2: Handle the edge case where all numbers were 9s (e.g., [9, 9] ->)
        if (carriedAllTheWay) {
            int[] result = new int[n + 1];
            result[0] = 1;
            return result;
        }

        // Step 3: Count how many leading zeros exist in the modified array
        int leadingZeros = 0;
        while (leadingZeros < n && digits[leadingZeros] == 0) {
            leadingZeros++;
        }

        // Step 4: Special case if the entire array becomes empty (e.g., input was)
        if (leadingZeros == n) {
            return new int[] { 0 };
        }

        // Step 5: Copy the remaining valid elements into a new trimmed array
        int[] result = new int[n - leadingZeros];
        System.arraycopy(digits, leadingZeros, result, 0, result.length);

        return result;
    }
}
