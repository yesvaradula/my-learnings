import java.util.Arrays;

class NonRepeating {

	public static void main(String[] args) {
		String S = "aa";
		// String S = "ab";
		char res = solve(S);
		System.out.println(res);
	}

	static char solve(String S) {
		int[] n = new int[26];
		for( int i = 0; i < S.length(); i++ ){
			n[S.charAt(i) - 'a'] += 1;
		}

 

		for( int i = 0; i < S.length(); i++ ) {
			if ( n[S.charAt(i) - 'a'] == 1 ) {
				return S.charAt(i);
			} 
		}
	
		return ' ';

	}

}