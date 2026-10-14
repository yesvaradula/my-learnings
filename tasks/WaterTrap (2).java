public class WaterTrap {
    public static void main(String args[]) {
        int[] A = { 10, 6, 2, 7, 5, 8, 12 };

        int waterTrapped = 0;
        int leftMax = 0;
        int rightMax = 0;

        int right = A.length - 1;

        for (int left = 0; left < A.length; left++) {
            leftMax = Math.max(leftMax, A[left]);
            rightMax = Math.max(rightMax, A[right]);
            waterTrapped += Math.min(leftMax, rightMax) - A[left];
            right--;
        }

        System.out.println("Water trapped: " + waterTrapped);
    }
}
