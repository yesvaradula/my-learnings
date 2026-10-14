class MaxProfit {

	public static void main(String[] args) {
		int[] A = { 7,1,5,3,6,4};
		int Buy = A[0];
		int profit = 0;
		int sell = A[1];
		
		for( int i = 1; i < A.length; i++ ) {
			// selling from second date. 
			int pp = A[i] - Buy;
			if( pp > profit ) {
				profit = pp;
				sell = A[i];
			}
			
			if ( A[i] < Buy ) {
				Buy = A[i];	
			}			
		}
		
		System.out.println(profit + " Buying at " + Buy + " and selling at " + sell);
	}
}