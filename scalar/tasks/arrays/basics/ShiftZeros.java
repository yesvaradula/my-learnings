
/*
The Warehouse Shelf Reorganization

A warehouse tracks inventory slots on a long shelf using an array, where each position holds either a product code (a positive number) or 0 if that slot is currently empty.

The warehouse manager wants the shelf reorganized so that all the actual products are shifted to the front of the shelf, in the exact same relative order they were originally in, and all the empty slots (zeros) get pushed to the back. This needs to happen directly on the shelf — no separate staging area (i.e., no extra array) — since there's physically nowhere else to temporarily store the inventory.

What's the core issue here, and what does "no separate staging area" tell you about the approach you need?
*/

public class ShiftZeros {
	public static void main(String args[]) {
		int[] A = { 123, 0, 0, 0, 8, 0, 82, 12, 4, 3 };

		int j = 0;

		for (int i = 0; i < A.length; i++) {
			if (A[i] != 0) {
				A[j] = A[i];
				j++;
			}
		}

		while (j < A.length) {
			A[j] = 0;
			j++;
		}

		System.out.println(java.util.Arrays.toString(A));
	}
}