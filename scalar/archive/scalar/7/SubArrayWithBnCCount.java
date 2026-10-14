/**
 * Given an array A of length N. Also given are integers B and C.
 * 
 * Return 1 if there exists a subarray with
 * length B having sum C and 0
 * otherwise
 * 
 * 
 * 
 * Problem Constraints
 * 
 * 1 <= N <= 105
 * 
 * 1 <= A[i] <= 104
 * 
 * 1 <= B <= N
 * 
 * 1 <= C <= 109
 * 
 * 
 * 
 * Input Format
 * 
 * First argument A is an array of integers.
 * 
 * The remaining arguments B and C are integers
 * 
 * 
 * 
 * Output Format
 * 
 * Return 1 if such a subarray exist and 0 otherwise
 * 
 * 
 * Example Input
 * 
 * Input 1:
 * 
 * 
 * A = [4, 3, 2, 6, 1]
 * B = 3
 * C = 11
 * Input 2:
 * 
 * A = [4, 2, 2, 5, 1]
 * B = 4
 * C = 6
 * 
 * 
 * 
 * 
 * Example Output
 * 
 * Output 1:
 * 1
 * Output 2:
 * 
 * 0
 * SubArrayWithBnCCount
 */
public class SubArrayWithBnCCount {

    static void main() {
        int[] A = { 6, 3, 3, 6, 7, 8, 7, 3, 7 }; // 6, 9, 6,
        int B = 2;
        int C = 10;

        int results = findSolution(A, B, C);
        System.out.println("");
        System.out.println("The result is : " +results);
    }

    static int findSolution(int[] A, int B, int C) {
        int start = 0;
        int sum = 0;
        int breakPoint = B;
        int results = 0;
        for(int end = 0; end < A.length; end++ ) {

            if( breakPoint == 0) {
                System.out.println("Final sum : " + sum);
                sum -= A[start];
                breakPoint = B;
                start++;
                System.out.println("Reccalculated sum : " + sum);
            }

            sum += A[end];

            System.out.println(sum);

            if( sum == C ) {
                return 1;
            }
            breakPoint--;
        }

        return results;
    }
}
