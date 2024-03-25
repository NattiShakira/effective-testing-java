# atoi
By Elena Battiston
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


# combination_sum
By Markus Niemack
## 1. Specification testing

I applied the seven step principle from chapter 2 in the book. Meaning first I read the README.md, then read the code to
understand what's going on. Then I identified some partitions.

The partitions are:

- candidates contains no entries in list.

- candidates contain multiple entries in list.

- candidates contain multiple valid combinations in list.

- candidates contain only negative integers.

- candidates contain only positive integers.

- candidates contain only 0.

- candidates contain a list of mixed positive and negative integers, as well as 0.

- target is 0

- target is positive

- target is negative

- target is smaller than smallest candidate

- target is greater than all candidates

- candidates contain duplicates

- for a target, more than 150 combinations are listed.

the boundaries I identified for this problem are:

- empty candidate list

- target smaller than smallest candidate

- target larger than all candidates

- negative candidates

I then implemented test cases based on these assumptions.
The tests, testNegativeCandidates and testMixedCandidates both provoke an out of bounds error,
as there is no input sanitation for negative numbers. line 20 infinitely recurs.
The reason for this is, that the condition target >= candidates[i] will always be true
if the target is positive, and some of the candidates negative.
The fix for this is to skip negative candidates, if that is the intended behaviour.
This could be done by adding:

(this fix will be fixed in part 3, mutation testing)
```java
if (candidates[i] < 0){
    continue;
}
```

at the start of the for loop.


The test testDuplicateCandidates reveals another bug in the code, namely that
duplicate candidates lead to duplicate combinations in the result.
This is easily fixed by adding the condition:

```java
if(i > 0 && candidates[i] == candidates[i-1]){
    continue;
}
```

At the start of the for loop. you could combine this with the above condition, but I didn't, as to  make it easier to realize what fixes what.
The test testTooManyCombinations also fails, as there's no check to see whether there are 150 or more combinations in the result.
to fix this, at the very start of the for loop, I added:

```java
if(result.size() >= 149){
    break;
}
```

Note that this does not check if a target only has 149 or less combinations,
but this is hard to know before computation, therefore this just breaks out
of the loop on the 150th iteration. If the wanted behaviour was to send an error, you would have to change the break statement, into a -1 return, or similar.
The test negativeCandidatesAndTarget fails too, but it's unclear to me
whether this is a bug or not, as negative integers are not mentioned in the requirements.
I'm assuming negative integers are unwanted inputs, therefore no fix is needed, as it returns
an empty list.



## 2. Structural testing

This test suite already gives a very good coverage, only missing line 7, which is the
Class initiation. All the structural testing I did, was just one test, which tests
said Initiation by testing if an instance of the class is created. With this
I have a suite that has 100% line and branch coverage.

## 3. Mutation testing

The mutation tests uncovered an issue with the bugfix I provided above. It missed
cases where the candidate is 0.

```java
if (candidates[i] <= 0)
    {
        continue;
    }
```

This is the fixed fix.
It also uncovered another problem, removing line 2 didn't break any tests,
as all my tests had a pre-sorted candidate list. I fixed this by adding a
test case with an unsorted list.
With these fixes, the mutation tests let no mutants survive.


# frac2dec

By Markus Niemack

## 1. Specification testing

From the README.md file i gathered the following partitions:

- Numerator and Denominator positive

- Numerator and Denominator negative

- Numerator 0

- Denominator 0

- Numerator and Denominator are of different signs

- goal fraction includes a repeating part

Possible boundaries include:

- Very large Denominator

- Very large Numerator

- Numerator and Denominator around 0 (both negative and positive)

I then devised test cases for these, and implemented them. The test "testDenominatorIsZero"
reveals a bug. There is no safeguard against dividing by zero in the code. This is easily fixed
by inserting this line at the start of the method:
```java
if (denominator == 0) return null;
```
The test testVeryLargeDenumerator reveals another bug, namely that the answer string is way longer than the first 104 digits.
To fix this I changed the conditional of the while loop to:
```java
while (num != 0 && res.length() < 103)
```
This doesn't round the last digit, but I think 101 digits after the decimal points are precise enough.

## 2. Structural testing

The only line not covered is the initiation of the class. To achieve coverage for this line, I added a test "testConstructor"
which just checks whether an instance of Frac2Dec gets created on call. With this 100% Branch and Line coverage is achieved.

## 3. Mutation testing

The first mutation test report revealed some mutants that survive, namely changing the conditional boundaries in this line:

```Java
res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
```

I added extenisve tests in testNumDenAroundZero, but the mutants survive. This leads me to conclude that these are equivalent mutants,
as changing > to >= or similar has no impact on the code. This is due to the first two conditional checks in the code, that handle
the case where either or both the numerator or denominator are 0, therefore the above line will never even get to check cases where either
numerator or denominator are 0.

All other mutants are killed.

# Generate_parentheses
By Markus Senn

## Specification Testing
### 1. Understanding the requirements
#### Business rules
By giving a number, we generate all possible combinations of well-formed parenthesis with the length * 2 of said number.\
Well-formed means that for every opening bracket, there is a closing bracket further down the line.\
We do not accept negative numbers or 0, as well as numbers greater than 8.

#### Input
Non-negative number between 1 and 8 (inclusive).

#### Output
An array with all permutations (or an empty one for invalid inputs).

### 2. Exploring the program
Tested some basic inputs.

### 3. Identifying partitions
Given the task description the partitions are as follows:
- Input
  - positive integers in range
  - positive integers out of range (empty array)
  - negative integers (empty array)
  - 0 (empty array)
  - (invalid types are not in the description and do not need to be covered since the parameter restricts the type to int)

### 4. Analyzing boundaries
The boundary testing should include:
- Going from positive to 0
  - On point: 1
  - Off point: 0
- Leaving the allowed range
  - On point: 8
  - Off point: 9

### 5. Devising test cases
Let's combine partitions with some sense.
- T1: Empty array from `0`
- T2: Empty array from `-1`
- T3: Lower boundary `1`
- T4: Upper boundary `8`
- T5: Invalid number out of boundary `9`

The 8 allowed integers can be tested against their corresponding solution arrays.
- T6: Integers `2-7` (1 & 8 being already covered before)

### 6. Automating test cases
Leveraging JUnit 5 we create the following test cases.

```java
class GenerateParenthesesTest {
    @Test
    void test1Zero() {
        List<String> expected = List.of();
        List<String> actual = GenerateParentheses.generateParentheses(0);
        assertEquals(expected, actual);
    }

    @Test
    void test2Negative() {
        List<String> expected = List.of();
        List<String> actual = GenerateParentheses.generateParentheses(-1);
        assertEquals(expected, actual);
    }

    @Test
    void test3LowerBoundary() {
        List<String> expected = List.of("()");
        List<String> actual = GenerateParentheses.generateParentheses(1);
        assertEquals(expected, actual);
    }

    @Test
    void test4UpperBoundary() {
        List<String> expected = List.of("(((((((())))))))", "((((((()()))))))", "...");
        List<String> actual = GenerateParentheses.generateParentheses(8);
        assertIterableEquals(expected, actual);
    }

    @Test
    void test5OutOfBoundary() {
        List<String> expected = List.of();
        List<String> actual = GenerateParentheses.generateParentheses(9);
        assertEquals(expected, actual);
    }

    @Test
    void test6ValidIntegers() {
        List<List<String>> solveArray = List.of(
            List.of("(())", "()()"),
            List.of("((()))", "(()())", "(())()", "()(())", "()()()"),
            List.of("(((())))", "((()()))", "((())())", "((()))()", "(()(()))", "(()()())", "(()())()", "(())(())", "..."),
            List.of("((((()))))", "(((()())))", "(((())()))", "(((()))())", "(((())))()", "((()(())))", "((()()()))", "..."),
            List.of("(((((())))))", "((((()()))))", "((((())())))", "((((()))()))", "((((())))())", "((((()))))()", "..."), 
            List.of("((((((()))))))", "(((((()())))))", "(((((())()))))", "(((((()))())))", "(((((())))()))", "...")
        ;
        for (int i = 2; i <= 7; i++) {
            assertIterableEquals((solveArray.get(i - 2)), GenerateParentheses.generateParentheses(i));
        }
    }
}
```


### 7. Augmenting with creativity and experience
The following changes to the code have happened during the development of the test cases.
1. There was no upper boundary check in place, which was revealed by T5

Leading to this code change:
```java
if (n<=0) return combinations;
```
to
```java
if (n<=0 || n>8) return combinations;
```

## Structural Testing
Using the JaCoCo plugin to measure condition+branch coverage has lead
to the following discoveries:\
I have 100% condition+branch coverage, not counting the class declaration line,
thus I will not add any new tests at this stage.

## Mutation Testing
The first iteration of the mutation testing revealed only 1 surviving mutant which
was related to a changed return value (95% mutation coverage).\
I added a dedicated test for the class initialization (T7) and ran it a 2nd time.
The mutant persisted on the 2nd run too, but I achieved 100% line coverage now and the mutant
is in the same category as before (EmptyObjectReturnValsMutator), where I cannot gain anything from it.\
Because of this I concluded the test suite development.

# median_of_arrays
By Natalia Carrion Gimenez
## 1. Specification-based testing

First, I read the requirements to understand the goal of the program and its inputs and outputs:
1. The goal of the program is to find a median of the two sorted in ascending order arrays.
2. The program receives two parameters: a sorted in ascending order array of integers of length m and a sorted in ascending order array of integers of length n.
3. The program returns a double representing the median of the two sorted arrays.

Second, I explored what the program does for various inputs and wrote several tests for happy cases:
1. Given arrays [1, 2, 3] and [4, 5, 6], the program should return 3.5.
2. Given arrays [1, 2, 3] and [1, 2, 3], the program should return 2.
3. Given arrays [] and [2, 3, 5], the program should return 3.

These tests passed!

Next, I explored possible inputs and outputs and identified partitions and boundaries.
1. First input is an array of integers of length m, thus we need to check the following cases:
- array is null;
- array is empty;
- array's length = 1;
- array's length > 1. In this case, there should be checked several options: 
- an array is sorted in ascending order;
- an array is sorted in descending order;
- an array is sorted but has duplicate values;
- an array is not sorted.

Second input is an array of integers of length n. Partitions for this input is similar to the case above.

2. Speaking about input combinations, the two arrays have a relationship:
- the sum of the lengths of the arrays can be odd or even. In case it is odd, the program should return a certain 
value that belongs to one of the arrays. In case it is even, the program should return an average of two values that 
can belong to the same array or to different arrays. 
- the lengths of the arrays can be the same, or one of the arrays can be larger than the other. The program should handle these cases.
- values of the arrays should be also considered: there can be cases when the last value of one array is smaller that the first value of another array, in this case a "merger" is not required; in the opposite case, a "merger" can be necessary.

3. Since the program outputs a single double, there are not so many options to check but the program should check when the output is 0 (either array is null or empty) or -1 (both arrays are empty).

Taking the above discussion into account, the following test cases were devised:
1. Exceptional cases
- T1: the first array is null
- T2: the second array is null
- T3: the first array is empty
- T4: the second array is empty
- T5: both arrays are empty
- T6: the first array is sorted in descending order
- T7: the second array is sorted in descending order
- T8: the first array is not sorted
- T9: the second array is not sorted

2. Cases that don't require to "merge" arrays (the last value of the first arrays if smaller than the first value of the second array):

2.1. The sum of lengths of arrays is odd:
- T10: first array goes first, and it's length is exactly 1 bigger than the length of the second array
- T11: first array goes first, but the length of the second array is 1 bigger than the length of the first array
- T12: first array goes first, and it's length is more than 1 element bigger than the length of the second array
- T13: first array goes first, but the length of the second array is more than 1 element bigger than the length of the first array
- T14-T17: similar to T10-13, but now the second array goes first.

2.2. The sum of lengths of arrays is even:
- T18: first array goes first, lengths of both arrays are equal
- T19: first array goes first, and it's length is 2 elements bigger that the length of the second array
- T20-T21: similar to T18-19, but now the second array goes first

3. Cases that require to "merge" arrays (their values are mixed):

3.1. The sum of lengths of arrays is odd:
- T22: the length of the first array is bigger
- T23: the length of the second array is bigger

3.2. The sum of the lengths of arrays is even:
- T24: length of each array is the same (and odd), the program returns an average of values belonging to the same array
- T25: length of each array is the same (and odd), the program returns an average of values belonging to different arrays
- T26: length of the first array is bigger, the program returns an average of values belonging to the same array
- T27: length of the first array is bigger, the program returns an average of values belonging to different arrays
- T28-29: similar to T26-27, but now the second array's length is bigger

4. An array has duplicate values:
- T30: first array has duplicate values
- T31: second array has duplicate values

After all the tests were implemented, 29 of them passed, but 2 broke. T30 and T31 didn't pass, because the program didn't consider an 
array containing duplicate values being properly sorted. To solve this problem, the following fix was made to a method isArraySortedAscending:

a conditional boundary 
```java
if (array[i] >= array[i + 1]) {
    return false;
}
```
was changed to
```java
if (array[i] > array[i + 1]) {
    return false;
}
```

## 2. Structural testing

Jacoco coverage report showed that the implemented test suite has a 100% line, condition and branch coverage.

## 3. Mutation testing

Pitest ran 130 tests with 46 generated mutations out of which 44 were killed (96%).
Inspection of the survived mutants revealed the following:
1. The first mutant replaced integer addition with subtraction in the conditional statement of the method findMedianSortedArrays:

The condition
```java
if ((m + n) % 2 == 0) {...}
```
was changed to
```java
if ((m - n) % 2 == 0) {...}
```

Since both versions of the conditional statements evaluate to truth or false given the same values of m and n, 
this mutant does not affect the program. Thus, it was decided to leave it as is, instead of writing a specific test that would check that the program performs addition and not subtraction in the conditional statement.

2. The second mutant changed a conditional boundary in the method getMin:

Supposedly, the conditional boundary 
```java
return nums1[p1] < nums2[p2] ? nums1[p1++] : nums2[p2++];
```
was changed to
```java
return nums1[p1] <= nums2[p2] ? nums1[p1++] : nums2[p2++];
```

Thorough analysis of the program showed that it doesn't matter what conditional boundary is used. In both cases, the program 
works correctly. The only aspect affected by this change, is the internal mechanics of the method getMin. More precisely, it affects what pointer (p1 or p2) moves first 
when the values to which these pointers point are equal. If we use <, then p2 moves first; if we use <=, then p1 moves first. For this reason, it was decided to let this mutant live.


# Needle_in_hay
By Markus Senn

## Specification Testing
### 1. Understanding the requirements
#### Business rules
Given 2 strings, one being called the needle, we search for the first occurrence of the needle in the other string.\
There are rules for empty words in both cases.

#### Input
2 strings that could be empty or null.

#### Output
The index of the **first** occurrence or a predefined integer.
- -1 for no match, or at least 1 parameter being null
- 0 when both parameters are empty

### 2. Exploring the program
Tested some different strings and types.

### 3. Identifying partitions
Given the task description the partitions are as follows:
- Input
  - strings with length > 0
  - empty strings
  - null
  - (other invalid types are not in the description and do not need to be covered since the parameter restricts the types to string)

### 4. Analyzing boundaries
The boundary testing should include:
- Normal to empty strings
  - On point: length 1
  - Off point: length 0
- Multiple occurrences of needle
  - On point: once
  - Off point: twice
- Case-sensitive strings
  - On point: with alternating cases
  - Off point: all lowercase
- Strings with whitespaces or special characters will be caught since we compare characters.
  If anything this is a sub-category of the case-sensitive boundary.
- Needle being bigger than the haystack, but this will terminate normally with -1

### 5. Devising test cases
Let's design test cases according to the facts at hand.
- T1: `null` for one & both
- T2: both are `empty`
- T3: 1 is `empty`
- T4: both size `1`
- T5: needle appears twice
- T6: case switching

### 6. Automating test cases
Leveraging JUnit 5 we create the following test cases.
```java
class NeedleInHayTest {
    @Test
    void test1Null(){
        assertEquals(-1, NeedleInHay.find("hehe", null));
        assertEquals(-1, NeedleInHay.find(null, "hihi"));
        assertEquals(-1, NeedleInHay.find(null, null));
    }

    @Test
    void test2BothEmpty(){
        assertEquals(0, NeedleInHay.find("",""));
    }

    @Test
    void test3OneEmpty(){
        assertEquals(-1, NeedleInHay.find("", "Donde"));
        assertEquals(-1, NeedleInHay.find("Dante", ""));
    }

    @Test
    void test4Size1(){
        assertEquals(-1, NeedleInHay.find("f", "s"));
        assertEquals(0, NeedleInHay.find("f", "f"));
    }

    @Test
    void test5NeedleTwice(){
        assertEquals(5, NeedleInHay.find("stackhaydayafterhayday", "hay"));
    }

    @Test
    void test6CaseSensitivity(){
        assertEquals(3, NeedleInHay.find("duRdAmA","dA"));
    }
}
```


### 7. Augmenting with creativity and experience
The following changes to the code have happened during the development of the test cases.
1. T3 initially returned as faulty with a StringIndexOutOfBoundsException.
   Upon further inspection of the code I found this passage that needed work:
```java
if (haystack.isEmpty() && needle.isEmpty()) return 0;
```
in combination with
```java
for(int i = 0; i < (lenHay-lenNed + 1); i++) {
  if(haystack.charAt(i) == needle.charAt(0)) {
```

I tackled it at the beginning with an additional check for one empty parameter.
```java
if (haystack.isEmpty() || needle.isEmpty()) return -1;
```


## Structural Testing
Using the JaCoCo plugin to measure condition+branch coverage has lead
to the following discovery:\
I have 100% condition+branch coverage, not counting the class declaration line, for which I added another test T7.
```java
@Test
void test7Constructor() {
  NeedleInHay testInstance = new NeedleInHay();
  assertInstanceOf(NeedleInHay.class, testInstance);
}
```

## Mutation Testing
The mutation testing showed no surviving mutants (100% mutation coverage).\
I finish the test suite development at this point .


# Palindrome
By Elena Battiston

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