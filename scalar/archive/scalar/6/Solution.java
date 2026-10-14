public class Solution {
    public static void main(String[] args) {
        solve(5);
    }

    public static void solve(int n) {
        System.out.println("n = " + n);
        if (n <= 1) {
            return;
        }
        solve(n / 2); // O(log N)
        solve(n / 2); // O(log N)
        for (int i = 0; i < n; i++) {
            System.out.print(i + ", ");
        }
    }
}

// TC: O(n Log n) SC: O(log n) - recursive stack space
/*
 * 
 * Recursion is a genuinely new pattern, so let's build the intuition carefully.
 * The loop-multiplying rules don't apply directly here; recursion needs a
 * different way of counting.
 * The key questions for any recursive function:
 * 
 * 1. How many times does it call itself? → two calls: solve(n/2) and solve(n/2)
 * 2. How does the input shrink each call? → halved (n/2)
 * 3. How much non-recursive work per call? → the loop, which is O(n)
 * 
 * Now, how many levels are there? The input halves each time (n → n/2 → n/4 →
 * ... → 1), which takes log n levels (the divide-by-2 pattern you already know
 * well).
 * So the total work is:
 * work per level × number of levels = n × log n = O(n log n)
 * So the answer is O(n log n).
 * Where the O(n³) guess likely came from: there's no natural "cubing" here.
 * With recursion, you can't just multiply loop bounds — you have to count the
 * tree. Two things drive it:
 * 
 * The branching (how the number of calls grows): 2 calls per node here
 * The depth (how many times you can halve n): log n here
 * 
 * A useful mental hook: this exact structure — split into two halves, do O(n)
 * work to combine — is merge sort, and merge sort is famously O(n log n). You
 * just derived it from scratch.
 */
