public class BinaryString {
    public static void main(String[] args) {
        String[] tests = { "010", "111", "1101010001", "0", "000", "1001" };
        for (String t : tests)
            System.out.println(t + " → " + java.util.Arrays.toString(countMaxOnes(t)));

    }

    // Time Complexity: O(n)
    // Space Complexity: O(1)
    static int[] countMaxOnes(String A) {
        int n = A.length();
        int currentOnesCount = 0;
        int maxOnesCount = 0;
        int start = 0;
        int leftIndex = -1;
        int rightIndex = -1;

        for (int i = 0; i < n; i++) {
            currentOnesCount += A.charAt(i) == '0' ? 1 : -1;

            if (currentOnesCount < 0) {
                currentOnesCount = 0;
                start = i + 1;
                continue;
            }

            if (currentOnesCount > maxOnesCount) {
                maxOnesCount = currentOnesCount;
                leftIndex = start;
                rightIndex = i;
            }
        }

        if (leftIndex == -1 || rightIndex == -1) {
            return new int[] {};
        }

        return new int[] { leftIndex + 1, rightIndex + 1 };
    }
}
