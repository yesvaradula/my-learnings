public class PeakElement {
    public static void main(String[] args) {
        int[] A = { 5, 20, 100, 9 };
        int ans = solve(A);
        System.out.println(ans);
    }

    static int solve(int[] A) {
        // TC: O(log n)
        // SC: O(1)
        int ans = 0;
        int n = A.length;

        if (n == 1)
            return A[0];

        if (A[0] >= A[1])
            return A[0];

        if (A[n - 1] >= A[n - 2])
            return A[n - 1];

        int s = 1;
        int e = n - 2;
        int m = 0;

        while (s <= e) {
            m = s + (e - s) / 2;

            if (A[m] >= A[m + 1] && A[m] >= A[m - 1]) {
                return A[m];
            }

            if (A[m - 1] > A[m])
                e = m - 1;
            else
                s = m + 1;
        }

        return ans;
    }
}
