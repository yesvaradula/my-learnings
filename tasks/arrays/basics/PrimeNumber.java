class PrimeNumber {

	public static void main(String[] args) {

		int[] A = {2, 0,0,3};		
		System.out.println(java.util.Arrays.toString(A));
		System.out.println(longestDistance(A));

	}
	

	static int longestDistance(int[] L) { 

		 int min = -1;
    		 int max = -1;

    		 for (int i = 0; i < L.length; i++) {
        		if (isPrime(L[i])) {
            			if (min == -1) {
                			min = i; 
            			}
            			max = i;
			}
        	}
    	

    		 
   		if (min == -1 || min == max) {
        		return -1;  
    		}

    		return (max - min) - 1;
	}
	
	static boolean isPrime(int n) {

		if( n <= 1 ) return false;	
		if(n > 1 && n < 4 ) return true;		
		if( n % 2 == 0 ) return false;		
				
 		for ( int i = 3; i * i <= n; i++) {
			if( n % i == 0 ) return false;
		}
				
		return true;

	}
}
