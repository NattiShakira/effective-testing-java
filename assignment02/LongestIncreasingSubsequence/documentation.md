# LongestIncreasingSubsequence

## Task 1: Code Coverage

After analyzing the structure of the code, two test cases were devised:
1) A null array or an empty array is passed as an argument.
2) An array containing multiple elements in a random order is passed as an argument.

With these two test cases, we have reached 100% line coverage and 100% branch coverage. The report can be seen in the asset folder.

## Task 2: Designing Contracts

According to the description of the task and our own analysis, the following pre-conditions, post-conditions and invariants must be satisfied:
1) Pre-conditions: if a passed array is null or empty, the method should return 0. This pre-condition is already contained in the method, we didn't have to add any additional code to check it.

2) Post-conditions: output of the method has to be a non-negative integer. In addition to that, the returned result cannot be larger than the length of a passed array. 
To check these conditions, we introduced an auxiliary package-private method resultSatisfiesConditions:
```java
 boolean resultSatisfiesConditions(int result, int[] array) {
   if (result < 0) throw new RuntimeException("Result must be a non-negative integer");
   if (result > array.length) throw new RuntimeException("Result cannot be bigger that the length of the input array");
   return true;
}
```
This method is called inside the method lengthOfLIS right before the result is returned to the client. If the result is correct, the check is passed. Otherwise, method resultSatisfiesConditions (and lengthOfLIS as well) throws a RuntimeException:
```java
public int lengthOfLIS(int[] nums) {
   ...
   
   resultSatisfiesConditions(maxLength, nums);

   return maxLength;
}
```
3) Invariants: the method under test computes the longest increasing subsequence by filling in an additional array. This array has to have the same length as the input array (invariant 1). 
In addition, each element of this additional array cannot be smaller than 1 (invariant 2). To check these invariants, we created two auxiliary package-private methods invariant1SatisfiesConditions and invariant2SatisfiesConditions:
```java
boolean invariant1SatisfiesConditions(int[] dp, int[] nums) {
   if (dp.length != nums.length) throw new RuntimeException("DP array has to have the same length as the input array");
   return true;
}

boolean invariant2SatisfiesConditions(int num) {
   if (num < 1) throw new RuntimeException("Elements of DP array cannot be smaller than 1");
   return true;
}
```
These methods are called inside the method lengthOfLIS:
- method invariant1SatisfiesConditions is called when the array is just initialized (before the loop) and after it's filled with values (after the loop):
```java
public int lengthOfLIS(int[] nums) {
   ...
   // Invariant 1
   invariant1SatisfiesConditions(dp, nums);

   for (int i = 0; i < nums.length; i++) {
      ...
   }

   // Invariant 1
   invariant1SatisfiesConditions(dp, nums);
   ...
}
```
- method invariant2SatisfiesConditions is called each time a value is assigned to a cell of the additional array:
```java
public int lengthOfLIS(int[] nums) {
   ...
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
      ...
}
```

If any of the mentioned invariants don't hold, the corresponding methods that checks an invariant (and lengthOfLIS as well) will throw a RuntimeException.

## Task 3: Testing Contracts

Since the above pre-conditions were already tested in a test testArrayIsNullOrEmpty (Task 1), we focused on testing post-conditions and invariants.
For this purpose, three tests were devised (each test contains multiple assertions):
1) Test testResultSatisfiesConditions checks post-conditions by checking the correctness of the auxiliary method resultSatisfiesConditions in three cases: 
- one assertion checks a "good" case (result is bigger than 0) and 
- two other assertions check "bad" cases:
- passed array has a length of 5, but the returned result is negative (-1)
- passed array has a length of 5, but the returned result is bigger than the length (10).

2) Test testInvariant1SatisfiesConditions checks invariant 1 by checking the correctness of the auxiliary method invariant1SatisfiesConditions in three cases: 
- a "good" case when two arrays have the same length
- two "bad" cases: one of the arrays is bigger.

3) Test testInvariant2SatisfiesConditions checks invariant 2 by checking the correctness of the auxiliary method invariant2SatisfiesConditions in three cases:
- two "good" cases when a value in an array is positive: 1 (boundary) and 5 (some random positive number)
- one "bad" case: a value in an array is not positive (0).

Code coverage reports generated after running these tests (and tests from task 1) can be found in the asset folder.

## Task 4: Property-Based Testing

Following the property-based testing approach, we identified the following properties:
1) Given an array sorted in ascending order and with no duplicates, the output should always be equal to the length of the input array.

To generate random arrays according to the given conditions, we created a method ascendingSortedUniqueArray that generates an Arbitrary<int[]> 
(with the help of several private methods: ensureUniqueness and sortAscending). Then this Arbitrary is passed to the test method testAscendingSortedUniqueArray.

2) Given an array sorted in descending order, the output should always be 1. Presence (or absence) of duplicates doesn't matter in this case.

To generate random arrays sorted in descending order, we created a method descendingSortedArray that generates an Arbitrary<int[]>
(with the help of private methods: reverseArray and sortDescending). Then this Arbitrary is passed to the test method testDescendingSortedArray.

3) Given an array sorted in ascending order and with duplicates, the output should always be equal to the length of the input array minus the number of duplicated elements 
(one of the duplicated elements is account for, all the other duplicates of this element are discarded).

To generate random arrays sorted in ascending order and containing 1 duplicate, we created a method ascendingSortedArrayWithDuplicates that generates an Arbitrary<int[]>
(with the help of a private method insertDuplicate that inserts 1 duplicate in the middle of the array). Then this Arbitrary is passed to the test method testAscendingSortedArrayWithDuplicates.

4) Given an array consisting of 4 parts:
- parts 1 and 3 are sorted in ascending order and parts 2 and 4 - in descending
- range of values of part 2 is smaller than the range of values of part 1
- range of values of part 4 is smaller than the range of values of part 3
- range of values of part 3 is bigger than the range of values of part 1,

the output should be smaller or equal to the total number of elements in parts 1 and 3.

Example: array consisting of 4 parts with the values in the following ranges: [-999, 0] sorted asc, [Integer.MIN_VALUE, -1000] sorted desc, [1000, Integer.MAX_VALUE] sorted acs, [1, 999] sorted desc. 

To generate random arrays that satisfy the above criteria, we created a method mixedUniqueArraysAscendingDescending that generates an Arbitrary<int[]> 
(basically, it creates several Arbitraries that are then concatenated into one single arbitrary using private method concatenateArrays). 
The length of the parts is chosen to be: part 1 = 10, part 2 = 15, part 3 = 15, part 4 = 10.
Then, this Arbitrary is passed to the test method testMixedUniqueArraysAD. The output of the method under test should be no bigger than 25 (10 + 15).

5) Given an array consisting of 4 parts:
- parts 2 and 4 are sorted in ascending order and parts 1 and 3 - in descending
- range of values of part 1 is bigger than the range of values of part 2
- range of values of part 3 is smaller than the range of values of part 2
- range of values of part 4 is bigger than the range of values of part 2, 

the output should be smaller or equal to the total number of elements in parts 2 and 4.

Example: array consisting of 4 parts with the values in the following ranges: [1000, Integer.MAX_VALUE] sorted desc, [-999, 0] sorted asc, [Integer.MIN_VALUE, -1000] sorted desc, [1, 999] sorted asc.

To generate random arrays that satisfy the above criteria, we created a method mixedUniqueArraysDescendingAscending that generates an Arbitrary<int[]>.
The length of the parts is chosen to be: part 1 = 10, part 2 = 15, part 3 = 15, part 4 = 10.
Then, this Arbitrary is passed to the test method testMixedUniqueArraysDA. The output of the method under test should be no bigger than 25 (15 + 10).

Code coverage reports generated after running these tests (and tests from tasks 1 and 3) can be found in the asset folder. Results of the tests (including jqwik reports) can be seen in log.txt file.
