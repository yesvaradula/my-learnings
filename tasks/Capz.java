

class Capz {

	public static void main(String[] args) {
		String A = "good morning";
		String c =  "g";
		String r = solve(A, c);
		System.out.println(r);

	}

	static String solve(String A, String c) {
		StringBuilder sb = new StringBuilder(A);
		int fromIndex = -1;
		
		for(int i = 0; i < A.length(); i++ ) {
			if(c.charAt(0) == A.charAt(i)) {
				fromIndex = i;
				break;
			}
		}

		for( int i = fromIndex; i < A.length(); i++ ) {

			char ch = A.charAt(i);
		
			if( Character.isLowerCase(ch) ) {
				sb.setCharAt(i, Character.toUpperCase(ch));
			}
		
		}
		return sb.toString();
	}

}