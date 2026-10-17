public class RotateMatrix {
    public static void main(String[] args) {
        int[][] A = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        };

        int[][] B = solve(A);

    }

    static int[][] solve(int[][] A) {
        // tc: o(n*n);
        // sc: o(1);

        int r = A.length - 1;
        int c = A[0].length - 1;

        int x = 0, y = 0;

        while (i < r && j < c) {
            int temp = A[x][y];
            A[x][y] = A[y][x];
            A[y][x] = temp;
            x++;
            y--;
        }

        for (int i = 0; i < r; i++) {
            int j = 0;
            while (j < c) {
                int temp = A[i][j];
                A[i][j] = A[i][c];
                A[i][c] = temp;
                c--;
                j++;
            }
        }

        return A;
    }

    static void swap()
}
