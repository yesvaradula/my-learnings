import java.util.Arrays;

class RangeDivisibility {

	public static void main(String[] args) {
		int[] A = {49, 82, 81, 22, 98, 49, 16, 1, 62, 28, 81, 29, 28, 42, 37, 27, 59, 19, 21, 95, 82, 32};
		int[][] B = {
    {15, 18},
    {0, 12},
    {2, 7},
    {9, 16},
    {3, 18},
    {0, 21},
    {0, 4},
    {5, 11},
    {5, 18},
    {2, 20},
    {8, 16},
    {2, 13},
    {10, 13},
    {2, 11},
    {4, 10},
    {8, 14},
    {3, 11},
    {18, 18},
    {4, 11},
    {3, 5},
    {7, 18},
    {6, 19},
    {12, 21},
    {3, 16},
    {10, 19},
    {1, 7},
    {4, 15},
    {7, 9},
    {4, 19},
    {3, 8},
    {4, 15},
    {2, 10},
    {13, 13},
    {3, 11},
    {13, 20},
    {6, 21},
    {6, 9},
    {5, 6},
    {6, 17},
    {8, 12},
    {17, 17},
    {6, 15},
    {3, 5},
    {2, 7},
    {4, 10},
    {4, 20},
    {4, 9},
    {3, 9},
    {2, 3},
    {11, 20},
    {2, 21},
    {14, 19},
    {14, 20}
};
		int[] R = solve(A, B);
		System.out.println(Arrays.toString(R));
	}


	static int[] solve(int[] A, int[][] B) {
		
		int[] pf = new int[A.length];
		pf[0] = (A[0] % 7 == 0 ) ? 1 : 0;		
 
		for( int i = 1; i < A.length; i++ ) {
			A[i] = ( A[i] % 7 == 0 ) ? 1 : 0;
			pf[i] = pf[i-1] + A[i];
			
		}

		System.out.println(Arrays.toString(pf));

		int[] result = new int[B.length];

		for(int i = 0; i < B.length; i++ ) {
			int L = B[i][0];
			int R = B[i][1];
			result[i] = (L == 0 ) ? pf[R] : pf[R] - pf[L-1];	
		} 

		return result;
	}
}