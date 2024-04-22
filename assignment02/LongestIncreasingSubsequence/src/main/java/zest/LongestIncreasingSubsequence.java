package zest;

public class LongestIncreasingSubsequence {
    public int lengthOfLIS(int[] nums) {
        // Pre-condition
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int[] dp = new int[nums.length];
        int maxLength = 0;

        // Invariant 1
        invariant1SatisfiesConditions(dp, nums);

        for (int i = 0; i < nums.length; i++) {
            dp[i] = 1; // Each element is an increasing subsequence of length 1

            // Check all previous elements to find longer increasing subsequences
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    // Invariant 2
                    invariant2SatisfiesConditions(dp[i]);
                }
            }

            maxLength = Math.max(maxLength, dp[i]);
        }

        // Invariant 1
        invariant1SatisfiesConditions(dp, nums);

        // Post-conditions
        resultSatisfiesConditions(maxLength, nums);

        return maxLength;
    }

    Boolean resultSatisfiesConditions(int result, int[] array) {
        if (result < 0) throw new RuntimeException("Result must be a non-negative integer");
        if (result > array.length) throw new RuntimeException("Result cannot be bigger that the length of the input array");
        return true;
    }

    Boolean invariant1SatisfiesConditions(int[] dp, int[] nums) {
        if (dp.length != nums.length) throw new RuntimeException("DP array has to have the same length as the input array");
        return true;
    }

    Boolean invariant2SatisfiesConditions(int num) {
        if (num < 1) throw new RuntimeException("Elements of DP array cannot be smaller than 1");
        return true;
    }

}

