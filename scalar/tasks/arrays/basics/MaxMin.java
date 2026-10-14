class MaxMin {
    public static void main(String[] args) {
        int[] A = { 2, 3, 4, 59, 0, 23, 45, 100 };

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < A.length; i++) {
            max = Math.max(max, A[i]);
            min = Math.min(min, A[i]);
        }

        System.out.println("Maximum : " + max + ". Minimum : " + min);
    }
}