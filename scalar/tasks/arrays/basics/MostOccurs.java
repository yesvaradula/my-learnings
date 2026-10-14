
import java.util.HashMap;
import java.util.Map;

public class MostOccurs {
    public static void main(String args[]) {
        int[] nums = { 3, 3, 3, 2, 2, 2, 2, 1, 2, 2, 2 };

        HashMap<Integer, Integer> hs = new HashMap<>();

        for (int num : nums) {
            hs.put(num, hs.getOrDefault(num, 0) + 1);
        }

        int mostFrequentElement = nums[0];
        int maxFrequency = 1;

        for (Map.Entry<Integer, Integer> entry : hs.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentElement = entry.getKey();
            }
        }

        System.out.println("Most frequent element: " + mostFrequentElement);
        System.out.println("Frequency: " + maxFrequency);
    }
}
