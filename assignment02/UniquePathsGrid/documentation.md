# UniquePathsGrid

## Task 1: Code Coverage

After analyzing the structure of the code, two test cases were devised: 
1) A simple square grid (3x3) is passed as an argument.
2) A non-square grid (3x7) is passed as an argument.

With these two test cases, we have reached 100% line coverage and 100% branch coverage. The report can be seen in the asset folder.

## Task 2: Designing Contracts

According to the description of the task and our own analysis, the following pre-conditions, post-conditions and invariants must be satisfied:
1) Pre-conditions: number of rows m and number of columns n must be in range [1, 100]. To check these pre-conditions, we added the following code to the method under test:
```java
if ((m < 1 || m > 100)  || (n < 1 || n > 100)) throw new IllegalArgumentException("Passed argument is outside the range of 1 and 100");
```
It was decided that if a wrong argument is passed, the method should throw an IllegalArgumentException.

2) Post-conditions: output of the method is a non-negative integer. In fact, the output has to be positive, since in the simplest case when m=1 and n=1, the method returns 1. 
To enforce the check of this condition, we introduced an auxiliary package-private method resultSatisfiesConditions that checks if the result is not smaller than 1:
```java
boolean resultSatisfiesConditions(int result) {
  if (result < 1) throw new RuntimeException("Result must be bigger than 0");
  return true;
}
```
This method is called inside the method uniquePaths right before the result is returned to the client. If the result is correct (>= 1), the check is passed. Otherwise, method resultSatisfiesConditions (and uniquePaths as well) throws a RuntimeException:
```java
public int uniquePaths(int m, int n) {
  ...
  
  resultSatisfiesConditions(result);

  return result;
}
```
3) Invariants: the method under test computes the number of unique passes by filling a grid. Each time a number is put in a cell of this grid, it must be larger than the number contained in the cell to the left and in the cell above. To check this invariant, we created an auxiliary package-private method invariantSatisfiesConditions:
```java
boolean invariantSatisfiesConditions(int a, int b, int c) {
        if (a <= b || a <= c) throw new RuntimeException("Value of a cell should be bigger than both, the value of a cell to the left and of an upper cell");
        return true;
    }
```
This method is called inside the method uniquePaths each time a cell of the grid is assigned a number. If this number doesn't satisfy the condition, method invariantSatisfiesConditions (and uniquePaths as well) throws a RuntimeException:
```java
public int uniquePaths(int m, int n) {
  ...
  
  for (int i = 1; i < m; i++) {
    for (int j = 1; j < n; j++) {
      dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
      invariantSatisfiesConditions(dp[i][j], dp[i - 1][j], dp[i][j - 1]);
    }
  }
  ...

return result;
}
```

## Task 3: Testing Contracts

To test the above pre-conditions, post-conditions and invariants, three tests were devised (each test contains multiple assertions):
1) Test testPreConditionsAreSatisfied checks pre-conditions: one assertion checks a "good" case (m = 3, n = 7) and six other assertions check "bad" cases (mainly boundaries):
- m = 0, n = 7
- m = -1, n = 7
- m = 101, n = 7
- m = 7, n = 0
- m = 7, n = -1
- m = 7, n = 101.

2) Test testResultSatisfiesConditions checks post-conditions by checking the correctness of the auxiliary method resultSatisfiesConditions in three cases: a "good" case (output is bigger than 0) and two "bad" cases (output is equal to zero or is negative).

3) Test testInvariantSatisfiesConditions checks invariants: one assertion checks a "good" case (when the number in a cell is bigger than the numbers in the cell to the left and in the cell above) and two "bad" cases (the number is not bigger than either the number in the cell to the left or the number in the cell above).

Code coverage reports generated after running these tests (and tests from task 1) can be found in the asset folder.

## Task 4: Property-Based Testing

Following the property-based testing approach, we identified the following six properties:
1) If the grid contains only one row (m = 1), no matter how many columns there are (given that n is in range [1, 100]), there is only one unique path (the output will always be equal to 1); this property is tested in a test testOnlyOneRow.
2) Same holds for the columns: if the grid contains only one column (n = 1), no matter how many rows there are (given that m is in range [1, 100]), there is only one unique path (the output will always be equal to 1); this property is tested in a test testOnlyOneColumn.
3) If the number of rows is invalid (m < 1 or m > 100), the method should throw an IllegalArgumentException, even if the number of columns satisfies the conditions; this property is tested in a test testInvalidN.
4) Same holds for the columns: if the number of columns is invalid (n < 1 or n > 100), the method should throw an IllegalArgumentException, even if the number of rows satisfies the conditions; this property is tested in a test testInvalidM.
5) Using ChatGPT, we found out that the number of unique paths can be calculated using a formula of binomial coefficient: (m + n - 2) choose (m - 1). To check if our method always returns a correct number of paths, no matter what values of m and n are passed (given they are both in range [1, 100]), we, first, had to implement auxiliary private methods calculatePaths and binomialCoeff (with the help of ChatGPT). These methods calculate the number of unique paths using the binomial coefficient formula for a correct grid. Then, in tests testvValidM and testvValidN, we computed the number of unique paths for a grid (with varying parameters m and n) using our method under test and checked the results with those of the method for binomial coefficient.

Code coverage reports generated after running these tests (and tests from tasks 1 and 3) can be found in the asset folder. Results of the tests (including jqwik reports) can be seen in log.txt file.
