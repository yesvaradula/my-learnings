public class AFterRPalind {
    public static void main(String args[]) {
        String s = "abcab";
        boolean result = afterRCheck(s);
        System.out.println(result);
    }

    public static boolean afterRCheck(String s) {

        int l = 0;
        int r = s.length() - 1;

        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return isPalindrome(s, l + 1, r);
            }
            l++;
            r--;
        }

        return true;

    }

    public static boolean isPalindrome(String str, int l, int r) {
        while (l < r) {
            if (str.charAt(l) != str.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}