class SumNAvg {
    public static void main(String[] args) {
        int[] A = { 10000, 289823, 3090348, 465230, 528937890 };

        double[] B = solve(A);
        System.out.println("Sum is : " + B[0] + ". Avg is : " + B[1]);
    }

    static double[] solve(int[] A) {
        long sum = 0;

        for (long num : A) {
            sum += num;
        }

        double avg = (double) sum / A.length;

        return new double[] { (double) sum, avg };
    }
}