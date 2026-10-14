import java.util.ArrayList;

public class CircularPathCount {
    public static void main(String[] args) {
        int[][] A = {
                { 01, 02, 03, 04, 05, 06 },
                { 07, 8, 9, 10, 11, 12 },
                { 13, 14, 15, 16, 17, 18 },
                { 19, 20, 21, 22, 23, 24 },
                { 25, 26, 27, 28, 29, 30 },
                { 31, 32, 33, 34, 35, 36 }

        };

        System.out.println(java.util.Arrays.deepToString(A));

        int[] result = circularPathCount(A);
        System.out.println(java.util.Arrays.toString(result));
    }

    static int[] circularPathCount(int[][] A) {

        ArrayList<Integer> result = new ArrayList<>();

        int top = 0;
        int bottom = A.length - 1;
        int left = 0;
        int right = A[0].length - 1;
        int n = A.length;

        while (top < bottom && left < right) {

            for (int in = left; in < right; in++) {
                result.add(A[top][in]);
            }

            for (int in = top; in < bottom; in++) {
                result.add(A[in][right]);
            }

            for (int in = right; in > left; in--) {
                result.add(A[bottom][in]);
            }

            for (int in = bottom; in > top; in--) {
                result.add(A[in][left]);
            }

            top++;
            bottom--;
            left++;
            right--;
            n = n - 2;

        }

        System.out.println(n);
        if (n == 1) {
            result.add(A[left][right]);
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
