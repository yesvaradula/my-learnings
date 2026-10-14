import java.util.ArrayList;

class LeaderElements {
    public static void main(String[] args) {
        int[] A = { 1, 4, 12, 2 };
        solve(A);
    }

    static void solve(int[] A) {
        if (A.length == 0) {
            System.out.println("Array empty does not have leaders");
            return;
        }

        int n = A.length;
        int max_element = A[n - 1];
        ArrayList<Integer> leaders = new ArrayList<>();

        leaders.add(max_element);

        for (int i = n - 2; i >= 0; i--) {
            if (A[i] > max_element) {
                leaders.add(A[i]);
                max_element = A[i];
            }
        }

        System.out.println(leaders);
    }
}