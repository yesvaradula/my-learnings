class MaxProduct {

	public static void main(String[] args) {

		int[] A = {2, 3, -2, 4, -1};
		int maxP = A[0];
		int minP = A[0];
		int result = A[0];

		for( int i = 1; i < A.length; i++ ) {
			int c = A[i];
			
			if( c >= 0 ) {
				maxP = Math.max(c, maxP * c);
				minP = Math.min(c, minP * c);
			}
			else {
				int temp  = maxP;
				maxP  = Math.max(c, minP * c);
				minP = Math.min(c, temp * c);
			}
			System.out.println("Max is : " + maxP + " and min is : " + minP);
			result = Math.max(result, maxP);
		}
	
		System.out.println(result);
	
	}
}