public class ClockWiseBoundary {
    public static void main(String[] args) {
        int[][] A = {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 16 }
        };

        // out put should be
        // 1 2 3 4 8 12 16 15 14 13 9 5

        int r = A.length;
        int c = A[0].length;
        int size = 2 * (r + c - 2);
        int[] result = new int[size];

        int index = 0;

        for (int i = 0; i < c - 1; i++) {
            result[index++] = A[0][i];
        }

        for (int i = 0; i < r - 1; i++) {
            result[index++] = A[i][c - 1];
        }

        for (int i = c - 1; i > 0; i--) {
            result[index++] = A[r - 1][i];
        }

        for (int i = r - 1; i > 0; i--) {
            result[index++] = A[i][0];
        }

        // Print the result
        for (int i = 0; i < size; i++) {
            System.out.print(result[i] + " ");
        }
    }
}
