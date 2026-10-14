import java.util.*;

class MaxRight {

	public static void main(String[] args) {
		int[] A = { 16, 17, 4, 3, 5, 2 };
		int[] R = solve(A);
		System.out.println(Arrays.toString(R)); 
	}

	static int[] solve(int[] A) {
		int MAX_RIGHT = Integer.MIN_VALUE;
		ArrayList<Integer> al = new ArrayList<Integer>();

		for(int i = A.length-1; i > 0; i-- ) {
			if( A[i] > MAX_RIGHT ) {
				al.add(0, A[i]);
				MAX_RIGHT = A[i];
			}
		}

		int[] B = new int[al.size()];
		for( int i = 0; i < B.length; i++ ) {
			B[i] = al.get(i);
		}
	
		return B;
	}
}