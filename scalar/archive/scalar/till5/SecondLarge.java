import java.util.ArrayList;
import java.util.Arrays;

class SecondLarge {
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1));
        int secondLargest = calulateSecondLargest(numbers);
        System.out.println("Second largest element is: " + secondLargest);
    }

    public static int calulateSecondLargest(ArrayList<Integer> A) {
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        for (int v : A) {
            if (v > largest) {
                secondLargest = largest;
                largest = v;
            } else if (v > secondLargest && v != largest) {
                secondLargest = v;
            }
        }

        return (secondLargest == Integer.MIN_VALUE) ? -1 : secondLargest;
    }
}