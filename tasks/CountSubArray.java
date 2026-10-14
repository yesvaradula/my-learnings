class CountSubArray {

	public static void main(String[] args) {
		int[] A = { 1, 11, 2, 3, 15 };
		int B = 10;
		int count = 0;
			
		int start = 0;
		int sum = 0;
		int end = 0;

		while ( end < A.length ) {
			sum += A[end];
							
			while (start <= end && sum >= B) {
                		sum -= A[start];
                		start++;
            		}
			count += (end - start + 1);		

			end++;  
		}
		
		
		System.out.println(count);
	}
}