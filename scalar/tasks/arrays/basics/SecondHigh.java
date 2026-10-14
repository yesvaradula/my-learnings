
public class SecondHigh {

    public static void main() {
        int[] A = {2,2,2};

        int high = Integer.MIN_VALUE;
        int secondHigh = Integer.MIN_VALUE;
 

        for( int i = 0;  i < A.length;i++) {

            if( A[i] > secondHigh && A[i] < high) {
                secondHigh = A[i];
            }

            if( A[i] > high) {
                secondHigh = high;
                high = A[i];
            }
            
        }

        System.out.println(high + " -- " + secondHigh);
    }

}
