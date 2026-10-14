public class FindOccurances {

public static void main(String[] args) {
String str = "he``llo world, hello everyone";
int[] A = { 1, 2, 3, 4, 5 };
int B = 2;
int count = countOccurrences(A, B);
System.out.println("The element '" + B + "' occurs " + count + " times in the
array.");
}

    public static int countOccurrences(int[] A, int B) {
        int count = 0;
        for (int v : A) {
            if (v == B) {
                count++;
            }
        }
        return count;
    }
}
