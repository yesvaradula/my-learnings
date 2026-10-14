public class BestOnesRow {
    public static void main(String args[]) {
        int[][] A = {
                { 0, 0, 1, 1 },
                { 0, 0, 0, 1 },
                { 0, 0, 1, 1 },
                { 0, 1, 1, 1 }
        };

        System.out.println("Best row: " + BestOnes(A));
    }

    static int BestOnes(int[][] A) {
        int rows = A.length;
        int cols = A[0].length;

        int r = 0;
        int c = cols - 1;
        int bestRow = 0; // Default to row 0 if all rows have zero 1s

        while (r < rows && c >= 0) {
            if (A[r][c] == 1) {
                bestRow = r; // Found a row with more 1s
                c--; // Look for more 1s to the left
            } else {
                r++; // Cannot beat the current max, move down
            }
        }

        return bestRow;
    }
}
