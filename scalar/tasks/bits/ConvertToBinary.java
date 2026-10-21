
public class ConvertToBinary {
    public static void main(String[] args) {
        int x = 6;
        System.out.println(toBinary(x));
    }

    // TC: O(32) = O(1), one check per bit. SC: O(32) = O(1) for the output string.
    static String toBinary(int x) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;

        for (int k = 31; k >= 0; k--) {
            int bit = (x >> k) & 1;
            if (bit == 1)
                started = true;
            if (started)
                sb.append(bit);
        }

        return started ? sb.toString() : "0"; // x == 0 has no 1 bits at all
    }
}
