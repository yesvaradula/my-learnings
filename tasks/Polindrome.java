class Polindrom {

	public static void main(String[] args) {
		String S = "abccba";
		boolean isPoli = solve(S);
		System.out.println(isPoli);
	}

	static boolean solve(String S) {
		
		int i = 0;
		int j = S.length()-1;
		
		while( i < j ) {
			if( i <= j && S.charAt(i) != S.charAt(j) ) {
				return false;
			}
			i++;
			j--;
		}		

		return true;
	}
}