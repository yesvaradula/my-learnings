public class Power {
    public static void main(String[] args) {
        int a = 3;
        int b = 5;
        System.out.println(fastPower(a, b));
    }

    // TC: O(log b), SC: O(log b).
    static long fastPower(int a, int b) {

        if (b == 0)
            return 1;

        long half = fastPower(a, b / 2);
        if (b % 2 == 0) { // even
            return half * half;
        } else {
            return a * half * half;
        }
    }
}
