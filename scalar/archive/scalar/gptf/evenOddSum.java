public class evenOddSum {
    public static void main(String[] args) {
        int[] A = { 5, 3, 8, 6, 2, 7 };
        System.out.println("The Original Array: " + java.util.Arrays.toString(A));
        generateEvenPrefixSum(A);
        // int oddSum = generateOddPrefixSum(A);
        // System.out.println("The total Sum of Even and Odd Indexed Elements: " +
        // (evenSum + oddSum));

    }

    static void generateEvenPrefixSum(int[] A) {
        int[] B = new int[A.length];
        B[0] = A[0];
        B[1] = A[1];
        for (int i = 2; i < A.length; i++) {
            B[i] = B[i - 2] + A[i];
        }

        System.out.println("The Even Odd Prefix Sum Array: " + java.util.Arrays.toString(B));

        int ArraySize = A.length;
        int sum = B[ArraySize - 1] + B[ArraySize - 2];
        System.out.println("The Sum of Even Indexed Elements: " + sum);
    }

}
