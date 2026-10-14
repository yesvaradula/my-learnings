public class AddOne {
    public static void main(String[] args) {
        int[] digits = { 0, 0, 0 };
        System.out.println("Input: " + java.util.Arrays.toString(digits));
        int[] result = plusOne(digits);
        System.out.println(java.util.Arrays.toString(result));
    }

    // Time Complexity: O(n)
    // Space Complexity: O(n)
    static int[] plusOne(int[] digits) {
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

        int leadingZeros = 0;
        while (leadingZeros < n && digits[leadingZeros] == 0) {
            leadingZeros++;
        }

        // if (leadingZeros == n) {
        // return new int[] { 0 };
        // }

        // Step 5: Copy the remaining valid elements into a new trimmed array
        int[] result = new int[n - leadingZeros];
        System.arraycopy(digits, leadingZeros, result, 0, result.length);

        return result;
    }
}
