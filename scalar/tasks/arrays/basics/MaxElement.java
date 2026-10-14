class MaxElement {
	public static void main(String[] args) {
		int[] A = {1};
		int max = solve(A);
		
		System.out.println(max);
		
	}

	static int solve(int[] A) {
	
		if( A.length == 0 ) throw new IllegalArgumentException("Array must not be empty");
		 
		
		int max = Integer.MIN_VALUE;
			
		for (int num : A) {
			max = Math.max(max, num);
		}
		
		return max;
	
	}
}