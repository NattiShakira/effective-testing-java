package zest;

public class ClimbingStairs {
    public long climbStairs(int n) {
        //pre-condition
        if (n < 0) throw new IllegalArgumentException("Input has to be greater than 0");
        if (n > 91) throw new IllegalArgumentException("Input cannot be greater than 91 :(");


        if (n <= 2) {
            return n;
        }
        long oneStepBefore = 2;
        long twoStepsBefore = 1;
        long allWays = 0;

        for (int i = 2; i < n; i++) {
            allWays = oneStepBefore + twoStepsBefore;
            //invariant
            if (oneStepBefore < twoStepsBefore)
                throw new RuntimeException("oneStepBefore should be greater than or equal to twoStepsBefore");
            twoStepsBefore = oneStepBefore;
            oneStepBefore = allWays;
        }

        //post-condition
        if (allWays < 0) throw new RuntimeException("Output must be greater than 0");
        return allWays;

    }
}
