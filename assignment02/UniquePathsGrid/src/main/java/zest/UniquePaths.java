package zest;

public class UniquePaths{
    public int uniquePaths(int m, int n) {
        // Pre-conditions
        if ((m < 1 || m > 100)  || (n < 1 || n > 100)) throw new IllegalArgumentException("Passed argument is outside the range of 1 and 100");

        int[][] dp = new int[m][n];

        // Initialize the first row and column to 1 since there's only one way to reach any cell in the first row or column
        // Filling in the first column of the table with 1s
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        // Filling in the first row of the table with 1s
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }

        // Fill the DP table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1]; // The number of paths to the current cell is the sum of the paths to the cell above and to the left
                // Invariant
                invariantSatisfiesConditions(dp[i][j], dp[i - 1][j], dp[i][j - 1]);
            }
        }

        int result = dp[m - 1][n - 1]; // The bottom-right cell contains the total number of unique paths

        // Post-condition
        resultSatisfiesConditions(result);

        return result;
    }

    boolean resultSatisfiesConditions(int result) {
        if (result < 1) throw new RuntimeException("Result must be bigger than 0");
        return true;
    }

    boolean invariantSatisfiesConditions(int a, int b, int c) {
        if (a <= b || a <= c) throw new RuntimeException("Value of a cell should be bigger than both, the value of a cell to the left and of an upper cell");
        return true;
    }
}
