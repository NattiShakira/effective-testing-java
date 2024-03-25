## Specification-based testing
### 1. Understanding the requirements, inputs, outputs
#### Business Rules
The core functionality of the programs is to transform a string rapresentation of a number into a 32-bit signed integer. 
#### Inputs
The input received is a String. It can be of any type.

#### Outputs
The output of the programs is an integer value, it comprises the sign value when present.

### 2. Explore what the program does for various inputs
Different inputs were used in order to understand fully the functionalities of the methods.

### 3. Explore possible inputs and outputs and identify partitions
Based on the definition of a palindrome and the constraints given:

- Input: string 
    - Null string
    - Empty string
    - String of length 1
    - String of length > 1
    - String starting with -
    - String starting with +
    - String contains integer
    - String contains integers + character
    - String starts with characters
    - String with whitespace

### 4. Analyze the boundaries
Boundary analysis must consider:

- _Null_
- _Empty_
- _Starts with - or +_:  
  - return negative or positive integer  
- _Starts with character + value_:  
  - return 0
- _Start with number + character_:  
  - return only number
- _Lower boundary exceed -2^31_:  
  - clamped to -2^31
- _Higher boundary exceed > 2^31-1_:  
  - clamped to 2^31-1
- _Starts with whitespace_:  
  - ignore whitespace

### 5. Devise Test Cases
Focus on creating test cases that cover the various scenarios identified in the previous steps.
- **T1**: `null` input
  - **Input**: `null`
  - **Expected Output**: `0`

- **T2**: Empty string
  - **Input**: `""` (an empty string)
  - **Expected Output**: `0`

- **T3**: Starting whitespace
  - **Input**: `"    42"`
  - **Expected Output**: `42`

- **T4**: Starting plus sign
  - **Input**: `"+42"`
  - **Expected Output**: `42`

- **T5**: Starting minus sign
  - **Input**: `"-42"`
  - **Expected Output**: `-42`

- **T6**: Non-digit characters after number
  - **Input**: `"4193 with words"`
  - **Expected Output**: `4193`

- **T7**: Number exceeds positive boundary
  - **Input**: `"999999999999"`
  - **Expected Output**: `2147483647` (Integer.MAX_VALUE)

- **T8**: Number exceeds negative boundary
  - **Input**: `"-999999999999"`
  - **Expected Output**: `-2147483648` (Integer.MIN_VALUE)

- **T9**: Only non-digit characters
  - **Input**: `"words and 987"`
  - **Expected Output**: `0`

- **T10**: Digits with leading zeros
  - **Input**: `"0000000000012345678"`
  - **Expected Output**: `12345678`
  - 
- **T11**: Positive number just inside boundary
  - **Input**: `"2147483647"` (Integer.MAX_VALUE as a string)
  - **Expected Output**: `2147483647`
  
- **T12**: Negative number just inside boundary
  - **Input**: `"-2147483648"` (Integer.MIN_VALUE as a string)
  - **Expected Output**: `-2147483648`
  
- **T13**: Number with leading spaces
  - **Input**: `" 12"`
  - **Expected Output**: `12`


### 6. Automate the test cases
Consulting the devise of test case, a test for each studied behavior is implemented.
### 7. Augument the Test Suite with Creativity and Experience
The specification states that only the space character `' '` is considered a whitespace character for the purpose of leading whitespace. Since a tab character is not treated as whitespace by the specification, it should lead to the parsing being halted before the digits, resulting in `0`:  
- **T14**: Start with a tab character
  - **Input**: `"\t99"`
  - **Expected Output**: `0`

 **DEBUGGING**
T14 test failed, showing a bug in the system. The code of myAtoi has changed to solve this issue. T14 is run once more and does not fail.
## Structural Testing
Enhancements to the previous test suite were made by utilizing structural testing with the aim of maximizing condition and branch coverage. The JaCoCo plugin was employed to measure coverage, yielding the following results:
- Method Coverage: For the `isPalindrome(int)` method, both line and branch coverage reached 100%.
- For the whole program line coverage was not 100% because one line was missing: class MyAtoi, which will therefore not be taken into consideration.
In order to assure that branch+condition coverage is reached, the tests approved that each of the individual conditions are being evaluated to true and false at least once and the entire branch is being true and false at least once.
## Mutation testing
Mutation testing was performed using PITest to evaluate the robustness of the test suite for the atoi method.

## Mutation Coverage Results

The MyAtoi class achieved a mutation coverage of 96%, with one conditional boundary mutant surviving. The mutant involved a changed conditional boundary.
The input "2147483646" is chosen to kill it because it's one unit less than Integer.MAX_VALUE (2147483647), it's near overflowing with the addition of any digit greater than 1 (assuming the next operation would multiply it by 10 and potentially add up to 9, based on your method's logic).
```java
    @Test
void atPositiveOverflowBoundary() {
  assertEquals(MyAtoi.myAtoi("2147483646"), 2147483646);
}
```
The following test successfully kills the last mutant reaching 100% mutation coverage.
