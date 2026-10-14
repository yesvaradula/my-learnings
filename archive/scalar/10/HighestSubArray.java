public class HighestSubArray {
    public static void main(String[] args) {
        int[] A = { 3,2,1};

        int max = A[0];
        int current = A[0];

        for(int i = 1; i < A.length; i++) {
            if( A[i] < A[i-1]) {
                current += A[i];
            } else {
                current = A[i];
            }
            max = Math.max(current, max);
        }

        System.out.println(max);
    }
}