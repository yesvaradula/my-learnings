class SpecialIndex {

	public static void main(String[] args) {
		int[] A = {2,1,6,4};
		
		System.out.println(java.util.Arrays.toString(A));
		System.out.println("");
		int R = solve(A);
		System.out.println(R);	
	}
	
	static int solve(int[] A) {
		int n = A.length;

		// find even prefix of A
		int[] pfE = new int[n];
		int[] pfO = new int[n];
	
		pfE[0] = A[0];
		pfO[0] = 0;
	
		for(int i = 1; i < n; i++ ) { 
			pfE[i] = ( i % 2 == 0 ) ? pfE[i-1] + A[i] : pfE[i-1]; 
			pfO[i] = ( i % 2 != 0 ) ? pfO[i-1] + A[i] : pfO[i-1]; 
		}

		System.out.println(java.util.Arrays.toString(pfE));
		System.out.println(java.util.Arrays.toString(pfO));

		int oddSum = 0;
		int evenSum = 0;
		int specialIndex = 0;
		for( int i = 0; i < n; i++ ) {
			oddSum = (( i == 0 ) ? 0 : pfO[i-1] ) + pfE[n-1] - pfE[i];
			evenSum =(( i == 0 ) ? 0 : pfE[i-1] ) + pfO[n-1] - pfO[i];
		
			if( oddSum == evenSum ) {
				System.out.println("Special Index is : " + i );
				specialIndex++;
			}
		
		}

		return specialIndex;
	}
}