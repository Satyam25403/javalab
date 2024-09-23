public class UniqueBSTs {
    static int numTrees(int n) {
        int[] count = new int[n + 1];
        count[0] = 1; // Base case: empty tree

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                count[i] += count[j - 1] * count[i - j];
            }
        }

        return count[n];
    }

    public static void main(String[] args) {
        int n = 3; // Example input
        int result =numTrees(n);
        System.out.println("Number of structurally unique BSTs with " + n + " nodes: " + result);
    }
}

