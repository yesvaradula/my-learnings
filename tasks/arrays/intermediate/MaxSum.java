
public class MaxSum {

    public static void main(String args[]) {
        int[] A = { -1,-2,-3,-4,-1 };

        int l = 0;
        int sum = 0;
        int maxSum = 0;
        int[] result = new int[2];

        for (int r = 0; r < A.length; r++) {
            sum += A[r];

            if (sum > maxSum) {
                maxSum = sum;
                result[0] = l;
                result[1] = r;
            }

            if (sum < 0) {
                sum = 0;
                l = r + 1;
            }
        }

        System.out.println(java.util.Arrays.toString(result));

    }

}
