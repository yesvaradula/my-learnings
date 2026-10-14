class Arithmetic {

	public static void main(String[] args) {

		int[] A = { 2, 4, 1 };
		java.util.Arrays.sort(A); // 1, 3, 5 
		System.out.println(java.util.Arrays.toString(A));

		int l = 0;
		int diff = A[1] - A[0];
		int ret = 1;



		for( int r = 1; r < A.length; r++ ) {
			if( A[r] - A[l] != diff ) { ret = 0; break; }
			l++;
		}
		
		System.out.println(ret);		

	}

	
}