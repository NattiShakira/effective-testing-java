# Task 1: Code Coverage

Using the structure of the source code to guide testing, structural testing has been reached with 100% line coverage and
100% branch coverage. The code coverage took into account the in-point 2 and off-point, 3. The report can be seen in the
asset folder.

# Task 2: Designing Contracts

Following the initial setup, contracts were implemented to ensure robust method execution. Pre-conditions validate input
requirements, post-conditions verify the correctness of the method's outputs, and invariants ensure state consistency
throughout the method’s lifecycle.
The design contracts are incorporated into the source code

- For the pre-conditions, the case where n is less than 0 is taken into account throwing an IllegalArgumentException.
  This case covers the requirement constraint that states the input should be positive.
- In post-conditions, the case where the result is negative is taken into account throwing an RuntimeException. This
  case covers the requirement constraint that states the input should not be negative.
- The invariant checks that 'oneStepBefore' is always greater than or equal to 'twoStepsBefore', maintaining the
  consistency of the algorithm in the loop. Since the ClimbingStairs class does not maintain mutable state, no
  additional class invariants are required.

To ensure contract enforcement:

- The 'negativeInputThrowError' test validates that providing a negative input correctly triggers a precondition
  violation.
- The 'largeNumberTestInvariant' test confirms the invariants are maintained, ensuring 'oneStepBefore' remains greater
  than or equal to 'twoStepsBefore' even with larger inputs.

- For post-conditions, the 'resultsShouldAlwaysBeNonNegative' test verifies that outputs for inputs ranging from 0 to 30
  are non-negative, as expected for post-execution.

# Task 3: Testing Contracts

Analysing the new test cases, it occurs that new tests should be done for the (x <0) conditions present in the contract.
The 'zeroNumber' test tackles this problem checking that a 0 input yields to a 0 output and 'LessThenTwoNumber' checks
that the input 1 returns 1 as
output.
Moreover, trying to assert for the upper-boundary (assertDoesNotThrow(() -> climber.climbStairs(Integer.MAX_VALUE));) it
can be noticed that the program shows integer overflow.

### Task 4: Property-Based Testing

Using property-based testing with jqwik, it was identified an integer overflow threshold at an argument value of 92.
This critical boundary condition has led to an additional precondition being implemented to prevent inputs greater than
91.

For this reason in the code a new precondition for (n > 91) is added.
Properties were then finalized and tested with Jqwik, as can be seen from the log.txt file.

- The first property checks the correct execution of the input with a range between 0 and 2, that should return the same
  number.
- The second property checks the correct execution of the algorithm as assessed by the invariant for an input ranging
  from
  3 to 91.
- The third property checks that invalid arguments, namely integers below 0 and above 91, will throw an exception.

All the line were covered except one, namely ->  throw new RuntimeException("oneStepBefore should be greater than or
equal to twoStepsBefore"); remains uncovered because the logic of the algorithm guarantees this condition will never
occur. The line was still kept as a defensive programming measure.