package zest;

public class FindDuplicate {
    public static int findDuplicate(int[] nums) {
        //pre-conditions: nums cannot be null and must have at least 2 elements:
        //                elements must be in range [1,n] (inclusive)
        checkInvariants(nums);
        int tortoise = nums[0];
        int hare = nums[0];
        // Phase 1: Finding the intersection point of the two runners.

        do {
            tortoise = nums[tortoise];
            hare = nums[nums[hare]];
        } while (tortoise != hare);

        // Phase 2: Finding the "entrance" to the cycle.
        tortoise = nums[0];
        while (tortoise != hare) {
            tortoise = nums[tortoise];
            hare = nums[hare];
        }

        //post-conditions: method must return an integer that is a duplicate number found in the array nums
        //                 returned duplicate must exist and occur more than once

        final int duplicate = hare;
        int occurrences = 0;
        for (int num : nums) {
            if (num == duplicate) {
                occurrences++;
            }
        }
        if (occurrences < 2) {
            throw new IllegalStateException("The returned number is not a duplicate.");
        }
        //final invariant checks
        checkInvariants(nums);

        return hare;
    }

    //Invariants: Array structure must stay the same.
    //            The content of the Array cannot change and must be valid according to constraints.
    private static void checkInvariants(int[] nums) {
        if (nums == null) {
            throw new IllegalStateException("The array must not be null.");
        }
        if (nums.length < 2) {
            throw new IllegalStateException("Array must have at least two elements.");
        }
        for (int num : nums) {
            if (num < 1 || num > nums.length - 1) {
                throw new IllegalStateException("Array elements must be within the range 1 to " + (nums.length - 1));
            }
        }
    }

}
