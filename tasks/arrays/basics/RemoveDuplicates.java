/*
 * 
 * You're given an integer array nums sorted in non-decreasing order. Remove the
 * duplicates in-place such that each unique element appears only once. The
 * relative order of elements should be kept the same.
 * 
 * Since it's not possible to change the length of an array in some languages,
 * you must instead have the result be placed in the first part of nums. More
 * formally: if there are k unique elements after removing duplicates, the first
 * k elements of nums should hold the final result. Return k (the number of
 * unique elements) — it doesn't matter what's left beyond the first k elements.
 * 
 * You must do this with O(1) extra space.
 * 
 * Example:
 * Input: nums = [1,1,2]
 * Output: k = 2, nums = [1,2,_] (the first two elements are 1, 2)
 * 
 * Input: nums = [0,0,1,1,1,2,2,3,3,4]
 * Output: k = 5, nums = [0,1,2,3,4,_,_,_,_,_]
 * 
 * Constraints:
 * 
 * 1 <= nums.length <= 3 * 10^4
 * -100 <= nums[i] <= 100
 * nums is sorted in non-decreasing order
 * 
 */

class RemoveDuplicates {
    public static void main(String[] args) {
        int[] A = { 1, 1, 2 };
        // int k = 5;

        int j = 1;

        for (int i = 1; i < A.length; i++) {
            if (A[i] != A[i - 1]) {
                A[j] = A[i];
                j++;
            }
        }

        System.out.println(java.util.Arrays.toString(A));
    }
}