public class RotateArr {
    public static void main(String[] args) {
        int[] arr = { 1, 2, 3 };
        int k = 3;
        rotate(arr, k);
        System.out.println(java.util.Arrays.toString(arr));
    }

    static void rotate(int[] arr, int k) {

        if (arr.length == 0 || k < 0) {
            return;
        }

        if (arr.length == 1 || k == 0) {
            return;
        }

        k = k % arr.length;
        reverse(arr, 0, arr.length - 1);
        reverse(arr, 0, k - 1);
        reverse(arr, k, arr.length - 1);

    }

    static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}

// Time Complexity: O(n)
// Space Complexity: O(1)