import java.util.Arrays;

public class NextHighPermutation {
    public static void main(String[] args) {
        int[] A = { 3, 2, 1 };
        // O.P : { 1, 3, 2}

        A = findHP(A);

        System.out.println(java.util.Arrays.toString(A));

    }

    static int[] findHP(int[] A) {
        int n = A.length;
        int i = n - 2;

        while (i >= 0 && A[i] >= A[i + 1]) {
            i--;
        }

        if (i == -1) {
            // reverse and return.
            reverseArray(A, 0, n - 1);
            return A;
        }

        int j = n - 1;

        while (A[j] <= A[i]) {
            j--;
        }

        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;

        // Arrays.sort(A);
        A = reverseArray(A, i + 1, n - 1);

        return A;
    }

    static int[] reverseArray(int[] A, int i, int j) {
        int l = i;
        int r = j;
        while (l < r) {
            int temp = A[l];
            A[l] = A[r];
            A[r] = temp;
            l++;
            r--;
        }
        return A;
    }
}
