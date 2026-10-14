/**
 * 
 * A coach is organizing a relay race with a fixed running order — each runner's position is stored in an array, e.g., runner at index 0 goes first, index 1 goes second, and so on.

Right before the race, the coach gets new instructions from the league: the starting lineup needs to rotate — whoever was k positions from the end should now start first, 
and everyone else shifts accordingly, wrapping around to the back. For example, if the order was [1,2,3,4,5] and they need to rotate by k=2, the new order should be [4,5,1,2,3]. 
This needs to happen directly on the existing lineup array — no separate roster sheet (extra array) allowed — and k could sometimes be larger than the number of runners on the team.
 *
 */


public class Rotate {
	public static void main(String args[]) {
		int[] A = {1,2,3,4,5, 6};

		int k = 2;

		k = k % A.length;
 
 
		System.out.println(k);


		A = rotate(A, 0, A.length - k - 1);   // reverse first (n-k) elements
        A = rotate(A, A.length - k, A.length - 1); // reverse last k elements
        A = rotate(A, 0, A.length - 1);       // reverse whole array

		System.out.println(java.util.Arrays.toString(A));


	}

	static int[] rotate(int[] A, int from, int to) {
		 while( from < to ) {
		 	int temp = A[from];
		 	A[from] = A[to];
		 	A[to] = temp;

		 	from++;
		 	to--;
		 }

		 return A;
	}
}