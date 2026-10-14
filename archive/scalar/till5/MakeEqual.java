import java.util.ArrayList;
import java.util.Arrays;

public class MakeEqual {
    public static void main(String[] args) {
        ArrayList<Integer> A = new ArrayList<>(
                Arrays.asList(731, 349, 490, 781, 271, 405, 811, 181, 102, 126, 866, 16, 622, 492, 194, 735));
        int result = CountOperations(A);
        System.out.println("Result: " + result);
    }

    public static int CountOperations(ArrayList<Integer> A) {
        int highValue = Integer.MIN_VALUE;
        int totalOperations = 0;

        for (int v : A) {
            totalOperations += v;
            if (v > highValue) {
                highValue = v;
            }
        }

        totalOperations = (highValue * A.size()) - totalOperations;
        return totalOperations;
    }
}
