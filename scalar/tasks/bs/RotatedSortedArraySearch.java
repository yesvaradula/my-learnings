public class RotatedSortedArraySearch {
    public static void main(String[] args) {
        // int[] A = {
        // 72, 9, 10, 11, 12, 14, 15, 17, 19, 24, 25, 30, 39, 40, 44, 46,
        // 48, 51, 53, 54, 56, 59, 60, 69, 70, 73, 75, 80, 87, 88, 89, 92, 93,
        // 97, 99, 104, 107, 109, 110, 111, 117, 123, 124, 125, 126, 127, 128,
        // 135, 136, 137, 141, 148, 153, 154, 161, 166, 167, 169, 175, 177,
        // 180, 181, 182, 185, 186, 189, 193, 198, 202, 1, 2, 6, 7
        // };
        // int B = 198;

        int[] A = { 5, 1, 3 };
        int B = 5;
        System.out.println(solve(A, B));
    }

    static int solve(int[] A, int B) {
        int ans = -1;

        int start = 0;
        int end = A.length - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (A[mid] == B)
                return mid;

            if (A[start] <= A[mid]) {
                if (A[start] <= B && B < A[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else {
                // A[start] > A[mid]
                if (A[mid] < B && B <= A[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }

        }

        return ans;
    }
}
