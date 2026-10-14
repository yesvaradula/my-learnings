import java.util.*;


class MergeArrays {

	public static void main(String[] args) {

		int[] A = {100, 32, 25, 90, 1};
        	int[] B = {51, 53, 99, 89, 16, 88};
	
		Arrays.sort(A); // O(N log N)
        	Arrays.sort(B); // O(M log M)

        	int[] result = new int[A.length + B.length];
        	int i = 0, j = 0, k = 0;

        	// Step 2: Two-pointer merge O(N + M)
        	while (i < A.length && j < B.length) {
            	if (A[i] <= B[j]) {
                	result[k++] = A[i++];
            	} else {
                	result[k++] = B[j++];
            		}
        	}

        	while (i < A.length) result[k++] = A[i++];
        	while (j < B.length) result[k++] = B[j++];		

		 
		System.out.println(java.util.Arrays.toString(result));
	}

	 
}