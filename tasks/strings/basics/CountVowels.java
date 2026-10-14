class CountVowels {


	public static void main(String[] args) {
		String S = "aeioubcd"; 
		int[] count = solve(S);
		System.out.println("The count of Vowels is " + count[0] + " and Consonents " + count[1]);
	}
	
	public static int[] solve(String S) {
		int[] count = new int[2];
		
		for(int i = 0; i < S.length(); i++ ) {
			switch(Character.toLowerCase(S.charAt(i))) {
				case 'a' : 
				case 'i' :
				case 'o' : 
				case 'e' : 
				case 'u' : {
					count[0]++;
					break;
				}
			}
		}
		count[1] = S.length() - count[0];
		return count;
	}
}