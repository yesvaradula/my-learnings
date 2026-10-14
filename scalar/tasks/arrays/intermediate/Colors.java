class Colors {

    public static void main(String[] args) {
        int[] nums = { 2, 0, 1, 0, 1, 2 };
        int[] sortedColors = sortColors(nums);
        System.out.println(java.util.Arrays.toString(sortedColors));
    }

    static int[] sortColors(int[] nums) {
        // your code here
        // [2, 0, 1, 0, 1, 2]
        // ouput - [0, 0, 1, 1, 2, 2]

        int low = 0;
        int mid = 0;
        int high = nums.length - 1;

        while (mid <= high) {
            switch (nums[mid]) {
                case 0:
                    swap(nums, low, mid);
                    low++;
                    mid++;
                    break;
                case 1:
                    mid++;
                    break;
                default:
                    swap(nums, mid, high);
                    high--;
                    break;
            }
        }

        return nums;
    }

    static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

}