public class FindNumber {
    public static void main(String args[]) {
        int[][] A = { { 2, 8, 8, 8 }, { 2, 8, 8, 8 }, { 2, 8, 8, 8 } };
        // [
        // [2,8,8,8],
        // [2,8,8,8],
        // [2,8,8,8]
        // ]
        int find = 8;

        System.out.println("Found at: " + findNumber(A, find));

    }

    static int findNumber(int[][] A, int find) {

        int row = A.length;
        int col = A[0].length;

        int result = Integer.MAX_VALUE;

        int i = 0;
        int j = col - 1;

        while (i < row && j >= 0) {
            if (A[i][j] == find) {
                result = Math.min(result, ((i + 1) * 1009) + (j + 1));
                j--;
            } else if (A[i][j] > find) {
                j--;
            } else {
                i++;
            }
        }

        return (result == Integer.MAX_VALUE) ? -1 : result;
    }
}
