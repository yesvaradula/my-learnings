import java.util.Arrays;

class StringReverse {

	
	public static void main(String[] args) {
		String A = "abcedfgh";
		String B = solve(A);
		System.out.println(B);
	}

	static String solve(String A) {
	
		char[] B = A.toCharArray();
		
		
		int i = 0;
		int j = B.length-1;

		while( i <= j ) {
			char temp = B[j];
			B[j] = B[i];
			B[i] = temp;
			i++;
			j--;
		}

		return new String(B);
	}

}