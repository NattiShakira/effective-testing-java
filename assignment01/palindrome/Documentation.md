## Specification-based testing
### 1. Understanding the requirements, inputs, outputs
#### Business Rules
The core functionality of the programs is to determine if an integer x is a palindrome. This involves checking whether x reads the same backward as forward. A palindrome remains unchanged when reversed. Moreover, negative numbers are not considered palindromes due to the negative sign affecting symmetry.

#### Inputs
Both programs receive a single input: an integer x. The value of x can range from -2^20 to 2^20 - 1, encompassing both negative and positive integers, including zero.

#### Outputs
The output of the programs is a boolean value, true if x is a palindrome and false if x is not a palindrome.

### 2. Explore what the program does for various inputs
Different inputs were used in order to understand fully the functionalities of the methods.

### 3. Explore possible inputs and outputs and identify partitions
Based on the definition of a palindrome and the constraints given:

- Input: integer value
    - Negative integers (should return false).
    - Zero (should return true).
    - Null (invalid or exception)
    - Positive integers that are single digits (should return true).
    - Positive integers that are palindromes with both even and odd numbers of digits (should return true).
    - Positive integers that are not palindromes, both even and odd numbers of digits (should return false).

### 4. Analyze the boundaries
Boundary analysis must consider:

- Negative to Non-Negative Transition:
    - Test Cases:
        - On Point: 0.
        - Off Point: -1.

- Single Digit to Double Digit Transition:
    - Test Cases:
        - On Point: 11.
        - Off Points: 10, and 9.

- Integer Limits:
    - Test Cases:
        - On Points: Specific palindromes near the upper limit (2^20 - 1) to ensure the algorithm handles large numbers correctly without overflow or logic errors.
        - Off Points: Numbers just outside the palindrome definition at high values, ensuring the transition from palindrome to non-palindrome is handled correctly.

### 5. Devise Test Cases
Focus on creating test cases that cover the various scenarios identified in the previous steps, including edge cases, boundary conditions, and typical palindrome and non-palindrome numbers.

#### Exceptional Cases and Boundary Testing
- **T1**: `null` input
- **T2**: Lower boundary just beyond constraint (`-2^20 - 1`)
- **T3**: Lower boundary (`-2^20`)
- **T4**: `-1` (Largest negative number within constraint)
- **T5**: `0` (Smallest non-negative number within constraint)
- **T6**: `1` (Smallest positive number within constraint)
- **T7**: Upper boundary (`2^20 - 1`)
- **T8**: Upper boundary just beyond constraint (`2^20`)

#### Transition Between Non-Palindromic and Palindromic States
- **T9**: Number just before the smallest two-digit palindrome (`10`)
- **T10**: Smallest two-digit palindrome (`11`)
- **T11**: Number just after a two-digit palindrome (`12`)

### 6. Automate the test cases

JUnit 5 will be used to automate the test cases. Tests should validate both methods, highlighting differences in behavior, if any.

```java
@Test
void testNullInput() {
    // As Java primitive types cannot be null, this test case is not applicable.
}

@Test
void testLowerBoundaryConstraint() {
    assertFalse(PalindromeTwo.isPalindrome(-(int)Math.pow(2, 20) - 1));
}

@Test
void testLowerBoundary() {
    assertFalse(PalindromeTwo.isPalindrome(-(int)Math.pow(2, 20)));
}

@Test
void testNegativeOne() {
    assertFalse(PalindromeTwo.isPalindrome(-1));
}

@Test
void testZero() {
    assertTrue(PalindromeTwo.isPalindrome(0));
}

@Test
void testOne() {
    assertTrue(PalindromeTwo.isPalindrome(1));
}

@Test
void testUpperBoundary() {
    assertFalse(PalindromeTwo.isPalindrome((int)Math.pow(2, 20) - 1));
}

@Test
void testUpperBoundaryConstraint() {
    assertFalse(PalindromeTwo.isPalindrome((int)Math.pow(2, 20)));
}

@Test
void testJustBeforeTwoDigitPalindrome() {
    assertFalse(PalindromeTwo.isPalindrome(10));
}

@Test
void testSmallestTwoDigitPalindrome() {
    assertTrue(PalindromeTwo.isPalindrome(11));
}

@Test
void testJustAfterTwoDigitPalindrome() {
    assertFalse(PalindromeTwo.isPalindrome(12));
}
```
The same are applied to PalindromeOne
**DEBUGGING**
Testing the PalindromeOne and PalindromeTwo we get that the testZero for PalindromeTwo given the wrong result, meaning that inserting one 0 the code return the boolean false wrongly.
In the code, the condition if (x % 10 == 0) return false; is causing 0 to be incorrectly evaluated as not a palindrome. To fix the method, a specific check for zero before this condition is added:     if (x == 0) return true;
The tests are run once more to check if the bug is correctly solved, and it is.

### 7. Augument the Test Suite with Creativity and Experience
This analysis evolved in the following considerations:

In the test the upperBoundary has been checked (2^20 - 1 = false) and also the upperBoundaryConstraint(2^20 = false) but in-points and out-points have not been checked (like 1,048,401 and 1,049,401)
The augmented test are:
```java
@Test
    void testClosestSmallerPalindromeToUpperBoundary() {
        assertTrue(PalindromeOne.isPalindrome(1_048_401));
    }

    @Test
    void testClosestLargerPalindromeToUpperBoundary() {
        assertFalse(PalindromeOne.isPalindrome(1_049_401));
    }
```
**DEBUGGING**
The tests indeed asses 1_049_401 as Palindrome, but the number id out of the constraint. This occurs because the constraints in the requirements are not covered in the code. So this case is resolved with this improvements in both the classes:
if (x < -(1 << 20) || x > ((1 << 20) - 1)) return false;
The tests are run once more to check if the cases are correctly solved, and they are.

## Structural Testing
Enhancements to the previous test suite were made by utilizing structural testing with the aim of maximizing condition and branch coverage. The JaCoCo plugin was employed to measure coverage, yielding the following results:

### PalindromeOne:
- Branch Coverage: No branches were missed; all 8 branches present were covered by tests.
- Line Coverage: One line in the source code was not executed; this line was the class declaration (`public class PalindromeOne`) and was therefore not considered relevant.
- Method Coverage: For the `isPalindrome(int)` method, both instruction and branch coverage reached 100%.

### PalindromeTwo:
- Branch Coverage: Partial. Line 19 had one of four branches missed, and line 27 had one of two branches missed. Also here 1 line in the source code was not executed; this line was the class declaration (`public class PalindromeTwo`) and was therefore not considered relevant.
- Instruction Coverage for the `isPalindrome(int)` method: Achieved 94%.
- Branch Coverage for the `isPalindrome(int)` method: Reached 91%.

Line 19: The conditional check if (x < 1000 && ((x / 100) * 10 + x % 10) % 11 == 0) was never true for both x < 1000 and ((x / 100) * 10 + x % 10) % 11 == 0 in test cases.
A test has been developed and tested to solve it:assertFalse(PalindromeTwo.isPalindrome(231));

Line 27: One of the two branches of if (v > x) is missing. There was missing the case of a palindrome with even length.
A test has been developed and tested to solve it: assertTrue(PalindromeTwo.isPalindrome(2222));

With these new two tests, for the `isPalindrome(int)` method, branch coverage reached 100%.

In order to assure that branch+condition coverage is reached, the test approved that each of the individual conditions are being evaluated to true and false at least once and the entire branch is being true and false at least once.
## Mutation testing
Mutation testing was performed using PITest to evaluate the robustness of the test suite for the palindrome checking methods in `PalindromeOne` and `PalindromeTwo`.

## Mutation Coverage Results

- `PalindromeOne` achieved a mutation coverage of 77%, with two conditional boundary mutants surviving.
- `PalindromeTwo` achieved a mutation coverage of 73%, with several conditional boundary and logic negation mutants surviving.

### Analysis of Surviving Mutants

### PalindromeOne
- The surviving mutants were related to conditional boundary logic. Tests covered boundary conditions, they are mutations that don't change the program's behavior.

### PalindromeTwo
- **Changed Conditional Boundary**:
- **Replaced Integer Modulus with Multiplication**:
- **Replaced Integer Addition with Subtraction (and vice versa)**: