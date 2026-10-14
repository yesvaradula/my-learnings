import java.util.Arrays;

class ElementRemoval {

	public static void main(String[] args) {

		int[] A = {2, 1, 0, 4, 6};
		int count = 0;

		Arrays.sort(A);

		System.out.println(java.util.Arrays.toString(A));
		for(int i = 0; i < A.length;i++ ) {
			count += A[i] * ( A.length - i );
		}
		
		System.out.println(count);

	
	}
}