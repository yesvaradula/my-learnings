import java.util.ArrayList;
import java.util.List;

public class NonOverlapping {
    public static void main(String args[]) {

        // int[][] A = {
        // { 1, 2 }, { 2, 4 }, { 6, 8 }
        // };

        int[][] A = {
                { 1, 17 }, { 1, 32 }, { 3, 13 }, { 4, 100 }, { 5, 31 }, { 5, 63 }, { 6, 11 },
                { 6, 57 },
                { 7, 24 }, { 7, 76 }, { 8, 36 }, { 9, 15 }, { 9, 44 }, { 10, 26 }, { 10, 71
                }, { 10, 76 },
                { 10, 87 }, { 11, 12 }, { 12, 24 }, { 12, 28 }, { 13, 67 }, { 14, 29 }, { 15,
                        88 },
                { 16, 21 }, { 17, 31 }, { 17, 85 }, { 18, 29 }, { 18, 42 }, { 18, 47 }, { 21,
                        35 },
                { 21, 35 }, { 21, 72 }, { 24, 67 }, { 25, 62 }, { 25, 78 }, { 27, 100 }, {
                        29, 37 },
                { 29, 83 }, { 29, 94 }, { 30, 63 }, { 30, 95 }, { 31, 73 }, { 31, 78 }, { 32,
                        70 },
                { 33, 40 }, { 33, 51 }, { 36, 53 }, { 37, 90 }, { 39, 70 }, { 40, 73 }, { 44,
                        59 },
                { 45, 90 }, { 46, 93 }, { 47, 74 }, { 48, 79 }, { 48, 94 }, { 49, 68 }, { 51,
                        63 },
                { 53, 55 }, { 56, 70 }, { 56, 87 }, { 58, 71 }, { 58, 79 }, { 62, 68 }, { 63,
                        73 },
                { 64, 74 }, { 66, 83 }, { 67, 100 }, { 68, 75 }, { 68, 78 }, { 69, 94 }, {
                        70, 73 },
                { 78, 83 }
        };

        // int[][] result = new int[A.length][2];
        List<int[]> result = new ArrayList<>();

        int s1 = A[0][0];
        int e1 = A[0][1];

        for (int i = 1; i < A.length; i++) {
            int s2 = A[i][0];
            int e2 = A[i][1];

            if (s2 <= e1) {
                s1 = Math.min(s1, s2);
                e1 = Math.max(e1, e2);
            } else {
                result.add(new int[] { s1, e1 });
                // result[i - 1][0] = s1;
                // result[i - 1][1] = e1;
                s1 = s2;
                e1 = e2;
            }
        }

        // result[A.length - 1][0] = s1;
        // int[] last = result.get(result.size() - 1);
        // result.add(last.length, new int[] { s1, e1 });
        result.add(new int[] { s1, e1 });

        int[][] finalResult = new int[result.size()][2];
        finalResult = result.toArray(finalResult);

        System.out.println(java.util.Arrays.deepToString(finalResult));

    }
}
