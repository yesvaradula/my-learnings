// Greatest Common Divisor
// Given two non-negative integers A and B, return gcd(A, B):
// the largest number that divides both. gcd(A, 0) = A.
// Constraints (Scaler style): 0 <= A, B <= 10^9, and A + B > 0.
//
// A = 4,  B = 6   ->  2
// A = 6,  B = 7   ->  1
// A = 12, B = 0   -> 12

public class GCD {
    public static void main(String[] args) {
        System.out.println(gcd(4, 6)); // 2
        // System.out.println(gcd(6, 7)); // 1
        // System.out.println(gcd(12, 0)); // 12
    }

    // TC: O(log n); SC: O(1) due to recursion stack
    static int gcd(int A, int B) {
        while (B != 0) {
            int r = A % B;
            A = B;
            B = r;
        }
        return A;
    }
}
