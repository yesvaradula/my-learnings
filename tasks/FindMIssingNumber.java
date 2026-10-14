class FindMissingNumber {
    public static int findMissingNumber(int[] A) {

        int missingNumber = 0;

        int n = A.length;
        int i = 0;

        while (i < n) {
            while (A[i] > 0 && A[i] <= n && A[i] != A[A[i] - 1]) {
                int temp = A[i];
                A[i] = A[temp - 1];
                A[temp - 1] = temp;
            }
            i++;
        }

        for (int j = 0; j < n; j++) {
            if (A[j] != j + 1) {
                return j + 1;
            }
        }

        return (missingNumber == 0) ? n + 1 : missingNumber;
    }

    public static void main(String args[]) {
        int[] arr = { 1, 2 };
        int missingNumber = findMissingNumber(arr);
        System.out.println("Missing number: " + missingNumber);
    }
}