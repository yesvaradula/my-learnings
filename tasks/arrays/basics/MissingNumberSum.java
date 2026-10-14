class MissingNumberSum {

	public static void main(String[] args) {
		int[] A = {9, 2, 3, 4, 8, 6, 7, 1};		
		int n = A.length + 1;
		
		long totalSum = (long) n * (n+1) / 2;
		long runningSum = 0;

		for( int i = 0; i < A.length; i++ ) {
			runningSum += A[i];
		}

		int missingNum = (int) (totalSum - runningSum);
		
		System.out.println(missingNum);
	}
}