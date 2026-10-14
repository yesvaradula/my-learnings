import java.util.Arrays;
import java.util.Collections;

/**
 * Given an integer array A, find if an integer p exists in the array
 * such that the number of integers greater than p in the array equals to p.
 */

class NobIndex {

    public static void main(String[] args) {

        int[] A = {1, 3, 4, 2, 10, 9, 7, 9 }; 

        int result = solve(A);
        System.out.println(result);
    }

    static public int solve(int[] A) {
        Arrays.sort(A);
        for (int i = 0; i < A.length; i++) {
          if( A[i] == A.length - i - 1)
              return 1;
        }
        return -1;
    }
}